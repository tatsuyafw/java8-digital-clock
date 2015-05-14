package model;

import java.lang.reflect.Method;

public class MethodInfo {
    private String methodName;
    private String packageName;

    MethodInfo(Method method) {
        methodName = method.getName();
        packageName = method.getDeclaringClass().getPackage().getName();
    }

    public String getMethodName() {
        return methodName;
    }

    public String getPackageName() {
        return packageName;
    }

    @Override
    public String toString() {
        return methodName;
    }

}
