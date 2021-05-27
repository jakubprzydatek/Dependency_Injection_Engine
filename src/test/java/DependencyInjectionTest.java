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
        Bar3 bar3 = (Bar3) container.resolve(Bar3.class);
        //then
        Assertions.assertEquals(bar3.getClass(), Bar3.class);
    }

    @Test
    public void checkIfCycleDetected() {
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
        Bar1 bar1 = (Bar1) container.resolve(Bar1.class);
        //then
        Assertions.assertEquals(bar1.getClass(), Bar1.class);
    }

    @Test
    public void checkIfCycleLength4Detected() {
        //given
        SimpleContainer container = new SimpleContainer();
        String expectedMessage = "Cycle occured - object of type "+ Foo1.class.getTypeName() +
                " needs object of type " + Foo1.class.getTypeName() + "!";
        //when
        IllegalContainerRequest thrown = Assertions.
                assertThrows(IllegalContainerRequest.class, () -> container.resolve(Foo1.class));
        //then
        Assertions.assertEquals(thrown.getMessage(), expectedMessage);

    }

}