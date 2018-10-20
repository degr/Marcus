package org.forweb.marcus;

import com.mysql.jdbc.Connection;
import org.apache.commons.lang3.StringUtils;
import org.forweb.marcus.sorter.StringSorter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by rsmirnou on 8/11/2015. 00
 */
public class Context {
    private Map<String, List<Pair<String, Object>>> context = new HashMap<>();

    public void addItem(String name, Object object){
        Class clazz = object.getClass();
        if(!context.containsKey(clazz.getName())) {
            context.put(clazz.getName(), new ArrayList<>());
        }
        context.get(clazz.getName()).add(new Pair<>(name, object));
    }

    public <T> T getItem(Class clazz, String name){
        List<Pair<String, Object>> list = context.get(clazz.getName());
        if(list == null || list.isEmpty()) {
            return null;
        }
        List<String> names = list.stream().map(v -> v.key).collect(Collectors.toList());
        List<String> compatibleNames = StringSorter.findCompatible(names, name);
        for(String compatible : compatibleNames) {
            for(Pair<String, Object> item : list) {
                if(item.key.equals(compatible)) {
                    return (T)item.item;
                }
            }
        }
        return null;
    }

    public  <T> T getItem(Class clazz){
        List<Pair<String, Object>> list = context.get(clazz.getName());
        if(list == null || list.isEmpty()) {
            return null;
        }
        return (T)list.get(0);
    }
    public <T>List<T> getItemsList(Class<T> clazz){
        return (List<T>)(context.get(clazz.getName()).stream().map(v -> v.item).collect(Collectors.toList()));
    }





}
