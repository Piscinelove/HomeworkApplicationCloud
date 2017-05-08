package com.example.audreycelia.homeworkapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

import db.Course;
import db.CoursesListAdapter;
import db.DatabaseHelper;


public class CourseFragment extends Fragment {

    private ListView listView;
    private CoursesListAdapter adapter;
    private DatabaseHelper db;
    private ImageButton addButton;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private SearchView searchBar;


    public CourseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.settings, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_course, container, false);

        //to see the menu on the top
        setHasOptionsMenu(true);

        //set the title on the app
        getActivity().setTitle(R.string.title_course);

        //initialise search bar
        searchBar = (SearchView) rootView.findViewById(R.id.search_bar_course);

        //fill the list view with the db
        db = new DatabaseHelper(getActivity().getApplicationContext());

        ArrayList<Course> listCourses = db.getAllCourses();
        listView = (ListView) rootView.findViewById(R.id.listCourses);
        adapter = new CoursesListAdapter(getActivity().getApplicationContext(), listCourses);
        listView.setAdapter(adapter);
        listView.setTextFilterEnabled(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int courseId = ((Course) listView.getItemAtPosition(position)).getCourseId();
                Bundle bundle = new Bundle();
                bundle.putInt("SelectedCourseId", courseId);
                fragmentManager = getActivity().getSupportFragmentManager();
                fragment = new EditCourseFragment();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.main_container, fragment).commit();

            }
        });

        //add button
        addButton = (ImageButton) rootView.findViewById(R.id.ib_plus_course);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getActivity().getSupportFragmentManager();
                fragment = new AddCourseFragment();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.main_container, fragment).commit();
            }
        });

        //when searching
        searchBar.setIconifiedByDefault(false);
        searchBar.setSubmitButtonEnabled(true);
        searchBar.setQueryHint(getText(R.string.search));
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String text = newText.toString();
                adapter.filter(text);
                return  true;
            }
        });

        return  rootView;
    }




}
