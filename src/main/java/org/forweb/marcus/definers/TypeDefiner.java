package org.forweb.marcus.definers;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by rsmirnou on 8/4/2015. 47
 */
public class TypeDefiner {

    public static Field defineField(Field[] declared, List<String> sortedNames, Object settedObject) {
        Stream<Field> stream = Arrays.stream(declared);
        Class objectClass = settedObject.getClass();
        for(String fName : sortedNames) {
            Field current = stream.filter(v -> fName.equals(v.getName())).findFirst().get();
            if(objectClass.equals(current.getType())) {
                return current;
            }
        }
        return null;
    }
}
