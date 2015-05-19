package nl.hz.ict.p2.zuul;

public class Zombie extends GameObject {

	private long speed;

	public Zombie(String name, Game world) {
		super(name, world);
		speed = (long) (500 + Math.random() * 1000);
        this.setCurrentRoom(getWorld().getSpawnArea());
		ZombieThread thread = new ZombieThread();
		thread.start();
	}

	@Override
	public void handleRoomEvent(String event) {
		// TODO add Zombie behaviour when entering a room

	}

	private class ZombieThread extends Thread {

		public ZombieThread() {
			super();
			this.setDaemon(true);
		}

		@Override
		public void run() {
			while (true) {
				Room currentRoom = getCurrentRoom();
				if (currentRoom != null) {
					String[] exits = currentRoom.getExits();
					String direction = exits[(int) (Math.random() * exits.length)];
					setCurrentRoom(currentRoom.getExit(direction));
				}
				try {
					Thread.sleep(speed);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

}
