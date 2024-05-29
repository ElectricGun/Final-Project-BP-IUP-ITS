import java.lang.Math;
import java.util.ArrayList;

public class Game {
    // tile number -1 is the Starting Tile. All players first spawn in on that tile. This tile is not displayed
    // tile number 0 is the first tile
    // tiles are displayed using their number + 1
    private ArrayList<Tile> board = new ArrayList<Tile>();

    private final int boardHeight = 10;
    private final int boardWidth = 10;
    private final int numberOfTiles = boardHeight * boardWidth;
    private final int spacesPerTile;
    private boolean active = true;

    private Player winner;

    private final ArrayList<Player> players = new ArrayList<Player>();
    private final int[][] tileJumps = {
        // -- {tilePosition, jumpIndex}
        // ladders
        {2, 23}, {8, 34}, {20, 77}, {32, 68}, {41, 79}, {74, 88}, {82, 100}, {85, 95},
        // snakes
        {29, 9}, {38, 15}, {47, 5}, {53, 33}, {62, 37}, {86, 54}, {92, 70},  {97, 25}
    };

    public Game(int numPlayers, int tileSize) {

        // ----- set tile size

        spacesPerTile = Functions.getDigits10(numberOfTiles) + tileSize + 1;

        // ----- initialise players
        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player(i + 1, -1));
        }

        // ----- initialise the game

        Tile initialTile = new Tile(-1, -1);
        initialTile.setName(TileNames.startingTile);
        board.add(initialTile);

        // -- create the tiles
        for (int i = 0; i < numberOfTiles; i++ ) {
            board.add(new Tile(i, -1));
        }

        // -- set the tile jump indeces

        for (int [] tileJump : tileJumps) {
            int boardIndex = tileJump[0];
            int jumpIndex = tileJump[1];

            Tile newTile = board.get(boardIndex);
            newTile.setJumpIndex(jumpIndex);

            board.set(boardIndex, newTile);
        }
    }

    public Player getPlayer (int playerIndex) {
        return players.get(playerIndex);
    }

    public void setPlayerPosition(int playerIndex, int newPosition) {
        Player currPlayer = this.getPlayer(playerIndex);

        currPlayer.setCurrentCase(newPosition);
        players.set(playerIndex, currPlayer);
    }   

    public void movePlayer(int playerIndex, int steps) {
        Player currPlayer = this.getPlayer(playerIndex);

        int currPlayerPosition = currPlayer.getCurrentCase();
        int jumpTo = steps + currPlayerPosition;

        // active tiles are all tiles minus startingTile (tile #-1)
        int activeTiles = numberOfTiles - 1;

        if (jumpTo > activeTiles) {
            this.setPlayerPosition(playerIndex, activeTiles - (jumpTo - activeTiles));
        } else {
            this.setPlayerPosition(playerIndex, jumpTo);
        }

        // to prevent player from returning to starting tile or below

        if (currPlayer.getCurrentCase() <= -1) {
            this.setPlayerPosition(playerIndex, Math.abs(currPlayer.getCurrentCase()));
        }
    }

    public void setWinner() {
        for (Player player : players) {
            if (player.getCurrentCase() == numberOfTiles -1) {
                winner = player;
            }
        }
    }

    public boolean isActive() {
        return active;
    }

    public void endGame() {
        active = false;
    }

    public Player getWinner() {
        return winner;
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public int getNumberOfTiles() {
        return numberOfTiles;
    }

    public Tile getTile(int tileIndex) {
        return board.get(tileIndex);
    }

    public int getTileIndex(Tile tile) {
        return board.indexOf(tile);
    }

    public int getTileSize() {
        return spacesPerTile;
    }

    public void printTable () {
        /*
        for(Tile tile : board) {
            System.out.println(
                tile.getColour()+
                tile.getName() + "\t" +
                tile.getNumber() + 
                (tile.getJumpIndex() == -1 ? "" : "--->" + tile.getJumpIndex())
            );
        }
        */
        
        for (int y = 0; y < boardHeight; y++) {

            int yIndex = boardHeight - 1 - y;

            int startIndex = yIndex * boardWidth;

            int remainder = 0;

            for (int x = 0; x < boardWidth + remainder; x++) {

                int currentTileIndex = x + startIndex;
                if (yIndex % 2 == 1) {
                    currentTileIndex = (startIndex + boardWidth) - x - 1;
                }

                Tile currentTile = board.get(currentTileIndex);

                int tileDisplayNumber = currentTileIndex + 1;

                int currentTileDigits = Functions.getDigits10(tileDisplayNumber);

                if (currentTile.getNumber() == 0 || currentTile.getNumber() == -1) {
                    currentTileDigits = 1;
                }
                
                System.out.print(currentTile.getColour() + (tileDisplayNumber));

                Functions.printLoop(" ", spacesPerTile - currentTileDigits); 

                // this is to print the arrows
                /*
                if (currentJumpIndex == -1) {
                    System.out.print("\t");
                } else {
                    System.out.print("-->" + currentJumpIndex + Pallette.ANSI_RESET);
                }

                System.out.print(Pallette.ANSI_RESET + "\t");
                */
            }

            Functions.printLoop("\n", spacesPerTile / 2);

        }
    }
}
