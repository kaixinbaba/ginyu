package ginyu.cmd.list;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: junjiexun
 * @date: 2020/10/19 10:49 下午
 * @description:
 */
@Getter
@AllArgsConstructor
@SuppressWarnings("all")
public class PushEvent {

    private String key;

    private Integer size;
}
