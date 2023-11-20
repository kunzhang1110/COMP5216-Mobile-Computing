package au.edu.sydney.comp5216.homework3;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Days;
import org.joda.time.Weeks;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class LogFragment extends Fragment {

    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private ArrayList<WeeklyInfo> weeklyInfoList;

    //database fields
    RunningEntryDB  db;
    RunningEntryDao dao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_log, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Setup database
        db = RunningEntryDB.getDatabase(getActivity().getApplication().getApplicationContext());
        dao = db.runningEntryDao();

        weeklyInfoList = new ArrayList<>();

        readItemsFromDatabase();

        listView = (ExpandableListView) getView().findViewById(R.id.lvShowEntries);
        listAdapter = new MyExpandableListAdapter(getActivity(),weeklyInfoList);
        listView.setAdapter(listAdapter);
    }

    // database methods
    private void readItemsFromDatabase() {
        try {
            new readItemsFromDatabase(this, dao).execute().get();
        }catch (Exception ex){
            Log.e("readItemsFromDatabase", ex.getStackTrace().toString());
        }
    }

    private static class readItemsFromDatabase extends AsyncTask<Void, Void, Void> {
        //Use asynchronous task to run query on the background and wait for result

        private final WeakReference<LogFragment> mActivityRef;
        private RunningEntryDao dao;

        // Constructor
        readItemsFromDatabase(LogFragment _activity, RunningEntryDao _dao){
            mActivityRef = new WeakReference<>(_activity);   // weak reference to prevent memory leak
            dao = _dao;
        }

        @Override
        // Read items from database
        protected Void doInBackground(Void... voids) { // ... == vargars == Variable Arguments
            LogFragment logFragment = mActivityRef.get();

            List<RunningEntry> entriesFromDB = dao.listAll();
            // base on timestamp calculate week
            DateTimeFormatter dtf = DateTimeFormat.forPattern("MMM dd, yyyy");
            ArrayList<DateTime> datetimeMondayList = new ArrayList<>();
            ArrayList<RunningEntry> tempList;

            DateTime datetime = new DateTime(entriesFromDB.get(0).getStartTime());
            DateTime datetimeMonday = datetime.withDayOfWeek(DateTimeConstants.MONDAY);
            datetimeMondayList.add(datetimeMonday);

            double totalDistance = 0;
            long totalDuration = (long) 0;
            ArrayList<Double> totalDistanceList = new ArrayList<>();
            ArrayList<Long> totalDurationList = new ArrayList<>();

            WeeklyInfo wk = new WeeklyInfo();
            tempList = wk.getRunningEntryList();
            tempList.add(entriesFromDB.get(0));
            wk.setRunningEntryList(tempList);

            for (RunningEntry entry : entriesFromDB){
                datetime = new DateTime(entry.getStartTime());
                datetimeMonday = datetime.withDayOfWeek(DateTimeConstants.MONDAY);


                if (Days.daysBetween(datetimeMondayList.get(datetimeMondayList.size()-1),
                        datetimeMonday).getDays()<=1){ //same week
                    totalDistance += entry.getDistance();
                    totalDuration +=  - (entry.getFinishTime() - entry.getStartTime()); //TODO: Running Entry Constructuor needs to be fixed

                    tempList = wk.getRunningEntryList();
                    tempList.add(entry);
                    wk.setRunningEntryList(tempList);

                    if(entriesFromDB.indexOf(entry) == entriesFromDB.size() -1){   //if last entry, add into lists
                        totalDistanceList.add(totalDistance);
                        totalDurationList.add(totalDuration);
                        logFragment.weeklyInfoList.add(wk);
                    }
                } else{
                    //
                    datetimeMondayList.add(datetimeMonday);
                    totalDistanceList.add(totalDistance);
                    totalDurationList.add(totalDuration);
                    logFragment.weeklyInfoList.add(wk);
                    wk = new WeeklyInfo();
                    totalDistance = 0;
                    totalDuration = (long) 0;
                }

            }

            for (DateTime e : datetimeMondayList){
                Log.i("xxx","-----end----"+ dtf.print(e));
            }
            for(int i = 0; i < logFragment.weeklyInfoList.size();i++){
                double distance = totalDistanceList.get(i);
                long duration = totalDurationList.get(i);
                float speed = StatCalculator.getSpeed((float) distance/1000, (float) duration/1000/60);
                logFragment.weeklyInfoList.get(i).setSummary(
                        "Week Starting on " +dtf.print(datetimeMondayList.get(i)));
                logFragment.weeklyInfoList.get(i).setStats(
                        "- Covered " + String.format(Locale.US,"%.2f", distance) + " m in "
                                + StatCalculator.getTimeStringFromMillis(duration) +"\n"
                                + "- Average Speed: " + String.format(Locale.US,"%.2f", speed) + " km/h");

            }
            return null;
        }
    }



}
