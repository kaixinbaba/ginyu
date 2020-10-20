package ginyu.event;

import ginyu.core.Client;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class BlockEvent implements Comparable<BlockEvent> {

    protected Client client;

    @Override
    public int compareTo(BlockEvent o) {
        return this.client.compareTo(o.client);
    }
}
