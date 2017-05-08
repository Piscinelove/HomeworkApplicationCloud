package com.example.audreycelia.homeworkapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.colorpicker.ColorPickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import db.Course;
import db.DatabaseHelper;
import db.Exam;


public class EditExamFragment extends Fragment {

    //DB
    private DatabaseHelper db;
    //FRAGMENTS HANDLE
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private Menu menu;

    //PICKERS AND VARIABLES NEEDED
    private TimePickerDialog timePickerDialog;
    private DatePickerDialog datePickerDialog;
    private int hour;
    private int minute;
    private int month;
    private int year;
    private int day;

    //FIELDS
    private EditText name;
    private EditText date;
    private EditText from;
    private EditText until;
    private Spinner course;
    private EditText room;
    private EditText grade;
    private EditText description;
    private ImageButton deleteButton;
    private ArrayAdapter<Course> spinnerAdapter;

    public EditExamFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.editactionbar, menu);
        inflater.inflate(R.menu.settings, menu);

        this.menu = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        MenuItem edit = menu.findItem(R.id.ab_edit_edit);
        MenuItem back = menu.findItem(R.id.ab_edit_back);
        MenuItem undo = menu.findItem(R.id.ab_edit_undo);
        MenuItem save = menu.findItem(R.id.ab_edit_save);

        final int examId = getArguments().getInt("SelectedExamId");

        switch (item.getItemId())
        {
            case R.id.ab_edit_back:
                getActivity().getSupportFragmentManager().popBackStack();
                return true;

            case R.id.ab_edit_edit:

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

                //Time picker for from time
                from.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Get current time
                        final Calendar c = Calendar.getInstance();
                        hour = c.get(Calendar.HOUR_OF_DAY);
                        minute = c.get(Calendar.MINUTE);

                        //Launch Time Picker Dialog
                        timePickerDialog = new TimePickerDialog(getActivity(),new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                            {

                                String hour = formatTimeString(hourOfDay+":"+minute);
                                from.setText(hour);

                            }
                        },hour,minute,true);
                        timePickerDialog.show();
                    }});

                //Time picker for until time
                until.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Get current time
                        final Calendar c = Calendar.getInstance();
                        hour = c.get(Calendar.HOUR_OF_DAY);
                        minute = c.get(Calendar.MINUTE);

                        //Launch Time Picker Dialog
                        timePickerDialog = new TimePickerDialog(getActivity(),new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,int minute)
                            {

                                String hour = formatTimeString(hourOfDay+":"+minute);
                                until.setText(hour);
                            }
                        },hour,minute,true);
                        timePickerDialog.show();
                    }});

                //Enable les fields
                editMode(true);

                //Gérer les boutons du menu
                edit.setVisible(false);
                back.setVisible(false);
                undo.setVisible(true);
                save.setVisible(true);

                return true;

            case R.id.ab_edit_undo:
                //Gérer les boutons du menu
                edit.setVisible(true);
                back.setVisible(true);
                undo.setVisible(false);
                save.setVisible(false);

                editMode(false);
                getActivity().getSupportFragmentManager().popBackStack();
                return true;

            case R.id.ab_edit_save:

                if(isValid(examId) == false)
                {
                    return  false;
                }

                db = new DatabaseHelper(getActivity().getApplicationContext());

                String examDate = convertTimeToDatabase(date.getText().toString());
                db.updateExam(examId,name.getText().toString(),examDate,from.getText().toString(),until.getText().toString(), Double.parseDouble(grade.getText().toString()), Integer.parseInt(room.getText().toString()),description.getText().toString(),((Course)course.getSelectedItem()).getCourseId());


                //Disable temporaiement les fields
                editMode(false);

                //Gérer les boutons du menu
                edit.setVisible(true);
                back.setVisible(true);
                undo.setVisible(false);
                save.setVisible(false);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_exam, container, false);
        setHasOptionsMenu(true);

        // Recupérer les éléments de la view
        name = (EditText) rootView.findViewById(R.id.et_edit_exam_name);
        date = (EditText) rootView.findViewById(R.id.et_edit_exam_date);
        from = (EditText) rootView.findViewById(R.id.et_edit_exam_from);
        until = (EditText) rootView.findViewById(R.id.et_edit_exam_until);
        course = (Spinner) rootView.findViewById(R.id.sp_edit_exam_course);
        room = (EditText) rootView.findViewById(R.id.et_edit_exam_room);
        grade = (EditText) rootView.findViewById(R.id.et_edit_exam_grade);
        description = (EditText) rootView.findViewById(R.id.et_edit_exam_description);
        deleteButton = (ImageButton) rootView.findViewById(R.id.ib_delete_edit_exam);

        final int examId = getArguments().getInt("SelectedExamId");
        db = new DatabaseHelper(getActivity().getApplicationContext());
        Exam exam = db.getExamFromId(examId);

        name.setText(exam.getName());
        date.setText(exam.getDate());
        from.setText(exam.getStart());
        until.setText(exam.getEnd());

        //Fill spinner from database
        db = new DatabaseHelper(getActivity().getApplicationContext());
        ArrayList<Course> courses = db.getAllCourses();
        spinnerAdapter = new ArrayAdapter<Course>(getContext(), android.R.layout.simple_spinner_dropdown_item, courses);
        course.setAdapter(spinnerAdapter);

        // Récupérer le nom pour le mettre comme élément sélectionner dans le spinner
        Course selectedCourse = db.getCourseFromId(exam.getCourseId());
        int position = spinnerAdapter.getPosition(selectedCourse);
        course.setSelection(position);

        room.setText(""+exam.getRoom());
        grade.setText(""+exam.getGrade());
        description.setText(exam.getDescription());

        //Disable temporaiement les fields
        editMode(false);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deleteExam(examId);
                deleteButton.setVisibility(View.INVISIBLE);

                fragmentManager = getActivity().getSupportFragmentManager();
                fragment = new ExamFragment();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.main_container, fragment).commit();

            }
        });

        return rootView;
    }

    public void editMode(boolean value)
    {
        //Disable temporaiement les fields
        name.setEnabled(value);
        date.setEnabled(value);
        from.setEnabled(value);
        until.setEnabled(value);
        course.setEnabled(value);
        room.setEnabled(value);
        grade.setEnabled(value);
        description.setEnabled(value);

        if(value == true)
            deleteButton.setVisibility(View.VISIBLE);
        else
            deleteButton.setVisibility(View.INVISIBLE);
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

    //FORMAT TIME FROM A STRING
    public Date formatTime(String time)
    {
        //FORMAT THE START TIME NAD
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date();

        try
        {
            date = hourFormat.parse(time);

        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return date;
    }

    //FORMAT TIME FROM A STRING AND RETURN THE STRING
    public String formatTimeString(String time)
    {
        //FORMAT THE START TIME NAD
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");
        Date date = new Date();

        try
        {
            date = hourFormat.parse(time);
            time = hourFormat.format(date);

        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return time;
    }

    public String convertTimeToDatabase(String examDate)
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

    //CHECK IF START TIME BEFORE END TIME

    public boolean checkTimeCorrect(String start, String end)
    {
        //SHIFT OF USER
        Date startA = formatTime(start);
        Date endA = formatTime(end);

        if((startA.before(endA) || startA.equals(endA)))
            //incorrect
            return true;

        //correct
        return false;

    }

    //CHECK IF START TIME AND END TIME OF THE COURSE
    //OVERLAPS WITH AN EXISTING COURSE IN DB

    public boolean checkTimeOverlap(String start, String end, String date, int examId)
    {

        //FORMAT THE START TIME AND END TIME
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");

        //SHIFT OF USER
        Date startA = formatTime(start);
        Date endA = formatTime(end);

        //EXISTING SHIFT IN DB
        Date startB;
        Date endB;

        //OPEN DATABASE HELPER
        db = new DatabaseHelper(getActivity().getApplicationContext());
        ArrayList<Exam> existingExams = db.getAllExams();

        //CHECKING IN ALL THE DB
        for (Exam existingExam : existingExams)
        {

            if(date.equals(existingExam.getDate()))
            {
                startB = formatTime(existingExam.getStart());
                endB = formatTime(existingExam.getEnd());

                if ((startA.before(endB) || startA.equals(endB)) && (startB.before(endA) || startB.equals(endA))
                        && (startA.before(endB) || startA.equals(endB)) && (startB.before(endB) || startB.equals(endB)) && existingExam.getExamId() != examId)
                    //overlaps
                    return true;
            }
        }

        //doesn't overlap
        return false;

    }

    public boolean isValid(int examId)
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

        if(TextUtils.isEmpty(from.getText().toString())) {
            Toast toast = Toast.makeText(getActivity(), R.string.nullfrom, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }

        if(TextUtils.isEmpty(until.getText().toString())) {
            Toast toast = Toast.makeText(getActivity(), R.string.nulluntil, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }


        //CHECK START BEFORE END
        if(!checkTimeCorrect(from.getText().toString(),until.getText().toString()))
        {
            Toast toast = Toast.makeText(getActivity(), R.string.incorrecttime, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }


        //CHECK OVERLAPS
        if(checkTimeOverlap(from.getText().toString(),until.getText().toString(), date.getText().toString(),examId))
        {
            Toast toast = Toast.makeText(getActivity(), R.string.overlapexam, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }

        if(course.getSelectedItem() == null)
        {
            Toast toast = Toast.makeText(getActivity(), R.string.nullcourse, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }

        if(TextUtils.isEmpty(room.getText().toString())) {
            room.setError(getText(R.string.nullRoom));
            return false;
        }

        return true;

    }
}
