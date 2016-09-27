package lib.process.monitor.common;

import lib.process.monitor.util.OnDeathRelaunch;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class DeathUtil {

    public static void onDeath(final String handlerClass, final String handlerClassCB, int handleCount) throws IOException {
        try {
            Socket socket = new Socket("localhost", 5232); // Random int ???
            socket.close();
            System.exit(1); // We can connect, so handler is already up
        } catch (Exception e) {
            ServerSocket socket = null;
            try {
                socket = new ServerSocket(5232);
            } catch (IOException e1) {
                System.exit(1); // Thread conflict, also exit.
            }

            new Thread() {
                @Override
                public void run() {
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
                    System.exit(1);
                }
            }.start();

            //noinspection InfiniteLoopStatement
            while (true)
                try {
                    socket.accept().close();
                } catch (IOException ignored) {
                }

        }
    }

}
