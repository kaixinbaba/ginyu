# ginyu
Redis implment with PURE Java
![小青蛙基纽](./ginyu.png)

## Redis Command List
<details>
<summary>
Cluster
</summary>

- [ ] Cluster
    - [ ] CLUSTER ADDSLOTS
    - [ ] CLUSTER COUNT-FAILURE-REPO
    - [ ] CLUSTER COUNTKEYSINSLOT
    - [ ] CLUSTER DELSLOTS
    - [ ] CLUSTER FAILOVER
    - [ ] CLUSTER FORGET
    - [ ] CLUSTER GETKEYSINSLOT
    - [ ] CLUSTER INFO
    - [ ] CLUSTER KEYSLOT
    - [ ] CLUSTER MEET
    - [ ] CLUSTER NODES
    - [ ] CLUSTER REPLICAS
    - [ ] CLUSTER REPLICATE
    - [ ] CLUSTER RESET
    - [ ] CLUSTER SAVECONFIG
    - [ ] CLUSTER SET-CONFIG-EPOCH
    - [ ] CLUSTER SETSLOT
    - [ ] CLUSTER SLAVES
    - [ ] CLUSTER SLOTS
    - [ ] CLUSTER READONLY
    - [ ] CLUSTER READWRITE
</details>

<details>
<summary>
Connection
</summary>

- [ ] Connection
    - [ ] AUTH
    - [x] ECHO
    - [x] PING
    - [x] QUIT
    - [x] SELECT
    - [x] SWAPDB
</details>

<details>
<summary>
Geo
</summary>

- [ ] Geo
    - [ ] GEOADD
    - [ ] GEOHASH
    - [ ] GEOPOS
    - [ ] GEODIST
    - [ ] GEORADIUS
    - [ ] GEORADIUSBYMEMBER
</details>

<details>
<summary>
Hashes
</summary>

- [ ] Hashes
    - [x] HDEL
    - [x] HEXISTS
    - [x] HGET
    - [x] HGETALL
    - [ ] HINCRBY
    - [ ] HINCRBYFLOAT
    - [x] HKEYS
    - [x] HLEN
    - [x] HMGET
    - [x] HMSET
    - [x] HSET
    - [ ] HSETNX
    - [x] HSTRLEN
    - [x] HVALS
    - [ ] HSCAN
</details>

<details>
<summary>
HyperLogLog
</summary>

- [ ] HyperLogLog
    - [ ] PFADD
    - [ ] PFCOUNT
    - [ ] PFMERGE
</details>

<details>
<summary>
Keys
</summary>

- [ ] Keys
    - [ ] DEL
    - [ ] DUMP
    - [ ] EXISTS
    - [ ] EXPIRE
    - [ ] EXPIREAT
    - [ ] KEYS
    - [ ] MIGRATE
    - [ ] MOVE
    - [ ] OBJECT
    - [ ] PERSIST
    - [ ] PEXPIRE
    - [ ] PEXPIREAT
    - [ ] PTTL
    - [ ] RANDOMKEY
    - [ ] RENAME
    - [ ] RENAMENX
    - [ ] RESTORE
    - [ ] SORT
    - [ ] TTL
    - [ ] TYPE
    - [ ] WAIT
    - [ ] SCAN
</details>

<details>
<summary>
Lists
</summary>

- [ ] Lists
    - [ ] BLPOP
    - [ ] BRPOP
    - [ ] BRPOPLPUSH
    - [ ] LINDEX
    - [ ] LINSERT
    - [ ] LLEN
    - [ ] LPOP
    - [ ] LPUSH
    - [ ] LPUSHX
    - [ ] LRANGE
    - [ ] LREM
    - [ ] LSET
    - [ ] LTRIM
    - [ ] RPOP
    - [ ] RPOPLPUSH
    - [ ] RPUSH
    - [ ] RPUSHX
</details>

<details>
<summary>
Pub/Sub
</summary>

- [ ] Pub/Sub
    - [ ] PSUBCRIBE
    - [ ] PUBSUB
    - [ ] PUBLISH
    - [ ] PUNSUBSCRIBE
    - [ ] SUBSRIBE
    - [ ] UNSUBSCRIBE
</details>

<details>
<summary>
Scripting
</summary>

- [ ] Scripting
    - [ ] EVAL
    - [ ] EVALSHA
    - [ ] SCRIPT DEBUG
    - [ ] SCRIPT EXISTS
    - [ ] SCRIPT FLUSH
    - [ ] SCRIPT KILL
    - [ ] SCRIPT LOAD
</details>

<details>
<summary>
Server
</summary>

- [ ] Server
    - [ ] BGREWRITEAOF
    - [ ] BGSAVE
    - [ ] CLIENT KILL
    - [ ] CLIENT LIST
    - [ ] CLIENT GETNAME
    - [ ] CLIENT ID
    - [ ] CLIENT PAUSE
    - [ ] CLIENT REPLY
    - [ ] CLIENT SETNAME
    - [ ] CLIENT UNBLOCK
    - [ ] COMMAND
    - [ ] COMMAND COUNT
    - [ ] COMMAND GETKEYS
    - [ ] COMMAND INFO
    - [ ] CONFIG GET
    - [ ] CONFIG REWRITE
    - [ ] CONFIG SET
    - [ ] CLIENT RESETSTAT
    - [ ] DBSIZE
    - [ ] DEBUG OBJECT
    - [ ] DEBUG SEGFAULT
    - [ ] FLUSHALL
    - [ ] FLUSHDB
    - [ ] INFO
    - [ ] LASTSAVE
    - [ ] MEMORY DOCTOR
    - [ ] MEMORY HELP
    - [ ] MEMORY-MALLOC-STATS
    - [ ] MEMORY-PURGE
    - [ ] MEMORY-STATS
    - [ ] MEMORY-USAGE
    - [ ] MONITOR
    - [ ] REPLICAOF
    - [ ] ROLE
    - [ ] SAVE
    - [ ] SHUTDOWN
    - [ ] SLAVEOF
    - [ ] SLOWLOG
    - [ ] SYNC
    - [ ] TIME
</details>

<details>
<summary>
Sets
</summary>

- [ ] Sets
    - [x] SADD
    - [x] SCARD
    - [x] SDIFF
    - [ ] SDIFFSTORE
    - [x] SINTER
    - [ ] SINTERSTORE
    - [ ] SISMEMBER
    - [x] SMEMBERS
    - [ ] SMOVE
    - [ ] SPOP
    - [ ] SRANDMEMBER
    - [ ] SREM
    - [ ] SUNION
    - [ ] SUNIONSTORE
    - [ ] SSCAN
</details>

<details>
<summary>
Sorted Sets
</summary>

- [ ] Sorted Sets
    - [ ] ZADD
    - [ ] ZCARD
    - [ ] ZCOUNT
    - [ ] ZINCRBY
    - [ ] ZINTERSTORE
    - [ ] ZLEXCOUNT
    - [ ] ZPOPMAX
    - [ ] ZPOPMIN
    - [ ] ZRANGE
    - [ ] ZRANGEBYLEX
    - [ ] ZREVRANGEBYLEX
    - [ ] ZRANGEBYSCORE
    - [ ] ZRANK
    - [ ] ZREM
    - [ ] ZREMRANGEBYLEX
    - [ ] ZREMRANGEBYRANK
    - [ ] ZREMRANGEBYSCORE
    - [ ] ZREVRANGE
    - [ ] ZREVRANGEBYSCORE
    - [ ] ZREVRANK
    - [ ] ZSCORE
    - [ ] ZUNIONSTORE
    - [ ] ZSCAN
</details>

<details>
<summary>
Streams
</summary>

- [ ] Streams
    - [ ] XACK
    - [ ] XADD
    - [ ] XCLAIM
    - [ ] XDEL
    - [ ] XGROUP
    - [ ] XINFO
    - [ ] XLEN
    - [ ] XPENDING
    - [ ] XRANGE
    - [ ] XREAD
    - [ ] XREADGROUP
    - [ ] XREVRANGE
    - [ ] XTRIM
</details>

<details>
<summary>
Strings
</summary>

- [ ] Strings
    - [ ] APPEND
    - [ ] BITCOUNT
    - [ ] BITFIELD
    - [ ] BITOP
    - [ ] BITPOS
    - [ ] DECR
    - [ ] DECRBY
    - [x] GET
    - [ ] GETBIT
    - [ ] GETRANGE
    - [ ] GETSET
    - [ ] INCR
    - [ ] INCRBY
    - [ ] INCRBYFLOAT
    - [ ] MGET
    - [ ] MSET
    - [ ] MSETNX
    - [ ] PSETEX
    - [x] SET
    - [ ] SETBIT
    - [ ] SETEX
    - [ ] SETNX
    - [ ] SETRANGE
    - [ ] STRLEN
</details>

<details>
<summary>
Transactions
</summary>

- [ ] Transactions
    - [ ] DISCARD
    - [ ] EXEC
    - [ ] MULTI
    - [ ] UNWATCH
    - [ ] WATCH
</details>

