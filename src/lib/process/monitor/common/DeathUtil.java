package lib.process.monitor.common;

import lib.process.monitor.util.OnDeathRelaunch;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

public class DeathUtil {

    public static void onDeath(String handlerClass, String handlerClassCB) {

        ArrayList<String> args = new ArrayList<>();
        args.add("java");
        args.add("-cp");
        args.add(handlerClassCB + File.pathSeparator + DeathUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        args.add(OnDeathRelaunch.class.getName());
        args.add(handlerClass);
        try {
            ProcessBuilder p = new ProcessBuilder(args);
            p.inheritIO();
            p.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
