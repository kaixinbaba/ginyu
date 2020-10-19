package object;

import lombok.Getter;

import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author: junjiexun
 * @date: 2020/10/19 11:03 上午
 * @description:
 */
public class List {

    @Getter
    private final LinkedBlockingDeque<String> list = new LinkedBlockingDeque<>();

    public void push(boolean left, String... values) {
        for (String value : values) {
            if (left) {
                this.list.addFirst(value);
            } else {
                this.list.addLast(value);
            }
        }
    }

    public String pop(boolean left) {
        if (left) {
            return this.list.pollFirst();
        } else {
            return this.list.pollLast();
        }
    }


    public int size() {
        return this.list.size();
    }

    public boolean isEmpty() {
        return this.list.isEmpty();
    }
}
