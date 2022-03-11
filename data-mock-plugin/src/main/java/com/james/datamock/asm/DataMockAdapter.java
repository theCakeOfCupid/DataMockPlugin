package com.james.datamock.asm;


import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * @author james
 * @date 2021/5/12
 */
public class DataMockAdapter extends AdviceAdapter {
    private String methodName = "";
    private static final String ON_LOCATION_CHANGED = "onLocationChanged";
    private static final String ON_MY_LOCATION_CHANGE = "onMyLocationChange";

    protected DataMockAdapter(int i, MethodVisitor methodVisitor, int i1, String s, String s1) {
        super(i, methodVisitor, i1, s, s1);
        methodName = s;
    }

    @Override
    public void visitCode() {
        super.visitCode();
        if (methodName.equals(ON_LOCATION_CHANGED) || methodName.equals(ON_MY_LOCATION_CHANGE)) {
            System.out.println("methodName:" + methodName);
            mv.visitFieldInsn(GETSTATIC, "com/james/datamock/DataMock", "INSTANCE",
                    "Lcom/james/datamock/DataMock;");
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKEVIRTUAL, "com/james/datamock/DataMock", "mockLocationIfNeeded", "(Ljava/lang/Object;)V", false);
        }
//        mv.visitLdcInsn("TAG");
//        mv.visitLdcInsn("james---->");
//        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "android/util/Log", "i", "(Ljava/lang/String;Ljava/lang/String;)I", false);
//        mv.visitInsn(Opcodes.POP);
    }

    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();
    }

    @Override
    protected void onMethodExit(int i) {
        super.onMethodExit(i);
    }
}
