package lib.process.monitor.host;

import lib.process.monitor.ChildError;
import lib.process.monitor.HandshakeConstants;
import lib.process.monitor.ProcessMonitorHost;
import lib.process.monitor.util.Utilities;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ChildIOMonitorThread extends Thread {

    Process processToWatch;
    ProcessMonitorHost host;
    int childPID;

    public ChildIOMonitorThread(Process processToWatch, ProcessMonitorHost host) throws IOException {
        this.processToWatch = processToWatch;
        this.host = host;
        registerID(Utilities.getPID());
    }

    public void registerID(int pid) throws IOException {
        OutputStream outputStream = processToWatch.getOutputStream();
        outputStream.write((HandshakeConstants.PID_REGISTER_ANNOUNCE + pid).getBytes());
        outputStream.write('\n');
        outputStream.flush();
    }

    public int getChildPID() {
        return childPID;
    }

    @Override
    public void run() {
        InputStream inputStream = processToWatch.getInputStream();
        OutputStream outputStream = processToWatch.getOutputStream();
        StringBuilder currentLine = new StringBuilder();
        int linesRead = 0;
        int childPID = -1;
        while (true) {
            try {
                char c = (char) inputStream.read();
                if (c == 65535) {
                    host.processKilled(String.valueOf(childPID == -1 ? "????" : childPID));
                    return;
                }
                if (c == '\n') {
                    String line = currentLine.toString();
                    currentLine = new StringBuilder();
                    System.out.println("Received line " + line);
                    switch (linesRead++) {
                        case 0:
                            if (line.startsWith(HandshakeConstants.PID_ANNOUNCE_START)) {
                                childPID = Integer.parseInt(line.substring(HandshakeConstants.PID_ANNOUNCE_START.length()));
                                this.childPID = childPID;
                                System.out.println("Connected to watcher " + childPID);
                                host.registerPID(childPID);
                            } else {
                                host.error("Child did not give correct handshake.", ChildError.HANDSHAKE_PID_ANNOUNCE_FAIL);
                            }
                            break;
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
