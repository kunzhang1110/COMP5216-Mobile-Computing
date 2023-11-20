package au.edu.sydney.comp5216.easydiet.log;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface LogEntryDao {
    @Query("SELECT * FROM LogEntry")
    List<LogEntry> listAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(LogEntry logEntry);

    @Insert
    void insertAll(LogEntry... logEntries);

    @Query("DELETE FROM LogEntry")
    void deleteAll();



}
