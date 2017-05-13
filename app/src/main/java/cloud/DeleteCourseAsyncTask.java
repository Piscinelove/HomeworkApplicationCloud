package cloud;

import android.os.AsyncTask;

import com.example.audreycelia.homeworkapp.backend.courseApi.CourseApi;
import com.example.audreycelia.homeworkapp.backend.courseApi.model.Course;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

/**
 * Created by Rafael Peixoto on 10.05.2017.
 */

public class DeleteCourseAsyncTask extends AsyncTask<Void, Void, Integer>{

    private static CourseApi courseApi = null;
    private static final String TAG = DeleteCourseAsyncTask.class.getName();
    private int courseId;

    public DeleteCourseAsyncTask(int courseId)
    {
        this.courseId = courseId;
    }

    @Override
    protected Integer doInBackground(Void... params) {

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
            //INSERT IN CLOUD
            if(courseId != 0) {
                courseApi.remove((long)courseId).execute();
            }

            return courseId;



        }catch (IOException e){
            return 0;
        }


    }
}
