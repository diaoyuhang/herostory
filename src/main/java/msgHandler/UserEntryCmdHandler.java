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
        GameMsgProtocol.UserEntryCmd entryCmd = o;
        result = GameMsgProtocol.UserEntryResult.newBuilder().setUserId(entryCmd.getUserId()).setHeroAvatar(entryCmd.getHeroAvatar()).build();

        Channel channel = channelHandlerContext.channel();
        channel.attr(AttributeKey.valueOf("userId")).set(entryCmd.getUserId());

        UserInfosUtils.put(entryCmd.getUserId(), new User(entryCmd.getUserId(), entryCmd.getHeroAvatar()));
        BroadcastChannelGroup.broadcast(result);
    }
}
