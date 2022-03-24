package stage2.practice.two.util;

import stage2.practice.two.DatabaseRecords.Locality;
import stage2.practice.two.DatabaseRecords.Street;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseExtractor {
    /*
    "Фабрика", которая извлекает классы из ResultSet и связывает их с классом
     */

    public static Object extract(Class clazz, ResultSet set) throws SQLException{
        Object result;
        if(clazz == Street.class){
            var street = new Street();
            street.readFromResultSet(set);
            result = street;
        } else if(clazz == Locality.class){
            var locality = new Locality();
            locality.readFromResultSet(set);
            result = locality;
        } else{
            result = null;
            throw new RuntimeException(clazz + " не определён в базе");
        }
        return result;
    }
}
