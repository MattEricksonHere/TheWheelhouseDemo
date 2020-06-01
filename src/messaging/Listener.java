package messaging;

import java.util.List;

public interface Listener {
	static final long WAITTIME = 100;
	abstract boolean receiveMessage(QueueMessage msg);
	abstract List<Integer> acceptedMessageTypes();
}
