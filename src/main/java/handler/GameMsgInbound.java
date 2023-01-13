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
        Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        UserInfosUtils.remove(userId);

        BroadcastChannelGroup.removeChannel(ctx.channel());

        GameMsgProtocol.UserQuitResult userQuitResult = GameMsgProtocol.UserQuitResult.newBuilder().setQuitUserId(userId).build();
        BroadcastChannelGroup.broadcast(userQuitResult);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        System.out.println("msg:" + o);
        CmdHandlerFactory.handler(channelHandlerContext, o);
    }


}
