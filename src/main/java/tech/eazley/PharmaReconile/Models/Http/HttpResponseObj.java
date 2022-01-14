package tech.eazley.PharmaReconile.Models.Http;

public class HttpResponseObj {
    String message;
    Object object;

    public HttpResponseObj(String message)
    {
        this.message = message;
    }

    public HttpResponseObj(String message,Object object)
    {
        this.message = message;
        this.object = object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    public String getMessage() {
        return message;
    }
}
