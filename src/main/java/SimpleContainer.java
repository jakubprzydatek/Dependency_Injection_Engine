import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.HashMap;

public class SimpleContainer {

    private final HashMap<Type, RegisteredObject> registeredObjects = new HashMap<>();

    public void registerType(Type type, boolean singleton)
            throws IllegalContainerRequest, ClassNotFoundException {
        if(registeredObjects.containsKey(type)) {

            registeredObjects.get(type).setLifeCycle(singleton ? ObjectLifeCycle.SINGLETON : ObjectLifeCycle.TRANSIENT);
        }
        else {
            registerType(type, type, singleton);
        }
    }

    public void registerType(Type from, Type to, boolean singleton)
            throws ClassNotFoundException, IllegalContainerRequest {
        String fromClass = from.getTypeName();
        String toClass = to.getTypeName();

        if(!Class.forName(fromClass).isAssignableFrom(Class.forName(toClass))) {
            throw new IllegalContainerRequest("First argument is not assignable from second argument!");
        }

        registeredObjects.put(from, new RegisteredObject(to,
                singleton ? ObjectLifeCycle.SINGLETON : ObjectLifeCycle.TRANSIENT));
    }

    public Object resolve(Type type)
            throws ClassNotFoundException, IllegalContainerRequest, NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException {

        if(!registeredObjects.containsKey(type)) {
            Class<?> typeClass = Class.forName(type.getTypeName());

            int modifier = typeClass.getModifiers();
            if(Modifier.isAbstract(modifier) || Modifier.isInterface(modifier)) {
                throw new IllegalContainerRequest("Not concrete type is not resolvable!");
            }

            return typeClass.getConstructor().newInstance();
        }
        else {
            RegisteredObject regObj = registeredObjects.get(type);
            if(regObj.getInstance() == null || regObj.getLifeCycle() == ObjectLifeCycle.TRANSIENT) {
                regObj.createInstance();
            }
            return regObj.getInstance();
        }

    }

    public void registerInstance(Type type, Object instance) throws IllegalContainerRequest, ClassNotFoundException {
        if(!registeredObjects.containsKey(type)) {
            registerType(type, true);
        }
        else {
            registeredObjects.get(type).setLifeCycle(ObjectLifeCycle.SINGLETON);
        }
        registeredObjects.get(type).setInstance(instance);

    }


}
