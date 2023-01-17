package msgHandler;

import broadcast.BroadcastChannelGroup;
import broadcast.UserInfosUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import msg.GameMsgProtocol;
import pojo.User;

public class UserAttkCmdHandler implements ICmdHandler<GameMsgProtocol.UserAttkCmd> {

    @Override
    public void handler(ChannelHandlerContext channelHandlerContext, GameMsgProtocol.UserAttkCmd o) {
        Integer userId = (Integer) channelHandlerContext.channel().attr(AttributeKey.valueOf("userId")).get();
        //被攻击的用户id
        int targetUserId = o.getTargetUserId();

        //广播攻击
        GameMsgProtocol.UserAttkResult.Builder builder = GameMsgProtocol.UserAttkResult.newBuilder();
        builder.setAttkUserId(userId);
        builder.setTargetUserId(targetUserId);

        BroadcastChannelGroup.broadcast(builder.build());
        //广播扣血
        GameMsgProtocol.UserSubtractHpResult.Builder newBuilder = GameMsgProtocol.UserSubtractHpResult.newBuilder();
        newBuilder.setTargetUserId(targetUserId);
        newBuilder.setSubtractHp(10);
        BroadcastChannelGroup.broadcast(newBuilder.build());

        //广播死亡
        User user = UserInfosUtils.get(userId);
        user.setHp(user.getHp() - 10);

        if (user.getHp() <= 0) {
            GameMsgProtocol.UserDieResult.Builder dieBuilder = GameMsgProtocol.UserDieResult.newBuilder();
            dieBuilder.setTargetUserId(targetUserId);
            BroadcastChannelGroup.broadcast(dieBuilder.build());
        }
    }
}
