package ginyu.exception;

/**
 * @author: junjiexun
 * @date: 2020/10/13 4:54 下午
 * @description:
 */
public class CommandValidateException extends GinyuException {

    public CommandValidateException(String format, Object... args) {
        super(format, args);
    }
}
