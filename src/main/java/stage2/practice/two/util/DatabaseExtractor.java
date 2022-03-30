package stage2.practice.two.util;

import stage2.practice.two.DatabaseRecords.DatabaseRecord;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseExtractor {
    /*
    "Фабрика", которая извлекает классы из ResultSet и связывает их с классом
     */

    public static <T extends DatabaseRecord> List<T> extractList(T clazz, ResultSet set) throws SQLException{
        var list = new ArrayList<T>();
        while(set.next()){
            clazz.readFromResultSet(set);
            list.add((T) clazz.clone());
        }
        return list;
    }

    public static <T extends DatabaseRecord> T extractOne(T clazz, ResultSet set) throws SQLException{
        set.next();
        clazz.readFromResultSet(set);
        return clazz;
    }
}
