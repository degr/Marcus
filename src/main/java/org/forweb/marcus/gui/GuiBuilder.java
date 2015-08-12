package org.forweb.marcus.gui;

import com.sun.org.apache.xpath.internal.operations.Mod;
import org.forweb.marcus.gui.builder.BooleanComponent;
import org.forweb.marcus.gui.builder.FloatComponent;
import org.forweb.marcus.gui.builder.NumberComponent;
import org.forweb.marcus.gui.builder.StringComponent;
import org.forweb.marcus.reflection.ReflectUtils;

import javax.swing.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Created by rsmirnou on 8/10/2015. 15
 */
public class GuiBuilder {
    public static JPanel buildGui(Field[] fields, Class objectClass, Object dto) {
        JPanel out = new JPanel();
        BoxLayout bl = new BoxLayout(out, BoxLayout.Y_AXIS);
        out.setLayout(bl);
        String[] className = objectClass.getName().split("\\.");
        if(className.length > 0) {
            out.setBorder(BorderFactory.createTitledBorder(className[className.length - 1]));
        }
        try{
            for(Field f : fields) {
                if(!ReflectUtils.isObjectModified(f)){
                    continue;
                }
                boolean accessible = f.isAccessible();
                if(!accessible) {
                    f.setAccessible(true);
                }
                Object value = dto != null ? f.get(dto) : null;
                Class fieldType = f.getType();
                if(isSimilar(String.class, fieldType)) {
                    out.add(new StringComponent(f.getName(), value != null ? (String)value : null));
                } else if(isSimilar(Integer.class, fieldType)) {
                    out.add(new NumberComponent<>(f.getName(), value != null ? (Integer)value : null));
                } else if(isSimilar(Long.class, fieldType)) {
                    out.add(new NumberComponent<>(f.getName(), value != null ? (Long)value : null));
                } else if(isSimilar(Float.class, fieldType)) {
                    out.add(new FloatComponent<>(f.getName(), value != null ? (Float)value : null));
                } else if(isSimilar(Double.class, fieldType)) {
                    out.add(new FloatComponent<>(f.getName(), value != null ? (Double)value : null));
                } else if(isSimilar(Boolean.class, fieldType)) {
                    out.add(new BooleanComponent(f.getName(), value != null ? (Boolean)value : false));
                } else {
                    out.add(buildGui(fieldType.getDeclaredFields(),fieldType,  value != null ? value : null));
                }
                if(!accessible) {
                    f.setAccessible(false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }

    private static boolean isSimilar(Class clazz, Class fieldType) {
        return clazz.equals(fieldType) || clazz.isInstance(fieldType);
    }


}
