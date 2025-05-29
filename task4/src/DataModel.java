public class DataModel {
    private int id;
    private String content;
    private boolean readOnly;

    public DataModel(int id, String content, boolean readOnly) {
        this.id = id;
        this.content = content;
        this.readOnly = readOnly;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    @Override
    public String toString() {
        return "DataModel{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", readOnly=" + readOnly +
                '}';
    }
}