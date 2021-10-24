package Core.handlers;

import io.netty.handler.codec.http.FullHttpRequest;

import java.util.Map;

public class HttpRequestHandler {
    public FullHttpRequest fullHttpRequest;
    public Map<String,Object>params;

    public HttpRequestHandler(FullHttpRequest req,Map<String,Object>params) {
        this.fullHttpRequest = req;
        this.params=params;
    }

    public Object getParameter(String name) {
        return params.getOrDefault(name,null);
    }
}
