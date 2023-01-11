package msgHandler;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;
import msg.GameMsgProtocol;

public class CmdHandler {

    public static void handler(ChannelHandlerContext channelHandlerContext, Object o) {
        if (o instanceof GameMsgProtocol.UserEntryCmd) {
            (new UserEntryCmdHandler()).handler(channelHandlerContext, cast(o));
        } else if (o instanceof GameMsgProtocol.WhoElseIsHereCmd) {
            (new WhoElseIsHereCmdHandler()).handler(channelHandlerContext, cast(o));
        } else if (o instanceof GameMsgProtocol.UserMoveToCmd) {
            (new UserMoveToCmdHandler()).handler(channelHandlerContext, cast(o));
        }
    }

    private static <TCmd extends GeneratedMessageV3> TCmd cast(Object o) {
        if (o == null) {
            return null;
        }
        return (TCmd) o;
    }
}
