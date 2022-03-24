package stage2.practice.two.util;

import stage2.practice.two.DatabaseRecords.Locality;
import stage2.practice.two.DatabaseRecords.Street;

import java.sql.SQLException;

public interface ILocalityManipulator {
    /*
    Обеспечивает команды DML для Locality и Street
     */
    void createLocalities(Locality... localities) throws SQLException;
    void createStreets(Street... streets) throws SQLException;
    void updateLocalities(Locality ... localities) throws SQLException;
    void updateStreets(Street ... streets) throws SQLException;
    void deleteLocalities(Locality ... localities) throws SQLException;
    void deleteStreets(Street ... streets) throws SQLException;
}
