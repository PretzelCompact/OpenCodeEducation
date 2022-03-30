package stage2.practice.two.DatabaseRecords;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Locality implements DatabaseRecord {
    private long id;
    private float square;
    private String name;
    private String kind;
    private int population;
    private Date establishmentDate;

    @Override
    public Object clone(){
        var other = new Locality();
        other.setId(id);
        other.setEstablishmentDate(establishmentDate);
        other.setKind(kind);
        other.setName(name);
        other.setPopulation(population);
        other.setSquare(square);
        return other;
    }
    @Override
    public void readFromResultSet(ResultSet set) throws SQLException {
        id = set.getInt("id");
        name = set.getString("name");
        kind = set.getString("kind");
        population = set.getInt("population");
        establishmentDate = set.getDate("establish_date");
    }

    @Override
    public void setValuesToStatement(PreparedStatement statement) throws SQLException {
        statement.setFloat(1,getSquare());
        statement.setString(2,getName());
        statement.setString(3, getKind());
        statement.setDate(4,establishmentDate);
        statement.setInt(5,getPopulation());
    }

    public float getSquare() {
        return square;
    }

    public void setSquare(float square) {
        this.square = square;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Date getEstablishmentDate() {
        return establishmentDate;
    }

    public void setEstablishmentDate(Date establishmentDate) {
        this.establishmentDate = establishmentDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    @Override
    public int hashCode(){
        return Long.hashCode(id);
    }

    @Override
    public boolean equals(Object o){
        if(this == o)
            return true;

        if(o == null)
            return false;

        if(getClass() != o.getClass())
            return false;

        Locality other = (Locality)o;
        return  id == other.id;
    }

    @Override
    public String toString(){
        return getKind() + "  \"" + getName() + "\"";
    }
}
