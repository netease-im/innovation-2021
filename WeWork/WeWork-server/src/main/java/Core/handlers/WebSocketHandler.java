package Core.handlers;

import Core.net.NettyGroup;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.*;

public class WebSocketHandler extends SimpleChannelHandler<Object>{
    private WebSocketServerHandshaker handshake;
    private static final String WEB_SOCKET_URL = "ws://localhost:81";
//    private static final String WEB_SOCKET_URL ="ws://49.234.97.138";
    @Override
    protected void messageReceived(ChannelHandlerContext cxt, Object o) throws Exception {
        if(o instanceof WebSocketFrame){
            if(o instanceof CloseWebSocketFrame){
                NettyGroup.group.remove(cxt.channel());
                handshake.close(cxt.channel(),((CloseWebSocketFrame) o).retain());
                return;
            }
            String msg = ((TextWebSocketFrame) o).text();
            //format: refresh?uid
            String[]args=msg.split("\\?");
            if("refresh".equals(args[0])){
                // send message to the client to refresh the messageList
                TextWebSocketFrame tws=new TextWebSocketFrame(args[0]);
                if(NettyGroup.user.containsKey(Integer.parseInt(args[1]))){
                    int uid=Integer.parseInt(args[1]);
                    if(!NettyGroup.user.containsKey(uid)){
                        return;
                    }
                    Channel c=NettyGroup.group.find(NettyGroup.user.get(uid));
                    if(c==null||!c.isActive()){
                        NettyGroup.user.remove(uid);
                        return;
                    }
                    c.writeAndFlush(tws);
                }
            }else if("IM".equals(args[0])){

                //format: uid & uid & uid & uid & plan_name & host_id & sender & msg
                String[]split=args[1].split("&");
                int l= split.length;
                String text=split[l-1];
                String sender=split[l-2];
                String host_id=split[l-3];
                String plan_name=split[l-4];


                int i=0;
                for(String x:split){
                    if(i==l-4){
                        break;
                    }
                    int uid=Integer.parseInt(x);
                    if(!NettyGroup.user.containsKey(uid)){
                        i++;
                        continue;
                    }
                    TextWebSocketFrame tws=new TextWebSocketFrame(args[0]+"&"+plan_name+"&"+host_id+"&"+sender+"&"+text);
                    Channel c=NettyGroup.group.find(NettyGroup.user.get(uid));
                    if(c==null||!c.isActive()){
                        NettyGroup.user.remove(uid);
                        i++;
                        continue;
                    }
                    c.writeAndFlush(tws);
                    i++;
                }
            }
        }else if(o instanceof FullHttpRequest){
            FullHttpRequest req=(FullHttpRequest)o;
            if(req.headers().contains("Upgrade")&&"websocket".contentEquals(req.headers().get("Upgrade"))){
                WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(WEB_SOCKET_URL, null, false);
                handshake = wsFactory.newHandshaker(req);
                if(handshake !=null){
                    handshake.handshake(cxt.channel(),req);
                    String uri=req.uri();
                    int uid=Integer.parseInt(uri.substring(uri.indexOf("?")).split("=")[1]);
                    NettyGroup.group.add(cxt.channel());
                    NettyGroup.user.put(uid,cxt.channel().id());
                }
            }
        }
    }
}
