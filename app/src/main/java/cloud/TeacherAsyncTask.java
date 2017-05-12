package cloud;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;


import com.example.audreycelia.homeworkapp.MainActivity;
import com.example.audreycelia.homeworkapp.R;
import com.example.audreycelia.homeworkapp.backend.teacherApi.TeacherApi;
import com.example.audreycelia.homeworkapp.backend.teacherApi.model.Teacher;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import db.DatabaseHelper;

/**
 * Created by Rafael Peixoto on 10.05.2017.
 */

public class TeacherAsyncTask extends AsyncTask<Void, Void, List<Teacher>>{

    private static TeacherApi teacherApi = null;
    private static final String TAG = TeacherAsyncTask.class.getName();
    private Teacher teacher;
    private DatabaseHelper db;


    //Progress dialog
    private MainActivity mainActivity;


    public TeacherAsyncTask(DatabaseHelper db, MainActivity mainActivity)
    {

        this.db = db;
        this.mainActivity = mainActivity;
    }

    public  TeacherAsyncTask(Teacher teacher, DatabaseHelper db,MainActivity mainActivity)
    {
        this.teacher = teacher;
        this.db = db;
        this.mainActivity = mainActivity;
    }

    @Override
    protected List<Teacher> doInBackground(Void... params) {

        if(teacherApi == null)
        {
            //ONLY ONCE
            TeacherApi.Builder builder = new TeacherApi.Builder(AndroidHttp.newCompatibleTransport(),new AndroidJsonFactory(), null);
            builder.setRootUrl("https://homeworkapplicationcloud.appspot.com/_ah/api");
            builder.setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                @Override
                public void initialize(AbstractGoogleClientRequest<?> request) throws IOException {
                    request.setDisableGZipContent(true);
                }
            });

            teacherApi = builder.build();
        }

        try{
            //CALL HERE THE WISHED METHODS ON THE ENDPOINTS

            //INSERT IN CLOUD
            if(teacher != null) {
                teacherApi.insert(teacher).execute();
                Log.i(TAG, "insert employee");
            }

            return teacherApi.list().execute().getItems();



        }catch (IOException e){
            Log.e(TAG, e.toString());
            return new ArrayList<Teacher>();
        }
    }

    @Override
    protected void onPostExecute(List<Teacher> teachers) {


        if(teachers != null) {
            for (Teacher teacher : teachers) {
                Log.i(TAG, "First name: " + teacher.getFirstName() + " Last name: "
                        + teacher.getLastName());

            }
        }

        if(teachers != null) {
            db.cloudToSqlTeachers(teachers);
        }

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(mainActivity != null && mainActivity.getProgressDialog() != null) {
            mainActivity.getProgressDialog().setIndeterminate(false);
            mainActivity.getProgressDialog().setCancelable(false);
            mainActivity.getProgressDialog().setMessage(mainActivity.getString(R.string.syncroTeachers));
            mainActivity.getProgressDialog().show();
        }
    }
}
