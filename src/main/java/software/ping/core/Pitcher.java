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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

            long totalNumber = 0;
            int previousNumber = 0;

            long timeSendingPCP;
            long timeReceivingPCP;

            long timeSendingPC;
            long timeReceivingPC;

            long timeSendingCP;
            long timeReceivingCP;

            /*
             * average times for cycle (Pitcher->Catcher->Pitcher) averageTimePCP
             * average times for cycle (Pitcher->Catcher) averageTimePC
             * average times for cycle (Catcher->Pitcher) averageTimeCP
            */
            int[] averageTimePCP = new int[this.params.getMps() + 1];
            int[] averageTimePC = new int[this.params.getMps() + 1];
            int[] averageTimeCP = new int[this.params.getMps() + 1];

            while (true) {
                // send message to the Catcher and get it back (number message starts from one)
                for (int i = 1; i <= this.params.getMps(); i++ ) {
                    timeSendingPCP = System.currentTimeMillis();
                    timeSendingPC = System.currentTimeMillis();
                    this.sendMessage(messageGenerator.generateMessage(this.params.getMessageLength()), i);

                    timeReceivingPC = System.currentTimeMillis();
                    averageTimePC[i] = (int)(timeReceivingPC - timeSendingPC);

                    timeSendingCP = System.currentTimeMillis();

                    String gotMessage = in.readLine();

                    timeReceivingPCP = System.currentTimeMillis();
                    timeReceivingCP = System.currentTimeMillis();

                    int messageLength = gotMessage.split(":")[1].length();
                    String numberMessage = gotMessage.split(":")[0];

                    if (messageLength == this.params.getMessageLength() && Integer.parseInt(numberMessage) == i) {
                        previousNumber += 1;
                        totalNumber += 1;
                        averageTimeCP[i] = (int)(timeReceivingCP - timeSendingCP);
                        averageTimePCP[i] = (int)(timeReceivingPCP - timeSendingPCP);
                    }
                }

                // output with an appropriate format
                System.out.println("---------------------------------------------------------------------------------" +
                                   "--------------------------------------------------------");
                System.out.printf("%10s %15s %6s %25s %30s %20s %20s", "Time", "Total messages", "Speed", "Average time (A->B->A)",
                                  "Total maximum time (A->B->A)", "Average time (A->B)", "Average time (B->A)");
                System.out.println();
                System.out.println("---------------------------------------------------------------------------------" +
                                   "--------------------------------------------------------");
                System.out.format("%10s %15d %6d %25s %30s %20s %20s", LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
                                                                       totalNumber, previousNumber, this.getAverageTime(averageTimePCP) + "ms",
                                                                       this.getTotalMaximumTime(averageTimePCP) + "ms",
                                                                       this.getAverageTime(averageTimePC) + "ms",
                                                                       this.getAverageTime(averageTimeCP) + "ms");
                System.out.println();
                System.out.println("---------------------------------------------------------------------------------" +
                                   "-------------------------------------------------------- \n");

                try {
                    previousNumber = 0;
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

        System.out.println("Trying to reconnect to the Catcher (" + this.params.getHostName() + ") ... \n");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            System.out.println("Error: " + ex.getMessage());
        }

        this.startPitcher();
    }

    /**
     * Send a message to the Catcher.
     *
     * @param message {@link String}
     * @param numberMessage {@link int}
     */
    private void sendMessage(String message, int numberMessage) {
        out.println(numberMessage + ":" + message);
    }

    /**
     * Get average time in the last second needed for the message to complete a certain cycle.
     * (Pitcher->Catcher->Pitcher), (Pitcher->Catcher), (Catcher->Pitcher)
     *
     * @param times {@link int[]}
     * @return double {@link double}
     */
    private double getAverageTime(int[] times) {
        long sum = 0;

        for (int time: times) {
            sum += time;
        }

        return (double)(sum / times.length);
    }

    /**
     * Get total maximum time in the last second needed for the message to complete a cycle (Pitcher->Catcher->Pitcher).
     *
     * @param times {@link int[]}
     * @return int {@link int}
     */
    private int getTotalMaximumTime(int[] times) {
        List<Integer> integerList = Arrays.stream(times).boxed().collect(Collectors.toList());
        return Collections.max(integerList);
    }

    /**
     * Stop pitcher and release resources.
     */
    public void stopPitcher() {
        try {
            // close one by one
            if (in != null) {
                in.close();
            }

            if (out != null) {
                out.close();
            }

            if (pitcherSocket != null && !pitcherSocket.isClosed()) {
                pitcherSocket.close();
            }
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

}
