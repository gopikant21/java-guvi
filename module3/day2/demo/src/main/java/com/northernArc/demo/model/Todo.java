package com.northernArc.demo.model;

public class Todo {
    private int id;
    private String content;
    private boolean completed;

    public Todo(){};

    public Todo(int id, String content, boolean completed) {
        this.id = id;
        this.content = content;
        this.completed = completed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", completed=" + completed +
                '}';
    }
}
