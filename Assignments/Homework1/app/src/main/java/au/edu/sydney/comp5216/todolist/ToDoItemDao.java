package au.edu.sydney.comp5216.todolist;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.arch.persistence.room.OnConflictStrategy;

import java.util.List;


@Dao
public interface ToDoItemDao {
    @Query("SELECT * FROM todolist")
    List<ToDoItem> listAll();

    // get all items sorted by datetime in descending order
    @Query("SELECT * FROM todolist ORDER BY datetime(toDoItemTime) DESC")
    List<ToDoItem> listAllDesc();

    @Insert
    Long insert(ToDoItem toDoItem); // return newly inserted entry id

    @Insert
    void insertAll(ToDoItem... toDoItems);


    @Query("DELETE FROM todolist")
    void deleteAll();

    @Query("DELETE FROM todolist  WHERE toDoItemID = :id")
    void deleteItem(Long id);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(ToDoItem toDoItem);
}