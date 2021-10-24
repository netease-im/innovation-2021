package Core.net;

import Core.annotation.ServiceHandlerPackage;
import Core.annotation.SingletonObjHandlerPackage;
import Core.annotation.StaticPath;
import Core.factory.FactoryUtils;
import Core.factory.SingletonObjInitFactory;
import Core.handlers.ServiceHandlerCenter;
import Core.handlers.ReleaseResourceHandler;
import Core.handlers.StaticHandler;
import Core.handlers.WebSocketHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.concurrent.TimeUnit;
import Core.factory.ServiceHandlerFactory;


//知识点:
//Netty:Netty 是一个利用 Java 的高级网络的能力，隐藏其背后的复杂性而提供一个易于使用的 API 的客户端/服务器框架。

// Netty和Tomcat最大的区别就在于通信协议，Tomcat是基于Http协议的，
// 他的实质是一个基于http协议的web容器，但是Netty不一样，他能通过编程自定义各种协议，
// 因为netty能够通过codec自己来编码/解码字节流，完成类似redis访问的功能，这就是netty和tomcat最大的不同。

//EventLoop:a programming construct that waits for and dispatches events or messages in a program
//          Event Loop是一个程序结构，用于等待和发送消息和事件

//channel:可以理解为 Socket 连接,是通讯的载体,，它负责基本的 IO 操作，例如：bind，connect，read，write 等等

//option和childOption的区别:ServerBootStrap.option parameters apply to the server socket (Server channel)
//                        that is listening for connections and
//                        ServerBootStrap.childOption parameters apply to the socket
//                        that gets created once the connection is accepted by the server socket
//                        简单来讲：option作用于bossGroup,childOption作用于workerGroup



public  class NetService {

    private final Logger log= LoggerFactory.getLogger(NetService.class);
    private final String resourcesPath;

    public NetService(Class<?>c) {

        File file=new File(FactoryUtils.planCenterUrl);
        if(!file.exists()){
            if(file.mkdir()){
                log.info("create dir planCenter to {}",FactoryUtils.planCenterUrl);
            }
        }

        SingletonObjHandlerPackage o=c.getAnnotation(SingletonObjHandlerPackage.class);
        String[]paths=o.value();
        SingletonObjInitFactory.setInstance(new SingletonObjInitFactory(paths));

        ServiceHandlerPackage s=c.getAnnotation(ServiceHandlerPackage.class);
        paths=s.value();
        ServiceHandlerFactory.setInstance(new ServiceHandlerFactory(paths));

        if(c.isAnnotationPresent(StaticPath.class)){
            resourcesPath=c.getAnnotation(StaticPath.class).value();
        }else{
            resourcesPath="";
        }
    }

    /**
     * <p>
     *     start a server
     * </p>
     *
     * @param portTmp the port that the server is on
     */
    public void startServer(int portTmp){
        int port = NetUtils.getPort(portTmp);
        //开启一个服务器实例
        ServerBootstrap server=new ServerBootstrap();

        //WorkerEventLoopGroup:用于服务器端接受客户端的连接,只负责处理连接，
        // 故开销非常小，如果连接到来，马上按照策略将SocketChannel转发给WorkerEventLoopGroup
        EventLoopGroup bossGroup = new NioEventLoopGroup();

        //WorkerEventLoopGroup:用于网络事件的处理
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        //绑定线程池,一个请求对应一个线程
        server.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
            protected void initChannel(SocketChannel c) throws Exception {
                ChannelPipeline p=c.pipeline();
                p.addLast(new IdleStateHandler(0, 0, 30 * 3, TimeUnit.SECONDS));
                p.addLast(new HttpServerCodec());
                p.addLast(new HttpObjectAggregator(5 * 1024 * 1024));
                p.addLast("http-chunked", new ChunkedWriteHandler());

                //deal with websocket
                p.addLast(new WebSocketHandler());

                //deal with static resources request
                p.addLast(new StaticHandler(resourcesPath));

                //deal with service request (servlet)
                p.addLast(new ServiceHandlerCenter());

                //release the resources
                p.addLast(new ReleaseResourceHandler());

            }
        }).childOption(ChannelOption.SO_KEEPALIVE,true)   //开启心跳机制
                .option(ChannelOption.SO_BACKLOG, 1024);  //等待队列最大等待量

        try {
            ChannelFuture f=server.bind(port).sync();
            log.info("the server has been started successfully on the port[{}]",port);
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.info("the server has been stopped");
        }finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
