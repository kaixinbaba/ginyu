package ginyu.event.list;

import ginyu.core.Client;
import ginyu.event.BlockEvent;
import lombok.Getter;

/**
 * @author: junjiexun
 * @date: 2020/10/19 10:49 下午
 * @description:
 */
@Getter
@SuppressWarnings("all")
public class PushEvent extends BlockEvent {

    private String key;

    private Integer listSize;

    public PushEvent(Client client, String key, Integer listSize) {
        super(client);
        this.key = key;
        this.listSize = listSize;
    }
}
