package au.edu.sydney.comp5216.homework3;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {RunningEntry.class}, version = 1, exportSchema = false)
public abstract class RunningEntryDB extends RoomDatabase {
    private static final String DATABASE_NAME = "RunningEntryDB";
    private static RunningEntryDB DBINSTANCE;

    public abstract RunningEntryDao runningEntryDao();

    public static RunningEntryDB getDatabase(Context context) {
        if (DBINSTANCE == null) {
            synchronized (RunningEntryDB.class) {   //static method passing class name instead of object (this)
                DBINSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        RunningEntryDB.class, DATABASE_NAME).build();
            }
        }
        return DBINSTANCE;
    }

    public static void destroyInstance() {
        DBINSTANCE = null;
    }
}
