package handler;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import msg.GameMsgProtocol;

public class GameMsgDecoder extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //前面的HttpServerCodec已经保证的消息的完整性
        BinaryWebSocketFrame binaryWebSocketFrame = (BinaryWebSocketFrame) msg;
        ByteBuf byteBuf = binaryWebSocketFrame.content();
        //读掉消息的长度
        byteBuf.readShort();
        //读取到消息的类型
        short msgCode = byteBuf.readShort();

        byte[] msgBody = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(msgBody);

        GeneratedMessageV3 cmd = null;
        MsgCodecUtils.getGeneratedMessageV3FromMsgCode(msgBody,msgCode);
        if (null != cmd) {
            ctx.fireChannelRead(cmd);
        }
    }
}
