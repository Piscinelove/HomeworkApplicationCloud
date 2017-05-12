package cloud;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.example.audreycelia.homeworkapp.MainActivity;
import com.example.audreycelia.homeworkapp.R;
import com.example.audreycelia.homeworkapp.backend.courseApi.CourseApi;
import com.example.audreycelia.homeworkapp.backend.courseApi.model.Course;
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

public class ExamAsyncTask extends AsyncTask<Void, Void, List<Exam>>{

    private static ExamApi examApi = null;
    private static final String TAG = ExamAsyncTask.class.getName();
    private Exam exam;
    private DatabaseHelper db;

    //Progress dialog
    private MainActivity mainActivity;


    public ExamAsyncTask(DatabaseHelper db, MainActivity mainActivity)
    {
        this.mainActivity = mainActivity;
        this.db = db;
    }

    public ExamAsyncTask(Exam exam, DatabaseHelper db,MainActivity mainActivity)
    {
        this.exam = exam;
        this.db = db;
        this.mainActivity = mainActivity;
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

            examApi = builder.build();
        }

        try{
            //CALL HERE THE WISHED METHODS ON THE ENDPOINTS

            //INSERT IN CLOUD
            if(exam != null) {
                examApi.insert(exam).execute();
                Log.i(TAG, "insert course");
            }

            if(mainActivity != null && mainActivity.getProgressDialog().isShowing())
                mainActivity.getProgressDialog().dismiss();


            return examApi.list().execute().getItems();



        }catch (IOException e){
            Log.e(TAG, e.toString());
            return new ArrayList<Exam>();
    }
    }

    @Override
    protected void onPostExecute(List<Exam> exams) {


        if(exams != null) {
            for (Exam exam : exams) {
                Log.i(TAG, "First name: " + exam.getName());

            }
        }

        if(exams != null) {
            db.cloudToSqlExams(exams);
        }

        if(mainActivity != null && mainActivity.getProgressDialog().isShowing())
            mainActivity.getProgressDialog().dismiss();

    }
}
