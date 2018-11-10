package au.edu.sydney.comp5216.homework3;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "RunningEntry")
public class RunningEntry {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "startTime")
    private long startTime;

    @ColumnInfo(name = "finishTime")
    private long finishTime;

    @ColumnInfo(name = "distance")
    private double distance;

    public RunningEntry(@NonNull long startTime, long finishTime, double distance) {
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.distance = distance;
    }

    @NonNull
    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(@NonNull long startTime) {
        this.startTime = startTime;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(long finishTime) {
        this.finishTime = finishTime;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "RunningEntry{" +
                "startTime=" + startTime +
                ", finishTime=" + finishTime +
                ", distance=" + distance +
                '}';
    }
}
