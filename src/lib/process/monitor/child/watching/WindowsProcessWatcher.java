package lib.process.monitor.child.watching;

import lib.process.monitor.child.watching.ProcessWatcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class WindowsProcessWatcher extends ProcessWatcher {
    @Override
    public boolean isAlive(int pid) {
        try {
            Process process = new ProcessBuilder("tasklist").start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder data = new StringBuilder();
            boolean found = false;

            while (true) {
                String l = reader.readLine();
                if (l == null) break;
                if (l.toLowerCase().contains("java"))
                data.append(l).append("\n");
            }
            for (String s : data.toString().split("\n")) {
                s = s.replaceAll("  +", " ");
                String[] parts = s.split(" ");
                int foundPID = Integer.parseInt(parts[1]);
                if (foundPID == pid) found = true;
            }
            return found;
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return true;
    }
}
