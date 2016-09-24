package lib.process.monitor.handle;

/**
 * Define a class to handle deaths. This class needs a empty constructor.
 *
 * Since there can be no guarantee that a certain process was killed, do not assumy any variables are defined.
 *
 * All data should be retrieved in the onDeath method.
 *
 */
public interface ProcessDeathHandler {

    void onDeath();

}
