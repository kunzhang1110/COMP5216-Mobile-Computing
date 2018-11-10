package au.edu.sydney.comp5216.homework3;

import java.util.ArrayList;

public class WeeklyInfo {

    private String summary;
    private String stats;
    private ArrayList<RunningEntry> runningEntryList = new ArrayList<RunningEntry>();

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getStats() {
        return stats;
    }

    public void setStats(String stats) {
        this.stats = stats;
    }

    public ArrayList<RunningEntry> getRunningEntryList() {
        return runningEntryList;
    }

    public void setRunningEntryList(ArrayList<RunningEntry> runningEntryList) {
        this.runningEntryList = runningEntryList;
    }

}
