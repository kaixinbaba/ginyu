package exception;

/**
 * @author: junjiexun
 * @date: 2020/10/13 4:54 下午
 * @description:
 */
public class SetWrongTypeException extends GinyuException {

    public SetWrongTypeException(String format, Object... args) {
        super(format, args);
    }
}
