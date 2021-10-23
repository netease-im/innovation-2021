package Core.handlers;

public class SingletonObjHandler {
    private Object obj;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public SingletonObjHandler(Object obj, String id) {
      this.setId(id);
      this.setObj(obj);
    }
}
