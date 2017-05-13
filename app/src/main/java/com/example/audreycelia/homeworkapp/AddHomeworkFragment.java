package com.example.audreycelia.homeworkapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import db.Course;
import db.DatabaseHelper;


public class AddHomeworkFragment extends Fragment {

    private DatabaseHelper db;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private DatePickerDialog datePickerDialog;
    private int month;
    private int year;
    private int day;

    //FIELDS
    private EditText name;
    private EditText date;
    private Spinner course;
    private CheckBox done;
    private EditText description;


    public AddHomeworkFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.addactionbar, menu);
        inflater.inflate(R.menu.settings, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_bar_back:
                getActivity().getSupportFragmentManager().popBackStack();
                return true;

            case R.id.ab_save:

                if(isValid() == false)
                    return  false;

                db = new DatabaseHelper(getActivity().getApplicationContext());
                //transform date format for correct handling in db
                String examDate = convertDateToDatabase(date.getText().toString());

                boolean checked;
                if(done.isChecked())
                    checked = true;
                else
                    checked = false;

                db = new DatabaseHelper(getActivity().getApplicationContext());
                db.insertHomework(name.getText().toString(),examDate,checked,description.getText().toString(),((Course)course.getSelectedItem()).getCourseId());

                if(((MainActivity)getActivity()).isCloudStorageActivated())
                    db.sqlToCloudHomework();

                fragmentManager = getActivity().getSupportFragmentManager();
                fragment = new HomeworkFragment();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.main_container, fragment).commit();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_add_homework, container, false);

        setHasOptionsMenu(true);

        //INITIATE FIELDS
        name = (EditText) rootView.findViewById(R.id.et_add_homework_name);
        date = (EditText) rootView.findViewById(R.id.et_add_homework_date);
        course = (Spinner) rootView.findViewById(R.id.sp_add_homework_course);
        done = (CheckBox) rootView.findViewById(R.id.cb_add_homework_done);
        description = (EditText) rootView.findViewById(R.id.et_add_homework_description);

        //Fill spinner from database
        db = new DatabaseHelper(getActivity().getApplicationContext());
        ArrayList<Course> courses = db.getAllCourses();
        ArrayAdapter<Course> dataAdapter = new ArrayAdapter<Course>(getActivity(), android.R.layout.simple_spinner_item, courses);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        course.setAdapter(dataAdapter);

        //Date picker for date
        date.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //Get current time
                final Calendar c = Calendar.getInstance();
                month = c.get(Calendar.MONTH);
                year = c.get(Calendar.YEAR);
                day = c.get(Calendar.DAY_OF_MONTH);

                //Launch date Picker Dialog
                datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        month++;
                        String courseDate = formatDateString(dayOfMonth+"."+month+"."+year);
                        date.setText(courseDate);

                    }
                },year,month, day);
                datePickerDialog.show();


            }
        });

        return  rootView;
    }

    //FORMAT DATE FROM A STRING AND RETURN THE STRING
    public String formatDateString(String dateString)
    {
        //FORMAT THE START TIME NAD
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date date;

        try
        {
            date = dateFormat.parse(dateString);
            dateString = dateFormat.format(date);

        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return dateString;
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

    public boolean isValid()
    {
        if(TextUtils.isEmpty(name.getText().toString())) {
            name.setError(getText(R.string.wrongName));
            return false;
        }

        if(TextUtils.isEmpty(date.getText().toString())) {
            Toast toast = Toast.makeText(getActivity(), R.string.nulldate, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }

        if(course.getSelectedItem() == null)
        {
            Toast toast = Toast.makeText(getActivity(), R.string.nullcourse, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }

       return true;

    }

    public boolean isCloudStorageActivated()
    {
        boolean isActivated = PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("CLOUD", false);
        return isActivated;
    }
}
