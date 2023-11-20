package au.edu.sydney.comp5216.easydiet.log;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {LogEntry.class}, version = 1, exportSchema = false)
public abstract class LogEntryDB extends RoomDatabase {
    private static final String DATABASE_NAME = "LogEntryDB";
    private static LogEntryDB DBINSTANCE;

    public abstract LogEntryDao logEntryDao();

    public static LogEntryDB getDatabase(Context context) {
        if (DBINSTANCE == null) {
            synchronized (LogEntryDB.class) {   //static method passing class name instead of object (this)
                DBINSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        LogEntryDB.class, DATABASE_NAME).build();
            }
        }
        return DBINSTANCE;
    }

    public static void destroyInstance() {
        DBINSTANCE = null;
    }
}
