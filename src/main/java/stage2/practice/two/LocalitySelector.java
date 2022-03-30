package stage2.practice.two;

import stage2.practice.two.DatabaseRecords.Locality;
import stage2.practice.two.DatabaseRecords.Street;
import stage2.practice.two.util.DatabaseExtractor;
import stage2.practice.two.util.ILocalitySelector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LocalitySelector implements ILocalitySelector {
    private Connection connection;

    public LocalitySelector(Connection connection){
        this.connection = connection;
    }

    private List<Locality> getLocalities(ResultSet set) throws SQLException{
        return DatabaseExtractor.extractList(new Locality(), set);
    }

    private List<Street> getStreets(ResultSet set) throws SQLException{
        return DatabaseExtractor.extractList(new Street(), set);
    }

    @Override
    public Locality getLocalityFromName(String name) throws SQLException {
        var sql = "select * from Localities where name=?";

        var statement = connection.prepareStatement(sql);
        statement.setString(1, name);
        statement.execute();

        var loc = getLocalities(statement.getResultSet()).get(0);

        statement.close();
        return loc;
    }

    @Override
    public List<Street> getStreetsOfLocality(long localityId) throws SQLException {
        var sql = "select * from Streets where localityId=?";

        var statement = connection.prepareStatement(sql);
        statement.setLong(1, localityId);
        statement.execute();

        var result = getStreets(statement.getResultSet());
        statement.close();
        return result;
    }

    @Override
    public List<Street> getStreetsOfLocality(long localityId, String streetNamePattern) throws SQLException {
        var sql = "select * from Streets where localityId=? and name like ?";

        var statement = connection.prepareStatement(sql);
        statement.setLong(1, localityId);
        statement.setString(2, streetNamePattern);
        statement.execute();

        var result = getStreets(statement.getResultSet());
        statement.close();
        return result;
    }

    @Override
    public List<Locality> getLocalitiesFromQuery(LocalityQuery query) throws SQLException{
        var sql = query.toString();

        var statement = connection.createStatement();
        statement.execute(sql);
        var result = getLocalities(statement.getResultSet());

        statement.close();
        return result;
    }
}
