package ginyu.object;

import ginyu.exception.CommandValidateException;
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

    public void incrBy(Integer incrValue) {
        try {
            int intValue = Integer.parseInt(this.value);
            this.setValue(intValue + incrValue);
        } catch (NumberFormatException e) {
            throw new CommandValidateException("value is not an integer or out of range");
        }
    }
}
