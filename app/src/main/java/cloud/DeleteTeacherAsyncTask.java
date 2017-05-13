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

/**
 * Created by Rafael Peixoto on 10.05.2017.
 */

public class DeleteTeacherAsyncTask extends AsyncTask<Void, Void, Integer>{

    private static TeacherApi teacherApi = null;
    private static final String TAG = DeleteTeacherAsyncTask.class.getName();

    private int teacherId;


    public DeleteTeacherAsyncTask(int teacherId)
    {
        this.teacherId = teacherId;
    }



    @Override
    protected Integer doInBackground(Void... params) {

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
            //DELETE IN CLOUD
            if(teacherId != 0)
            {
                //DELETE IN CLOUD
                teacherApi.remove((long)teacherId).execute();
            }

            return teacherId;


        }catch (IOException e){
            Log.e(TAG, e.toString());
            return 0;
        }
    }
}
