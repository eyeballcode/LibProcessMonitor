package lib.process.monitor.child;

import lib.process.monitor.child.watching.ProcessWatcher;
import lib.process.monitor.child.watching.UNIXProcessWatcher;
import lib.process.monitor.child.watching.WindowsProcessWatcher;
import lib.process.monitor.common.DeathUtil;
import lib.process.monitor.util.Utilities;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ProcessWatchingThread extends Thread {

    private ProcessWatcher watcher;

    private ArrayList<Integer> pids = new ArrayList<>();

    private String handlerClass, handlerSource;


    public void setHandlerClass(String handlerClass) {
        this.handlerClass = handlerClass;
    }

    public void setHandlerSource(String handlerSource) {
        this.handlerSource = handlerSource;
    }

    public ProcessWatchingThread() {
        switch (Utilities.OSUtils.getOS()) {
            case LINUX:
            case MACOSX:
                // Both are unix based and have /proc
                watcher = new UNIXProcessWatcher();
                break;
            case WINDOWS:
                watcher = new WindowsProcessWatcher();
        }
    }

    public void addPID(int pid) {
        pids.add(pid);
    }

    @Override
    public void run() {
        while (true) {
            for (int pid : pids) {
                if (!watcher.isAlive(pid)) {
                    DeathUtil.onDeath(handlerClass, handlerSource);
                    return;
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
