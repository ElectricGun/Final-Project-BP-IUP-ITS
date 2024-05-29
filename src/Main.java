import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner read = new Scanner(System.in);
        System.out.println();
        System.out.println("Welcome to the Snakes and Ladders !");
        System.out.println();
        System.out.println("The objective of this game is to reach the 100 case first !");
        System.out.println("But be careful ! If you step on a snake case, you will fall down...");
        System.out.println("But also if you step on a ladder case, you will go up !");
        System.out.println("The snake cases are colored in RED and ladder cases in GREEN");
        System.out.println("And the players will be displayed in BLUE.");
        System.out.println();
        System.out.println("Now, please enter the number of players (max 4).");

        // get the number of players
        int numPlayers = 0;
        while (true) {
            numPlayers = read.nextInt();
            if(numPlayers > 4) {
                System.out.println("Maximum number of players is 4!");
            } else if (numPlayers <= 0) {
                System.out.println("Number of players cannot be less than 1!");
            } else {
                break;
            }
            System.out.println("Please enter the number of players (max 4).");
        }

        Game game = new Game(numPlayers, 0);

        Dice dice = new Dice(6);

        System.out.println(
            "\n\n" +
             "Players: " + numPlayers + "\n" +
             "Board Width " + game.getBoardWidth() + "\n" +
             "Board Height " + game.getNumberOfTiles() / game.getBoardWidth() + "\n" +
             "Tile Size " + game.getTileSize()
        );

        game.printTable();

        while (game.isActive()) {

            for (int i = 0; i < numPlayers; i++) {

                Player currPlayer = game.getPlayer(i);
                // get input
                System.out.println(currPlayer.getName() + "'s turn! \nEnter anything to continue.");
                String rolling = read.nextLine();
                    
                while(rolling.isBlank())
                    rolling = read.nextLine();

                Functions.printLoop("\n", 15);

                // roll dice and move
                int moveSteps = dice.roll();
                
                System.out.println("\n" + currPlayer.getName() + " has rolled " + moveSteps);
                int previousCase = currPlayer.getCurrentCase();

                game.movePlayer(i, moveSteps);
                System.out.println(currPlayer.getName() + " has moved from " + (previousCase + 1) + " to " + (currPlayer.getCurrentCase() + 1));
                
                // -------- check if player has stepped on special case-

                while (true) {
                    previousCase = currPlayer.getCurrentCase();
                    int playerPosition = currPlayer.getCurrentCase();
                    Tile tileAtPosition = game.getTile(playerPosition);
                    String tileName = tileAtPosition.getName();

                    System.out.println(
                        game.getTile(currPlayer.getCurrentCase()).getColour() + currPlayer.getName() + " has stepped on a " + game.getTile(currPlayer.getCurrentCase()).getName() + " (Tile: " + (game.getTileIndex(game.getTile(currPlayer.getCurrentCase())) + 1) + ")" + Pallette.ANSI_RESET
                    );

                    if (tileName == TileNames.ladder || tileName == TileNames.snake) {
                        game.setPlayerPosition(i, tileAtPosition.getJumpIndex());
                        System.out.println(currPlayer.getName() + " has moved from " + (previousCase + 1) + " to " + (currPlayer.getCurrentCase() + 1));
                    } else {
                        // if the player has stepped on a normal tile, break the loop
                        break;
                    }
                }

                // check if anyone has won
                game.setWinner();

                Player winner = game.getWinner();

                if (winner != null) {
                    System.out.println("The winner is: " + winner.getName() + "!");
                    game.endGame();
                    break;
                }

                System.out.println();
                game.printTable();
            }
        }
    }
}
