package cloud;

import android.os.AsyncTask;
import android.util.Log;

import com.example.audreycelia.homeworkapp.backend.homeworkApi.HomeworkApi;
import com.example.audreycelia.homeworkapp.backend.homeworkApi.model.Homework;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

/**
 * Created by Rafael Peixoto on 10.05.2017.
 */

public class DeleteHomeworkAsyncTask extends AsyncTask<Void, Void, Integer>{

    private static HomeworkApi homeworkApi = null;
    private static final String TAG = DeleteHomeworkAsyncTask.class.getName();
    private int homeworkId;


    public DeleteHomeworkAsyncTask(int homeworkId)
    {
        this.homeworkId = homeworkId;
    }

    @Override
    protected Integer doInBackground(Void... params) {

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
            //INSERT IN CLOUD
            if(homeworkId != 0) {
                homeworkApi.remove((long)homeworkId).execute();
            }

            return homeworkId;



        }catch (IOException e){
            Log.e(TAG, e.toString());
            return 0;
        }
    }

}
