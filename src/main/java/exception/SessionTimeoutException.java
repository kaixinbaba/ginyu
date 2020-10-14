package exception;

/**
 * @author: junjiexun
 * @date: 2020/10/13 4:54 下午
 * @description:
 */
public class SessionTimeoutException extends GinyuException {

    public SessionTimeoutException(String format, Object... args) {
        super(format, args);
    }
}
