package ginyu.common;

import ginyu.cmd.hash.HashEntry;

/**
 * @author: junjiexun
 * @date: 2020/10/11 10:23 下午
 * @description:
 */
public abstract class Constants {

    public static final String CR = "\r";
    public static final String LF = "\n";

    public static final String RESP_SPLIT = CR + LF;

    public static final byte[] SPLIT_BYTE = RESP_SPLIT.getBytes();

    public static final int SKIP = RESP_SPLIT.length();

    public static final String SIMPLE_STRINGS_FLAG = "+";

    public static final String ERRORS_FLAG = "-";

    public static final String BULK_STRINGS_FLAG = "$";

    public static final String INTEGERS_FLAG = ":";

    public static final String ARRAYS_FLAG = "*";

    public static final String[] STR_EMPTY_ARRAY = new String[]{};
    public static final HashEntry[] HASH_ENTRIES_EMPTY_ARRAY = new HashEntry[]{};

    public static String SLOGAN = "ギニュー特戦队!";

    public static String DEFAULT_SNAPSHOT_PATH = "./dump.ginyu";
}
