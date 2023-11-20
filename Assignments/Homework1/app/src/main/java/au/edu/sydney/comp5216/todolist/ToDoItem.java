package au.edu.sydney.comp5216.todolist;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "todolist")
public class ToDoItem {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "toDoItemID")
    private Long toDoItemID;

    @ColumnInfo(name = "toDoItemName")
    private String toDoItemName;

    @ColumnInfo(name = "toDoItemTime")
    private String toDoItemTime;


    public ToDoItem(String toDoItemName, String toDoItemTime) {
        this.toDoItemName = toDoItemName;
        this.toDoItemTime = toDoItemTime;
    }

    public Long getToDoItemID() {
        return toDoItemID;
    }

    public void setToDoItemID(Long toDoItemID) {
        this.toDoItemID = toDoItemID;
    }

    public String getToDoItemName() {
        return toDoItemName;
    }

    public void setToDoItemName(String toDoItemName) {
        this.toDoItemName = toDoItemName;
    }

    public String getToDoItemTime() {
        return toDoItemTime;
    }

    public void setToDoItemTime(int toDoItemID) {
        this.toDoItemTime = toDoItemTime;
    }
}
