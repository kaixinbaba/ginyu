package ginyu.exception;

/**
 * @author: junjiexun
 * @date: 2020/10/13 4:54 下午
 * @description:
 */
public class GinyuException extends RuntimeException {

    public GinyuException(String format, Object... args) {
        super(String.format("GinyuErr " + format, args));
    }
}
