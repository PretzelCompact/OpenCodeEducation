package stage2.practice.three.DatabaseRecords;

import stage2.practice.two.DatabaseRecords.DatabaseRecord;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GridRecord implements DatabaseRecord {
    private long id;
    private String name;
    private int width;
    private int height;
    private State state;

    @Override
    public void readFromResultSet(ResultSet set) throws SQLException {
        id = set.getLong("id");
        name = set.getString("name");
        width = set.getInt("width");
        height = set.getInt("height");
        state = State.valueOf(set.getString("state"));
    }

    @Override
    public void setValuesToStatement(PreparedStatement statement) throws SQLException {
        statement.setString(1,name);
        statement.setInt(2,width);
        statement.setInt(3,height);
        statement.setString(4,state.toString());
    }

    @Override
    public String toString(){
        String result = "name=" + name
                + "\nwidth=" + width
                + "\nheight=" + height
                + "\nstate=" + state.toString();
        return  result;
    }

    @Override
    public Object clone(){
        var other = new GridRecord();
        other.setState(state);
        other.setWidth(width);
        other.setHeight(height);
        other.setName(name);
        other.setId(id);
        return other;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public enum State{
        NotInitialized,
        Initialized,
        PathFromAStartCreated,
        PathFromBFSCreated,
        PathFromDFSCreated
    }
}
