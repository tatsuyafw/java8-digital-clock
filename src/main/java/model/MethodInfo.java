package model;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MethodInfo {
    private boolean isStatic = false ;
    private String packageName;
    private String type;
    private String typeName;
    private String methodName;
    private Parameter[] parameters;
    private String returnType;

    MethodInfo(Method method) {
        Class<?> klass = method.getDeclaringClass();
        int mod = method.getModifiers();

        isStatic = Modifier.isStatic(mod);
        packageName = method.getDeclaringClass().getPackage().getName();
        type = klass.isInterface() ? "interface" : "class";
        typeName = klass.getSimpleName();
        methodName = method.getName();
        parameters = method.getParameters();
        returnType = method.getReturnType().getSimpleName();
    }

    public String getPackageName() {
        return packageName;
    }

    public String getMethodName() {
        return methodName;
    }

    public boolean isMatchedLambdaSignature(String[] params, String returnType) {
        String[] parameters = parametersToStringList(this.parameters).toArray(new String[this.parameters.length]);

        for (int i = 0; i < params.length; i++) {
            if (i >= parameters.length) return false;
            if ( ! params[i].equals(parameters[i]) ) return false;
        }

        if (returnType != null && ! returnType.equals(this.returnType) ) {
            return false;
        }

        return true;
    }

    private static List<String> parametersToStringList(Parameter[] parameters) {
        return Stream.of(parameters).map(parameter -> parameter.getType().getSimpleName()).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        String separator = isStatic ? "." : "::";

        return typeName + separator + methodName + "("
          + String.join(", ", parametersToStringList(parameters)) + ") -> " + returnType;
    }

}
