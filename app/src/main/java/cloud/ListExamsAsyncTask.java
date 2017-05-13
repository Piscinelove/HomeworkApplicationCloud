package cloud;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.example.audreycelia.homeworkapp.LoadingActivity;
import com.example.audreycelia.homeworkapp.MainActivity;
import com.example.audreycelia.homeworkapp.R;
import com.example.audreycelia.homeworkapp.backend.examApi.ExamApi;
import com.example.audreycelia.homeworkapp.backend.examApi.model.Exam;
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

public class ListExamsAsyncTask extends AsyncTask<Void, Void, List<Exam>>{

    private static ExamApi examApi = null;
    private static final String TAG = ListExamsAsyncTask.class.getName();
    //DATABASE HELPER
    private DatabaseHelper db;
    //LOADING ACTIVITY
    private LoadingActivity loadingActivity;
    //STATE TEXT
    private TextView state;


    public ListExamsAsyncTask(DatabaseHelper db, LoadingActivity loadingActivity, TextView state)
    {

        this.db = db;
        this.loadingActivity = loadingActivity;
        this.state = state;
    }

    @Override
    protected void onPreExecute() {
        //CHANGE STATE TEXT
        state.setText(R.string.syncroExams);
    }

    @Override
    protected List<Exam> doInBackground(Void... params) {

        if(examApi == null)
        {
            //ONLY ONCE
            ExamApi.Builder builder = new ExamApi.Builder(AndroidHttp.newCompatibleTransport(),new AndroidJsonFactory(), null);
            builder.setRootUrl("https://homeworkapplicationcloud.appspot.com/_ah/api");
            builder.setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                @Override
                public void initialize(AbstractGoogleClientRequest<?> request) throws IOException {
                    request.setDisableGZipContent(true);
                }
            });
            builder.setApplicationName("MeMinder");

            examApi = builder.build();
        }

        try{
            //CALL HERE THE WISHED METHODS ON THE ENDPOINTS
            //GET COURSES FROM CLOUD
            return examApi.list().execute().getItems();



        }catch (IOException e){
            Log.e(TAG, e.toString());
            return new ArrayList<Exam>();
    }
    }

    @Override
    protected void onPostExecute(List<Exam> exams) {

        if(exams != null && !exams.isEmpty()) {
            db.cloudToSqlExams(exams);
        }

        db.close();
        loadingActivity.finishLoading();

    }
}
