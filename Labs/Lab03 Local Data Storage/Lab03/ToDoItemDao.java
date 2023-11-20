package comp5216.sydney.edu.au.todolist;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ToDoItemDao {
    @Query("SELECT * FROM todolist")
    List<ToDoItem> listAll();

    @Insert
    void insert(ToDoItem toDoItem);

    @Insert
    void insertAll(ToDoItem... toDoItems);

    @Query("DELETE FROM todolist")
    void deleteAll();
}
