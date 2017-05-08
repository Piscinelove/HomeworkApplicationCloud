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

import db.DatabaseHelper;
import db.Homework;
import db.HomeworksListAdapter;


public class HomeworkFragment extends Fragment {

    private ListView listView;
    private HomeworksListAdapter adapter;
    private DatabaseHelper db;
    private ImageButton addButton;
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private SearchView searchBar;

    public  HomeworkFragment()
    {
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
        View rootView =  inflater.inflate(R.layout.fragment_homework, container, false);

        //to see the menu on the top
        setHasOptionsMenu(true);

        //set the title on the app
        getActivity().setTitle(R.string.title_homework);

        //initialise search bar
        searchBar = (SearchView) rootView.findViewById(R.id.search_bar_homework);

        db = new DatabaseHelper(getActivity().getApplicationContext());

        ArrayList<Homework> listHomeworks = db.getAllHomeworks();
        listView = (ListView) rootView.findViewById(R.id.listHomeworks);
        adapter = new HomeworksListAdapter(getActivity().getApplicationContext(), listHomeworks);
        listView.setAdapter(adapter);
        listView.setTextFilterEnabled(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int homeworkId = ((Homework) listView.getItemAtPosition(position)).getHomeworkId();
                Bundle bundle = new Bundle();
                bundle.putInt("SelectedHomeworkId", homeworkId);
                fragmentManager = getActivity().getSupportFragmentManager();
                fragment = new EditHomeworkFragment();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.addToBackStack(null);
                transaction.replace(R.id.main_container, fragment).commit();

            }
        });

        //add button
        addButton = (ImageButton) rootView.findViewById(R.id.ib_plus_homework);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager = getActivity().getSupportFragmentManager();
                fragment = new AddHomeworkFragment();
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
