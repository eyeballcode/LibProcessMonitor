package lib.process.monitor;

import lib.process.monitor.child.HostIOMonitorThread;
import lib.process.monitor.util.Utilities;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

public class ProcessMonitorChildMain {

    public static void main(String[] args) {
        System.out.println(HandshakeConstants.PID_ANNOUNCE_START + Utilities.getPID());
        HostIOMonitorThread hostIOMonitorThread = new HostIOMonitorThread();
        hostIOMonitorThread.start();
    }

}
