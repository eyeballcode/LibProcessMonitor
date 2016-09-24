package lib.process.monitor.child;

import lib.process.monitor.HandshakeConstants;
import lib.process.monitor.ProcessMonitorChildMain;

import javax.swing.*;
import java.io.IOException;

public class HostIOMonitorThread extends Thread {

    private ProcessWatchingThread watchingThread = new ProcessWatchingThread();

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
                        watchingThread.addPID(pid);
                    } else if (line.startsWith(HandshakeConstants.SEND_HANDLER)) {
                        String handlerClass = line.substring(HandshakeConstants.SEND_HANDLER.length());
                        watchingThread.setHandlerClass(handlerClass);
                    } else if (line.startsWith(HandshakeConstants.SEND_HANDLER_SOURCE)) {
                        String handlerSource = line.substring(HandshakeConstants.SEND_HANDLER_SOURCE.length());
                        watchingThread.setHandlerSource(handlerSource);
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
