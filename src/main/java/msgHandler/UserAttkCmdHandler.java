package msgHandler;

import broadcast.BroadcastChannelGroup;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import msg.GameMsgProtocol;

public class UserAttkCmdHandler implements ICmdHandler<GameMsgProtocol.UserAttkCmd>{

    @Override
    public void handler(ChannelHandlerContext channelHandlerContext, GameMsgProtocol.UserAttkCmd o) {
        Integer userId = (Integer) channelHandlerContext.channel().attr(AttributeKey.valueOf("userId")).get();

        int targetUserId = o.getTargetUserId();

        GameMsgProtocol.UserAttkResult.Builder builder = GameMsgProtocol.UserAttkResult.newBuilder();
        builder.setAttkUserId(userId);
        builder.setTargetUserId(targetUserId);

        BroadcastChannelGroup.broadcast(builder.build());

        GameMsgProtocol.UserSubtractHpResult.Builder newBuilder = GameMsgProtocol.UserSubtractHpResult.newBuilder();
        newBuilder.setTargetUserId(targetUserId);
        newBuilder.setSubtractHp(100);
        BroadcastChannelGroup.broadcast(newBuilder.build());

    }
}
