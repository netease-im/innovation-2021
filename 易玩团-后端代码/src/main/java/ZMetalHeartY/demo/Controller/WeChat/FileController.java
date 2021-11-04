package ZMetalHeartY.demo.Controller.WeChat;

import ZMetalHeartY.demo.Utils.FilesUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Slf4j
@Controller
@ResponseBody
@RequestMapping("/WeChat/file")
public class FileController {

    FilesUtils filesUtils;

    public FileController(FilesUtils filesUtils) {
        this.filesUtils = filesUtils;
    }

    @PostMapping("uploadImg")
    @ResponseBody
    public String updateImg(HttpServletRequest request) {

        JSONObject json = new JSONObject();

        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        MultipartFile multipartFile = null;
        if (files.size()>0){
            multipartFile = files.get(0);
            multipartFile = files.get(0);
        }else {
            json.put("msg","读取文件失败");
            json.put("code", 400);
            json.put("data", null);
            json.put("count", 0);
            return JSON.toJSONString(json);
        }
        /*验证上传文件大小,防止浪费空间内存*/
        long sizeMb = multipartFile.getSize() / (1024 * 1024);
        if (sizeMb > 50){
            json.put("msg","文件过大");
            json.put("code", 400);
            json.put("data", null);
            json.put("count", 0);
            return JSON.toJSONString(json);
        }

        //图片相对路径（数据库保存路径）
        String imgPath = filesUtils.FileSave(multipartFile);
        json.put("data", imgPath);
 /*       json.put("data", imgPath);
        json.put("count", 0);
        System.out.println("=="+json);
        */

        return json.toJSONString();
    }
}
