package software.ping.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Catcher handler for multiply catchers.
 */
public class CatcherHandler extends Thread {

    private Socket catcherSocket;

    /**
     * Constructor.
     *
     * @param catcherSocket {@link Socket}
     */
    public CatcherHandler(Socket catcherSocket) {
        this.catcherSocket = catcherSocket;
    }

    public void run() {
        try (PrintWriter out = new PrintWriter(catcherSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(catcherSocket.getInputStream()))) {

            String getMessage;
            while ((getMessage = in.readLine()) != null) {
                out.println(getMessage);
            }

            catcherSocket.close();

        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

}
