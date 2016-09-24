package lib.process.monitor;

import lib.process.monitor.host.ChildIOMonitorThread;
import lib.process.monitor.util.Utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ProcessMonitorHost {

    public int myPID = Utilities.getPID();

    int watcherCount;

    ArrayList<Integer> pids = new ArrayList<>();
    ArrayList<ChildIOMonitorThread> childIOMonitorThreads = new ArrayList<>();

    public ProcessMonitorHost(int numberOfWatchers) {
        watcherCount = numberOfWatchers;
    }

    public void createWatchers() throws IOException {
        File codeBase = new File(ProcessMonitorHost.class.getProtectionDomain().getCodeSource().getLocation().getPath());

        ArrayList<String> args = new ArrayList<>();
        args.add(new File(new File(new File(System.getProperty("java.home")), "bin"), "java").getAbsolutePath());


        args.add("-cp");
        args.add(codeBase.getAbsolutePath());

        args.add(ProcessMonitorChildMain.class.getName());

        ProcessBuilder processBuilder = new ProcessBuilder(args);
        for (int i = 0; i < watcherCount; i++) {
            Process watcher = processBuilder.start();
            ChildIOMonitorThread monitorThread = new ChildIOMonitorThread(watcher, this);
            monitorThread.start();
            childIOMonitorThreads.add(monitorThread);
        }
    }

    public void registerPID(int pid) throws IOException {
        for (ChildIOMonitorThread thread : childIOMonitorThreads) {
            if (thread.getChildPID() == pid) continue;
            thread.registerID(pid);
        }
    }

    public void error(String errorName, ChildError handshakePidAnnounceFail) {
        System.out.print(errorName + "  ");
        System.out.println(handshakePidAnnounceFail.name());
    }

    public void processKilled(String pid) {
//        while (true) {
            System.out.println("PROCESS " + pid + " KILLED!!! I WON'T STOP SPAMMING!! :D:D:D");
//        }
    }
}
