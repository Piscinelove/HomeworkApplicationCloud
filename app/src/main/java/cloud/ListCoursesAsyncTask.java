package cloud;

import android.os.AsyncTask;
import android.widget.TextView;

import com.example.audreycelia.homeworkapp.LoadingActivity;
import com.example.audreycelia.homeworkapp.R;
import com.example.audreycelia.homeworkapp.backend.courseApi.CourseApi;
import com.example.audreycelia.homeworkapp.backend.courseApi.model.Course;
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

public class ListCoursesAsyncTask extends AsyncTask<Void, Void, List<Course>>{

    private static CourseApi courseApi = null;
    private static final String TAG = ListCoursesAsyncTask.class.getName();
    //DATABASE HELPER
    private DatabaseHelper db;
    //LOADING ACTIVITY
    private LoadingActivity loadingActivity;
    //STATE TEXT
    private TextView state;


    public ListCoursesAsyncTask(DatabaseHelper db, LoadingActivity loadingActivity, TextView state)
    {

        this.db = db;
        this.loadingActivity = loadingActivity;
        this.state = state;
    }


    @Override
    protected void onPreExecute() {
        //CHANGE STATE TEXT
        state.setText(R.string.syncroCourses);
    }

    @Override
    protected List<Course> doInBackground(Void... params) {

        if(courseApi == null)
        {
            //ONLY ONCE
            CourseApi.Builder builder = new CourseApi.Builder(AndroidHttp.newCompatibleTransport(),new AndroidJsonFactory(), null);
            builder.setRootUrl("https://homeworkapplicationcloud.appspot.com/_ah/api");
            builder.setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                @Override
                public void initialize(AbstractGoogleClientRequest<?> request) throws IOException {
                    request.setDisableGZipContent(true);
                }
            });
            builder.setApplicationName("MeMinder");

            courseApi = builder.build();
        }

        try{
            //CALL HERE THE WISHED METHODS ON THE ENDPOINTS
            //GET COURSES FROM CLOUD
            return courseApi.list().execute().getItems();



        }catch (IOException e){
            return new ArrayList<Course>();
        }


    }

    @Override
    protected void onPostExecute(List<Course> courses) {
        //SAVING CLOUD DATA IN LOCAL DB
        if(courses != null && !courses.isEmpty()) {
            db.cloudToSqlCourses(courses);
        }

        //THREAD SLEEP
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new ListHomeworksAsyncTask(db,loadingActivity,state).execute();



    }
}
