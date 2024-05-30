import java.lang.Math;
import java.util.ArrayList;

public class Game {
    // tile number 0 is the Starting Tile. All players first spawn in on that tile. This tile is not displayed
    // tile number 1 is the first tile
    // tiles are displayed using their number + 1
    private ArrayList<Tile> board = new ArrayList<Tile>();

    public final int offset = 1;
    private final int boardHeight = 10;
    private final int boardWidth = 10;
    private final int numberOfTiles = boardHeight * boardWidth;
    private final int spacesPerTile;
    private boolean active = true;

    private Player winner;

    private final ArrayList<Player> players = new ArrayList<Player>();
    private final int[][] tileJumps = {
        // -- {tileIndex, jumpIndex}
        // ladders
        {2, 23}, {8, 34}, {20, 77}, {32, 68}, {41, 79}, {74, 88}, {82, 100}, {85, 95},
        // snakes
        {29, 9}, {38, 15}, {47, 5}, {53, 33}, {62, 37}, {86, 54}, {92, 70},  {97, 25}
    };

    public Game(int numPlayers, int tileScale) {

        // ----- set tile size

        spacesPerTile = (Math.max(Functions.getDigits10(numberOfTiles * 2), numPlayers * 2)) * tileScale + 3;

        // ----- initialise players
        String [] playerColours = {Pallette.ANSI_CYAN, Pallette.ANSI_PURPLE, Pallette.ANSI_RED, Pallette.ANSI_YELLOW};
        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player(i + 1, 0, playerColours[i % numPlayers]));
        }

        // ----- initialise the game

        // -- crate initial tile

        Tile initialTile = new Tile(0, -1);
        initialTile.setName(TileNames.startingTile);
        board.add(initialTile);

        // -- create the tiles
        for (int i = 1; i <= numberOfTiles; i++ ) {
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

        // active tiles is the max index of all tiles minus startingTile (tile 0)
        int activeTiles = numberOfTiles -1;

        if (jumpTo > activeTiles) {
            this.setPlayerPosition(playerIndex, activeTiles - (jumpTo - activeTiles));
        } else {
            this.setPlayerPosition(playerIndex, jumpTo);
        }

        // to prevent player from returning to starting tile or below

        if (currPlayer.getCurrentCase() <= 0) {
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
        }*/
        

        // get player positions

        int [] playerPositions = new int[players.size()];

        for (int p = 0; p < players.size(); p++) {
            playerPositions[p] = this.getPlayer(p).getCurrentCase();
        }
        
        for (int y = 0; y < boardHeight; y++) {

            int yIndex = boardHeight - 1 - y;

            int startIndex = yIndex * boardWidth;

            int remainder = 0;

            for (int x = 0; x < boardWidth + remainder; x++) {

                int currentTileIndex = x + startIndex;
                if (yIndex % 2 == 1) {
                    currentTileIndex = (startIndex + boardWidth) - x - 1;
                }
                currentTileIndex += offset;

                Tile currentTile = board.get(currentTileIndex);

                int tileDisplayNumber = currentTileIndex;
                int currentTileDigits = Functions.getDigits10(tileDisplayNumber);
                int currentJumpIndex = currentTile.getJumpIndex();

                if (currentTile.getNumber() <= 0) {
                    currentTileDigits = 1;
                }

                boolean doPrintNumber = true;
                int playersInTile = 0;

                // print players
                for (int p = 0; p < players.size(); p++) {
                    Player currPlayer = this.getPlayer(p);
                    if (playerPositions[p] == tileDisplayNumber) {
                        System.out.print(currPlayer.getColour() + "P" + (p + 1) + Pallette.ANSI_RESET);
                        doPrintNumber = false;
                        playersInTile ++;
                    }
                }
                if (playersInTile > 0) {
                    Functions.printLoop(" ", spacesPerTile - playersInTile * 2);
                }
                if (doPrintNumber) {

                    System.out.print(currentTile.getColour() + (tileDisplayNumber));

                    // this is to print the arrows
                    
                    if (currentJumpIndex == -1) {
                        Functions.printLoop(" ", spacesPerTile - currentTileDigits);
                    } else {
                        System.out.print("->" + currentJumpIndex + Pallette.ANSI_RESET);
                        Functions.printLoop(" ", spacesPerTile - currentTileDigits - 2 - Functions.getDigits10(currentJumpIndex));
                    }
                    
                    System.out.print(Pallette.ANSI_RESET);

                    
                }
            }

            Functions.printLoop("\n", spacesPerTile / 2);

        }
    }
}
