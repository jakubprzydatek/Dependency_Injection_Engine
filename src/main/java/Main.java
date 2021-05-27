import resolveTestClasses.Bar1;
import resolveTestClasses.Bar2;
import resolveTestClasses.DependecyConstructor;
import testClasses.Foo;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class Main {
    public static void main(String[] args) throws Exception {
       Constructor<?>[] barConstructors = Bar1.class.getConstructors();
        /*for (Constructor<?> constructor:
             barConstructors) {
            System.out.println(constructor.getAnnotationsByType(DependecyConstructor.class));
        }*/
    }


}
