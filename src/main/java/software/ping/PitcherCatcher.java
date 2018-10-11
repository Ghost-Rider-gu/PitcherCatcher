package software.ping;

import software.ping.util.PitcherCatcherStarter;

/**
 * Entry point for PitcherCatcher.
 */
public class PitcherCatcher {

    public static void main(String[] args) {
        PitcherCatcherStarter initApplication = new PitcherCatcherStarter();
        initApplication.prepareToStart(args);
    }

}
