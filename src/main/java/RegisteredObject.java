import java.lang.reflect.Constructor;
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

    public void createInstance(Object... constructorParams) {
        Class<?> typeClass = null;
        try{
            typeClass = Class.forName(type.getTypeName());
        }catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            return;
        }

        Constructor<?> constructor = null;

        try{
            constructor = typeClass.getConstructor();
        }
        catch (NoSuchMethodException e)
        {
            e.printStackTrace();
            return;
        }

        try{
            instance = constructor.newInstance(constructorParams);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    public ObjectLifeCycle getLifeCycle() {
        return lifeCycle;
    }

    public void setLifeCycle(ObjectLifeCycle lifeCycle) {
        this.lifeCycle = lifeCycle;
    }
}
