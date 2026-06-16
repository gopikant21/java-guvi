package entity;

public class Todo {
    private String id;
    private String description;
    private boolean done;

    public Todo(String id, String description, boolean done) {
        this.id = id;
        this.description = description;
        this.done = done;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", done=" + done +
                '}';
    }
}
