package msgHandler;

import broadcast.UserInfosUtils;
import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import msg.GameMsgProtocol;
import pojo.User;

public class WhoElseIsHereCmdHandler implements ICmdHandler<GameMsgProtocol.WhoElseIsHereCmd>{
    @Override
    public void handler(ChannelHandlerContext channelHandlerContext, GameMsgProtocol.WhoElseIsHereCmd o) {
        GeneratedMessageV3 result;
        Integer userId = (Integer) channelHandlerContext.channel().attr(AttributeKey.valueOf("userId")).get();

        GameMsgProtocol.WhoElseIsHereResult.Builder whoElseIsHereResultBuilder = GameMsgProtocol.WhoElseIsHereResult.newBuilder();

        for (User user : UserInfosUtils.userList()) {
            //排除当前用户
            if (!user.getUserId().equals(userId)) {
                GameMsgProtocol.WhoElseIsHereResult.UserInfo.Builder userInfoBuilder = GameMsgProtocol.WhoElseIsHereResult.UserInfo.newBuilder();
                userInfoBuilder.setUserId(user.getUserId());
                userInfoBuilder.setHeroAvatar(user.getHeroAvatar());

                GameMsgProtocol.WhoElseIsHereResult.UserInfo.MoveState moveState = user.getMoveState();
                if (moveState!=null){
                    userInfoBuilder.setMoveState(moveState);
                }

                whoElseIsHereResultBuilder.addUserInfo(userInfoBuilder.build());
            }
        }
        channelHandlerContext.writeAndFlush(whoElseIsHereResultBuilder.build());
    }
}
