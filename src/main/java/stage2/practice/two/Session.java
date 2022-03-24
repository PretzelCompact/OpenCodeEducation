package stage2.practice.two;

import stage2.practice.two.DatabaseRecords.Locality;
import stage2.practice.two.DatabaseRecords.Street;
import stage2.practice.two.util.DatabaseConnector;
import stage2.practice.two.util.ILocalityManipulator;
import stage2.practice.two.util.ILocalitySelector;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Session implements ILocalitySelector, ILocalityManipulator, AutoCloseable{
    /*
    Устанавливает "Сессию" с БД(соединение через конкретного пользователя)
     */

    private ILocalitySelector selector;
    private ILocalityManipulator manipulator;
    private Connection connection;

    //Комит в соединение только тогда, когда нужно
    private boolean needCommit;
    private void commitIfNeeded() throws SQLException{
        if(needCommit){
            connection.commit();
            needCommit = false;
        }
    }

    public Session(DatabaseConnector connector, String username, String password){
        connection = connector.getConnection(username, password);
        selector = new LocalitySelector(connection);
        manipulator = new LocalityManipulator(connection);
        try{
            var statement = connection.createStatement();
            statement.execute("create table if not exists Localities(" +
                    "id identity not null," +
                    "square real, "+
                    "name varchar(255), " +
                    "kind varchar(255), " +
                    "establish_date date, "+
                    "population int,"+
                    "primary key(id))");
            statement.execute(
                    "create table if not exists Streets(" +
                    "id identity not null,name varchar(255)," +
                    "kind varchar(255)," +
                    "localityId bigint,"+
                    "primary key(id),"+
                    "foreign key(localityId) references Localities(id) "+
                    "on update Cascade " +
                    "on delete set null)");
            connection.setAutoCommit(false);
        } catch (SQLException e){
            System.out.println("Ошиба соединения с БД");
        }
    }

    @Override
    public void createLocalities(Locality... localities) throws SQLException {
        manipulator.createLocalities(localities);
        needCommit = true;
    }

    @Override
    public void createStreets(Street... streets) throws SQLException {
        manipulator.createStreets(streets);
        needCommit = true;
    }

    @Override
    public void updateLocalities(Locality... localities) throws SQLException {
        manipulator.updateLocalities(localities);
        needCommit = true;
    }

    @Override
    public void updateStreets(Street... streets) throws SQLException {
        manipulator.updateStreets(streets);
        needCommit = true;
    }

    @Override
    public void deleteLocalities(Locality... localities) throws SQLException {
        manipulator.deleteLocalities(localities);
        needCommit = true;
    }

    @Override
    public void deleteStreets(Street... streets) throws SQLException {
        manipulator.deleteStreets(streets);
        needCommit = true;
    }

    @Override
    public Locality getLocalityFromName(String name) throws SQLException {
        commitIfNeeded();
        return selector.getLocalityFromName(name);
    }

    @Override
    public List<Street> getStreetsOfLocality(long localityId) throws SQLException {
        commitIfNeeded();
        return selector.getStreetsOfLocality(localityId);
    }

    @Override
    public List<Street> getStreetsOfLocality(long localityId, String streetNamePattern) throws SQLException {
        commitIfNeeded();
        return selector.getStreetsOfLocality(localityId, streetNamePattern);
    }

    @Override
    public List<Locality> getLocalitiesFromQuery(LocalityQuery query) throws SQLException {
        commitIfNeeded();
        return selector.getLocalitiesFromQuery(query);
    }

    @Override
    public void close() throws Exception {
        commitIfNeeded();
        connection.close();
    }
}
