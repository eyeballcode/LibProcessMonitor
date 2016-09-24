package lib.process.monitor.util;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class OnDeathRelaunch {

    public static void main(String[] args) throws Exception {
        try {
            Class<?> c = Class.forName(args[0]);
            Method m = c.getMethod("onDeath");
            Constructor<?> t = c.getConstructor();
            m.setAccessible(true);
            t.setAccessible(true);
            m.invoke(t.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
