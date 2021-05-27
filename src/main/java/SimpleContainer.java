import resolveTestClasses.DependecyConstructor;

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

    public Object resolve(Type type) throws IllegalContainerRequest, ClassNotFoundException,
            InvocationTargetException, InstantiationException, IllegalAccessException {
        return resolveInstance(Class.forName(type.getTypeName()), new HashSet<>());

    }

    public Object resolveInstance(Class<?> classType, HashSet<Class<?>> forbiddenTypes)
            throws IllegalContainerRequest, ClassNotFoundException, InvocationTargetException,
            InstantiationException, IllegalAccessException {
        Class<?> rootClassType = classType;
        boolean objectRegistered = registeredObjects.containsKey(classType);
        boolean singleton = false;
        if(objectRegistered) {
            singleton = registeredObjects.get(classType).getLifeCycle() == ObjectLifeCycle.SINGLETON;
        }

        if(forbiddenTypes.contains(classType)) {
            throw new IllegalContainerRequest("Cycle occured - object of type " + classType.getTypeName()
                    + " needs object of type " + classType.getTypeName() + "!");
        }
        else {
            forbiddenTypes.add(classType);
        }

        if(objectRegistered && singleton) {
            if(registeredObjects.get(classType).getInstance() != null) {
                return registeredObjects.get(classType).getInstance();
            }
        }

        if(Modifier.isAbstract(classType.getModifiers()) || Modifier.isInterface(classType.getModifiers())) {
            if(objectRegistered) {
                classType = Class.forName(registeredObjects.get(classType).getType().getTypeName());

            }
            else {
                throw new IllegalContainerRequest("Not concrete type is not resolvable!");
            }
        }

        Constructor<?>[] constructors = classType.getConstructors();
        int constructorIndex = 0;
        int maxParametersNum = 0;
        int i = 0;
        boolean foundAnnotation = false;
        for (Constructor<?> constructor: constructors) {
            int paramsNumber = constructor.getParameterTypes().length;
            if(constructor.getAnnotation(DependecyConstructor.class) != null) {
                if(foundAnnotation) {
                    throw new IllegalContainerRequest("Found more than one DependencyConstructor annotations!");
                }
                constructorIndex = i;
                maxParametersNum = paramsNumber;
                foundAnnotation = true;
            }
            if(paramsNumber > maxParametersNum && !foundAnnotation) {
                constructorIndex = i;
                maxParametersNum = paramsNumber;
            }
            i++;
        }
        Constructor<?> chosenConstructor = constructors[constructorIndex];
        Class<?>[] paramsTypes = chosenConstructor.getParameterTypes();
        Object[] paramsInstances = new Object[maxParametersNum];
        for(int j=0; j<maxParametersNum; j++) {
            HashSet<Class<?>> forbiddenTypesCopy = new HashSet<>(forbiddenTypes);
            paramsInstances[j] = resolveInstance(paramsTypes[j], forbiddenTypesCopy);
        }

        if(objectRegistered) {
            registeredObjects.get(rootClassType).setInstance(chosenConstructor.newInstance(paramsInstances));
            return registeredObjects.get(rootClassType).getInstance();
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
