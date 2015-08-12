package org.forweb.marcus.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

/**
 * Created by rsmirnou on 8/11/2015. 45
 */
public class ReflectUtils {
    static HashMap<String, Field[]> classFields = new HashMap<>();

    public static Field[] getFields(Object o) {
        return getFields(o.getClass().getName());
    }
    public static Field[] getFields(Class clazz) {
        if(!classFields.containsKey(clazz.getName())) {
            classFields.put(clazz.getName(), clazz.getDeclaredFields());
        }
        return classFields.get(clazz.getName());
    }

    public static boolean isObjectModified(Field f) {
        return (f.getModifiers() & Modifier.STATIC) == 0 && (f.getModifiers() & Modifier.FINAL) == 0;
    }

    public static boolean isObjectRelated(Field f) {
        return (f.getModifiers() & Modifier.STATIC) == 0;
    }

    public static boolean isStatic(Field f){
        return (f.getModifiers() & Modifier.STATIC) == Modifier.STATIC;
    }

    public static boolean isInherit(Class candidate, Class parent) {
        return parent.isAssignableFrom(candidate);
    }
    public static boolean isNormalClass(Class clazz) {
        return !Modifier.isInterface(clazz.getModifiers()) && !Modifier.isAbstract(clazz.getModifiers());
    }
}
