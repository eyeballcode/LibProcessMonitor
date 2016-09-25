package lib.process.monitor.common;

import lib.process.monitor.util.OnDeathRelaunch;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class DeathUtil {

    public static void onDeath(String handlerClass, String handlerClassCB, int handleCount) throws IOException {
        try {
            Socket socket = new Socket("localhost", 5232); // Random int ???
            socket.close();
            System.exit(1);
        } catch (Exception e) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        ServerSocket s = new ServerSocket(5232);
                        while (true)
                            s.accept().close();
                    } catch (IOException e1) {
                        System.exit(1); // If already bound then exit
                    }
                }
            }.start();
        }
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
