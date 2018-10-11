package software.ping.core;

import software.ping.util.CommandLineParams;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;

/**
 * Catcher is receiving the messages and sending replies to the Pitcher.
 */
public class Catcher {

    private CommandLineParams params;
    private ServerSocket catcherSocket;

    /**
     * Constructor.
     *
     * @param params {@link CommandLineParams}
     */
    public Catcher(CommandLineParams params) {
        this.params = new CommandLineParams();

        this.params.setHostName(params.getHostName());
        this.params.setPort(params.getPort());
    }

    /**
     * Start the Catcher.
     */
    public void startCatcher() {
        try {
            InetAddress address = InetAddress.getByName(this.params.getHostName());
            catcherSocket = new ServerSocket(this.params.getPort(), 5, address);

            System.out.println("Catcher was started. Listening port " + this.params.getPort() + " ...");

            while (true) {
                new CatcherHandler(catcherSocket.accept()).start();
            }

        } catch (ConnectException ex) {
            System.out.println("Connection error: " + ex.getMessage());
            this.stopCatcher();
        } catch (SocketTimeoutException ex) {
            System.out.println("Timeout error: " + ex.getMessage());
            this.stopCatcher();
        } catch (IOException ex) {
            System.out.println("Input / Output error: " + ex.getMessage());
            this.stopCatcher();
        }
    }

    /**
     * Stop the Catcher and release resources.
     */
    public void stopCatcher() {
        try {
            if (catcherSocket != null && !catcherSocket.isClosed()) {
                catcherSocket.close();
            }
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

}
