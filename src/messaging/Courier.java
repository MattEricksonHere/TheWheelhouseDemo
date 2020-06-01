package messaging;

import java.util.ArrayList;
import java.util.List;

public class Courier {
	
	private static Courier instance = null;
	
	private List<Listener> listeners = new ArrayList<Listener>();
	private List<Thread> listenerThreads = new ArrayList<Thread>();
	
	private Courier() {

	}
	
	public static Courier getInstance() {
		if (instance == null) {
			instance = new Courier();
		}
		return instance;
	}
	
	protected void broadcast(QueueMessage msg) {
		for (Listener listener : listeners) {
			if (listener.acceptedMessageTypes().contains(msg.type)) {
				if (listener.receiveMessage(msg)) {
//					System.out.println(listener.getClass().getSimpleName() + " took " + msg.type);
					//Success
				} else {
					//Fail
				}
			}
		}
	}
	
	public static void registerListener(Listener listener) {
		int currentPostfix = 1;
		String threadName = listener.getClass().getSimpleName() + "Listener" + currentPostfix;
		for (int i = 0; i < getInstance().listenerThreads.size(); i++) {
			if (getInstance().listenerThreads.get(i).getName().equals(threadName)) {
				currentPostfix++;
				threadName = threadName.substring(0, threadName.length() - 1) + currentPostfix;
				i = 0;
			}
		}
		getInstance().listeners.add(listener);
	}
}
