package Core.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

@SuppressWarnings(value = "unchecked")
public abstract class SimpleChannelHandler<I> extends SimpleChannelInboundHandler<I> {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (this.acceptInboundMessage(msg)) {
            this.messageReceived(ctx, (I)msg);
            ctx.fireChannelRead(msg);
        } else {
            ReferenceCountUtil.release(msg);
        }
    }
}

