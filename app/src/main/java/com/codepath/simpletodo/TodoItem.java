package com.codepath.simpletodo;

/**
 * Created by pavan on 1/19/15.
 */

public class TodoItem {
    private int id;
    private String body;
    private boolean completed;

    public TodoItem(String body) {
        super();
        this.body = body;
        this.completed = false;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
