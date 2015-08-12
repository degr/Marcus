package org.forweb.marcus.skills.development.utils;

import java.sql.Driver;

/**
 * Created by rsmirnou on 8/11/2015. 37
 */
public class Scope {
    public Driver driver;
    public String packageName;
    public String language;

    public Scope(String language, Driver driver, String packageName) {
        this.language = language;
        this.driver = driver;
        this.packageName = packageName;
    }
}
