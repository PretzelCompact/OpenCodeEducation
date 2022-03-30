package stage2.practice.three.pathfinding;

public class Cell {
    private State state;
    private int x;
    private int y;

    public Cell(int x, int y, State state){
        this.x = x;
        this.y = y;
        this.state = state;
    }
    public Cell(int x, int y){
        this(x,y,State.EMPTY);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public State getState(){
        return state;
    }
    public void setState(State state){
        this.state = state;
    }

    @Override
    public boolean equals(Object o){
        if(this == o)
            return true;

        if(o == null)
            return false;

        if(getClass() != o.getClass())
            return false;

        Cell other = (Cell) o;
        return x == other.x && y == other.y;
    }

    @Override
    public int hashCode(){
        return Integer.hashCode(x) * Integer.hashCode(y);
    }

    public enum State{
        EMPTY(" "),
        BLOCKED("X"),
        START("S"),
        END("E"),
        PATH("*");

        State(String str){
            this.str = str;
        }

        private String str;
        public String getStr(){
            return str;
        }
    }
}
