package sample.Beans;

public class noticeFull {
    private int id;
    private int acceptor;
    private int sender;
    private String content;
    private int type;
    private int read;
    private String sender_name;
    private String sender_email;


    public String getSender_email() {
        return sender_email;
    }

    public void setSender_email(String sender_email) {
        this.sender_email = sender_email;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public int getId() {
        return id;
    }

    public int getAcceptor() {
        return acceptor;
    }

    public int getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }

    public int getRead() {
        return read;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAcceptor(int acceptor) {
        this.acceptor = acceptor;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setRead(int read) {
        this.read = read;
    }
}
