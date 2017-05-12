package cloud;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.example.audreycelia.homeworkapp.MainActivity;
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

public class CourseAsyncTask extends AsyncTask<Void, Void, List<Course>>{

    private static CourseApi courseApi = null;
    private static final String TAG = CourseAsyncTask.class.getName();
    private Course course;
    private DatabaseHelper db;

    //Progress dialog
    private MainActivity mainActivity;


    public CourseAsyncTask(DatabaseHelper db, MainActivity mainActivity)
    {
        this.mainActivity = mainActivity;
        this.db = db;
    }

    public CourseAsyncTask(Course course, DatabaseHelper db,MainActivity mainActivity)
    {
        this.course = course;
        this.db = db;
        this.mainActivity = mainActivity;
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

            courseApi = builder.build();
        }

        try{
            //CALL HERE THE WISHED METHODS ON THE ENDPOINTS

            //INSERT IN CLOUD
            if(course != null) {
                courseApi.insert(course).execute();
                Log.i(TAG, "insert course");
            }

            return courseApi.list().execute().getItems();



        }catch (IOException e){
            Log.e(TAG, e.toString());
            return new ArrayList<Course>();
        }


    }

    @Override
    protected void onPostExecute(List<Course> courses) {


        if(courses != null) {
            for (Course course : courses) {
                Log.i(TAG, "First name: " + course.getName());

            }
        }

        if(courses != null && !courses.isEmpty()) {
            db.cloudToSqlCourses(courses);
        }


    }
}
