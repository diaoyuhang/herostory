package handler;

import broadcast.BroadcastChannelGroup;
import broadcast.UserInfosUtils;
import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import msg.GameMsgProtocol;
import msgHandler.*;
import pojo.User;

import java.util.HashMap;
import java.util.Map;

public class GameMsgInbound extends SimpleChannelInboundHandler {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        BroadcastChannelGroup.addChannel(ctx.channel());
        super.channelActive(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        BroadcastChannelGroup.removeChannel(ctx.channel());
        Object userIdObj = ctx.channel().attr(AttributeKey.valueOf("userId")).get();

        if (userIdObj==null){
            return;
        }

        Integer userId = (Integer)userIdObj;
        UserInfosUtils.remove(userId);


        GameMsgProtocol.UserQuitResult userQuitResult = GameMsgProtocol.UserQuitResult.newBuilder().setQuitUserId(userId).build();
        BroadcastChannelGroup.broadcast(userQuitResult);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        CmdHandlerFactory.handler(channelHandlerContext, o);
    }


}
