import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

public class Server {

    public static void main(String[] args) throws InterruptedException {

        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup workers = new NioEventLoopGroup(3);

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        ChannelFuture channelFuture = serverBootstrap.group(boss, workers).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                ChannelPipeline pipeline = nioSocketChannel.pipeline();
                pipeline.addLast(new HttpServerCodec());
                pipeline.addLast(new HttpObjectAggregator(65535));
                pipeline.addLast(new WebSocketServerProtocolHandler("/websocket"));
                pipeline.addLast(new GameMsgInbound());

            }
        }).bind(8080).sync();

        System.out.println("服务启动！");
        channelFuture.channel().closeFuture().sync();

    }

}
