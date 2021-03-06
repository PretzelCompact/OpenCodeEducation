package stage2.practice.two.DatabaseRecords;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface DatabaseRecord extends Cloneable {
    void readFromResultSet(ResultSet set) throws SQLException;
    void setValuesToStatement(PreparedStatement statement) throws SQLException;
    Object clone();
}
