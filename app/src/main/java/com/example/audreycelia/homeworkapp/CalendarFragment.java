package com.example.audreycelia.homeworkapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import db.Course;
import db.CoursesListAdapterCalendar;
import db.DatabaseHelper;
import db.Exam;
import db.ExamsListAdapterCalendar;
import db.Homework;
import db.HomeworksListAdapterCalendar;


public class CalendarFragment extends Fragment {

    //LISTVIEWS
    private ListView listViewCourses;
    private ListView listViewExams;
    private ListView listViewHomeworks;


    //CALENDAR
    private Calendar c;
    private CalendarView calendar ;
    private int month;
    private int year;
    private int day;

    //DB
    private DatabaseHelper db;

    //ARRAYLIST
    private ArrayList<Course> listCourses;
    private ArrayList<Exam> listExams;
    private ArrayList<Homework> listHomeworks;

    public CalendarFragment(){

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.settings, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

        //to see the menu on the top
        setHasOptionsMenu(true);

        //set the title on the app
        getActivity().setTitle(R.string.app_name);

        //GET CURRENT DAY
        c = Calendar.getInstance();
        month = c.get(Calendar.MONTH);
        year = c.get(Calendar.YEAR);
        day = c.get(Calendar.DAY_OF_WEEK);

        db = new DatabaseHelper(getActivity().getApplicationContext());

        listCourses = db.getAllCoursesFromDay(c.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.LONG, Locale.US));
        listViewCourses = (ListView) rootView.findViewById(R.id.listCoursesCalendar);
        listViewCourses.setAdapter(new CoursesListAdapterCalendar(getActivity().getApplicationContext(), listCourses));

        int actualDay = day-1;
        int actualMonth = month+1;
        System.out.println(actualDay+"."+actualMonth+"."+year);
        listExams = db.getAllExamsFromDate(convertDateToDatabase(actualDay+"."+actualMonth+"."+year));
        listViewExams = (ListView) rootView.findViewById(R.id.listExamsCalendar);
        listViewExams.setAdapter(new ExamsListAdapterCalendar(getActivity().getApplicationContext(), listExams));

        listHomeworks = db.getAllHomeworksFromDate(convertDateToDatabase(actualDay+"."+actualMonth+"."+year));
        listViewHomeworks = (ListView) rootView.findViewById(R.id.listHomeworksCalendar);
        listViewHomeworks.setAdapter(new HomeworksListAdapterCalendar(getActivity().getApplicationContext(), listHomeworks));

        ListUtils.setDynamicHeight(listViewCourses);
        ListUtils.setDynamicHeight(listViewExams);
        ListUtils.setDynamicHeight(listViewHomeworks);


        //click on a day
        calendar=(CalendarView) rootView.findViewById(R.id.simpleCalendarView);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                    Calendar newC = Calendar.getInstance();
                    newC.set(year,month,dayOfMonth);

                    month++;

                    listCourses = db.getAllCoursesFromDay(newC.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.LONG, Locale.US));
                    listExams = db.getAllExamsFromDate(convertDateToDatabase(dayOfMonth+"."+month+"."+year));
                    listHomeworks = db.getAllHomeworksFromDate(convertDateToDatabase(dayOfMonth+"."+month+"."+year));

                    listViewCourses.setAdapter(new CoursesListAdapterCalendar(getActivity().getApplicationContext(), listCourses));
                    listViewExams.setAdapter(new ExamsListAdapterCalendar(getActivity().getApplicationContext(), listExams));
                    listViewHomeworks.setAdapter(new HomeworksListAdapterCalendar(getActivity().getApplicationContext(), listHomeworks));

                    ListUtils.setDynamicHeight(listViewCourses);
                    ListUtils.setDynamicHeight(listViewExams);
                    ListUtils.setDynamicHeight(listViewHomeworks);


            }
        });
        return  rootView;
    }

    public static class ListUtils {
        public static void setDynamicHeight(ListView mListView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            int height = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, mListView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
            mListView.setLayoutParams(params);
            mListView.requestLayout();
        }
    }

    public String convertDateToDatabase(String examDate)
    {
        SimpleDateFormat dateFormatin = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat dateFormatout = new SimpleDateFormat("yyyyMMdd");
        Date dateTime;
        try
        {
            dateTime = dateFormatin.parse(examDate);
            examDate = dateFormatout.format(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return  examDate;
    }

}
