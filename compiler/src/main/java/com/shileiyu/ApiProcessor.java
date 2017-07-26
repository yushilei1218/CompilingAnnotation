package com.shileiyu;

import com.google.auto.service.AutoService;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;

/**
 * @author shilei.yu
 * @since on 2017/7/26.
 */
@AutoService(Processor.class)
public class ApiProcessor extends AbstractProcessor {
    private Filer mFileUtils;
    private Elements mElementUtils;
    private Messager mMessager;
    private ProxyInfo2 proxyInfo2;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFileUtils = processingEnv.getFiler();
        mElementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotationTypes = new LinkedHashSet<String>();
        annotationTypes.add(RetrofitProxy.class.getCanonicalName());
        annotationTypes.add(RetrofitTarget.class.getCanonicalName());
        return annotationTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }


    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        System.out.println("process");
        Set<? extends Element> types = roundEnvironment
                .getElementsAnnotatedWith(RetrofitProxy.class);
        Set<? extends Element> elements = roundEnvironment
                .getElementsAnnotatedWith(RetrofitTarget.class);
        if (types.size() != 1 || elements.size() != 1) {
            return false;
        }
//        TypeElement next = (TypeElement) types.iterator().next();
//        ProxyInfo info = new ProxyInfo(mElementUtils, next);
        System.out.println("process ok");
        Element type = types.iterator().next();
        Element variable = elements.iterator().next();
        boolean isType = type instanceof TypeElement;
        boolean isVariable = variable instanceof VariableElement;
        if (isType && isVariable) {
            TypeElement type1 = (TypeElement) type;
            VariableElement variable1 = (VariableElement) variable;
            proxyInfo2 = new ProxyInfo2(mElementUtils, type1, variable1);

            JavaFileObject sourceFile = null;
            try {
                sourceFile = mFileUtils.createSourceFile(
                        proxyInfo2.getProxyClassFullName(), proxyInfo2.getTypeElement());
                Writer writer = sourceFile.openWriter();
                writer.write(proxyInfo2.generateJavaCode());
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return true;
        }

        return false;
    }
}
