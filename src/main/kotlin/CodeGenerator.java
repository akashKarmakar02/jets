import com.amberj.ast.StatementNode;
import com.amberj.node.ConsoleLogNode;
import com.amberj.node.LiteralNode;
import com.amberj.node.VariableDeclarationNode;
import com.amberj.node.VariableReferenceNode;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;

import java.util.List;

public class CodeGenerator {
    private ClassWriter classWriter;

    public byte[] generate(List<StatementNode> ast) {
        classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        // Define the class
        classWriter.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, "GeneratedClass", null, "java/lang/Object", null);

        // Generate constructor
        generateConstructor();

        // Generate main method
        generateMainMethod(ast);

        classWriter.visitEnd();
        return classWriter.toByteArray();
    }

    // Generate constructor
    private void generateConstructor() {
        MethodVisitor constructor = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        constructor.visitCode();
        constructor.visitVarInsn(Opcodes.ALOAD, 0);
        constructor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        constructor.visitInsn(Opcodes.RETURN);
        constructor.visitMaxs(1, 1);
        constructor.visitEnd();
    }

    // Generate the main method
    private void generateMainMethod(List<StatementNode> ast) {
        MethodVisitor mv = classWriter.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
        mv.visitCode();

        // Visit each statement in the AST and generate bytecode
        for (StatementNode statement : ast) {
            if (statement instanceof VariableDeclarationNode) {
                generateVariableDeclaration(mv, (VariableDeclarationNode) statement);
            } else if (statement instanceof ConsoleLogNode) {
                generateConsoleLog(mv, (ConsoleLogNode) statement);
            }
        }

        mv.visitInsn(Opcodes.RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }

    // Generate bytecode for variable declarations
    private void generateVariableDeclaration(MethodVisitor mv, VariableDeclarationNode node) {
        // For simplicity, we only support integers for now
        if (node.getInitializer() instanceof LiteralNode && ((LiteralNode) node.getInitializer()).getValue() instanceof Integer) {
            int value = (int) ((LiteralNode) node.getInitializer()).getValue();
            mv.visitIntInsn(Opcodes.BIPUSH, value);
            mv.visitVarInsn(Opcodes.ISTORE, 1);  // Store the variable in local variable 1
        }
    }

    // Generate bytecode for console.log() (System.out.println)
    private void generateConsoleLog(MethodVisitor mv, ConsoleLogNode node) {
        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");

        // Handle literal or variable argument for log
        if (node.getArgument() instanceof LiteralNode && ((LiteralNode) node.getArgument()).getValue() instanceof Integer) {
            int value = (int) ((LiteralNode) node.getArgument()).getValue();
            mv.visitIntInsn(Opcodes.BIPUSH, value);
        } else if (node.getArgument() instanceof VariableReferenceNode) {
            mv.visitVarInsn(Opcodes.ILOAD, 1);  // Load variable from local variable 1
        }

        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);
    }
}
