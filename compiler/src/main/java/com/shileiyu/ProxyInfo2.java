package com.shileiyu;

import java.lang.reflect.Method;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

/**
 * @auther by yushilei.
 * @time 2017/7/26-21:50
 * @desc
 */

public class ProxyInfo2 {
    private String packageName;
    private String proxyClassName;
    private TypeElement typeElement;
    private VariableElement variableElement;

    public ProxyInfo2(Elements elementUtils, TypeElement classElement, VariableElement variableElement) {
        this.typeElement = classElement;
        this.variableElement = variableElement;
        PackageElement packageElement = elementUtils.getPackageOf(classElement);
        String packageName = packageElement.getQualifiedName().toString();
        //classname
        String className = ClassValidator.getClassName(classElement, packageName);
        this.packageName = packageName;
        this.proxyClassName = className + "$$" + "PROXY";
    }

    public String getProxyClassFullName() {
        return packageName + "." + proxyClassName;
    }

    public TypeElement getTypeElement() {
        return typeElement;
    }

    public String generateJavaCode() {
        TypeElement type = (TypeElement) variableElement.getEnclosingElement();

        StringBuilder builder = new StringBuilder();
        builder.append("// Generated code. Do not modify!\n");
        builder.append("package ").append(packageName).append(";\n\n");
        builder.append("import com.shileiyu.compilingannotation.bean.net.*;\n");
        builder.append("import com.shileiyu.compilingannotation.net.*;\n");
        builder.append("import " + type.getQualifiedName() + ";\n");
        builder.append('\n');

        builder.append("public class ").append(proxyClassName).append(" implements " + typeElement.getQualifiedName());
        builder.append(" {\n");
        builder.append("private int taskId;\n");
        builder.append("public " + proxyClassName + "(int taskId){" +
                "this.taskId=taskId;}\n");

        generateMethods(builder);
        Name qualifiedName = typeElement.getQualifiedName();

        builder.append(qualifiedName.toString()+"\n");
        try {
            Class<?> apiClass = Class.forName(qualifiedName.toString());
            Method[] methods = apiClass.getDeclaredMethods();
            for (Method m : methods) {
                builder.append(m.getName() + "/");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        builder.append('\n');

        builder.append("}\n");
        return builder.toString();
    }

    private void generateMethods(StringBuilder builder) {

    }
}
