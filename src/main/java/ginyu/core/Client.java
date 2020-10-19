package ginyu.core;

import lombok.Data;

/**
 * @author: junjiexun
 * @date: 2020/10/13 10:42 下午
 * @description:
 */
@Data
public class Client {

    private Integer id;

    private Integer db;

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Client)) {
            return false;
        }
        return this.id.equals(((Client) obj).id);
    }
}
