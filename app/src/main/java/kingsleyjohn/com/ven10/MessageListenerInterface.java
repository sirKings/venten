package kingsleyjohn.com.ven10;

public interface MessageListenerInterface {

    /**
     * To call this method when new message received and send back
     * @param message Message
     */
    void messageReceived(String message);
}
