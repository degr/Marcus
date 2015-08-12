package org.forweb.marcus.skills.development.utils;

import java.util.List;

/**
 * Created by rsmirnou on 8/11/2015. 17
 */
public interface Reader {
    List<String> getClasses(String fileContent);
    List<String> getInterfaces(String fileContent);
    List<String> getMethods(String fileContent);
}
