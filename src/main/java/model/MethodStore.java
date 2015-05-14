package model;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MethodStore {

    private List<MethodInfo> methodData;

    public MethodStore() {
        methodData = allMethodInfo();
    }

    public List<MethodInfo> search(String query) {
        // TODO: not implementd yet.
        // ここで全体の中から条件に合うものだけをフィルタする
        return methodData;
    }

    private static List<MethodInfo> allMethodInfo() {
        // TODO: not implementd yet.
        // すべての Method Info を取得する

        List<MethodInfo> methods = new ArrayList<>();
        String pack = "java.lang.";

        try {
            for (Method method : Class.forName(pack + "Integer").getDeclaredMethods() ){
                methods.add(new MethodInfo(method));
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        return methods;
    }
}
