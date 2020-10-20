package ginyu.core;

import ginyu.db.Database;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author: junjiexun
 * @date: 2020/10/13 10:42 下午
 * @description:
 */
@Data
public class Client implements Comparable<Client> {

    private Integer id;

    private volatile Integer db;

    private volatile Database database;

    private ChannelHandlerContext ctx;

    private Set<String> blockKeys = new HashSet<>(4);

    public synchronized void addBlockKeys(String... keys) {
        Collections.addAll(this.blockKeys, keys);
    }

    public synchronized boolean stillBlocked() {
        return !this.blockKeys.isEmpty();
    }

    public synchronized void addBlockKeys(Collection<String> keys) {
        this.blockKeys.addAll(keys);
    }

    public synchronized void clearBlock() {
        this.blockKeys.clear();
    }

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

    public void select(Integer db) {
        this.setDb(db);
        this.setDatabase(Server.INSTANCE.getDb().getDatabase(this.getDb()));
    }

    @Override
    public int compareTo(Client o) {
        return this.id.compareTo(o.id);
    }
}
