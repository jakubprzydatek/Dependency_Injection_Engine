import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import resolveTestClasses.*;
import testClasses.IFoo;

import java.util.HashMap;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class DependencyInjectionTest {
    @Test
    public void simpleNoCycleTest() throws Exception {
        //given
        SimpleContainer container = new SimpleContainer();
        //when
        Bar3 bar3 = (Bar3) container.resolveInstance(Bar3.class, new HashSet<>());
        //then
        Assertions.assertEquals(bar3.getClass(), Bar3.class);
    }

    @Test
    public void checkIfCycleDetected() throws Exception {
        //given
        SimpleContainer container = new SimpleContainer();
        //when
        IllegalContainerRequest thrown = Assertions.
                assertThrows(IllegalContainerRequest.class, () -> container.resolveInstance(Bar5.class, new HashSet<>()));
    }

    @Test
    public void checkIfThereIsNoFalseCycleDetected() throws Exception {
        //given
        SimpleContainer container = new SimpleContainer();
        //when
        Bar1 bar1 = (Bar1) container.resolveInstance(Bar1.class, new HashSet<>());
        //then
        Assertions.assertEquals(bar1.getClass(), Bar1.class);
    }

    @Test
    public void checkIfCycleLength4Detected() throws Exception {
        //given
        SimpleContainer container = new SimpleContainer();
        //when
        Foo1 foo1 = (Foo1) container.resolveInstance(Foo1.class, new HashSet<>());
        //then
        Assertions.assertEquals(foo1.getClass(), Foo1.class);

    }

}