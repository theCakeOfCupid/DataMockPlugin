package com.james.datamock.asm;


import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

/**
 * @author james
 * @date 2021/5/11
 */
public class DataMockClassVisitor extends ClassVisitor {
    private boolean isAmapCallBack;
    private final String mAmapLocationListenerPath = "com/amap/api/location/AMapLocationListener";
    private final String mMyLocationChangeListenerPath = "com/amap/api/maps/AMap$OnMyLocationChangeListener";

    public DataMockClassVisitor(int api, ClassVisitor classVisitor) {
        super(api, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        System.out.println("superName:" + superName);
        for (String interfaceName : interfaces) {
            System.out.println("interfaceName:" + interfaceName);
            if (interfaceName.equals(mAmapLocationListenerPath) || interfaceName.equals(mMyLocationChangeListenerPath)) {
                System.out.println("find amap callback: " + name);
                isAmapCallBack = true;
            }
        }
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if (isAmapCallBack) {
            MethodVisitor methodVisitor = cv.visitMethod(access, name, descriptor, signature, exceptions);
            return new DataMockAdapter(api, methodVisitor, access, name, descriptor);
        }
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }
}
