package msgHandler;

import broadcast.BroadcastChannelGroup;
import broadcast.UserInfosUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import msg.GameMsgProtocol;
import pojo.User;

import java.util.Date;

public class UserMoveToCmdHandler implements ICmdHandler<GameMsgProtocol.UserMoveToCmd>{
    @Override
    public void handler(ChannelHandlerContext channelHandlerContext, GameMsgProtocol.UserMoveToCmd userMoveToCmd) {
        Integer userId = (Integer) channelHandlerContext.channel().attr(AttributeKey.valueOf("userId")).get();

        Date curDate = new Date();
        GameMsgProtocol.UserMoveToResult userMoveToResult = GameMsgProtocol.UserMoveToResult.newBuilder()
                .setMoveUserId(userId)
                .setMoveFromPosX(userMoveToCmd.getMoveFromPosX())
                .setMoveFromPosY(userMoveToCmd.getMoveFromPosY())
                .setMoveToPosX(userMoveToCmd.getMoveToPosX())
                .setMoveToPosY(userMoveToCmd.getMoveToPosY())
                .setMoveStartTime(curDate.getTime())
                .build();

        User user = UserInfosUtils.get(userId);
        GameMsgProtocol.WhoElseIsHereResult.UserInfo.MoveState.Builder moveStateBuilder = GameMsgProtocol.WhoElseIsHereResult.UserInfo.MoveState.newBuilder();
        moveStateBuilder.setFromPosX(userMoveToCmd.getMoveFromPosX());
        moveStateBuilder.setFromPosY(userMoveToCmd.getMoveFromPosY());
        moveStateBuilder.setToPosX(userMoveToCmd.getMoveToPosX());
        moveStateBuilder.setToPosY(userMoveToCmd.getMoveToPosY());
        moveStateBuilder.setStartTime(curDate.getTime());

        user.setMoveState(moveStateBuilder.build());

        BroadcastChannelGroup.broadcast(userMoveToResult);
    }
}
