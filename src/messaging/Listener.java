package messaging;

import java.util.List;

/**
 * Interface for class which will receive messages
 * @author Me
 */
public interface Listener {
	static final long WAITTIME = 100;
	abstract boolean receiveMessage(QueueMessage msg);
	abstract List<Integer> acceptedMessageTypes();
}
