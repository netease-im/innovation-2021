package ZMetalHeartY.demo.Propertie;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "filepath")
public class FilePathProperties {

    private String DocBase;

    private String FilesPath;
    private String FilesDocBase;

    private String UserImgsPath;
    private String UserImgsDocBase;

    private String staticurl;
    private String positionImg;

    private String echostaticurl;
    private String echopositionImg;


    public String getDocBase() {
        return DocBase;
    }

    public void setDocBase(String docBase) {
        DocBase = docBase;
    }


    public String getFilesPath() {
        return FilesPath;
    }

    public void setFilesPath(String filesPath) {
        FilesPath = filesPath;
    }

    public String getFilesDocBase() {
        return FilesDocBase;
    }

    public void setFilesDocBase(String filesDocBase) {
        FilesDocBase = filesDocBase;
    }

    public String getUserImgsPath() {
        return UserImgsPath;
    }

    public void setUserImgsPath(String userImgsPath) {
        UserImgsPath = userImgsPath;
    }

    public String getUserImgsDocBase() {
        return UserImgsDocBase;
    }

    public void setUserImgsDocBase(String userImgsDocBase) {
        UserImgsDocBase = userImgsDocBase;
    }

    public String getStaticurl() {
        return staticurl;
    }

    public void setStaticurl(String staticurl) {
        this.staticurl = staticurl;
    }

    public String getPositionImg() {
        return positionImg;
    }

    public void setPositionImg(String positionImg) {
        this.positionImg = positionImg;
    }

    public String getEchostaticurl() {
        return echostaticurl;
    }

    public void setEchostaticurl(String echostaticurl) {
        this.echostaticurl = echostaticurl;
    }

    public String getEchopositionImg() {
        return echopositionImg;
    }

    public void setEchopositionImg(String echopositionImg) {
        this.echopositionImg = echopositionImg;
    }
}
