package broadcast;

import io.netty.channel.Channel;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class BroadcastChannelGroup {

    private static DefaultChannelGroup defaultChannelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private BroadcastChannelGroup() {
    }

    public static void addChannel(Channel channel) {
        defaultChannelGroup.add(channel);
    }

    public static void removeChannel(Channel channel) {
        defaultChannelGroup.remove(channel);
    }

    public static void broadcast(Object msg) {
        defaultChannelGroup.writeAndFlush(msg);
    }
}
