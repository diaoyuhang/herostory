package msgHandler;

import broadcast.BroadcastChannelGroup;
import broadcast.UserInfosUtils;
import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import msg.GameMsgProtocol;
import pojo.User;

public class UserEntryCmdHandler implements ICmdHandler<GameMsgProtocol.UserEntryCmd> {

    @Override
    public void handler(ChannelHandlerContext channelHandlerContext, GameMsgProtocol.UserEntryCmd o) {
        GeneratedMessageV3 result;

        Channel channel = channelHandlerContext.channel();
        Integer userId = (Integer) channel.attr(AttributeKey.valueOf("userId")).get();
        User user = UserInfosUtils.get(userId);
        if (user==null){
            return;
        }

        result = GameMsgProtocol.UserEntryResult.newBuilder().setUserId(user.getUserId()).setHeroAvatar(user.getHeroAvatar()).build();

        BroadcastChannelGroup.broadcast(result);
    }
}
