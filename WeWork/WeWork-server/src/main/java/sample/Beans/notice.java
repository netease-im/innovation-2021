package sample.Beans;

public class notice {
    private int id;
    private int acceptor;
    private int sender;
    private String content;
    private int type;
    private int read;

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
