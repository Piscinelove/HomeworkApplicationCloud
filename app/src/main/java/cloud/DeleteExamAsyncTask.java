package cloud;

import android.os.AsyncTask;

import com.example.audreycelia.homeworkapp.backend.examApi.ExamApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

/**
 * Created by Rafael Peixoto on 10.05.2017.
 */

public class DeleteExamAsyncTask extends AsyncTask<Void, Void, Integer>{

    private static ExamApi examApi = null;
    private static final String TAG = DeleteExamAsyncTask.class.getName();
    private int examId;

    public DeleteExamAsyncTask(int examId)
    {
        this.examId = examId;
    }

    @Override
    protected Integer doInBackground(Void... params) {

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
            //INSERT IN CLOUD
            if(examId != 0) {
                examApi.remove((long)examId).execute();
            }


            return examId;



        }catch (IOException e){
            return 0;
        }
    }

}
