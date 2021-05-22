import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import resolveTestClasses.*;
import testClasses.IFoo;

import java.util.HashMap;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class DependencyInjectionTest {
    @Test
    public void checkIfCycleDetected() throws Exception {
        //given
        SimpleContainer container = new SimpleContainer();
        //when
        IllegalContainerRequest thrown = Assertions.
                assertThrows(IllegalContainerRequest.class, () -> container.resolveInstance(Bar5.class, new HashMap<>()));
    }

    @Test
    public void checkIfThereIsNoFalseCycleDetected() throws Exception {
        //given
        SimpleContainer container = new SimpleContainer();
        //when
        Bar1 bar1 = (Bar1) container.resolveInstance(Bar1.class, new HashMap<>());
        //then
        Assertions.assertEquals(bar1.getClass(), Bar1.class);
    }

}