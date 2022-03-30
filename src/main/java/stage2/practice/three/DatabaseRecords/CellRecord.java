package stage2.practice.three.DatabaseRecords;

import stage2.practice.three.pathfinding.Cell;
import stage2.practice.two.DatabaseRecords.DatabaseRecord;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CellRecord implements DatabaseRecord {
    private long id;
    private int x;
    private int y;
    private Cell.State state;
    private long gridId;

    @Override
    public void readFromResultSet(ResultSet set) throws SQLException {
        setId(set.getLong("id"));
        setX(set.getInt("x"));
        setY(set.getInt("y"));
        setState(Cell.State.valueOf(set.getString("state")));
        setGridId(set.getLong("gridId"));
    }

    @Override
    public void setValuesToStatement(PreparedStatement statement) throws SQLException {
        statement.setInt(1, getX());
        statement.setInt(2, getY());
        statement.setString(3, getState().toString());
        statement.setLong(4, getGridId());
    }

    @Override
    public Object clone(){
        var other = new CellRecord();
        other.setX(x);
        other.setY(y);
        other.setId(id);
        other.setState(state);
        other.setGridId(gridId);
        return other;
    }

    public void readFromCell(Cell cell, long id, long gridId){
        x = cell.getX();
        y = cell.getY();
        state = cell.getState();
        this.id = id;
        this.gridId = gridId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Cell.State getState() {
        return state;
    }

    public void setState(Cell.State state) {
        this.state = state;
    }

    public long getGridId() {
        return gridId;
    }

    public void setGridId(long gridId) {
        this.gridId = gridId;
    }
}
