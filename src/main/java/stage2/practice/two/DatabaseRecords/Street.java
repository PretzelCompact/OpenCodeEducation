package stage2.practice.two.DatabaseRecords;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Street implements DatabaseRecord {
    private long id;
    private String name;
    private String kind;
    private long localityId;

    @Override
    public Object clone(){
        var other = new Street();
        other.setKind(kind);
        other.setLocalityId(localityId);
        other.setName(name);
        other.setId(id);
        return other;
    }

    @Override
    public void readFromResultSet(ResultSet set) throws SQLException {
        id = set.getInt("id");
        name = set.getString("name");
        kind = set.getString("kind");
        localityId = set.getInt("localityId");
    }

    @Override
    public void setValuesToStatement(PreparedStatement statement) throws SQLException {
        int i=1;
        statement.setString(i++, getName());
        statement.setString(i++, getKind());
        statement.setLong(i++, getLocalityId());
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLocalityId() {
        return localityId;
    }

    public void setLocalityId(long localityId) {
        this.localityId = localityId;
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

        Street other = (Street) o;
        return id == other.id;
    }

    @Override
    public String toString(){
        return getKind() + "  \"" + getName() + "\"";
    }
}
