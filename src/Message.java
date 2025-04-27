import java.io.Serializable;

public class Message implements Serializable {
    private String type;
    private Object val;

    public Message(String type, Object val){
        this.type = type;
        this.val  = val;
    }

    public Object getVal() {
        return val;
    }

    public String getType() {
        return type;
    }
}
