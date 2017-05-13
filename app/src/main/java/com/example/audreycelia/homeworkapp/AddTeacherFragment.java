package com.example.audreycelia.homeworkapp;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.audreycelia.homeworkapp.backend.teacherApi.model.Teacher;

import db.DatabaseHelper;


public class AddTeacherFragment extends Fragment {

    private DatabaseHelper db;
    private Fragment fragment;
    private FragmentManager fragmentManager;

    //FIELDS
    EditText firstName;
    EditText lastName;
    EditText email;
    EditText phone;
    EditText description;

    public AddTeacherFragment() {
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


                Teacher t = new Teacher();
                t.setFirstName(firstName.getText().toString());
                t.setLastName(lastName.getText().toString());
                t.setPhone(phone.getText().toString());
                t.setEmail(email.getText().toString());
                t.setDescription(description.getText().toString());


                db = new DatabaseHelper(getActivity().getApplicationContext());
                db.insertTeacher(firstName.getText().toString().substring(0,1).toUpperCase() +firstName.getText().toString().substring(1).toLowerCase(),lastName.getText().toString().substring(0,1).toUpperCase() +lastName.getText().toString().substring(1).toLowerCase(),phone.getText().toString(),email.getText().toString(),description.getText().toString());

                if(((MainActivity)getActivity()).isCloudStorageActivated())
                    db.sqlToCloudTeacher();


                fragmentManager = getActivity().getSupportFragmentManager();
                fragment = new TeacherFragment();
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
        final View rootView = inflater.inflate(R.layout.fragment_add_teacher, container, false);
        setHasOptionsMenu(true);

        //INITIATE FIELDS
        firstName = (EditText) rootView.findViewById(R.id.et_add_teacher_firstname);
        lastName = (EditText) rootView.findViewById(R.id.et_add_teacher_lastname);
        phone = (EditText) rootView.findViewById(R.id.et_add_teacher_phone);
        email = (EditText) rootView.findViewById(R.id.et_add_teacher_email);
        description = (EditText) rootView.findViewById(R.id.et_add_teacher_description);

        return  rootView;
    }

    private boolean isEmailValid(CharSequence email)
    {
        if(!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches())
            return true;
        return false;
    }

    private boolean isPhoneValid(CharSequence phone)
    {
        if(Patterns.PHONE.matcher(phone).matches())
            return true;
        return false;
    }

    public boolean isValid()
    {
        if(TextUtils.isEmpty(firstName.getText().toString())) {
            firstName.setError(getText(R.string.nullFirstname));
            return false;
        }

        if(TextUtils.isEmpty(lastName.getText().toString())) {
            lastName.setError(getText(R.string.nullLastname));
            return false;
        }


        if(!TextUtils.isEmpty(phone.getText().toString()) && !isPhoneValid(phone.getText()))
        {
            phone.setError(getText(R.string.wrongPhone));
            return false;
        }

        if(!TextUtils.isEmpty(email.getText().toString()) && !isEmailValid(email.getText()))
        {
            email.setError(getText(R.string.wrongEmail));
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
