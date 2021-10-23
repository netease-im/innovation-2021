package sample;


import Core.annotation.ServiceHandlerPackage;
import Core.annotation.SingletonObjHandlerPackage;
import Core.annotation.StaticPath;
import Core.net.NetService;

@SingletonObjHandlerPackage("sample.SingletonHandler")
@ServiceHandlerPackage("sample.ServiceHandler")
@StaticPath("/static")
public class serverDemo {
    public static void main(String[] args) {
        new NetService(serverDemo.class).startServer(80);
    }
}

