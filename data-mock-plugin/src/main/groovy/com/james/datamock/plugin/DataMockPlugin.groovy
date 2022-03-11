package com.james.datamock.plugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @author james* @date 2021/5/11
 */
public class DataMockPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        def android = project.getExtensions().getByType(AppExtension)
        def dataMockTransform = new DataMockTransform()
        android.registerTransform(dataMockTransform)
    }
}
