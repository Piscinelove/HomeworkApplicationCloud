package cloud;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.example.audreycelia.homeworkapp.MainActivity;
import com.example.audreycelia.homeworkapp.R;
import com.example.audreycelia.homeworkapp.backend.courseApi.CourseApi;
import com.example.audreycelia.homeworkapp.backend.courseApi.model.Course;
import com.example.audreycelia.homeworkapp.backend.homeworkApi.HomeworkApi;
import com.example.audreycelia.homeworkapp.backend.homeworkApi.model.Homework;
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

public class HomeworkAsyncTask extends AsyncTask<Void, Void, List<Homework>>{

    private static HomeworkApi homeworkApi = null;
    private static final String TAG = HomeworkAsyncTask.class.getName();
    private Homework homework;
    private DatabaseHelper db;

    //Progress dialog
    private MainActivity mainActivity;


    public HomeworkAsyncTask(DatabaseHelper db, MainActivity mainActivity)
    {
        this.mainActivity = mainActivity;
        this.db = db;
    }

    public HomeworkAsyncTask(Homework homework, DatabaseHelper db,MainActivity mainActivity)
    {
        this.homework = homework;
        this.db = db;
        this.mainActivity = mainActivity;
    }

    @Override
    protected List<Homework> doInBackground(Void... params) {

        if(homeworkApi == null)
        {
            //ONLY ONCE
            HomeworkApi.Builder builder = new HomeworkApi.Builder(AndroidHttp.newCompatibleTransport(),new AndroidJsonFactory(), null);
            builder.setRootUrl("https://homeworkapplicationcloud.appspot.com/_ah/api");
            builder.setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                @Override
                public void initialize(AbstractGoogleClientRequest<?> request) throws IOException {
                    request.setDisableGZipContent(true);
                }
            });

            homeworkApi = builder.build();
        }

        try{
            //CALL HERE THE WISHED METHODS ON THE ENDPOINTS

            //INSERT IN CLOUD
            if(homework != null) {
                homeworkApi.insert(homework).execute();
                Log.i(TAG, "insert course");
            }

            return homeworkApi.list().execute().getItems();



        }catch (IOException e){
            Log.e(TAG, e.toString());
            return new ArrayList<Homework>();
        }
    }

    @Override
    protected void onPostExecute(List<Homework> homeworks) {


        if(homeworks != null) {
            for (Homework homework : homeworks) {
                Log.i(TAG, "First name: " + homework.getName());

            }
        }

        if(homeworks != null) {
            db.cloudToSqlHomeworks(homeworks);
        }

    }

}
