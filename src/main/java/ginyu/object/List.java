package ginyu.object;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author: junjiexun
 * @date: 2020/10/19 11:03 上午
 * @description: TODO 自己实现链表双端队列
 */
public class List {

    @Getter
    @Setter
    private volatile LinkedBlockingDeque<String> list = new LinkedBlockingDeque<>();

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

    public int indexOf(String target) {
        int i = 0;
        for (String value : this.list) {
            if (value.equals(target)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public String get(int index) {
        int i = 0;
        for (String value : this.list) {
            if (i == index) {
                return value;
            }
            i++;
        }
        return null;
    }

    public java.util.List<String> range(int includeStart, int includeStop) {
        int i = 0;
        java.util.List<String> rangeList = new ArrayList<>(includeStop - includeStart + 1);
        for (String value : this.list) {
            if (i > includeStop) {
                break;
            }
            if (i >= includeStart) {
                rangeList.add(value);
            }
            i++;
        }
        return rangeList;
    }

    public void clear() {
        this.list.clear();
    }
}
