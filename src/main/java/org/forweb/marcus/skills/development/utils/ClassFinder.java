package org.forweb.marcus.skills.development.utils;

import java.util.List;

/**
 * Created by rsmirnou on 8/11/2015. 08
 */
public interface ClassFinder {

    List<String> getClasses(String scannedPackage, boolean recursively);
    List<String> getPackages(String scannedPackage, boolean recursively);
}
