package org.forweb.marcus.skills.development.java;

import org.forweb.marcus.Marcus;
import org.forweb.marcus.reflection.ReflectUtils;
import org.forweb.marcus.skills.development.utils.ClassFinder;
import org.forweb.marcus.sorter.StringSorter;

import java.io.File;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rsmirnou on 8/11/2015. 42
 */
public class JavaClassFinder implements ClassFinder{
    private static final char DOT = '.';

    private static final char SLASH = '/';

    private static final String CLASS_SUFFIX = ".class";

    private static final String BAD_PACKAGE_ERROR = "Unable to get resources from path '%s'. Are you sure the package '%s' exists?";

    @Override
    public List<String> getClasses(String scannedPackage, boolean recursively) {
        String scannedPath = scannedPackage.replace(DOT, SLASH);
        URL scannedUrl = Thread.currentThread().getContextClassLoader().getResource(scannedPath);
        if (scannedUrl == null) {
            Marcus.out(String.format(BAD_PACKAGE_ERROR, scannedPath, scannedPackage));
        }
        File scannedDir = new File(scannedUrl.getFile());
        List<String> classes = new ArrayList<>();
        for (File file : scannedDir.listFiles()) {
            classes.addAll(find(file, scannedPackage, recursively, true));
        }
        return classes;
    }

    @Override
    public List<String> getPackages(String scannedPackage, boolean recursively) {
        String scannedPath = scannedPackage.replace(DOT, SLASH);
        URL scannedUrl = Thread.currentThread().getContextClassLoader().getResource(scannedPath);
        List<String> packages = new ArrayList<>();
        if (scannedUrl == null) {
            Marcus.out(String.format(BAD_PACKAGE_ERROR, scannedPath, scannedPackage));
            return packages;
        }
        File scannedDir = new File(scannedUrl.getFile());
        for (File file : scannedDir.listFiles()) {
            packages.addAll(find(file, scannedPackage, recursively, false));
        }
        return packages;
    }

    private static List<String> find(File file, String scannedPackage, boolean recursively, boolean getClasses) {
        List<String> out = new ArrayList<>();
        String resource = scannedPackage + DOT + file.getName();
        if (file.isDirectory()) {
            if(!getClasses) {
                out.add(resource);
            }
            if(recursively) {
                for (File child : file.listFiles()) {
                    out.addAll(find(child, resource, true, getClasses));
                }
            }
        } else if (resource.endsWith(CLASS_SUFFIX) && getClasses) {
            int endIndex = resource.length() - CLASS_SUFFIX.length();
            String className = resource.substring(0, endIndex);
            try {
                Class.forName(className);
                out.add(className);
            } catch (ClassNotFoundException ignore) {
            }
        }
        return out;
    }

    public <C>Class<C> getClass(String packageName, Class<C> clazz, String className) {
        List<String> classNames = getClasses(packageName, true);
        List<String> sorted = StringSorter.findCompatible(classNames, className);
        for(String currentClassName : sorted) {
            try {
                Class current = Class.forName(currentClassName);
                if(!ReflectUtils.isInherit(current, clazz)) {
                    continue;
                }
                if (ReflectUtils.isNormalClass(current)) {
                    continue;
                }
                return current;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
