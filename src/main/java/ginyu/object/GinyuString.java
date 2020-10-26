package ginyu.object;

import lombok.Getter;

/**
 * @author: junjiexun
 * @date: 2020/10/26 6:27 下午
 * @description:
 */
public class GinyuString {

    @Getter
    private String value;

    public void setValue(String value) {
        this.value = value;
    }

    public void setValue(Integer value) {
        this.setValue(String.valueOf(value));
    }

    public GinyuString(String value) {
        this.value = value;
    }
}
