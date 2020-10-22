package ginyu.protocol;

import ginyu.core.Server;
import ginyu.exception.CommandValidateException;
import ginyu.exception.SetWrongTypeException;
import ginyu.object.ObjectType;
import ginyu.object.RedisObject;
import ginyu.utils.ProtocolValueUtils;

/**
 * @author: junjiexun
 * @date: 2020/10/14 5:25 下午
 * @description:
 */
@SuppressWarnings("all")
public abstract class Validates {

    public static void validateArraysSize(String commandName, Arrays arrays, Integer target) {
        if (arrays.getData().size() != target) {
            throw new CommandValidateException("wrong number of arguments for '%s' command", commandName);
        }
    }

    public static void validateArraysSize(String commandName, Arrays arrays, Integer min, Integer max) {
        int size = arrays.getData().size();
        if ((min != null && size < min) || (max != null && size > max)) {
            throw new CommandValidateException("wrong number of arguments for '%s' command", commandName);
        }
    }


    public static Integer validateInteger(Arrays arrays, int index, String argName) {
        try {
            return ProtocolValueUtils.getIntFromBulkStringsInArrays(arrays, index);
        } catch (NumberFormatException e) {
            throw new CommandValidateException("%s must be integer", argName);
        }
    }

    public static Long validateLong(Arrays arrays, int index, String argName) {
        try {
            return ProtocolValueUtils.getLongFromBulkStringsInArrays(arrays, index);
        } catch (NumberFormatException e) {
            throw new CommandValidateException("%s must be long", argName);
        }
    }

    public static Double validateDouble(Arrays arrays, int index, String argName) {
        try {
            return ProtocolValueUtils.getDoubleFromBulkStringsInArrays(arrays, index);
        } catch (NumberFormatException e) {
            throw new CommandValidateException("%s must be double", argName);
        }
    }

    public static Integer validateDbIndex(Arrays arrays, int arrayIndex, String argName) {
        Integer index = validateInteger(arrays, arrayIndex, argName);
        if (index < 0 || index >= Server.INSTANCE.getGinyuConfig().getDbSize()) {
            throw new CommandValidateException("DB index is out of range");
        }
        return index;
    }

    public static <T extends RedisObject> T validateType(RedisObject redisObject, ObjectType expect) {
        if (redisObject != null && !redisObject.getType().equals(expect)) {
            throw new SetWrongTypeException(
                    "WRONGTYPE Operation against a key holding the wrong kind of value, expect %s but %s",
                    expect, redisObject.getType()
            );
        }
        return (T) redisObject;
    }

    public static Double s2d(String content, String argName) {
        try {
            if (content.equalsIgnoreCase("+inf")
                    || content.equalsIgnoreCase("inf")) {
                return Double.POSITIVE_INFINITY;
            } else if (content.equalsIgnoreCase("-inf")) {
                return Double.NEGATIVE_INFINITY;
            } else {
                return Double.valueOf(content);
            }
        } catch (NumberFormatException e) {
            throw new CommandValidateException("%s must be double", argName);
        }
    }
}
