package lib.process.monitor.child;

import lib.process.monitor.HandshakeConstants;
import lib.process.monitor.ProcessMonitorChildMain;

import java.io.IOException;

public class HostIOMonitorThread extends Thread {

    ProcessWatchingThread watchingThread = new ProcessWatchingThread();

    @Override
    public void run() {
        watchingThread.start();
        StringBuilder currentLine = new StringBuilder();
        while (true) {
            try {
                char c = (char) System.in.read();
                if (c == '\n') {
                    String line = currentLine.toString();
                    currentLine = new StringBuilder();
                    if (line.startsWith(HandshakeConstants.PID_REGISTER_ANNOUNCE)) {
                        int pid = Integer.parseInt(line.substring(HandshakeConstants.PID_REGISTER_ANNOUNCE.length()));
                        ProcessMonitorChildMain.pidsToWatch.add(pid);
                        watchingThread.addPID(pid);
                    }
                } else {
                    currentLine.append(c);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
