package handler;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import msg.GameMsgProtocol;

public class GameMsgEncoder extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {

        if (msg == null || !(msg instanceof GeneratedMessageV3)) {
            super.write(ctx, msg, promise);
            return;
        }

        short msgCode = (short) MsgCodecUtils.getMsgCodeFromGeneratedMessageV3(msg);

        if (msgCode == -1){
            return;
        }

        ByteBuf buffer = ctx.alloc().buffer();
        buffer.writeShort((short) 0);
        buffer.writeShort(msgCode);

        GeneratedMessageV3 result = (GeneratedMessageV3) msg;
        buffer.writeBytes(result.toByteArray());

        BinaryWebSocketFrame binaryWebSocketFrame = new BinaryWebSocketFrame(buffer);
        super.write(ctx, binaryWebSocketFrame, promise);
    }
}
