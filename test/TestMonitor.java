import lib.process.monitor.ProcessMonitorHost;
import lib.process.monitor.handle.ProcessDeathHandler;
import lib.process.monitor.util.Utilities;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class TestMonitor {

    public static void main(String[] args) throws IOException {
        System.out.println("This is " + Utilities.getPID());
        ProcessMonitorHost monitorHost = new ProcessMonitorHost(5, new PDH());
        monitorHost.createWatchers();
    }

}
