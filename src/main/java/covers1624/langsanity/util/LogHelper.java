package covers1624.langsanity.util;

/**
 * Created by covers1624 on 5/4/2016.
 */
public class LogHelper {

    /**
     * Log with a supplied level.
     */
    public static void log(Level logLevel, Object object) {
        System.out.println(String.format("[%s] : %s", logLevel.name(), String.valueOf(object)));
    }

    //Standard log entries.

    public static void error(Object object) {
        log(Level.ERROR, object);
    }

    public static void fatal(Object object) {
        log(Level.FATAL, object);
    }

    public static void info(Object object) {
        log(Level.INFO, object);
    }

    public static void warn(Object object) {
        log(Level.WARN, object);
    }

    public static void debug(Object object) {
        log(Level.DEBUG, object);
    }

    //log with format.

    public static void error(String object, Object... format) {
        log(Level.ERROR, String.format(object, format));
    }

    public static void fatal(String object, Object... format) {
        log(Level.FATAL, String.format(object, format));
    }

    public static void info(String object, Object... format) {
        log(Level.INFO, String.format(object, format));
    }

    public static void warn(String object, Object... format) {
        log(Level.WARN, String.format(object, format));
    }

    public static void debug(String object, Object... format) {
        log(Level.DEBUG, String.format(object, format));
    }

    public static void bigError(String format, Object... data) {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        error("****************************************");
        error("* " + format, data);
        for (int i = 2; i < 8 && i < trace.length; i++) {
            error("*  at %s%s", trace[i].toString(), i == 7 ? "..." : "");
        }
        error("****************************************");
    }

    public static void bigFatal(String format, Object... data) {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        fatal("****************************************");
        fatal("* " + format, data);
        for (int i = 2; i < 8 && i < trace.length; i++) {
            fatal("*  at %s%s", trace[i].toString(), i == 7 ? "..." : "");
        }
        fatal("****************************************");
    }

    public static void bigInfo(String format, Object... data) {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        info("****************************************");
        info("* " + format, data);
        for (int i = 2; i < 8 && i < trace.length; i++) {
            info("*  at %s%s", trace[i].toString(), i == 7 ? "..." : "");
        }
        info("****************************************");
    }

    public static void bigWarn(String format, Object... data) {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        warn("****************************************");
        warn("* " + format, data);
        for (int i = 2; i < 8 && i < trace.length; i++) {
            warn("*  at %s%s", trace[i].toString(), i == 7 ? "..." : "");
        }
        warn("****************************************");
    }

    public static void bigDebug(String format, Object... data) {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        debug("****************************************");
        debug("* " + format, data);
        for (int i = 2; i < 8 && i < trace.length; i++) {
            debug("*  at %s%s", trace[i].toString(), i == 7 ? "..." : "");
        }
        debug("****************************************");
    }
}
