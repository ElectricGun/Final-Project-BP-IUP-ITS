public class Player {
    private final int number;
    private String name = "Player ";
    private int currentTile;
    public Player(int number, int currentTile){
        this.number = number;
        this.name += number;
        this.currentTile = currentTile;
    }
    public int getNumber(){
        return this.number;
    }
    public String getName(){
        return this.name;
    }
    public int getCurrentCase(){
        return this.currentTile;
    }
    public void setCurrentCase(int n){
        this.currentTile = n;
    }
}
