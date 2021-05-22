import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import testClasses.*;

class SimpleContainerTest {

    @Test
    public void checkIFReturnsFooObjectWithoutRegistering() throws Exception {
        //given
        SimpleContainer container = new SimpleContainer();
        //when
        Foo fooInc = (Foo)container.resolve(Foo.class);
        //then
        Assertions.assertEquals(fooInc.getClass(), Foo.class);
    }

    @Test
    public void checkIFReturnsFooObjectWithRegistering() throws Exception {
        //given
        SimpleContainer container = new SimpleContainer();
        //when
        container.registerType(Foo.class,false);
        Foo fooInc = (Foo)container.resolve(Foo.class);
        //then
        Assertions.assertEquals(fooInc.getClass(), Foo.class);
    }

    @Test
    public void checkIFReturnsSingletonFooObject() throws Exception {
        //given
        SimpleContainer container = new SimpleContainer();
        //when
        container.registerType(Foo.class,true);
        Foo fooInc1 = (Foo)container.resolve(Foo.class);
        Foo fooInc2 = (Foo)container.resolve(Foo.class);
        //then
        Assertions.assertEquals(fooInc1.getClass(), Foo.class);
        Assertions.assertEquals(fooInc1, fooInc2);
    }

    @Test
    public void checkIFReturnsTransientFooObject() throws Exception {
        //given
        SimpleContainer container = new SimpleContainer();
        //when
        container.registerType(Foo.class,false);
        Foo fooInc1 = (Foo)container.resolve(Foo.class);
        Foo fooInc2 = (Foo)container.resolve(Foo.class);
        //then
        Assertions.assertEquals(fooInc1.getClass(), Foo.class);
        Assertions.assertNotEquals(fooInc1, fooInc2);
    }

    @Test
    public void checkIFThrowsExceptionWhenInstanceOfNotConcreteTypeRequested()
    {
        //given
        SimpleContainer container = new SimpleContainer();
        //when
        IllegalContainerRequest thrown = Assertions.
                assertThrows(IllegalContainerRequest.class, () -> container.resolve(IFoo.class));
        //then
        Assertions.assertEquals("Not concrete type is not resolvable!", thrown.getMessage());
    }

    @Test
    public void checkIfReturnsObjectOfClassAssignableFromInterface() throws Exception {
        //given
        SimpleContainer container = new SimpleContainer();
        //when
        container.registerType(IFoo.class, Foo.class, false);
        IFoo foo = (IFoo)container.resolve(IFoo.class);
        //then
        Assertions.assertEquals(foo.getClass(), Foo.class);
    }

    @Test
    public void checkIfReturnsObjectOfClassAssignableFromAbstractClass() throws Exception {
        //given
        SimpleContainer container = new SimpleContainer();
        //when
        container.registerType(AbstractFoo.class, ConcreteFoo.class, false);
        AbstractFoo foo = (AbstractFoo)container.resolve(AbstractFoo.class);
        //then
        Assertions.assertEquals(foo.getClass(), ConcreteFoo.class);
    }

    @Test
    public void checkIfReturnsSingletonObjectOfClassAssignableFromInterfaceOrAbstractClass() throws Exception {
        //given
        SimpleContainer container = new SimpleContainer();
        //when
        container.registerType(IFoo.class, Foo.class, true);
        IFoo foo1 = (IFoo)container.resolve(IFoo.class);
        IFoo foo2 = (IFoo)container.resolve(IFoo.class);
        //then
        Assertions.assertEquals(foo1, foo2);
        Assertions.assertEquals(foo1.getClass(), Foo.class);
    }

    @Test
    public void checkIfReturnsTransientObjectOfClassAssignableFromInterfaceOrAbstractClass() throws Exception {
        //given
        SimpleContainer container = new SimpleContainer();
        //when
        container.registerType(IFoo.class, Foo.class, false);
        IFoo foo1 = (IFoo)container.resolve(IFoo.class);
        IFoo foo2 = (IFoo)container.resolve(IFoo.class);
        //then
        Assertions.assertNotEquals(foo1, foo2);
        Assertions.assertEquals(foo1.getClass(), Foo.class);
    }

    @Test
    public void checkIfOnlyLastRegisterMatters() throws Exception {
        //given
        SimpleContainer container = new SimpleContainer();
        //when
        container.registerType(IFoo.class, Foo.class, false);
        container.registerType(IFoo.class, FooSecond.class, false);

        IFoo foo = (IFoo)container.resolve(IFoo.class);
        //then
        Assertions.assertEquals(foo.getClass(), FooSecond.class);
    }

    @Test
    public void checkIfThrowsExceptionWhenFirstIsNotAssignableFromSecond() {
        //given
        SimpleContainer container = new SimpleContainer();
        //when
        IllegalContainerRequest thrown = Assertions.
                assertThrows(IllegalContainerRequest.class,
                        () -> container.registerType(IFoo.class, String.class, false));
        //then
        Assertions.assertEquals("First argument is not assignable from second argument!", thrown.getMessage());
    }

    @Test
    public void checkIfReturnsRegisteredInstance() throws Exception {
        //given
        SimpleContainer container = new SimpleContainer();
        Foo fooInstance = new Foo(5);//TODO usunuąć 5
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
        Foo fooInstance = new Foo(5);//TODO usunuąć 5
        //when
        container.registerType(IFoo.class, Foo.class, true);
        container.registerInstance(IFoo.class, fooInstance);
        IFoo fooContainerInstance = (IFoo) container.resolve(IFoo.class);
        //then
        Assertions.assertEquals(fooInstance, fooContainerInstance);
    }


}