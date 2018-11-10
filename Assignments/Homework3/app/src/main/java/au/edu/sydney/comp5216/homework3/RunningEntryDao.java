package au.edu.sydney.comp5216.homework3;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface RunningEntryDao {
    @Query("SELECT * FROM RunningEntry")
    List<RunningEntry> listAll();

    @Insert
    void insert(RunningEntry runningEntry);

    @Insert
    void insertAll(RunningEntry... runningEntries);

    @Query("DELETE FROM RunningEntry")
    void deleteAll();
}
