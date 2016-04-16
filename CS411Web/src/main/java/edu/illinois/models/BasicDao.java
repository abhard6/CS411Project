package edu.illinois.models;

import javax.xml.transform.Result;
import java.lang.reflect.ParameterizedType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nprince on 4/12/16.
 */
public abstract class BasicDao<T> {
    protected String tableName; // Note that the table name will always be the class name. This can be overriden

    abstract T singleResult(ResultSet r);

    protected MySql mySql;

    public BasicDao() {
        Class<T> type = (Class<T>)
                ((ParameterizedType)getClass()
                        .getGenericSuperclass())
                        .getActualTypeArguments()[0];
        tableName = type.getSimpleName();


        mySql = MySql.getInstance();
    }

    protected List<T> fromResultSet(ResultSet r) {
        ArrayList<T> results = new ArrayList<T>();
        try {
            while (r.next()) {
                results.add(singleResult(r));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }
    

    public void deleteById(long val) {
        mySql.executeUpdate("DELETE FROM " + tableName + " WHERE id=" + val);
    }

    public List<T> findById(long val) {
        ResultSet r = mySql.executeQuery("SELECT * FROM " + tableName + " WHERE id=\"" + val + "\"");
        return fromResultSet(r);
    }

    public List<T> findAll() {
        ResultSet r = mySql.executeQuery("SELECT * FROM " + tableName);
        return fromResultSet(r);
    }
}
