package au.edu.sydney.comp5216.homework3;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Locale;

public class MyExpandableListAdapter implements ExpandableListAdapter {

    private Context context;
    private ArrayList<WeeklyInfo> weeklyInfoList;

    public MyExpandableListAdapter(Context _context, ArrayList<WeeklyInfo> _weeklyInfoList) {
        this.context = _context;
        this.weeklyInfoList = _weeklyInfoList;
    }
    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        return weeklyInfoList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<RunningEntry> runningEntryList = weeklyInfoList.get(groupPosition).getRunningEntryList();
        return runningEntryList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return weeklyInfoList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<RunningEntry> runningEntryList = weeklyInfoList.get(groupPosition).getRunningEntryList();
        return runningEntryList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        WeeklyInfo headerInfo = (WeeklyInfo) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.group_items, null);
        }

        TextView heading = (TextView) convertView.findViewById(R.id.heading);
        heading.setText(headerInfo.getSummary());
        TextView stats = (TextView) convertView.findViewById(R.id.stats);
        stats.setText(headerInfo.getStats());
        return convertView;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        RunningEntry detailInfo = (RunningEntry) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.child_items, null);
        }
        TextView childItem = (TextView) convertView.findViewById(R.id.childItem);

        DateTimeFormatter timeDTF = DateTimeFormat.forPattern("hh:mm:ss aa");
        DateTimeFormatter dateDTF = DateTimeFormat.forPattern("MMM dd");

        double distance = detailInfo.getDistance();
        long startTime = detailInfo.getFinishTime();
        long finishTime = detailInfo.getStartTime();
        long duration = finishTime - startTime;
        float speed = StatCalculator.getSpeed((float) distance/1000, (float) duration/1000/60);

        childItem.setText(
                "\t\t" + dateDTF.print(detailInfo.getFinishTime()) + "\n" +
                "\t\t \u2022 Covered "+ String.format(Locale.US,"%.2f", distance) + " m\n" +
                "\t\t \u2022 from "+ timeDTF.print(detailInfo.getFinishTime())  +
                " to " + timeDTF.print(detailInfo.getStartTime()) + "\n" +
                "\t\t \u2022 with speed " + String.format(Locale.US,"%.2f", speed) + " km/h"
        );


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }
}

