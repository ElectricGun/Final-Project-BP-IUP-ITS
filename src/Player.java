public class Player {
    private final int number;
    private final String colour;
    private String name = "Player ";
    private int currentTile;
    private int numberOfTurns = 0;
    private int score = 0;

    public Player(int number, int currentTile, String colour){
        this.number = number;
        this.name += number;
        this.currentTile = currentTile;
        this.colour = colour;
    }

    public int getNumberOfTurns() {
        return this.numberOfTurns;
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

    public String getColour() {
        return this.colour;
    }

    public void setNumberOfTurns(int n) {
        this.numberOfTurns = n;
    }

    public void addTurns(int n) {
        this.numberOfTurns += n;
    }

    public void setScore(int n) {
        this.score = n;
    }

    public void addScore(int n) {
        this.score = this.score + n;
    }
    
    public int getScore() {
        return this.score;
    }
    public void setCurrentCase(int n){
        this.currentTile = n;
    }
}
