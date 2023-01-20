package msgHandler;

import broadcast.UserInfosUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import msg.GameMsgProtocol;
import pojo.User;

import java.util.concurrent.ThreadLocalRandom;

public class UserLoginCmdHandler implements ICmdHandler<GameMsgProtocol.UserLoginCmd>{
    @Override
    public void handler(ChannelHandlerContext channelHandlerContext, GameMsgProtocol.UserLoginCmd o) {

        GameMsgProtocol.UserLoginResult.Builder builder = GameMsgProtocol.UserLoginResult.newBuilder();
        builder.setUserId(ThreadLocalRandom.current().nextInt(100000));
        builder.setUserName(o.getName());
        builder.setHeroAvatar("Hero_Hammer");
        GameMsgProtocol.UserLoginResult loginResult = builder.build();


        Channel channel = channelHandlerContext.channel();
        channel.attr(AttributeKey.valueOf("userId")).set(loginResult.getUserId());

        UserInfosUtils.put(loginResult.getUserId(), new User(loginResult.getUserId(),"Hero_Hammer",o.getName()));

        channelHandlerContext.writeAndFlush(loginResult);
    }
}
