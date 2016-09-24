package lib.process.monitor.child;

import lib.process.monitor.child.watching.ProcessWatcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class WindowsProcessWatcher extends ProcessWatcher {
    @Override
    public boolean isAlive(int pid) {
        try {
            Process process = new ProcessBuilder("tasklist").start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder data = new StringBuilder();
            ArrayList<String> javaLines = new ArrayList<>();
            boolean found = false;
            int linesRead = 0;
            while (true) {
                if (linesRead < 2) {
                    linesRead++;
                    continue; // Skip header
                }
                String l = reader.readLine();
                if (l == null) break;
                data.append(l);
                linesRead++;
            }
            for (String line : data.toString().split("\n")) {
                line = line.trim();
                System.out.println(line);
                if (line.toLowerCase().contains("java")) {
                    javaLines.add(line);
                }
            }
            for (String s : javaLines) {
                s = s.replaceAll("  +", " ");
                String[] parts = s.split(" ");
                int foundPID = Integer.parseInt(parts[1]);
                if (foundPID == pid) found = true;
            }
            return found;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
