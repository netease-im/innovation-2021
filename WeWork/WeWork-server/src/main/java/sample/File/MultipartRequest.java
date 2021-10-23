package sample.File;

import com.alibaba.fastjson.JSONObject;
import io.netty.handler.codec.http.multipart.FileUpload;

import java.util.Map;

public class MultipartRequest {
    private Map<String, FileUpload> fileUploads;
    private JSONObject params;

    public Map<String, FileUpload> getFileUploads() {
        return fileUploads;
    }

    public void setFileUploads(Map<String, FileUpload> fileUploads) {
        this.fileUploads = fileUploads;
    }

    public JSONObject getParams() {
        return params;
    }

    public void setParams(JSONObject params) {
        this.params = params;
    }
}
