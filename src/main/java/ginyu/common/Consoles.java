package ginyu.common;

import ginyu.utils.OSUtils;

import java.util.logging.Level;
import java.util.regex.Matcher;

/**
 * @author: junjiexun
 * @date: 2020/10/15 10:12 上午
 * @description: 负责终端彩色输出
 */
public abstract class Consoles {

    private static final String RESET = "\033[0m";
    private static final int LIGHT_GRAY = 39;
    private static final int WHITE = 30;
    private static final int RED = 31;
    private static final int GREEN = 32;
    private static final int YELLOW = 33;
    private static final int BLUE = 34;
    private static final int MAGENTA = 35;
    private static final int CYAN = 36;
    private static final int GRAY = 37;
    public volatile static Level LEVEL = Level.CONFIG;
    private static boolean enableColor = true;

    static {
        if (System.console() != null) {
            // windows dos, do not support color
            enableColor = !OSUtils.isWindows();
        }
        // cygwin and mingw support color
        if (OSUtils.isCygwinOrMinGW()) {
            enableColor = true;
        }
    }

    private static String colorStr(String msg, int colorCode) {
        return String.format("\033[%sm%s%s", colorCode, msg, RESET);
    }

    public static void lightGray(String msg) {
        System.out.println(colorStr(msg, LIGHT_GRAY));
    }

    public static void blue(String format, Object... arguments) {
        blue(format(format, arguments));
    }

    public static void blue(String msg) {
        System.out.println(colorStr(msg, BLUE));
    }

    public static void cyan(String msg) {
        System.out.println(colorStr(msg, CYAN));
    }

    public static void gray(String msg) {
        System.out.println(colorStr(msg, GRAY));
    }

    public static void red(String msg) {
        System.out.println(colorStr(msg, RED));
    }

    public static void green(String msg) {
        System.out.println(colorStr(msg, GREEN));
    }

    public static void yellow(String msg) {
        System.out.println(colorStr(msg, YELLOW));
    }

    public static void magenta(String msg) {
        System.out.println(colorStr(msg, MAGENTA));
    }

    public static void white(String msg) {
        System.out.println(colorStr(msg, WHITE));
    }

    public static void trace(String msg) {
        if (canLog(Level.FINE)) {
            if (enableColor) {
                cyan(msg);
            } else {
                System.out.println(msg);
            }
        }
    }

    public static void trace(String format, Object... arguments) {
        trace(format(format, arguments));
    }

    public static void debug(String msg) {
        if (canLog(Level.CONFIG)) {
            if (enableColor) {
                gray(msg);
            } else {
                System.out.println(msg);
            }
        }
    }

    public static void debug(String format, Object... arguments) {
        debug(format(format, arguments));
    }

    public static void info(String msg) {
        if (canLog(Level.INFO)) {
            if (enableColor) {
                green(msg);
            } else {
                System.out.println(msg);
            }
        }
    }

    public static void info(String format, Object... arguments) {
        info(format(format, arguments));
    }

    public static void warn(String msg) {
        if (canLog(Level.WARNING)) {
            if (enableColor) {
                yellow(msg);
            } else {
                System.out.println(msg);
            }
        }
    }

    public static void warn(String format, Object... arguments) {
        warn(format(format, arguments));
    }

    public static void error(String msg) {
        if (canLog(Level.SEVERE)) {
            if (enableColor) {
                red(msg);
            } else {
                System.out.println(msg);
            }
        }
    }

    public static void error(String format, Object... arguments) {
        error(format(format, arguments));
    }

    private static String format(String from, Object... arguments) {
        if (from != null) {
            String computed = from;
            if (arguments != null && arguments.length != 0) {
                for (Object argument : arguments) {
                    computed = computed.replaceFirst("\\{\\}", Matcher.quoteReplacement(argument.toString()));
                }
            }
            return computed;
        }
        return null;
    }

    private static boolean canLog(Level level) {
        return level.intValue() >= LEVEL.intValue();
    }

}
