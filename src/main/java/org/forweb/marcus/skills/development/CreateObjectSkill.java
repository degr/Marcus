package org.forweb.marcus.skills.development;

import com.thoughtworks.paranamer.Paranamer;
import org.forweb.marcus.Context;
import org.forweb.marcus.skills.Skill;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by rsmirnou on 8/11/2015. 04
 */
public class CreateObjectSkill implements Skill {

    private Paranamer paranamer;
    private Context context;

    public CreateObjectSkill(Context context, Paranamer paranamer) {
        this.context = context;
        this.paranamer = paranamer;
    }


    @Override
    public Object execute(Object ... arguments) {
        if(arguments.length == 0) {
            return null;
        }
        if(arguments[0] instanceof Constructor) {
            return createObject((Constructor) arguments[0]);
        } else if (arguments[0] instanceof Class){
            return createObject((Class) arguments[0]);
        }
        return null;
    }

    public <T> T createObject (Class<T> creationObjectClass) {
        Constructor[] constructors = creationObjectClass.getDeclaredConstructors();
        if(constructors.length == 1) {
            return createObject(constructors[0]);
        }
        List<Constructor> sortedConstructors = Arrays.stream(constructors)
                .sorted((v1, v2) -> v2.getParameterCount() - v1.getParameterCount())
                .collect(Collectors.toList());

        for(Constructor constructor : sortedConstructors) {
            T created = createObject(constructor);
            if(created != null) {
                return created;
            }
        }
        return null;
    }

    private Object[] getParamsByTypes(Class[] parameterTypes) {
        Object[] out = new Object[parameterTypes.length];
        for(int i = 0; i < parameterTypes.length; i++) {
            out[i] = context.getItem(parameterTypes[i]);
            if(out[i] == null){
                return null;
            }
        }
        return out;
    }

    private <O> O createObject(Constructor constructor) {
        try {
            int count = constructor.getParameterCount();
            if (count == 0) {
                return (O) constructor.newInstance();
            }
            String[] paramNames = paranamer != null ? paranamer.lookupParameterNames(constructor) : null;
            Object[] params;
            if (paramNames != null && paramNames.length == constructor.getParameterCount()) {
                params = getParamsByName(paramNames, constructor.getParameterTypes());
            } else {
                params = getParamsByTypes(constructor.getParameterTypes());
            }
            if (params == null) {
                return null;
            }
            return (O)constructor.newInstance(params);
        } catch (InvalidParameterException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
            return null;
        }
    }


    private Object[] getParamsByName(String[] paramNames, Class[] parameterTypes) {
        Object[] out = new Object[parameterTypes.length];
        for(int i = 0; i < parameterTypes.length; i++) {
            out[i] = context.getItem(parameterTypes[i], paramNames[i]);
            if(out[i] == null){
                return null;
            }
        }
        return out;
    }
}
