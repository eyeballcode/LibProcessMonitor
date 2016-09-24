import lib.process.monitor.handle.ProcessDeathHandler;

import javax.swing.*;
import java.awt.*;

public class PDH implements ProcessDeathHandler {
    @Override
    public void onDeath() {
        JFrame f = new JFrame("Serious?");
        f.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
        f.pack();
        f.setVisible(true);
    }
}
