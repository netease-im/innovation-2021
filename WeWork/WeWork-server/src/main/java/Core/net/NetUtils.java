package Core.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * <p>
 *     net utils, deal with net work
 * </p>
 *
 * @author ian 2021/7/19
 */
public class NetUtils {
    //使用指定的类XXX初始化日志对象，方便在日志输出的时候，可以打印出日志信息所属的类
    private static final Logger logger=LoggerFactory.getLogger(NetUtils.class);


    /**
     *
     * @param defaultPort if defaultPort is available, return the default port,
     *                    else return the available port which is the closest to
     *                    the default
     * @return the available port
     */
    public static int getPort(int defaultPort) {
        //在这个取值范围中1023以下的端口已经分配给了常用的一些应用程序，这个数字以后的端口部分被使用，
        // 所以网络编程可用的端口一般在1024之后选取。
         if(!isUsed(defaultPort)){
             return defaultPort;
         }
         int acc=1;
         while (defaultPort-acc>=1024||defaultPort+acc<=65535){
             int leftPort=defaultPort-acc;
             int rightPort=defaultPort+acc;
             if(leftPort>=1024&&!isUsed(leftPort)){
                 return leftPort;
             }
             if (rightPort<=65535&&!isUsed(rightPort)){
                 return rightPort;
             }
             acc++;
         }
         throw new RuntimeException("no port is available.");
    }


    /**
     *
     * @param port check if the port is used
     * @return if the port is used or not
     */
    public static boolean isUsed(int port) {
        boolean used;
        ServerSocket serverSocket = null;
        try {

            //socket可以使一个应用从网络中读取和写入数据，不同计算机上的两个应用可以通过连接发送和接受字节流，
            // 当发送消息时，你需要知道对方的ip和端口，在java中，socket指的是java.net.Socket类。

            //ServerSocket与Socket不同，ServerSocket是等待客户端的请求，
            // 一旦获得一个连接请求，就创建一个Socket示例来与客户端进行通信。

            serverSocket = new ServerSocket(port);
            used = false;
        } catch (IOException e) {
            logger.info("port[{}] is in use.", port);
            used = true;
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    logger.info("please check the method isUsed in class netUtils");
                }
            }
        }
        return used;
    }
}
