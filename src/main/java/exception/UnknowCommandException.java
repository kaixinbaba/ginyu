package exception;

/**
 * @author: junjiexun
 * @date: 2020/10/13 4:54 下午
 * @description:
 */
public class UnknowCommandException extends GinyuException {

    public UnknowCommandException(String format, Object... args) {
        super(format, args);
    }
}
