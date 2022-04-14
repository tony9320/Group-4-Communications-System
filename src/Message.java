import java.io.Serializable;

public class Message implements Serializable {
    protected String type;
    protected String status;
    protected String text;

    public Message(){
        this.type = "Undefined";
        this.status = "Undefined";
        this.text = "Undefined";
    }

    public Message(String type, String status, String text){
        setType(type);
        setStatus(status);
        setText(text);
    }

    private void setType(String type){
    	this.type = type;
    }

    public void setStatus(String status){
    	this.status = status;
    }

    public void setText(String text){
    	this.text = text;
    }

    public String getType(){
    	return type;
    }

    public String getStatus(){
    	return status;
    }

    public String getText(){
    	return text;
    }

}