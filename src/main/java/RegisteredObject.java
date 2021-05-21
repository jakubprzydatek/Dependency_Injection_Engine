import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

public class RegisteredObject {
    private Type type;
    private Object instance;
    private ObjectLifeCycle lifeCycle;

    public RegisteredObject(Type type, ObjectLifeCycle lifeCycle) {
        this.type = type;
        this.lifeCycle = lifeCycle;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }


    public Object getInstance() {
        return instance;
    }

    public void createInstance(Object... constructorParams)
            throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException,
            InstantiationException, IllegalAccessException {
        Class<?> typeClass = typeClass = Class.forName(type.getTypeName());
        Constructor<?> constructor = typeClass.getConstructor();
        instance = constructor.newInstance(constructorParams);
    }

    public void setInstance(Object instance)
    {
        this.instance = instance;
    }

    public ObjectLifeCycle getLifeCycle() {
        return lifeCycle;
    }

    public void setLifeCycle(ObjectLifeCycle lifeCycle) {
        this.lifeCycle = lifeCycle;
    }
}
