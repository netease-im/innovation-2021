package Core.handlers;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

public class HttpResponseHandler {
    public ChannelHandlerContext ctx;

    public HttpResponseHandler(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    public static void write(String content, ChannelHandlerContext ctx){
        FullHttpResponse response = new DefaultFullHttpResponse
                (HttpVersion.HTTP_1_1, HttpResponseStatus.OK,Unpooled.copiedBuffer(content, CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/html; charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    public void write(String content){
        FullHttpResponse response = new DefaultFullHttpResponse
                (HttpVersion.HTTP_1_1, HttpResponseStatus.OK,Unpooled.copiedBuffer(content, CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/html; charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    public  void writeStatus(HttpResponseStatus s){
        FullHttpResponse response = new DefaultFullHttpResponse
                (HttpVersion.HTTP_1_1, s);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/html; charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}
