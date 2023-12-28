import org.objectweb.asm.*;

public class ClassVisitorImpl extends ClassVisitor {
    private final String className;
    private boolean classModified = false;

    public ClassVisitorImpl(ClassVisitor cv, String className) {
        super(Opcodes.ASM7);
        this.className = className;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
                                     String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);

        if (mv != null) {
            return new MethodVisitorImpl(mv, className, name, descriptor);
        }

        return mv;
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
        classModified = true;
    }

    public boolean isClassModified() {
        return classModified;
    }
}