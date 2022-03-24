package stage2.practice.two;

import stage2.practice.two.DatabaseRecords.DatabaseRecord;
import stage2.practice.two.DatabaseRecords.Locality;
import stage2.practice.two.DatabaseRecords.Street;
import stage2.practice.two.util.ILocalityManipulator;

import java.sql.Connection;
import java.sql.SQLException;

public class LocalityManipulator implements ILocalityManipulator {
    private Connection connection;

    public LocalityManipulator(Connection connection){
        this.connection = connection;
    }

    private void processOverRecords(String SQL, DatabaseRecord... records) throws SQLException{
        var statement = connection.prepareStatement(SQL);

        for(var rec : records){
            rec.setValuesToStatement(statement);
            statement.addBatch();
        }
        statement.executeBatch();
        statement.close();
    }

    @Override
    public void createLocalities(Locality... localities) throws SQLException {
        var sql = "insert into Localities (square,name,kind,establish_date,population) values (?,?,?,?,?)";
        processOverRecords(sql, localities);
    }

    @Override
    public void createStreets(Street... streets) throws SQLException{
        var sql = "insert into Streets (name, kind, localityId) values(?,?,?)";
        processOverRecords(sql, streets);
    }

    @Override
    public void updateLocalities(Locality ... localities) throws SQLException{
        var sql = "update Localities set square=?, name=?, kind=?, establish_date=?, population=?";
        processOverRecords(sql, localities);
    }

    @Override
    public void updateStreets(Street ... streets) throws SQLException{
        var sql = "update Localities set name=?, kind=?, localityId=?";
        processOverRecords(sql,streets);
    }

    @Override
    public void deleteLocalities(Locality ... localities) throws SQLException{
        var sql = "delete from Localities where id=?";

        var statement = connection.prepareStatement(sql);

        for(var loc : localities){
            statement.setLong(1, loc.getId());
            statement.addBatch();
        }
        statement.executeBatch();
        statement.close();
    }

    @Override
    public void deleteStreets(Street ... streets) throws SQLException{
        var sql = "delete from Streets where id=?";

        var statement = connection.prepareStatement(sql);

        for(var s : streets){
            statement.setLong(1, s.getId());
            statement.addBatch();
        }
        statement.executeBatch();
        statement.close();
    }
}
