import java.util.ArrayList;

public class Tile {
    private int number;
    private int jumpIndex;

    // types are "Snake", "Ladder", "Tile"
    private String name;

    private ArrayList<Player> players = new ArrayList<Player>();
    
    private String colour = Pallette.ANSI_RESET;

    public Tile (int numberSet, int jumpIndexSet) {
        number = numberSet;
        jumpIndex = jumpIndexSet;

        if (jumpIndexSet < -1) {
            throw new IllegalArgumentException("Jump index of tile cannot be below -1!");
        }

        updateName();
    }

    private void updateName() {
        if (jumpIndex == -1) {
            name = TileNames.tile;
        }
        else if (jumpIndex > number) {
            name = TileNames.ladder;
        } else if (jumpIndex < number) {
            name = TileNames.snake;
        }

        updateColour();
    }

    private void updateColour()  {
        switch(name) {
            case TileNames.ladder:
                colour = Pallette.ANSI_GREEN;
                break;
            case TileNames.snake:
                colour = Pallette.ANSI_RED;
                break;
            default:
                break;
        }
    }

    // --------- setters

    public void addPlayer(Player newPlayer) {
        players.add(newPlayer);
    }

    public void deletePlayer(Player deletePlayer) {

        int indexOfPlayer = players.indexOf(deletePlayer);
        players.remove(indexOfPlayer);
    }

    public void setName(String newName) {   
        name = newName;
    }

    public void setNumber(int newNumber) {
        number = newNumber;
        updateName();
    }
    public void setJumpIndex(int newJumpIndex) {
        jumpIndex = newJumpIndex;
        updateName();
    }
    public void setColour(String newColour) {
        colour = newColour;
    }

    // --------- getters

    public String getName() {
        return name;
    }

    public ArrayList<Player> getPlayers () {
        return players;
    }

    public int getNumber() {
        return number;
    }
    public int getJumpIndex() {
        return jumpIndex;
    }
    public String getColour() {
        return colour;
    }
}
