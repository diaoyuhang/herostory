package msgHandler;

import broadcast.UserInfosUtils;
import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import msg.GameMsgProtocol;
import pojo.User;

import java.util.HashMap;
import java.util.Map;

public class WhoElseIsHereCmdHandler implements ICmdHandler<GameMsgProtocol.WhoElseIsHereCmd>{
    @Override
    public void handler(ChannelHandlerContext channelHandlerContext, GameMsgProtocol.WhoElseIsHereCmd o) {
        GeneratedMessageV3 result;
        Integer userId = (Integer) channelHandlerContext.channel().attr(AttributeKey.valueOf("userId")).get();

        for (User user : UserInfosUtils.userList()) {
            //排除当前用户
            if (user.getUserId() != userId) {
                GameMsgProtocol.WhoElseIsHereResult.UserInfo userInfo =
                        GameMsgProtocol.WhoElseIsHereResult.UserInfo.newBuilder().setUserId(user.getUserId()).setHeroAvatar(user.getHeroAvatar()).build();

                result = GameMsgProtocol.WhoElseIsHereResult.newBuilder().setUserInfo(user.getUserId(), userInfo).build();
                channelHandlerContext.writeAndFlush(result);
            }
        }
    }
}
