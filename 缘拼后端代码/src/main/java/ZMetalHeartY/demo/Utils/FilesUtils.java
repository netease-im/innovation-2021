package ZMetalHeartY.demo.Utils;

import ZMetalHeartY.demo.Propertie.FilePathProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Component
public class FilesUtils {


    private FilePathProperties filePathProperties;

    @Autowired
    public FilesUtils(FilePathProperties filePathProperties) {
        this.filePathProperties = filePathProperties;
    }

    public String FileSave(MultipartFile file){

        String fileName = System.currentTimeMillis() + "Z" + (int) (Math.random() * 10000) + "Z"
                + (int) (Math.random() * 10000)+ "Z" + (int) (Math.random() * 10000);
        String path = filePathProperties.getFilesPath()+fileName;

        File dest = new File(filePathProperties.getFilesDocBase() + fileName);
        try {
            file.transferTo(dest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }


}
