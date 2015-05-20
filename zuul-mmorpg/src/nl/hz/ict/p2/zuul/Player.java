package nl.hz.ict.p2.zuul;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.NoSuchElementException;


/**
 *  Player - Holds all state for a single user that plays the game
 *  
 *  It evaluates and executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.08.08
 */

public class Player extends GameObject {
	
    private Parser parser;
    
    private PrintStream out;
    
    /**
     * Create the game and initialise its internal map.
     */
    public Player(String name, Game game, InputStream externalInputStream, OutputStream externalOutputStream) 
    {
    	super(name, game);
        out = new PrintStream(externalOutputStream);
        parser = new Parser(externalInputStream);
        PlayerThread thread = new PlayerThread();
        thread.start();
    }
    
    /**
     * 
     */
    private class PlayerThread extends Thread {
    	
    	public PlayerThread() {
    		super();
    		this.setDaemon(true);
    	}
    	
    	@Override
    	public void run() {
    		System.out.println("Player " + name + " joined the game.");
			play();
			System.out.println("Player " + name + " left the game.");
    	}
    	
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        this.setCurrentRoom(getWorld().getSpawnArea());
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            out.print("> ");     // print prompt, verplaatst van de parser naar hier 
            
            try {
	            Command command = parser.getCommand();
	            finished = processCommand(command);
            }
            catch(NoSuchElementException e) {
            	System.out.println("Player " + name + " lost connection");
            	finished = true;
            }
        }
        out.println("Thank you for playing. Good bye.");
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
        out.println(getCurrentRoom().getLongDescription()); // Het printen verplaatst vanuit de Room klasse. 
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
        Room currentRoom = getCurrentRoom();
        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            out.println("There is no door!");
        }
        else {
            setCurrentRoom(nextRoom);
            out.println(getCurrentRoom().getLongDescription());
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

	@Override
	public void handleRoomEvent(String event) {
		out.println(event);
	}


}
