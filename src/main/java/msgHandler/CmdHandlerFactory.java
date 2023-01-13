package msgHandler;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;
import msg.GameMsgProtocol;

import java.util.HashMap;
import java.util.Map;

public final class CmdHandlerFactory {


    public static final Map<Class, ICmdHandler> cmdMap = new HashMap<>();

    static {
        cmdMap.put(UserEntryCmdHandler.class, new UserEntryCmdHandler());
        cmdMap.put(WhoElseIsHereCmdHandler.class, new WhoElseIsHereCmdHandler());
        cmdMap.put(UserMoveToCmdHandler.class, new UserMoveToCmdHandler());
    }

    public static void handler(ChannelHandlerContext channelHandlerContext, Object o) {
        cmdMap.get(o.getClass()).handler(channelHandlerContext, cast(o));

    }

    private static <TCmd extends GeneratedMessageV3> TCmd cast(Object o) {
        if (o == null) {
            return null;
        }
        return (TCmd) o;
    }
}
