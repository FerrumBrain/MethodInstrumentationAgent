import org.objectweb.asm.*;

public class MethodVisitorImpl extends MethodVisitor {
    private final String className;
    private final String methodName;
    private final String methodDescriptor;

    public MethodVisitorImpl(MethodVisitor mv, String className, String methodName, String methodDescriptor) {
        super(Opcodes.ASM7, mv);
        this.className = className;
        this.methodName = methodName;
        this.methodDescriptor = methodDescriptor;
    }

    @Override
    public void visitCode() {
        super.visitCode();
        mv.visitLdcInsn(className + "." + methodName);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "nanoTime", "()J", false);
        mv.visitVarInsn(Opcodes.LSTORE, 0);
    }

    @Override
    public void visitInsn(int opcode) {
        if ((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) || opcode == Opcodes.ATHROW) {
            mv.visitLdcInsn(className + "." + methodName);
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "nanoTime", "()J", false);
            mv.visitVarInsn(Opcodes.LLOAD, 0);
            mv.visitInsn(Opcodes.LSUB);
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;", false);
            mv.visitLdcInsn(className + "." + methodName + " executed in ");
            mv.visitVarInsn(Opcodes.LLOAD, 0);
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/String", "valueOf", "(J)Ljava/lang/String;", false);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        }
        super.visitInsn(opcode);
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        super.visitMaxs(maxStack + 4, maxLocals);
    }
}