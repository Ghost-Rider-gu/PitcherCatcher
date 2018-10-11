package software.ping.core;

import software.ping.util.CommandLineParams;
import software.ping.util.MessageGenerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Pitcher is generating the messages and sending their to Catcher.
 */
public class Pitcher {

    private CommandLineParams params;
    private Socket pitcherSocket;
    private PrintWriter out;
    private BufferedReader in;

    /**
     * Constructor with all fields.
     *
     * @param params {@link CommandLineParams}
     */
    public Pitcher(CommandLineParams params) {
        this.params = new CommandLineParams();

        this.params.setHostName(params.getHostName());
        this.params.setPort(params.getPort());

        if (params.getMps() != null) {
            this.params.setMps(params.getMps());
        } else {
            this.params.setMps(1);
        }

        if (params.getMessageLength() != null) {
            this.params.setMessageLength(params.getMessageLength());
        } else {
            this.params.setMessageLength(300);
        }
    }

    /**
     * Start the Pitcher.
     */
    public void startPitcher() {
        try {
            InetAddress address = InetAddress.getByName(this.params.getHostName());
            pitcherSocket = new Socket(address, this.params.getPort());

            out = new PrintWriter(pitcherSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(pitcherSocket.getInputStream()));

            MessageGenerator messageGenerator = new MessageGenerator();

            while (true) {
                // send message to the Catcher and get it back
                for (int i = 0; i <= this.params.getMps(); i++ ) {
                    this.sendMessage(messageGenerator.generateMessage(this.params.getMessageLength()), i);
                    System.out.println("Message number is " + i + " and length is " + this.params.getMessageLength() +
                                       " was sent to the Catcher (" + this.params.getHostName() + ")");

                    String gotMessage = in.readLine();
                    int messageLength = gotMessage.length();
                    String numberMessage = gotMessage.split(":")[0];
                    System.out.println("Message number is : " + numberMessage + " and length is " + messageLength +
                            " was sent to the Catcher (" + this.params.getHostName() + ")");
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
            }

        } catch (ConnectException ex) {
            System.out.println("Connection error: " + ex.getMessage());
            this.restartPitcher();
        } catch (SocketTimeoutException ex) {
            System.out.println("Timeout error: " + ex.getMessage());
            this.restartPitcher();
        } catch (IOException ex) {
            System.out.println("Input / Output error: " + ex.getMessage());
        }
    }

    /**
     * Trying to reconnect to the Catcher.
     * We could be restrict the quantity of connections (int connections = 100 for instance)
     */
    private void restartPitcher() {
        this.stopPitcher();

        System.out.println("Trying to reconnect to the Catcher (" + this.params.getHostName() + ") ...");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        this.startPitcher();
    }

    /**
     * Send message to the Catcher.
     *
     * @param message {@link String}
     * @param numberMessage {@link int}
     */
    private void sendMessage(String message, int numberMessage) {
        out.println(numberMessage + ":" + message);
    }

    /**
     * Stop pitcher and release resources.
     */
    public void stopPitcher() {
        try {
            in.close();
            out.close();
            pitcherSocket.close();
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

}
