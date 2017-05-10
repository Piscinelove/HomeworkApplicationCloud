package com.example.audreycelia.homeworkapp.backend;


import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
/**
 * Created by Rafael Peixoto on 06.04.2017.
 */

@Entity
public class Course {

    //private variables
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long courseId;
    private String name;
    private String day;
    private String start;
    private String end;
    private int color;
    private int room;
    private String description;
    private long teacherId;

    //empty constructor
    public Course()
    {

    }

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}

    public long getCourseId() {
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

    public long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getDay() {return day;}

    public void setDay(String day) {this.day = day;}
}
