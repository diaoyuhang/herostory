import handler.GameMsgDecoder;
import handler.GameMsgEncoder;
import handler.GameMsgInbound;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {
    static Logger logger = LoggerFactory.getLogger(Server.class);
    public static void main(String[] args) throws InterruptedException {

        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup workers = new NioEventLoopGroup(3);

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        ChannelFuture channelFuture = serverBootstrap.group(boss, workers).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                ChannelPipeline pipeline = nioSocketChannel.pipeline();

                //由于HttpServerCodec已经保证了消息的完整新，就不需要自己再进行拆包粘包处理
                pipeline.addLast(new HttpServerCodec());
                pipeline.addLast(new HttpObjectAggregator(65535));
                pipeline.addLast(new WebSocketServerProtocolHandler("/websocket"));
                pipeline.addLast(new GameMsgDecoder());
                pipeline.addLast(new GameMsgEncoder());
                pipeline.addLast(new GameMsgInbound());

            }
        }).bind(8080).sync();

//        System.out.println("服务启动！");
        logger.warn("服务启动成功");
        channelFuture.channel().closeFuture().sync();

    }

}
