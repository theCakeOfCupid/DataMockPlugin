package com.james.datamock.plugin

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.james.datamock.asm.DataMockClassVisitor
import groovy.io.FileType
import org.apache.commons.io.FileUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes

public class DataMockTransform extends Transform {

    @Override
    String getName() {
        return "DataMockTransform"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        def transformCollections = transformInvocation.inputs
        def outputProvider = transformInvocation.outputProvider
        if (null != outputProvider) {
            outputProvider.deleteAll()
        }
        try {
            transformCollections.each {
                transformInput ->
                    transformInput.directoryInputs.each {
                        directoryInput ->
                            def dir = directoryInput.file
                            if (dir) {
                                dir.traverse(type: FileType.FILES, nameFilter: ~/.*\.class/) {
                                    file ->
                                        handleFile(file)
                                }
                            }
                            //处理完输入文件后把输出传给下一个文件
                            def dest = outputProvider.getContentLocation(directoryInput.name, directoryInput.contentTypes,
                                    directoryInput.scopes, Format.DIRECTORY)
                            FileUtils.copyDirectory(directoryInput.file, dest)
                    }
                    //gradle 3.6之后的处理
                    transformInput.jarInputs.each { jarInput ->
                        def file = jarInput.file
                        System.out.println("find jar input: " + file.name)
                        //处理完输入文件后把输出传给下一个文件
                        def dest = outputProvider.getContentLocation(jarInput.name,
                                jarInput.contentTypes, jarInput.scopes, Format.JAR)
                        FileUtils.copyFile(file, dest)
                    }

            }
        } catch (Exception e) {
            println "james---" + e.getMessage()
            e.printStackTrace()
        }
        super.transform(transformInvocation)
    }

    private void handleFile(File file) {
        println "find class, name is ${file.name}"
        //读入class流
        ClassReader classReader = new ClassReader(file.bytes)
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
        //访问class文件相应的内容，解析到某一个结构就会通知到ClassVisitor的相应方法
        ClassVisitor classVisitor = new DataMockClassVisitor(Opcodes.ASM5, classWriter)
        //依次调用 ClassVisitor接口的各个方法
        classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
        //toByteArray方法会将最终修改的字节码以 byte 数组形式返回
        byte[] bytes = classWriter.toByteArray()
        ////通过文件流写入方式覆盖掉原先的内容，实现class文件的改写
        FileOutputStream fileOutputStream = new FileOutputStream(file.path)
        fileOutputStream.write(bytes)
        fileOutputStream.close()
    }

    private void handleJar(File file) {

    }
}