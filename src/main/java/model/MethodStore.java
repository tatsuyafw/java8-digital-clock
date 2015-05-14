package model;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import lib.ClassFinder;

public class MethodStore {
    public static List<MethodInfo> allMethodInfo(String rootPackage) {
        Set<Class<?>> klasses = ClassFinder.getClasses(rootPackage);

        List<MethodInfo> methods = new ArrayList<>();

        for (Class<?> klass : klasses) {
            for (Method method : klass.getDeclaredMethods() ){
                methods.add(new MethodInfo(method));
            }
        }
        return methods;
    }
}
