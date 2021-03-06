package com.example.audreycelia.homeworkapp.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

/**
 * Created by audreycelia on 22.04.17.
 */

@Entity
public class Exam {

    //private variables
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long examId;
    private String name;
    private String date;
    private String start;
    private String end;
    private double grade;
    private int room;
    private String description;
    private long courseId;

    //empty constructor
    public Exam(){

    }
    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}

    public long getExamId() { return examId;}

    public void setExamId(int examId) {this.examId = examId;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getDate() {return date;}

    public void setDate(String date) {
        //transform date format for correct handling
        SimpleDateFormat dateFormatin = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat dateFormatout = new SimpleDateFormat("dd.MM.yyyy");
        Date dateTime;

        try
        {
            dateTime = dateFormatin.parse(date);
            date = dateFormatout.format(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.date = date;
    }

    public String getStart() {return start;}

    public void setStart(String start) {this.start = start;}

    public String getEnd() {return end;}

    public void setEnd(String end) {this.end = end;}

    public double getGrade() {return grade;}

    public void setGrade(double grade) {this.grade = grade;}

    public int getRoom() {return room;}

    public void setRoom(int room) {this.room = room;}

    public long getCourseId() {return courseId;}

    public void setCourseId(int courseId) {this.courseId = courseId;}
}



