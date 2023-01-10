package handler;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import msg.GameMsgProtocol;
import pojo.User;

import java.util.HashMap;
import java.util.Map;

public class GameMsgInbound extends SimpleChannelInboundHandler {

    private static DefaultChannelGroup defaultChannelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static Map<Integer, User> users = new HashMap<>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        defaultChannelGroup.add(ctx.channel());
        super.channelActive(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        users.remove(userId);
        defaultChannelGroup.remove(ctx.channel());

        GameMsgProtocol.UserQuitResult userQuitResult = GameMsgProtocol.UserQuitResult.newBuilder().setQuitUserId(userId).build();
        defaultChannelGroup.writeAndFlush(userQuitResult);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        System.out.println("msg:" + o);
        GeneratedMessageV3 result = null;
        if (o instanceof GameMsgProtocol.UserEntryCmd) {
            GameMsgProtocol.UserEntryCmd entryCmd = (GameMsgProtocol.UserEntryCmd) o;
            result = GameMsgProtocol.UserEntryResult.newBuilder().setUserId(entryCmd.getUserId()).setHeroAvatar(entryCmd.getHeroAvatar()).build();

            Channel channel = channelHandlerContext.channel();
            channel.attr(AttributeKey.valueOf("userId")).set(entryCmd.getUserId());

            users.put(entryCmd.getUserId(), new User(entryCmd.getUserId(), entryCmd.getHeroAvatar()));
            defaultChannelGroup.writeAndFlush(result);
        } else if (o instanceof GameMsgProtocol.WhoElseIsHereCmd) {
            Integer userId = (Integer) channelHandlerContext.channel().attr(AttributeKey.valueOf("userId")).get();

            for (User user : users.values()) {
                //排除当前用户
                if (user.getUserId() != userId) {
                    GameMsgProtocol.WhoElseIsHereResult.UserInfo userInfo =
                            GameMsgProtocol.WhoElseIsHereResult.UserInfo.newBuilder().setUserId(user.getUserId()).setHeroAvatar(user.getHeroAvatar()).build();

                    result = GameMsgProtocol.WhoElseIsHereResult.newBuilder().setUserInfo(user.getUserId(), userInfo).build();
                    channelHandlerContext.writeAndFlush(result);
                }
            }
        } else if (o instanceof GameMsgProtocol.UserMoveToCmd) {
            GameMsgProtocol.UserMoveToCmd userMoveToCmd = (GameMsgProtocol.UserMoveToCmd) o;
            Integer userId = (Integer) channelHandlerContext.channel().attr(AttributeKey.valueOf("userId")).get();

            GameMsgProtocol.UserMoveToResult userMoveToResult = GameMsgProtocol.UserMoveToResult.newBuilder()
                    .setMoveUserId(userId)
                    .setMoveFromPosX(userMoveToCmd.getMoveFromPosX())
                    .setMoveFromPosY(userMoveToCmd.getMoveFromPosY())
                    .setMoveToPosX(userMoveToCmd.getMoveToPosX())
                    .setMoveToPosY(userMoveToCmd.getMoveToPosY())
                    .build();

            defaultChannelGroup.writeAndFlush(userMoveToResult);
        }


    }
}
