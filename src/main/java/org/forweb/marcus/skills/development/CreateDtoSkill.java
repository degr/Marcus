package org.forweb.marcus.skills.development;

import org.forweb.marcus.Context;
import org.forweb.marcus.Marcus;
import org.forweb.marcus.db.Localization;
import org.forweb.marcus.reflection.ReflectUtils;
import org.forweb.marcus.skills.Skill;
import org.forweb.marcus.skills.development.java.JavaClassFinder;
import org.forweb.marcus.skills.development.utils.ClassFinder;
import org.forweb.marcus.skills.development.utils.ObjectFactory;
import org.forweb.marcus.skills.development.utils.Scope;
import org.forweb.marcus.sorter.StringSorter;

import java.util.List;

/**
 * Created by rsmirnou on 8/11/2015. 22
 */
public class CreateDtoSkill implements Skill {


    String dtoClass = null ;
    Scope scope = null;
    ClassFinder languageClassFinder;
    ObjectFactory languageObjectFactory;

    @Override
    public Object execute(Object ... arguments) {
        if(!validateArgumens(arguments)) {
            return null;
        }
        languageClassFinder = defineLanguageClassFinder();
        if(languageClassFinder == null) {
            return null;
        }
        languageObjectFactory = defineLanguageObjectFactory();
        if(languageObjectFactory == null) {
            return null;
        }
        List<String> classList = languageClassFinder.getClasses(scope.packageName, true);
        return languageObjectFactory.build(dtoClass, classList);
    }

    private ObjectFactory defineLanguageObjectFactory() {
        languageObjectFactory = null;
        List<String> thisPackage = Marcus.JAVA_CLASS_FINDER.getClasses(this.getClass().getPackage().getName() + "." + scope.language, true);
        List<String> classFinderNames = StringSorter.findCompatible(thisPackage, ClassFinder.class.getSimpleName());
        for(String classFinderName : classFinderNames) {
            try {
                Class classFinderClass = Class.forName(classFinderName);
                if(ReflectUtils.isInherit(classFinderClass, ClassFinder.class)){
                    return Marcus.marcus.createObjectSkill.createObject(
                            Marcus.JAVA_CLASS_FINDER.getClass(Marcus.BASE_PACKAGE + ".skills",
                                    ObjectFactory.class, scope.language + "ObjectFactory"));
                }
            } catch (ClassNotFoundException e) {
            }
        }
        return null;
    }

    private ClassFinder defineLanguageClassFinder() {
        List<String> thisPackage = Marcus.JAVA_CLASS_FINDER.getClasses(this.getClass().getPackage().getName() + "." + scope.language, true);
        List<String> classFinderNames = StringSorter.findCompatible(thisPackage, ClassFinder.class.getSimpleName());
        for(String classFinderName : classFinderNames) {
            try {
                Class classFinderClass = Class.forName(classFinderName);
                if(ReflectUtils.isInherit(classFinderClass, ClassFinder.class)){
                    return Marcus.marcus.createObjectSkill.createObject(
                            Marcus.JAVA_CLASS_FINDER.getClass(Marcus.BASE_PACKAGE + ".skills",
                                    ClassFinder.class, scope.language + "ClassFinder"));
                }
            } catch (ClassNotFoundException e) {
            }
        }
        return null;
    }

    private boolean validateArgumens(Object[] arguments) {

        for(Object arg : arguments) {
            if(arg instanceof Class) {
                dtoClass = (String)arg;
            }
            if(arg instanceof Scope) {
                scope = (Scope) arg;
            }
        }
        if(scope == null) {
            Marcus.out(Localization.getPhrase("dto_creation_fail") + " " + Localization.getPhrase("undefined_scope"));
            return false;
        }
        if(dtoClass == null) {
            Marcus.out(Localization.getPhrase("dto_creation_fail") + " " + Localization.getPhrase("undefined_dto_class"));
            return false;
        }
        return true;
    }
}
