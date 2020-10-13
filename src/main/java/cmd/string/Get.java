package cmd.string;

import cmd.AbstractRedisCommand;
import cmd.Command;
import exception.CommandValidateException;
import protocol.Arrays;
import protocol.BulkStrings;
import protocol.Resp2;

/**
 * @author: junjiexun
 * @date: 2020/10/13 1:56 下午
 * @description:
 */
@SuppressWarnings("all")
@Command(name = "get")
public class Get extends AbstractRedisCommand<GetArg> {
    @Override
    public GetArg createArg(Arrays arrays) {
        BulkStrings bulkStrings = (BulkStrings) arrays.getData().get(1);
        return new GetArg(bulkStrings.getData().getContent());
    }

    @Override
    protected void validate(String commandName, Arrays arrays) {
        if (arrays.getData().size() != 2) {
            throw new CommandValidateException("ERR wrong number of arguments for '%s' command", commandName);
        }
    }

    @Override
    protected Resp2 doCommand0(GetArg arg) {
//        return BulkStrings.create("xjj");
        return BulkStrings.create(null);
    }
}
