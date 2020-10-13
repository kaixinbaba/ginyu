package cmd.string;

import cmd.AbstractRedisCommand;
import cmd.Command;
import exception.CommandValidateException;
import protocol.Arrays;
import protocol.BulkStrings;
import protocol.Resp2;
import protocol.SimpleStrings;

import java.util.List;

/**
 * @author: junjiexun
 * @date: 2020/10/13 5:48 下午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "set")
public class Set extends AbstractRedisCommand<SetArg> {

    private static final int XX = 0 << 1;
    private static final int NX = 0 << 2;

    /**
     * SET key value [EX seconds|PX milliseconds] [NX|XX] [KEEPTTL]
     *
     * @param arrays
     * @return
     */
    @Override
    protected SetArg createArg(Arrays arrays) {
        List<String> argList = arrays.map2String();
        String key = argList.get(1);
        String value = argList.get(2);
        boolean xx = false;
        boolean nx = false;
        long expiredSeconds = 0;
        long expiredMilliSeconds = 0;
        if (argList.size() > 3) {
            for (int i = 3; i < argList.size(); ) {
                String arg = argList.get(i);
                if (arg.equalsIgnoreCase("EX")) {
                    expiredSeconds = getExpiredTime("EX", i, argList);
                    i += 2;
                } else if (arg.equalsIgnoreCase("PX")) {
                    expiredMilliSeconds = getExpiredTime("PX", i, argList);
                    i += 2;
                } else if (arg.equalsIgnoreCase("XX")) {
                    xx = true;
                    i++;
                } else if (arg.equalsIgnoreCase("NX")) {
                    nx = true;
                    i++;
                }
            }
        }
        SetArg setArg = new SetArg();
        setArg.setKey(key);
        setArg.setValue(value);
        if (expiredMilliSeconds > 0) {
            // 优先毫秒
            setArg.setExpiredMilliSeconds(expiredMilliSeconds);
        } else if (expiredSeconds > 0) {
            // 其次秒
            setArg.setExpiredMilliSeconds(expiredSeconds * 1000);
        }
        setArg.setXx(xx);
        setArg.setNx(nx);
        return setArg;
    }

    private Long getExpiredTime(String flag, int i, List<String> argList) {
        try {
            return Long.valueOf(argList.get(i + 1));
        } catch (NumberFormatException n) {
            throw new CommandValidateException("%s argument must be an integer", flag);
        }
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        if (arrays.getData().size() < 3 || arrays.getData().size() > 9) {
            throw new CommandValidateException("ERR wrong number of arguments for '%s' command", commandName);
        }
    }

    @Override
    protected Resp2 doCommand0(SetArg arg) {
        System.out.println(arg);
        if (true) {
            return SimpleStrings.OK;
        } else {
            return BulkStrings.NULL;
        }
    }
}
