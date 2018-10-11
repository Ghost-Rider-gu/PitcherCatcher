package software.ping.util.core;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import software.ping.core.Catcher;
import software.ping.core.Pitcher;
import software.ping.util.CommandLineParams;

/**
 * Tests for Pitcher and Catcher.
 */
public class PitcherCatcherTest {

    private static final Integer PORT = 4550;
    private static final Integer MESSAGE_SIZE = 200;
    private static final String BIND_ADDRESS = "127.0.0.1";
    private static final String HOST = "127.0.0.1";
    private static final Integer MESSAGES_PER_SECOND = 20;

    private static Pitcher pitcher;
    private static Catcher catcher;

    @Before
    public void setup() {
        CommandLineParams pitcherParams = new CommandLineParams();
        pitcherParams.setHostName(HOST);
        pitcherParams.setMessageLength(MESSAGE_SIZE);
        pitcherParams.setMps(MESSAGES_PER_SECOND);
        pitcherParams.setPort(PORT);

        CommandLineParams catcherParams = new CommandLineParams();
        catcherParams.setPort(PORT);
        catcherParams.setHostName(BIND_ADDRESS);

        pitcher = new Pitcher(pitcherParams);
        catcher = new Catcher(catcherParams);
    }

    @After
    public void release() {
        catcher.stopCatcher();
        pitcher.stopPitcher();
    }

    @Test
    public void shouldStartPitcherCatcher() throws Exception {
        catcher.startCatcher();
        pitcher.startPitcher();
    }

}
