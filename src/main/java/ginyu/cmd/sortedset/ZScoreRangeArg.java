package ginyu.cmd.sortedset;

import ginyu.cmd.KeyArg;
import ginyu.protocol.Arrays;
import ginyu.protocol.Validates;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * @author: junjiexun
 * @date: 2020/10/16 2:54 下午
 * @description:
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ZScoreRangeArg extends KeyArg {

    private Boolean openIntervalMin;

    private Double min;

    private Boolean openIntervalMax;

    private Double max;


    public static ZScoreRangeArg create(Arrays arrays) {
        List<String> args = arrays.map2String(true);
        String key = args.remove(0);
        String minStr = args.remove(0);
        Boolean openIntervalMin = false;
        if (minStr.startsWith("(")) {
            openIntervalMin = true;
            minStr = minStr.substring(1);
        }
        Double min = Validates.s2d(minStr, "min");

        String maxStr = args.remove(0);
        Boolean openIntervalMax = false;
        if (maxStr.startsWith("(")) {
            openIntervalMax = true;
            maxStr = maxStr.substring(1);
        }
        Double max = Validates.s2d(maxStr, "max");
        ZScoreRangeArg zScoreRangeArg = new ZScoreRangeArg();
        zScoreRangeArg.setKey(key);
        zScoreRangeArg.setOpenIntervalMin(openIntervalMin);
        zScoreRangeArg.setMin(min);
        zScoreRangeArg.setOpenIntervalMax(openIntervalMax);
        zScoreRangeArg.setMax(max);
        return zScoreRangeArg;
    }
}
