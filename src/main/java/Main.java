import resolveTestClasses.Bar1;
import resolveTestClasses.Bar2;
import testClasses.Foo;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class Main {
    public static void main(String[] args) throws Exception {
       SimpleContainer container = new SimpleContainer();
       container.registerType(Bar1.class, true);
       container.registerType(Bar2.class, true);
       Bar1 result = (Bar1) container.resolveInstance(Bar1.class, new HashMap<Class<?>, HashSet<Class<?>>>());
        System.out.println(result);
    }


}
