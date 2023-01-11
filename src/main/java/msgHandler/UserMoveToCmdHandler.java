package msgHandler;

import broadcast.BroadcastChannelGroup;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import msg.GameMsgProtocol;

public class UserMoveToCmdHandler implements ICmdHandler<GameMsgProtocol.UserMoveToCmd>{
    @Override
    public void handler(ChannelHandlerContext channelHandlerContext, GameMsgProtocol.UserMoveToCmd userMoveToCmd) {
        Integer userId = (Integer) channelHandlerContext.channel().attr(AttributeKey.valueOf("userId")).get();

        GameMsgProtocol.UserMoveToResult userMoveToResult = GameMsgProtocol.UserMoveToResult.newBuilder()
                .setMoveUserId(userId)
                .setMoveFromPosX(userMoveToCmd.getMoveFromPosX())
                .setMoveFromPosY(userMoveToCmd.getMoveFromPosY())
                .setMoveToPosX(userMoveToCmd.getMoveToPosX())
                .setMoveToPosY(userMoveToCmd.getMoveToPosY())
                .build();

        BroadcastChannelGroup.broadcast(userMoveToResult);
    }
}
