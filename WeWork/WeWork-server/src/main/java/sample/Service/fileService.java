package sample.Service;
import Core.factory.FactoryUtils;
import Core.handlers.HttpResponseHandler;
import Core.handlers.StaticHandler;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.*;
import sample.Beans.fileInfo;
import sample.File.MultipartRequest;


import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class fileService {
    /**
     * 功能描述
     * <p>解析文件上传</p>
     * @params [ctx, httpDecode]
     */

    public   MultipartRequest getMultipartBody(FullHttpRequest request) {
        try {
            //创建HTTP对象工厂
            HttpDataFactory factory = new DefaultHttpDataFactory(true);
            //使用HTTP POST解码器
            HttpPostRequestDecoder httpDecoder = new HttpPostRequestDecoder(factory, request);
            httpDecoder.setDiscardThreshold(0);
            if (httpDecoder != null) {
                //获取HTTP请求对象
                final HttpContent chunk = (HttpContent) request;
                //加载对象到加吗器。
                httpDecoder.offer(chunk);
                if (chunk instanceof LastHttpContent) {
                    //自定义对象bean
                    MultipartRequest multipartRequest = new MultipartRequest();
                    //存放文件对象
                    Map<String, FileUpload> fileUploads = new HashMap<>();
                    //存放参数对象
                    JSONObject body = new JSONObject();
                    //通过迭代器获取HTTP的内容
                    java.util.List<InterfaceHttpData> InterfaceHttpDataList = httpDecoder.getBodyHttpDatas();
                    for (InterfaceHttpData data : InterfaceHttpDataList) {
                        //如果数据类型为文件类型，则保存到fileUploads对象中
                        if (data != null && InterfaceHttpData.HttpDataType.FileUpload.equals(data.getHttpDataType())) {
                            FileUpload fileUpload = (FileUpload) data;
                            fileUploads.put(data.getName(), fileUpload);
                        }
                        //如果数据类型为参数类型，则保存到body对象中
                        if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                            Attribute attribute = (Attribute) data;
                            body.put(attribute.getName(), attribute.getValue());
                        }
                    }
                    //存放文件信息
                    multipartRequest.setFileUploads(fileUploads);
                    //存放参数信息
                    multipartRequest.setParams(body);
                    return multipartRequest;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean storeFile(String name,byte[]b,String id){
        String centerUrl= FactoryUtils.planCenterUrl;
        String dirUrl=centerUrl+"/"+id;
        File file=new File(dirUrl);
        if(!file.exists()){
            if(!file.mkdir()){
                return false;
            }
        }
        File store=new File(dirUrl,name);
        try {
            FileOutputStream out=new FileOutputStream(store);
            out.write(b);
            out.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean downLoadFile(HttpResponseHandler res,String dir, String name) throws IOException {
        String path=FactoryUtils.planCenterUrl+"/"+dir+"/"+name;
//        System.out.println(path);
        return getFile(res, path);
    }

    public String getFileInfo(String plan_id){
        String path=FactoryUtils.planCenterUrl+"/"+plan_id;
        List<fileInfo>list=getFileInfoList(path);
        if(list!=null){
            return JSON.toJSONString(list);
        }else{
            return "[]";
        }
    }

    private List<fileInfo> getFileInfoList(String path){
        List<fileInfo>list=new ArrayList<>();
        File f=new File(path);
        if(!f.exists()||!f.isDirectory()){
            return null;
        }
        File[]fs=f.listFiles();
        if(fs!=null){
            for(File x:fs){
                if(!x.isDirectory()){
                    fileInfo info=new fileInfo();
                    info.setName(x.getName());
                    info.setSize(Long.toString(x.length()));
                    list.add(info);
                }
            }
        }else{
            return null;
        }
        return list;
    }

    public boolean getFile(HttpResponseHandler res, String path) throws IOException {
        ChannelHandlerContext c=res.ctx;
//        if(path.startsWith("/")){
//            path=path.substring(1);
//        }
//        System.out.println("path:   "+path);
        File f=new File(path);
        if(!f.exists()){
            return false;
        }
        //随机读取文件
        final RandomAccessFile raf = new RandomAccessFile(f, "r");
        long fileLength = raf.length();
        //定义response对象
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        //设置请求头部
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, Long.toString(fileLength));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/octet-stream; charset=UTF-8");
        response.headers().add(HttpHeaderNames.CONTENT_DISPOSITION,
                "attachment; filename=\"" + URLEncoder.encode(f.getName(), StandardCharsets.UTF_8) + "\";");
        res.ctx.write(response);
        //设置事件通知对象
        res.ctx.write(new DefaultFileRegion(raf.getChannel(), 0, fileLength), res.ctx.newProgressivePromise());

        ChannelFuture future = c.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        future.addListener(ChannelFutureListener.CLOSE);
        return true;
    }

    public boolean deleteFile(String dir, String name){
        String path=FactoryUtils.planCenterUrl+"/"+dir+"/"+name;
        File f=new File(path);
        if(!f.exists()){
            return false;
        }
        return f.delete();
    }
}

