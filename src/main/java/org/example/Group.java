package org.example;

import java.util.*;

public class Group<T> extends Task<T> {
    public String groupUuid;
    private List<Task<T>> tasks;

    public Group<T> addTask(Task<T> task) {
        if (tasks == null) {
            tasks = new ArrayList<>();
        }
        tasks.add(task);
        return this;
    }

    @Override
    public void freeze() {
        super.freeze();
        groupUuid = UUID.randomUUID().toString();
        for (Task<T> task: tasks) {
            task.freeze();
        }
    }

    @Override
    public void stamp(Visitor<T> visitor) {
        visitor.onGroupStart(this);
        for (Task<T> task: tasks) {
            task.stamp(visitor);
        }
        visitor.onGroupEnd(this);
//        Map visitor_headers = visitor.onSignature(visitor)
        // in canvas.py
    }

    @Override
    public void apply(T arg) {
        this.freeze();
        tasks = Collections.unmodifiableList(tasks);
        for (Task<T> task: tasks) {
            task.apply(arg);
        }
    }
}
