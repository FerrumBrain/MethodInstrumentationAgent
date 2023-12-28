import org.objectweb.asm.*;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.HashSet;
import java.util.Set;
import java.io.*;


public class TimingClassFileTransformer implements ClassFileTransformer {
    private static final String targetPackage = "com.example.pack";
    private static final Set<String> executedClasses = new HashSet<>();

    private static void saveExecutedClassesToFile(Set<String> executedClasses) {
        try (FileOutputStream fos = new FileOutputStream("executed_classes.txt")) {
            for (String executedClass : executedClasses) {
                fos.write((executedClass + System.lineSeparator()).getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer)
            throws IllegalClassFormatException {
        System.out.println(className);
        System.out.println(targetPackage.replace('.', '/'));
        System.out.println(0);
        if (className.startsWith(targetPackage.replace('.', '/'))) {
            System.out.println(1);
            ClassReader cr = new ClassReader(classfileBuffer);
            System.out.println(2);
            ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
            System.out.println(3);
            ClassVisitorImpl cv = new ClassVisitorImpl(cw, className);
            System.out.println(4);
            cr.accept(cv, ClassReader.EXPAND_FRAMES);
            System.out.println(5);
            if (cv.isClassModified()) {
                executedClasses.add(className);
                saveExecutedClassesToFile(executedClasses);
                return cw.toByteArray();
            }
        }
        return null;
    }
}