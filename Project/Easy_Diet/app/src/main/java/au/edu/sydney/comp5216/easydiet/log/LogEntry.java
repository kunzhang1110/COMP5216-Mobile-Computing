package au.edu.sydney.comp5216.easydiet.log;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

@Entity(tableName = "LogEntry")
public class LogEntry {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "date")
    private long startTime;

    @ColumnInfo(name = "plannedWeight")
    private double plannedWeight;

    @ColumnInfo(name = "actualWeight")
    private double actualWeight;

    public LogEntry(@NonNull long startTime, double plannedWeight, double actualWeight) {
        this.startTime = startTime;
        this.plannedWeight = plannedWeight;
        this.actualWeight = actualWeight;
    }

    @NonNull
    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(@NonNull long startTime) {
        this.startTime = startTime;
    }

    public double getPlannedWeight() {
        return plannedWeight;
    }

    public void setPlannedWeight(double plannedWeight) {
        this.plannedWeight = plannedWeight;
    }

    public double getActualWeight() {
        return actualWeight;
    }

    public void setActualWeight(double actualWeight) {
        this.actualWeight = actualWeight;
    }

    @Override
    public String toString() {
        return "LogEntry{" +
                "startTime=" + startTime +
                ", plannedWeight=" + plannedWeight +
                ", actualWeight=" + actualWeight +
                '}';
    }
}
