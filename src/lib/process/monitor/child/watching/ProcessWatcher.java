package lib.process.monitor.child.watching;

public abstract class ProcessWatcher {

    public abstract boolean isAlive(int pid);

}
