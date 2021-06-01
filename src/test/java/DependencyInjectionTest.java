import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import resolveTestClasses.*;
import testClasses.Foo;
import testClasses.IFoo;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class DependencyInjectionTest {
    @Test
    public void checkIfReturnsRegisteredInstance() throws Exception {
        //given
        SimpleContainer container = new SimpleContainer();
        Foo fooInstance = new Foo();
        //when
        container.registerInstance(IFoo.class, fooInstance);
        IFoo fooContainerInstance = (IFoo) container.resolve(IFoo.class);
        //then
        Assertions.assertEquals(fooInstance, fooContainerInstance);
    }

    @Test
    public void checkIfReturnsRegisteredInstanceAfterTypeRegistration() throws Exception {
        //given
        SimpleContainer container = new SimpleContainer();
        Foo fooInstance = new Foo();
        //when
        container.registerType(IFoo.class, Foo.class, true);
        container.registerInstance(IFoo.class, fooInstance);
        IFoo fooContainerInstance = (IFoo) container.resolve(IFoo.class);
        //then
        Assertions.assertEquals(fooInstance, fooContainerInstance);
    }

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
        String expectedMessage = "Cycle occured - object of type " + Bar5.class.getTypeName()
                + " needs object of type " + Bar5.class.getTypeName() + "!";
        //when
        IllegalContainerRequest thrown = Assertions.
                assertThrows(IllegalContainerRequest.class, () -> container.resolve(Bar5.class));

        //then
        Assertions.assertEquals(thrown.getMessage(), expectedMessage);
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

    @Test
    public void checkIfSingletonWorks() throws Exception {
        //given
        SimpleContainer container = new SimpleContainer();
        //when
        Bar3 bar3Instance = new Bar3();
        container.registerInstance(Bar3.class, bar3Instance);
        Bar2 bar2 = (Bar2) container.resolve(Bar2.class);
        //then
        Assertions.assertEquals(bar2.bar3, bar3Instance);
    }

    @Test
    public void checkIfTransientWorks() throws Exception {
        //given
        SimpleContainer container = new SimpleContainer();
        //when
        Bar2 bar2 = (Bar2) container.resolve(Bar2.class);
        Bar2 bar22 = (Bar2) container.resolve(Bar2.class);
        //then
        Assertions.assertNotEquals(bar2, bar22);
        Assertions.assertNotEquals(bar2.bar3, bar22.bar3);
    }

    @Test
    public void checkIfAnnotationDetected()throws Exception {
        //given
        SimpleContainer container = new SimpleContainer();
        //when
        Bar1 bar1 = (Bar1) container.resolve(Bar1.class);
        //then
        Assertions.assertEquals(bar1.getClass(), Bar1.class);
    }

    @Test
    public void checkIfThrowsExceptionWhenMoreThanOneAnnotation() {
        //given
        SimpleContainer container = new SimpleContainer();
        //when
        IllegalContainerRequest thrown = Assertions.assertThrows(IllegalContainerRequest.class, () ->  container.resolve(Bar6.class));
        //then
        Assertions.assertEquals(thrown.getMessage(), "Found more than one DependencyConstructor annotations!");
    }
    

}