package cloud;

import android.os.AsyncTask;
import android.util.Log;

import com.example.audreycelia.homeworkapp.backend.teacherApi.TeacherApi;
import com.example.audreycelia.homeworkapp.backend.teacherApi.model.Teacher;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

import db.DatabaseHelper;

/**
 * Created by Rafael Peixoto on 10.05.2017.
 */

public class TeacherAsyncTask extends AsyncTask<Void, Void, Teacher>{

    private static TeacherApi teacherApi = null;
    private static final String TAG = TeacherAsyncTask.class.getName();

    private Teacher teacher;


    public TeacherAsyncTask(Teacher teacher)
    {
        this.teacher = teacher;
    }
    @Override
    protected Teacher doInBackground(Void... params) {

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
            //INSERT IN CLOUD
            if(teacher != null)
            {
                //INSERT IN CLOUD
                teacherApi.insert(teacher).execute();
            }

            return teacher;

        }catch (IOException e){
            Log.e(TAG, e.toString());
            return  new Teacher();
        }
    }
}
