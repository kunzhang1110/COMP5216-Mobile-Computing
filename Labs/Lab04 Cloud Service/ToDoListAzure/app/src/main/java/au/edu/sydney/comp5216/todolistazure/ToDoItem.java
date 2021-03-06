package au.edu.sydney.comp5216.todolistazure;

import com.microsoft.windowsazure.mobileservices.table.DateTimeOffset;

public class ToDoItem {

    @com.google.gson.annotations.SerializedName("id")   //must have an "id"; this will be generated by Azure
    private String mId;

    @com.google.gson.annotations.SerializedName("toDoItemName")
    private String toDoItemName;

    public ToDoItem() {

    }

    public ToDoItem(String text, String id) {
        this.setToDoItemName(text);
        this.setId(id);
    }

    // Setters and Getters
    public String getId() {
        return mId;
    }
    public final void setId(String id) {
        mId = id;
    }

    public String getToDoItemName() {
        return toDoItemName;
    }
    public final void setToDoItemName(String name) {
        toDoItemName = name;
    }

    @Override
    public String toString() {
        return getToDoItemName();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ToDoItem && ((ToDoItem) o).mId == mId;
    }

}
