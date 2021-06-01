import resolveTestClasses.Bar1;
import resolveTestClasses.Bar2;
import resolveTestClasses.DependecyConstructor;
import testClasses.Foo;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println(Modifier.isAbstract(String.class.getModifiers()));
        System.out.println(Modifier.isInterface(String.class.getModifiers()));
    }


}
