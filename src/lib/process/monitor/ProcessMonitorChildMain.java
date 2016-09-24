package lib.process.monitor;

import lib.process.monitor.child.HostIOMonitorThread;
import lib.process.monitor.util.Utilities;

import java.util.ArrayList;

public class ProcessMonitorChildMain {

    public static ArrayList<Integer> pidsToWatch = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println(HandshakeConstants.PID_ANNOUNCE_START + Utilities.getPID());
        HostIOMonitorThread hostIOMonitorThread = new HostIOMonitorThread();
        hostIOMonitorThread.start();
    }

}
