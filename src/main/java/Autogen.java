import org.mdkt.compiler.InMemoryJavaCompiler;

import java.lang.reflect.Method;

/**
 * Created by rsmirnou on 7/31/2015. 06
 */
public class Autogen {
    public static void main(String ... args) throws Exception {
        StringBuffer sourceCode = new StringBuffer();
        sourceCode.append("package org.mdkt;\n");
        sourceCode.append("public class HelloClass {\n");
        sourceCode.append("   public String hello() { return \"hello\"; }");
        sourceCode.append("}");

        Class<?> helloClass = InMemoryJavaCompiler.compile("org.mdkt.HelloClass", sourceCode.toString());

        Object o = helloClass.getDeclaredConstructor().newInstance();
        Method m = helloClass.getDeclaredMethod("hello");
        System.out.println(m.invoke(o));
    }
}
