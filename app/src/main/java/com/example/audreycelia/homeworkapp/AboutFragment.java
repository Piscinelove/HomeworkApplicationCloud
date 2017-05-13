package com.example.audreycelia.homeworkapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class AboutFragment extends Fragment {


    private Menu menu;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.backactionbar, menu);
        this.menu = menu;
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_about, container, false);
        setHasOptionsMenu(true);
        //set the title on the app
        getActivity().setTitle(R.string.about);

        return rootView;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        MenuItem back = menu.findItem(R.id.ab_cloud_back);

        switch (item.getItemId())
        {
            case R.id.ab_cloud_back:
                getActivity().getSupportFragmentManager().popBackStack();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
