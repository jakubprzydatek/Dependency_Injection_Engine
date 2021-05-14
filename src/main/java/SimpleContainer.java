import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.HashMap;

public class SimpleContainer {

    private HashMap<Type, RegisteredObject> registeredObjects = new HashMap<>();

    public void registerType(Type type, boolean singleton)
    {
        if(registeredObjects.containsKey(type)) {

            registeredObjects.get(type).setLifeCycle(singleton ? ObjectLifeCycle.SINGLETON : ObjectLifeCycle.TRANSIENT);
        }
        else {
            registerType(type, type, singleton);
        }
    }

    public void registerType(Type from, Type to, boolean singleton) {
        String fromClass = from.getTypeName();
        String toClass = to.getTypeName();
        try{
            if(!Class.forName(fromClass).isAssignableFrom(Class.forName(toClass))) {
                throw new IllegalArgumentException();
            }
            else {
                System.out.println("ZAEBYŚCIE");
                registeredObjects.put(from, new RegisteredObject(to,
                        singleton ? ObjectLifeCycle.SINGLETON : ObjectLifeCycle.TRANSIENT));
            }
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Ekksepszyn");
        }
    }

    public Object resolve(Type type) {

        if(!registeredObjects.containsKey(type)) {
            Class<?> typeClass = null;
            try {
                typeClass = Class.forName(type.getTypeName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            int modifier = typeClass.getModifiers();
            if(Modifier.isAbstract(modifier) || Modifier.isInterface(modifier)) {
                throw new IllegalArgumentException(); //TODO ten wyjątek
            }
            Object objToReturn = null;
            try {
                objToReturn = typeClass.getConstructor().newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            return objToReturn;
        }
        else {
            RegisteredObject regObj = registeredObjects.get(type);
            if(regObj.getInstance() == null || regObj.getLifeCycle() == ObjectLifeCycle.TRANSIENT) {
                regObj.createInstance();
            }
            return regObj.getInstance();
        }

    }

}
