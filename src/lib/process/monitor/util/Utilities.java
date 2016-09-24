package lib.process.monitor.util;

import java.io.*;
import java.lang.management.ManagementFactory;

public class Utilities {

    public static int getPID() {
        return Integer.parseInt(ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
    }


    public static void copyFolder(File source, File destination) throws IOException {
        if (source.isDirectory()) {
            if (!destination.exists()) {
                destination.mkdirs();
            }

            String files[] = source.list();

            for (String file : files) {
                File srcFile = new File(source, file);
                File destFile = new File(destination, file);

                copyFolder(srcFile, destFile);
            }
        } else {
            InputStream in = null;
            OutputStream out = null;

            try {
                in = new FileInputStream(source);
                out = new FileOutputStream(destination);

                byte[] buffer = new byte[1024];

                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
            } finally {
                if (in != null)
                    in.close();
                if (out != null)
                    out.close();
            }
        }
    }

    public enum OS {
        WINDOWS, MACOSX, LINUX
    }

    public static class OSUtils {

        /**
         * Gets the OS
         *
         * @return The current OS
         */
        public static OS getOS() {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win"))
                return OS.WINDOWS;
            else if (os.contains("mac"))
                return OS.MACOSX;
            else if (os.contains("linux") || os.contains("unix"))
                return OS.LINUX;
            else return OS.WINDOWS;
        }
    }


}
