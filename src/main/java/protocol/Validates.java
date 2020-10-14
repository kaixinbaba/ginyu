package protocol;

import core.Server;
import exception.CommandValidateException;

/**
 * @author: junjiexun
 * @date: 2020/10/14 5:25 下午
 * @description:
 */
public abstract class Validates {

    public static void validateArraysSize(String commandName, Arrays arrays, Integer target) {
        if (arrays.getData().size() != target) {
            throw new CommandValidateException("ERR wrong number of arguments for '%s' command", commandName);
        }
    }

    public static void validateArraysSize(String commandName, Arrays arrays, Integer min, Integer max) {
        int size = arrays.getData().size();
        if ((min != null && size < min) || (max != null && size > max)) {
            throw new CommandValidateException("ERR wrong number of arguments for '%s' command", commandName);
        }
    }


    public static Integer validateInteger(Arrays arrays, int index, String argName) {
        try {
            return Integer.parseInt(((BulkStrings) arrays.getData().get(index)).getData().getContent());
        } catch (NumberFormatException e) {
            throw new CommandValidateException("%s must be integer", argName);
        }
    }

    public static Integer validateDbIndex(Arrays arrays, int arrayIndex, String argName) {
        Integer index = validateInteger(arrays, arrayIndex, argName);
        if (index < 0 || index >= Server.INSTANCE.getGinyuConfig().getDbSize()) {
            throw new CommandValidateException("DB index is out of range");
        }
        return index;
    }
}
