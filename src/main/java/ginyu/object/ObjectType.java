package ginyu.object;

import lombok.Getter;

public enum ObjectType {

    STRING,
    HASH,
    LIST,
    SET,
    ZSET,
    STREAM,
    ;

    @Getter
    private final String display;

    ObjectType() {
        this.display = this.name().toLowerCase();
    }
}
