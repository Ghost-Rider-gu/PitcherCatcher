package software.ping.util;

/**
 * Utility class for parameters.
 *
 * hostName - the name of the computer which runs Catcher
 * port - TCP socket port used for connecting
 * mps - messages per second
 * messageLength - message length
 */
public class CommandLineParams {

    private String hostName;
    private Integer port;
    private Integer mps;
    private Integer messageLength;

    /*
    * Getters and Setters for fields.
    * We could use lombok (@Getter, @Setter) for that but you might not have a plugin for your IDE.
    * So I chose plain Java. Also we use auto-boxing here
    * */

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Integer getMps() {
        return mps;
    }

    public void setMps(int mps) {
        this.mps = mps;
    }

    public Integer getMessageLength() {
        return messageLength;
    }

    public void setMessageLength(int messageLength) {
        this.messageLength = messageLength;
    }

}
