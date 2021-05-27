import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;

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

    public Object resolveInstance(Class<?> classType, HashSet<Class<?>> forbiddenTypes) throws Exception {
        if(forbiddenTypes.contains(classType)) {
            throw new IllegalContainerRequest("Wystąpił cykl - obiekt typu " + classType.getTypeName()
                    + " potrzebuje obiektu typu " + classType.getTypeName());
        }
        else {
            forbiddenTypes.add(classType);
        }

        Constructor<?>[] constructors = classType.getConstructors();
        int constructorIndex = 0;
        int maxParametersNum = 0;
        int i = 0;
        for (Constructor<?> constructor: constructors) {
            int paramsNumber = constructor.getParameterTypes().length;
            if(paramsNumber > maxParametersNum) {
                constructorIndex = i;
                maxParametersNum = paramsNumber;
            }
            i++;
        }
        Constructor<?> chosenConstructor = constructors[constructorIndex];
        Class<?>[] paramsTypes = chosenConstructor.getParameterTypes();
        Object[] paramsInstances = new Object[maxParametersNum];
        for(int j=0; j<maxParametersNum; j++) {
            Type param = paramsTypes[j].getClass();
            if (registeredObjects.containsKey(param)) {
                Class<?> paramToResolve = Class.forName(registeredObjects
                        .get(param)
                        .getType()
                        .getTypeName());
                HashSet<Class<?>> forbiddenTypesCopy = new HashSet<>();
                forbiddenTypesCopy.addAll(forbiddenTypes);
                paramsInstances[j] = resolveInstance(paramToResolve, forbiddenTypesCopy);
            }
            else if(Modifier.isAbstract(classType.getModifiers()) || Modifier.isInterface(classType.getModifiers()))
            {
                throw new IllegalContainerRequest("Cannot inject non concrete type because it is not registered");
            }
            else {
                HashSet<Class<?>> forbiddenTypesCopy = new HashSet<>();
                forbiddenTypesCopy.addAll(forbiddenTypes);
                paramsInstances[j] = resolveInstance(paramsTypes[j], forbiddenTypesCopy);
            }

        }
        return chosenConstructor.newInstance(paramsInstances);
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
