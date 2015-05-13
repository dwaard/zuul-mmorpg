package nl.hz.ict.p2.zuul;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This class is the new main class of the MMORPG version of the "World of Zuul"
 * application. "World of Zuul" is a very simple, text based adventure game.
 * 
 * Users can walk around some scenery. That's all. It should really be extended
 * to make it more interesting!
 * 
 * To play this game, start the server by calling the main method of this class.
 * Then connect to the server with a simple terminal client like PuTTy.
 * 
 * @author waar0003
 *
 */
public class Server {

	public static void main(String[] args) {
		Game game = new Game();
		int portNumber = 4444;
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(portNumber);
			while (true) {
				System.out.println("Waiting for connection...");
				Socket clientSocket = serverSocket.accept();
				System.out.println("Connection accepted");
				String name = clientSocket.getRemoteSocketAddress().toString();
				Player player = new Player(name, game,
						clientSocket.getInputStream(),
						clientSocket.getOutputStream());
				System.out.println("Player " + name + " joined the game.");
				player.play();
				System.out.println("Player " + name + " left the game.");
				clientSocket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (serverSocket != null)
				try {
					serverSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		System.out.println("Program closed!");
	}

}
