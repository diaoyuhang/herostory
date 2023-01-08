package handler;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import msg.GameMsgProtocol;

public class GameMsgInbound extends SimpleChannelInboundHandler {

    private static DefaultChannelGroup defaultChannelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        defaultChannelGroup.add(ctx.channel());
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        System.out.println("msg:" + o);
        GeneratedMessageV3 result = null;
        if (o instanceof GameMsgProtocol.UserEntryCmd){
            GameMsgProtocol.UserEntryCmd entryCmd = (GameMsgProtocol.UserEntryCmd) o;
            result = GameMsgProtocol.UserEntryResult.newBuilder().setUserId(entryCmd.getUserId()).setHeroAvatar(entryCmd.getHeroAvatar()).build();
        }

        defaultChannelGroup.writeAndFlush(result);

    }
}
