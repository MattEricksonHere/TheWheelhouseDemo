package messaging;

/**
 * A message that can be broadcasted to listeners
 * @author Me
 */
public class QueueMessage {

	// Play Panel
	public static final int PLAY_PRESSED = 0;
	public static final int PLAY_ONE_PRESSED = 1;
	public static final int PAUSE_PRESSED = 2;
	public static final int STOP_PRESSED = 3;
	
	// Player
	public static final int PLAYING_NEXT = 10;
	public static final int CURRENTLY_PLAYED_NOTES_CHANGED = 11;
	
	public int type;
	public byte[] arg;
	
	// Static Construction Only
	private QueueMessage(int type, byte[] message) {
		this.type = type;
		this.arg = message;
	}
	
	private QueueMessage(int type, String message) {
		this.type = type;
		this.arg = message.getBytes();
	}
	
	private static void broadcast(QueueMessage msg) {
		Courier.getInstance().broadcast(msg);
	}
	
	public static void playPressed() {
		broadcast(new QueueMessage(PLAY_PRESSED, ""));
	}
	
	public static void playOnePressed() {
		broadcast(new QueueMessage(PLAY_ONE_PRESSED, ""));
	}
	
	public static void pausePressed() {
		broadcast(new QueueMessage(PAUSE_PRESSED, ""));
	}
	
	public static void stopPressed() {
		broadcast(new QueueMessage(STOP_PRESSED, ""));
	}
	
	public static void playingNext(int phraseID) {
		broadcast(new QueueMessage(PLAYING_NEXT, Integer.toString(phraseID)));
	}
	
	public static void currentlyPlayedNotesChanged() {
		broadcast(new QueueMessage(CURRENTLY_PLAYED_NOTES_CHANGED, ""));
	}
}
