package au.edu.sydney.comp5216.todolist;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {ToDoItem.class}, version = 1, exportSchema = false)
public abstract class ToDoItemDB extends RoomDatabase {
    private static final String DATABASE_NAME = "todoitem_db";
    private static ToDoItemDB DBINSTANCE;

    public abstract ToDoItemDao toDoItemDao();

    static ToDoItemDB getDatabase(Context context) {
        if (DBINSTANCE == null) {
            synchronized (ToDoItemDB.class) {   //static method passing class name instead of object (this)
                DBINSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        ToDoItemDB.class, DATABASE_NAME).build();
            }
        }
        return DBINSTANCE;
    }

    public static void destroyInstance() {
        DBINSTANCE = null;
    }
}

