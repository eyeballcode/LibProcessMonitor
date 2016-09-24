package lib.process.monitor.child;

import lib.process.monitor.child.watching.ProcessWatcher;
import lib.process.monitor.child.watching.UNIXProcessWatcher;
import lib.process.monitor.util.Utilities;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ProcessWatchingThread extends Thread {

    ProcessWatcher watcher;

    ArrayList<Integer> pids = new ArrayList<>();

    public ProcessWatchingThread() {
        switch (Utilities.OSUtils.getOS()) {
            case LINUX:
            case MACOSX:
                // Both are unix based and have /proc
                watcher = new UNIXProcessWatcher();
                break;
            case WINDOWS:
                
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
                    JFrame f = new JFrame("MEOW MEOW PID " + pid + " DEAD! THIS IS PID " + Utilities.getPID() + " REPORTING FOR DUTY!");
                    f.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
                    f.pack();
                    f.setVisible(true);
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
