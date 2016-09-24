package lib.process.monitor.child.watching;

import java.io.File;

public class UNIXProcessWatcher extends ProcessWatcher {
    @Override
    public boolean isAlive(int pid) {
        return new File("/proc/" + pid).exists();
    }
}
