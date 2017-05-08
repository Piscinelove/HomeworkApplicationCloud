package db;

/**
 * Created by Rafael Peixoto on 06.04.2017.
 */

public class Course {

    //private variables
    private int courseId;
    private String name;



    private String day;
    private String start;
    private String end;
    private int color;
    private int room;
    private String description;
    private int teacherId;

    //constructor
    public Course(int courseId, String name,String day, String start, String end, int color, int room, String description, int teacherId)
    {
        this.courseId = courseId;
        this.name = name;
        this.day = day;
        this.start = start;
        this.end = end;
        this.color = color;
        this.room = room;
        this.description = description;
        this.teacherId = teacherId;
    }
    //empty constructor
    public Course()
    {

    }

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStart() {return start;}

    public void setStart(String start) {this.start = start;}

    public String getEnd() {return end;}

    public void setEnd(String end) {this.end = end;}

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getRoom() {
        return room;
    }

    public void setRoom(int room) {
        this.room = room;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getDay() {return day;}

    public void setDay(String day) {this.day = day;}

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Course)){
            return false;
        }
        Course c = (Course) obj;
        if(this.courseId == c.getCourseId()){
            return true;
        }
        return false;
    }
}
