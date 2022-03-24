package stage2.practice.two.util;

import stage2.practice.two.DatabaseRecords.Locality;
import stage2.practice.two.DatabaseRecords.Street;
import stage2.practice.two.LocalityQuery;

import java.sql.SQLException;
import java.util.List;

public interface ILocalitySelector {
    /*
    Обеспечивает команды DDL для Locality и Street
     */
    Locality getLocalityFromName(String name) throws SQLException;
    List<Street> getStreetsOfLocality(long localityId) throws SQLException;
    List<Street> getStreetsOfLocality(long localityId, String streetNamePattern) throws SQLException;
    List<Locality> getLocalitiesFromQuery(LocalityQuery query) throws SQLException;

}
