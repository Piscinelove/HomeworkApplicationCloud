package db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Rafael Peixoto on 06.04.2017.
 */

public class Homework {

    //private variables
    private int homeworkId;
    private String name;
    private String deadline;
    private boolean done;
    private String description;
    private int courseId;

    public Homework(int homeworkId, String name, String deadline, boolean done, String description, int courseId)
    {
        this.homeworkId = homeworkId;
        this.name = name;
        this.deadline = deadline;
        this.done = done;
        this.description = description;
        this.courseId = courseId;
    }

    //empty constructor
    public Homework(){

    }
    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}

    public int getHomeworkId() {
        return homeworkId;
    }

    public void setHomeworkId(int homeworkId) {
        this.homeworkId = homeworkId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline)
    {
        //transform date format for correct handling
        SimpleDateFormat dateFormatin = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat dateFormatout = new SimpleDateFormat("dd.MM.yyyy");
        Date dateTime;

        try
        {
            dateTime = dateFormatin.parse(deadline);
            deadline = dateFormatout.format(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.deadline = deadline;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}
