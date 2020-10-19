package event;

/**
 * @author: junjiexun
 * @date: 2020/10/19 9:41 下午
 * @description:
 */
@SuppressWarnings("all")
public class GenericEvent<T> {

    protected T source;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public GenericEvent(T source) {
        this.source = source;
    }

    public T getSource() {
        return (T) source;
    }
}
