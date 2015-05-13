package lib;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

public class ClassFinder {

    /**
     * Example
     */
    /*
    public static void main(String[] args) {
        getClasses("java").stream().sorted((a, b) -> {
            return a.getName().compareTo(b.getName());
        }).forEach(System.out::println);
    }
    */

    /**
     * See: http://qiita.com/kei2100/items/a9ba32a86bb0e685ecc7
     */
    public static Set<Class<?>> getClasses(String packageName) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        JavaFileManager jfm = compiler.getStandardFileManager(new DiagnosticCollector<JavaFileObject>(), null, null);

        Set<JavaFileObject.Kind> kind = new HashSet<JavaFileObject.Kind>();
        kind.add(JavaFileObject.Kind.CLASS);

        boolean recursive = true;
        Set<Class<?>> classes = new HashSet<>();

        try {
            for (JavaFileObject jfo : jfm.list(StandardLocation.PLATFORM_CLASS_PATH, packageName, kind, recursive)) {
                String uri = null;

                try {
                    uri = jfo.toUri().toString();
                    String uriDotSeparate = uri.replaceAll(File.separator, ".");

                    int indexOfPkgNmBegin = uriDotSeparate.indexOf(packageName);
                    String fqcnWithExtension = uriDotSeparate.substring(indexOfPkgNmBegin); // FQCN: Fully Qualified Class Name
                    String fqcn = fqcnWithExtension.replaceFirst(".class$", "");

                    Class<?> klass = Class.forName(fqcn);
                    classes.add(klass);
                } catch (NoClassDefFoundError e) {
                    System.err.println("get class failed. " + uri);
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    System.err.println("get class failed. " + uri);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.err.println("list class failed. packageName=" + packageName);
            e.printStackTrace();
        }

        return classes;
    }

    /**
     * See: http://unageanu.hatenablog.com/entry/20100107/1262869452
    private static void printer() {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        JavaFileManager fm = compiler.getStandardFileManager(
            new DiagnosticCollector<JavaFileObject>(), null, null);

        Set<JavaFileObject.Kind> kind = new HashSet<JavaFileObject.Kind>(){{
            add(JavaFileObject.Kind.CLASS);
        }};

        try {
            for ( JavaFileObject f : fm.list(StandardLocation.PLATFORM_CLASS_PATH, "java.io", kind, false)) {
                System.out.println( f.getName() );
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }
     */

}
