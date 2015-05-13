package nl.hz.ict.p2.zuul;

/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class, create a player and 
 *  call its "play" method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game. 
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.08.08
 */

public class Game 
{
	// dit is waar elke speler het spel binnenkomt
	private Room spawnArea;
	
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
    	createRooms();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room outside, theater, pub, lab, office;
      
        // create the rooms
        outside = new Room("outside the main entrance of the university");
        theater = new Room("in a lecture theater");
        pub = new Room("in the campus pub");
        lab = new Room("in a computing lab");
        office = new Room("in the computing admin office");
        
        // initialise room exits
        outside.setExit("east", theater);
        outside.setExit("south", lab);
        outside.setExit("west", pub);

        theater.setExit("west", outside);

        pub.setExit("east", outside);

        lab.setExit("north", outside);
        lab.setExit("east", office);

        office.setExit("west", lab);
        
        spawnArea = outside;  // start game outside

    }

    /**
     * Getter for the spawn area
     * @return the spawn area Room
     */
    public Room getSpawnArea() {
		return spawnArea;
	}

    /*
     * Test main methode. dit start het spel en je kunt als speler in de console spelen.
     */
    public static void main(String[] args) {
    	String name = "John Doe";
    	if (args.length>0)
    		name = args[0];
    	try {
			Game g = new Game();
			Player p = new Player(name, g, System.in, System.out);
			p.play();
		} catch (Exception e) {
			e.printStackTrace();
		}	
    }
}
