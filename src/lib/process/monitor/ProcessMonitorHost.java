package lib.process.monitor;

import lib.process.monitor.common.DeathUtil;
import lib.process.monitor.handle.ProcessDeathHandler;
import lib.process.monitor.host.ChildIOMonitorThread;
import lib.process.monitor.util.Utilities;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ProcessMonitorHost {

    public int myPID = Utilities.getPID();

    private int watcherCount;

    ArrayList<Integer> pids = new ArrayList<>();
    private ArrayList<ChildIOMonitorThread> childIOMonitorThreads = new ArrayList<>();

    private String handlerClass, handlerSource;

    public ProcessMonitorHost(int numberOfWatchers, ProcessDeathHandler handler) {
        watcherCount = numberOfWatchers;
        handlerClass = handler.getClass().getName();
        handlerSource = handler.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
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
            ChildIOMonitorThread monitorThread = new ChildIOMonitorThread(watcher, this, handlerClass, handlerSource);
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
        DeathUtil.onDeath(handlerClass, handlerSource);
    }
}
