package com.example.audreycelia.homeworkapp;

import android.os.AsyncTask;
import android.util.Log;


import com.example.audreycelia.homeworkapp.backend.teacherApi.TeacherApi;
import com.example.audreycelia.homeworkapp.backend.teacherApi.model.Teacher;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Rafael Peixoto on 10.05.2017.
 */

public class TeacherAsyncTask extends AsyncTask<Void, Void, ArrayList<Teacher>>{

    private static TeacherApi teacherApi = null;
    private static final String TAG = TeacherAsyncTask.class.getName();
    private Teacher teacher;

    public  TeacherAsyncTask(Teacher teacher)
    {
        this.teacher = teacher;
    }

    @Override
    protected ArrayList<Teacher> doInBackground(Void... params) {

        if(teacherApi == null)
        {
            TeacherApi.Builder builder = new TeacherApi.Builder(AndroidHttp.newCompatibleTransport(),new AndroidJsonFactory(), null);
            builder.setRootUrl("http://homeworkapplicationcloud.appspot.com/_ah/api");
            builder.setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                @Override
                public void initialize(AbstractGoogleClientRequest<?> request) throws IOException {
                    request.setDisableGZipContent(true);
                }
            });

            teacherApi = builder.build();
        }

        try{
            if(teacher != null) {
                teacherApi.insert(teacher).execute();
                Log.i(TAG, "insert employee");
            }

            return new ArrayList<Teacher>(teacherApi.list().execute().getItems());



        }catch (IOException e){
            Log.e(TAG, e.toString());
            return new ArrayList<Teacher>();
        }
    }

    @Override
    protected void onPostExecute(ArrayList<Teacher> teachers) {


        if(teachers != null) {
            for (Teacher teacher : teachers) {
                Log.i(TAG, "First name: " + teacher.getFirstName() + " Last name: "
                        + teacher.getLastName());

            }
        }
    }
}
