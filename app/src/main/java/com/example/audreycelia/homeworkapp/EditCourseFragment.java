package com.example.audreycelia.homeworkapp;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.colorpicker.ColorPickerDialog;
import com.android.colorpicker.ColorPickerSwatch;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import db.Course;
import db.DatabaseHelper;
import db.Teacher;


public class EditCourseFragment extends Fragment {

    //DB
    private DatabaseHelper db;
    //FRAGMENTS HANDLE
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private Menu menu;

    //PICKERS AND VARIABLES NEEDED
    private TimePickerDialog timePickerDialog;
    private ColorPickerDialog colorPickerDialog;
    private int hour;
    private int minute;

    //FIELDS
    private EditText name;
    private EditText from;
    private EditText until;
    private Button colorButton;
    private Spinner teacher;
    private EditText room;
    private EditText description;
    private Spinner day;
    private ImageButton deleteButton;
    private ArrayAdapter<Teacher> spinnerAdapter;


    public EditCourseFragment() {
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

        final int courseId = getArguments().getInt("SelectedCourseId");

        switch (item.getItemId())
        {
            case R.id.ab_edit_back:
                getActivity().getSupportFragmentManager().popBackStack();
                return true;

            case R.id.ab_edit_edit:

                //Color Picker
                colorPickerDialog = new ColorPickerDialog();
                int[] colors = {ContextCompat.getColor(getActivity(),R.color.primary1),
                        ContextCompat.getColor(getActivity(),R.color.primary2),
                        ContextCompat.getColor(getActivity(),R.color.primary3),
                        ContextCompat.getColor(getActivity(),R.color.primary4),
                        ContextCompat.getColor(getActivity(),R.color.primary5),
                        ContextCompat.getColor(getActivity(),R.color.primary6),
                        ContextCompat.getColor(getActivity(),R.color.primary7),
                        ContextCompat.getColor(getActivity(),R.color.primary8),
                        ContextCompat.getColor(getActivity(),R.color.primary9)};
                colorPickerDialog.initialize(R.string.colorChange,colors, colors[1], 3, colors.length);

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

                //Color picker when click on color button
                colorButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        colorPickerDialog.show(getActivity().getFragmentManager(), "test");
                        colorPickerDialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int color) {
                                colorButton.setBackgroundColor(color);
                                colorButton.setTextColor(Color.WHITE);
                                //change la couleur actuellement sélectionnée
                                colorPickerDialog.setSelectedColor(color);

                            }
                        });

                    }
                });

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

                if(isValid(courseId) == false)
                {
                    return  false;
                }

                int color = ((ColorDrawable)colorButton.getBackground()).getColor();
                db.updateCourse(courseId, name.getText().toString(),day.getSelectedItem().toString(),from.getText().toString(),until.getText().toString(), color, Integer.parseInt(room.getText().toString()),description.getText().toString(),((Teacher)teacher.getSelectedItem()).getTeacherId());

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
        View rootView = inflater.inflate(R.layout.fragment_edit_course, container, false);

        setHasOptionsMenu(true);


        // Recupérer les éléments de la view
        name = (EditText) rootView.findViewById(R.id.et_edit_course_name);
        from = (EditText) rootView.findViewById(R.id.et_edit_course_from);
        until = (EditText) rootView.findViewById(R.id.et_edit_course_until);
        teacher = (Spinner) rootView.findViewById(R.id.sp_edit_course_teacher);
        colorButton = (Button) rootView.findViewById(R.id.bt_edit_course_color);
        room = (EditText) rootView.findViewById(R.id.et_edit_course_room);
        description = (EditText) rootView.findViewById(R.id.et_edit_course_description);
        day = (Spinner) rootView.findViewById(R.id.sp_edit_course_day);
        deleteButton = (ImageButton) rootView.findViewById(R.id.ib_delete_edit_course);


        final int courseId = getArguments().getInt("SelectedCourseId");
        db = new DatabaseHelper(getActivity().getApplicationContext());
        Course course = db.getCourseFromId(courseId);

        name.setText(course.getName());
        from.setText(course.getStart());
        until.setText(course.getEnd());

        //Fill spinner from database
        db = new DatabaseHelper(getActivity().getApplicationContext());
        ArrayList<Teacher> teachers = db.getAllTeachers();
        spinnerAdapter = new ArrayAdapter<Teacher>(getContext(), android.R.layout.simple_spinner_dropdown_item, teachers);
        teacher.setAdapter(spinnerAdapter);

        // Récupérer le nom et prénom du teacher pour le mettre comme élément sélectionner dans le spinner
        Teacher selectedTeacher = db.getTeacherFromId(course.getTeacherId());

        int position = spinnerAdapter.getPosition(selectedTeacher);
        teacher.setSelection(position);



        colorButton.setBackgroundColor(course.getColor());
        room.setText(""+course.getRoom());
        description.setText(course.getDescription());
        day.setSelection(((ArrayAdapter)day.getAdapter()).getPosition(course.getDay()));


        //DISABLE FIELDS
        editMode(false);

        //int position = spinnerAdapter.getPosition()e

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //alert dialog
                new AlertDialog.Builder(getContext())
                        .setMessage(R.string.deleteCourse)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                db.deleteCourse(courseId);
                                deleteButton.setVisibility(View.INVISIBLE);

                                fragmentManager = getActivity().getSupportFragmentManager();
                                fragment = new CourseFragment();
                                FragmentTransaction transaction = fragmentManager.beginTransaction();
                                transaction.addToBackStack(null);
                                transaction.replace(R.id.main_container, fragment).commit();

                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();




            }
        });
        return  rootView;
    }

    public void editMode(boolean value)
    {
        //Disable temporaiement les fields
        name.setEnabled(value);
        from.setEnabled(value);
        until.setEnabled(value);
        teacher.setEnabled(value);
        colorButton.setEnabled(value);
        room.setEnabled(value);
        description.setEnabled(value);
        day.setEnabled(value);

        if(value == true)
            deleteButton.setVisibility(View.VISIBLE);
        else
            deleteButton.setVisibility(View.INVISIBLE);
    }

    //CHECK IF START TIME AND END TIME OF THE COURSE
    //OVERLAPS WITH AN EXISTING COURSE IN DB

    public boolean checkTimeOverlap(String start, String end, String day, int courseId)
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
        ArrayList<Course> existingCourses = db.getAllCourses();

        //CHECKING IN ALL THE DB
        for (Course existingCourse : existingCourses)
        {

            if(day.equals(existingCourse.getDay()))
            {
                startB = formatTime(existingCourse.getStart());
                endB = formatTime(existingCourse.getEnd());

                if ((startA.before(endB) || startA.equals(endB)) && (startB.before(endA) || startB.equals(endA))
                        && (startA.before(endB) || startA.equals(endB)) && (startB.before(endB) || startB.equals(endB)) && existingCourse.getCourseId() != courseId)
                    //overlaps
                    return true;
            }
        }

        //doesn't overlap
        return false;

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
        Date date;

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

    public boolean isValid(int courseId)
    {
        if(TextUtils.isEmpty(name.getText().toString())) {
            name.setError(getText(R.string.wrongName));
            return false;
        }

        if(day.getSelectedItem() == null)
        {
            Toast toast = Toast.makeText(getActivity(), R.string.nullday, Toast.LENGTH_SHORT);
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

        if(teacher.getSelectedItem()==null) {
            Toast toast = Toast.makeText(getActivity(), R.string.nullteacher, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }

        if(TextUtils.isEmpty(room.getText().toString())) {
            room.setError(getText(R.string.nullRoom));
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
        if(checkTimeOverlap(from.getText().toString(),until.getText().toString(), day.getSelectedItem().toString(), courseId))
        {
            Toast toast = Toast.makeText(getActivity(), R.string.overlapcourse, Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }

        return true;

    }
}
