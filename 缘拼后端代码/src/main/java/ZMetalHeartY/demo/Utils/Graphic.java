package ZMetalHeartY.demo.Utils;

import ZMetalHeartY.demo.Propertie.FilePathProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@Component
@Slf4j
public class Graphic {

    private FilePathProperties filePathProperties;

    @Autowired
    public Graphic(FilePathProperties filePathProperties) {
        this.filePathProperties = filePathProperties;
    }

    //    String staticurl = "/usr/local/tomcatSource/issueImgs/static/";// /usr/local/tomcatSource/issueImgs/static/     D:/test1/
//
//    String positionImg = "/usr/local/tomcatSource/issueImgs/positionImg/"; // /usr/local/tomcatSource/issueImgs/positionImg/   D:/test1/
//
//    //String echostaticurl = "/issueImgs/static/";// /usr/local/tomcatSource/issueImgs/static/     D:/test1/
//
//    String echopositionImg = "/issueImgs/positionImg/"; //

    public String transferImgForRoundImgage(String user_img_path,String sex,String user_name){

        log.info(filePathProperties.getDocBase());
        String[] strs = user_img_path.split("/");
        String afterurl = strs[2];//作为头像名
        String url = "file:"+filePathProperties.getDocBase()+""+user_img_path;

        File outFile = new File(filePathProperties.getPositionImg()+""+afterurl+".png");
        if(outFile.exists()){
            return filePathProperties.getEchopositionImg()+""+afterurl+".png";
        }


        BufferedImage resultImg = null;
        try {
            if (StringUtils.isBlank(url)) {
                return null;
            }
            BufferedImage buffImg1 = ImageIO.read(new URL(url));
            resultImg = new BufferedImage(buffImg1.getWidth(), buffImg1.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g = resultImg.createGraphics();
            Ellipse2D.Double shape = new Ellipse2D.Double(0, 0, buffImg1.getWidth(), buffImg1.getHeight());
            // 使用 setRenderingHint 设置抗锯齿
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            resultImg = g.getDeviceConfiguration().createCompatibleImage(buffImg1.getWidth(), buffImg1.getHeight(),
                    Transparency.TRANSLUCENT);
            //g.fill(new Rectangle(buffImg2.getWidth(), buffImg2.getHeight()));
            g = resultImg.createGraphics();
            // 使用 setRenderingHint 设置抗锯齿
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setClip(shape);
            g.drawImage(buffImg1, 0, 0, null);
            g.dispose();


            File outputfile = new File(filePathProperties.getPositionImg()+"saved.png");
            ImageIO.write(resultImg, "png", outputfile);


            /* 1 读取第bai一张图片*/ File fileOne = new File(filePathProperties.getPositionImg()+"saved.png");
            BufferedImage imageFirst = ImageIO.read(fileOne);

            File fileTwo = null;
            if ("男".equals(sex)){/* 1 对第二du张图片做相同的处理 */
                fileTwo = new File(filePathProperties.getStaticurl()+"mp.png");
            }else {
                fileTwo = new File(filePathProperties.getStaticurl()+"wp.png");
            }


            BufferedImage imageSecond = ImageIO.read(fileTwo);
            Graphics graphics = imageSecond.getGraphics();
            graphics.drawImage(resultImg,35,35,null);


            ImageIO.write(imageSecond, "png", outFile);// 写图片


        } catch (MalformedURLException e) {
            System.out.println("URL格式异常" + e.getCause());
        } catch (IOException e) {
            System.out.println("读取图片异常" + e.getCause());
        }


        return filePathProperties.getEchopositionImg()+""+afterurl+".png";
    }
}
