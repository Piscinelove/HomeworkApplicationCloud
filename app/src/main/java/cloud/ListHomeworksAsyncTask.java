package cloud;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.example.audreycelia.homeworkapp.LoadingActivity;
import com.example.audreycelia.homeworkapp.R;
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

public class ListHomeworksAsyncTask extends AsyncTask<Void, Void, List<Homework>>{

    private static HomeworkApi homeworkApi = null;
    private static final String TAG = ListHomeworksAsyncTask.class.getName();
    //DATABASE HELPER
    private DatabaseHelper db;
    //LOADING ACTIVITY
    private LoadingActivity loadingActivity;
    //STATE TEXT
    private TextView state;

    public ListHomeworksAsyncTask(DatabaseHelper db, LoadingActivity loadingActivity, TextView state)
    {

        this.db = db;
        this.loadingActivity = loadingActivity;
        this.state = state;
    }

    @Override
    protected void onPreExecute() {
        //CHANGE STATE TEXT
        state.setText(R.string.syncroHomeworks);
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
            builder.setApplicationName("MeMinder");

            homeworkApi = builder.build();
        }

        try{
            //CALL HERE THE WISHED METHODS ON THE ENDPOINTS
            //GET COURSES FROM CLOUD
            return homeworkApi.list().execute().getItems();



        }catch (IOException e){
            Log.e(TAG, e.toString());
            return new ArrayList<Homework>();
        }
    }

    @Override
    protected void onPostExecute(List<Homework> homeworks) {

        if(homeworks != null && !homeworks.isEmpty()) {
            db.cloudToSqlHomeworks(homeworks);
        }

        //THREAD SLEEP
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new ListExamsAsyncTask(db,loadingActivity,state).execute();





    }

}
