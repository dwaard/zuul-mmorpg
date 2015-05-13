package nl.hz.ict.p2.zuul;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;


/**
 *  Player - Holds all state for a single user that plays the game
 *  
 *  It evaluates and executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.08.08
 */

public class Player {
	
	private String name;
	
	private Game world;
	
    private Parser parser;
    private Room currentRoom;
    
    private PrintStream out;
    
    /**
     * Create the game and initialise its internal map.
     */
    public Player(String name, Game game, InputStream externalInputStream, OutputStream externalOutputStream) 
    {
    	this.name = name;
    	this.world = game;
        out = new PrintStream(externalOutputStream);
        parser = new Parser(externalInputStream);
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        currentRoom = world.getSpawnArea();
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            out.print("> ");     // print prompt, verplaatst van de parser naar hier
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        out.println();
        out.println("Welcome " + name + " to the World of Zuul!");
        out.println("World of Zuul is a new, incredibly boring adventure game.");
        out.println("Type 'help' if you need help.");
        out.println();
        out.println(currentRoom.getLongDescription()); // Het printen verplaatst vanuit de Room klasse. 
    }

	/**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        // else command not recognised.
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        out.println("You are lost. You are alone. You wander");
        out.println("around at the university.");
        out.println();
        out.println("Your command words are:");
        out.println(parser.showCommands()); // Het printen verplaatst vanuit de Parser (en CommandWords) klasse.
    }

    /** 
     * Try to in to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            out.println("There is no door!");
        }
        else {
            currentRoom = nextRoom;
            out.println(currentRoom.getLongDescription());
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }


}
