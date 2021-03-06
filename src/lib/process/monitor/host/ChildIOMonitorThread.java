package lib.process.monitor.host;

import lib.process.monitor.ChildError;
import lib.process.monitor.HandshakeConstants;
import lib.process.monitor.ProcessMonitorHost;
import lib.process.monitor.util.Utilities;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ChildIOMonitorThread extends Thread {

    private Process processToWatch;
    private ProcessMonitorHost host;
    private int childPID;

    private String handlerClass, handlerSource;

    public ChildIOMonitorThread(Process processToWatch, ProcessMonitorHost host, String handlerClass, String handlerSource) throws IOException {
        this.processToWatch = processToWatch;
        this.host = host;
        this.handlerClass = handlerClass;
        this.handlerSource = handlerSource;
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
        StringBuilder currentLine = new StringBuilder();
        int linesRead = 0;
        while (true) {
            try {
                char c = (char) inputStream.read();
                if (c == 65535) {
                    host.processKilled();
                    return;
                }
                if (c == '\n') {
                    String line = currentLine.toString();
                    currentLine = new StringBuilder();
                    switch (linesRead++) {
                        case 0:
                            if (line.startsWith(HandshakeConstants.PID_ANNOUNCE_START)) {
                                line = line.trim();
                                childPID = Integer.parseInt(line.substring(HandshakeConstants.PID_ANNOUNCE_START.length()));
                                System.out.println("Connected to watcher " + childPID);
                                host.registerPID(childPID);
                                registerHandler();
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

    private void registerHandler() throws IOException {
        OutputStream outputStream = processToWatch.getOutputStream();
        outputStream.write((HandshakeConstants.SEND_HANDLER + handlerClass).getBytes());
        outputStream.write('\n');
        outputStream.write((HandshakeConstants.SEND_HANDLER_SOURCE + handlerSource).getBytes());
        outputStream.write('\n');

        outputStream.flush();
    }
}
