package com.example.audreycelia.homeworkapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import db.DatabaseHelper;


public class CloudFragment extends Fragment {

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
        final View rootView = inflater.inflate(R.layout.fragment_cloud, container, false);
        setHasOptionsMenu(true);
        //set the title on the app
        getActivity().setTitle(R.string.cloud);

        Switch cloud = (Switch) rootView.findViewById(R.id.s_cloud_switch);

        if(((MainActivity)getActivity()).isCloudStorageActivated())
            cloud.setChecked(true);



        cloud.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    ((MainActivity)getActivity()).turnCloud(true);
                    Intent intent = new Intent(getContext(),LoadingActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else
                    ((MainActivity)getActivity()).turnCloud(false);
            }
        });

        return  rootView;
    }

    @Override
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
