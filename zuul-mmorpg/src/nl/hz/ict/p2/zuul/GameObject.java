package nl.hz.ict.p2.zuul;

public abstract class GameObject {

	protected String name;
	private Room currentRoom;
	private Game world;

	public GameObject(String name, Game world) {
		this.name = name;
		this.world = world;
	}

	public Room getCurrentRoom() {
		return currentRoom;
	}

	public void setCurrentRoom(Room currentRoom) {
		if (this.currentRoom != null)
			this.currentRoom.removeObject(this);
		this.currentRoom = currentRoom;
		if (this.currentRoom != null)
			this.currentRoom.addObject(this);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public Game getWorld() {
		return world;
	}

	public abstract void handleRoomEvent(String event);

}