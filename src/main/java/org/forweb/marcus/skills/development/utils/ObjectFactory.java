package org.forweb.marcus.skills.development.utils;

import java.util.List;
import java.util.Map;

/**
 * Created by rsmirnou on 8/11/2015. 36
 */
public interface ObjectFactory {
    Object build(String clazz, List<String> classNames);
}
