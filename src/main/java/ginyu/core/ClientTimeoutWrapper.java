package ginyu.core;

import ginyu.event.BlockEvent;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author: junjiexun
 * @date: 2020/10/20 3:54 下午
 * @description:
 */
@Data
@AllArgsConstructor
public class ClientTimeoutWrapper implements Comparable<ClientTimeoutWrapper> {

    private Long timeoutTimestamp;

    private BlockEvent blockEvent;


    @Override
    public int hashCode() {
        return this.blockEvent.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ClientTimeoutWrapper)) {
            return false;
        }
        return this.blockEvent.equals(((ClientTimeoutWrapper) obj).blockEvent);
    }

    @Override
    public int compareTo(ClientTimeoutWrapper o) {
        return this.timeoutTimestamp.compareTo(o.timeoutTimestamp);
    }
}
