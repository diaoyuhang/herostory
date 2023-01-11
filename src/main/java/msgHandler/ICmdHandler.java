package msgHandler;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;

public interface ICmdHandler<TCmd extends GeneratedMessageV3> {

    void handler(ChannelHandlerContext channelHandlerContext, TCmd o);
}
