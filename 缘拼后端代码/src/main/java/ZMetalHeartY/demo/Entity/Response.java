package ZMetalHeartY.demo.Entity;

/**
 * 返回服务器状态的bean类
 */

public class Response {
    private String status;
    private String msg;
    private Object obj;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public Response(String status, String msg, Object obj) {
        this.status = status;
        this.msg = msg;
        this.obj = obj;
    }
}
