package cloud;

import android.os.AsyncTask;
import android.widget.TextView;

import com.example.audreycelia.homeworkapp.LoadingActivity;
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
 * Created by peixotte on 13.05.2017.
 */

public class ListTeachersAsyncTask extends AsyncTask<Void, Void, List<Teacher>> {

    private static TeacherApi teacherApi = null;
    private static final String TAG = ListTeachersAsyncTask.class.getName();
    //DATABASE HELPER
    private DatabaseHelper db;
    //LOADING ACTIVITY
    private LoadingActivity loadingActivity;
    //STATE TEXT
    private TextView state;

    public ListTeachersAsyncTask(DatabaseHelper db, LoadingActivity loadingActivity, TextView state)
    {

        this.db = db;
        this.loadingActivity = loadingActivity;
        this.state = state;
    }

    @Override
    protected void onPreExecute() {
        //CHANGE STATE TEXT
        state.setText(R.string.syncroTeachers);
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
            builder.setApplicationName("MeMinder");

            teacherApi = builder.build();
        }

        try{
            //CALL HERE THE WISHED METHODS ON THE ENDPOINTS
            //GET TEACHERS FROM CLOUD
            return teacherApi.list().execute().getItems();



        }catch (IOException e){
            return new ArrayList<Teacher>();
        }


    }

    @Override
    protected void onPostExecute(List<Teacher> teachers) {
        //SAVING CLOUD DATA IN LOCAL DB
        if(teachers != null && !teachers.isEmpty()) {
            db.cloudToSqlTeachers(teachers);
        }

        //THREAD SLEEP
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //CALLING NEXT ASYNC TASK
        new ListCoursesAsyncTask(db,loadingActivity,state).execute();


    }
}
