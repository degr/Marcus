package org.forweb.marcus.db;

import org.forweb.marcus.definers.TypeDefiner;
import org.forweb.marcus.sorter.StringSorter;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by rsmirnou on 7/31/2015. 55
 */
public class DB{

    private static Connection connection;
    private static List<String> tables;


    public static void init(String host, String database, String USER, String PASS){
        String DB_URL =  "jdbc:mysql://" + host + "/" + database;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connection to database established.");

            initTables(database);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private static void initTables(String database) throws SQLException {
        String query = "show tables from "+database;
        PreparedStatement st = connection.prepareStatement(query);
        st.execute();
        tables = getList(String.class, query);

    }

    public static <T> T getRow(Class<T> clazz, String field, Object value) throws SQLException {
        List<T> list = getList(clazz, field, value);
        return !list.isEmpty() ? list.get(0) : null;
    }


    private static List<String> detectTable(String className) {
        String[] name = className.split("\\.");
        return StringSorter.findCompatible(tables, name[name.length - 1]);
    }

    public static <T> T getRow(Class<T> clazz, int id) throws SQLException {
        return getRow(clazz, "id", id);
    }


    /**
     * Ger objects list using key-value pair
     * @param clazz return type class
     * @param field string, field name
     * @param value object, field value. Object.toString will be used
     * @param <T> return type
     * @return List of objects
     * @throws SQLException
     */
    public static <T> List<T> getList(Class<T> clazz, String field, Object value) throws SQLException {
        String[] name = clazz.getName().split("\\.");
        String query = "select * from " + detectTable(name[name.length - 1]).get(0) + " where "+ field + " = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1,  value.toString());

        return getList(clazz, statement);
    }

    /**
     * Ger objects list using key-value pair
     * @param clazz return type class
     * @param query sql query string
     * @param <T> return type
     * @return List of objects
     * @throws SQLException
     */
    public static <T> List<T> getList(Class<T> clazz, String query) throws SQLException  {
        return getList(clazz, connection.prepareStatement(query));
    }

    /**
     * Get all table
     * @param clazz return type class
     * @param <T> return type
     * @return List of objects
     * @throws SQLException
     */
    public static <T> List<T> getList(Class<T> clazz) throws SQLException  {
        String[] name = clazz.getName().split("\\.");
        String query = "select * from " + detectTable(name[name.length - 1]).get(0);
        return getList(clazz, query);
    }

    /**
     * Get list of objects using statement
     * @param clazz return type class
     * @param statement statement with sql query
     * @param <T> return type
     * @return list of objects
     * @throws SQLException
     */
    public static <T> List<T> getList(Class<T> clazz, PreparedStatement statement) throws SQLException  {
        statement.execute();
        ResultSet rs = statement.getResultSet();
        List<T> out = new ArrayList<>();
        while(rs.next()) {
            out.add(buildRow(clazz, rs));
        }
        return out;
    }

    /**
     * Build object usig class
     * @param clazz reutrn type class
     * @param resultSet result set on current iteration
     * @param <T> return generic type
     * @return object
     */
    private static <T> T buildRow(Class<T> clazz, ResultSet resultSet) {

        Object o = null;
        try {
            if(clazz.equals(String.class)) {
                return (T)resultSet.getString(1);
            }
            if(clazz.equals(Integer.class)) {
                return (T)new Integer(resultSet.getInt(1));
            }
            if(clazz.equals(Map.class)) {
                return (T)buildAsSimpleTable(resultSet);
            }
            ResultSetMetaData metaData = resultSet.getMetaData();
            o = clazz.getConstructor().newInstance();
            int count = metaData.getColumnCount();
            Field[] declared = clazz.getDeclaredFields();
            List<String> fieldNames = Arrays.stream(declared)
                    .map(Field::getName)
                    .collect(Collectors.toList());

            for(int i = 0 ; i < count; i++) {
                String columnName = metaData.getColumnName(i + 1);
                List<String> sortedNames = StringSorter.findCompatible(fieldNames, columnName);
                Object settedObject = resultSet.getObject(i + 1);
                Field f = TypeDefiner.defineField(declared, sortedNames, settedObject);
                if(f == null) {
                    System.out.println("Can't find class for name: " + columnName + " in " + clazz.getName());
                    continue;
                }
                try {
                    f.set(o, settedObject);
                } catch (IllegalArgumentException e) {
                    System.out.println("Data type mismatch: " +
                            (resultSet.getObject(i + 1) != null ? resultSet.getObject(i + 1).getClass() : "null"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T)o;
    }

    private static Map<String, String> buildAsSimpleTable(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int count = metaData.getColumnCount();
        Map<String, String> row = new LinkedHashMap<>();
        for(int i = 0 ; i < count; i++) {
            Object o = resultSet.getObject(i + 1);
            row.put(metaData.getColumnName(i + 1), o == null ? null : o.toString());
        }
        return row;
    }

    public static <T>T getCell(Class<T> clazz, String query) throws SQLException {
        List<T>list = getList(clazz, query);
        return list != null && !list.isEmpty() ? getList(clazz, query).get(0) : null;
    }

    public static void query(String query, String ... params) {
        PreparedStatement st = null;
        try {
            st = connection.prepareStatement(query);
            for(int i = 0; i < params.length; i++) {
                st.setString(i+1, params[i]);
            }
            st.execute();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
