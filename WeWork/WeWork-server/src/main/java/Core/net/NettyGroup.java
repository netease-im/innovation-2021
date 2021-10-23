package Core.net;

import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NettyGroup {
    public static Map<Integer, ChannelId>user=new ConcurrentHashMap<>();
    public static ChannelGroup group=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
}
