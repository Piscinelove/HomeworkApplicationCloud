package db;

import android.provider.BaseColumns;

/**
 * Created by Rafael Peixoto on 06.04.2017.
 */

public final class DatabaseContract {

    //Empty Constructor
    //Prevent someone from accidentally instantiating the contract class
    private DatabaseContract()
    {

    }

    //Inner class that defines the table contents
    //Courses table
    public static abstract class Courses implements BaseColumns
    {
        //Table name
        public static final String TABLE_NAME = "courses";
        //Columns names
        public static final String COURSE_ID = "courseId";
        public static final String COURSE_NAME = "name";
        public static final String COURSE_DAY = "day";
        public static final String COURSE_START = "start";
        public static final String COURSE_END = "end";
        public static final String COURSE_COLOR = "color";
        public static final String COURSE_ROOM = "room";
        public static final String COURSE_DESCRIPTION = "description";
        public static final String COURSE_TEACHER_ID = "teacherId";
    }

    //Inner class that defines the table contents
    //Homeworks table
    public static abstract class Homeworks implements BaseColumns
    {
        //Table name
        public static final String TABLE_NAME = "homeworks";
        //Columns names
        public static final String HOMEWORK_ID = "homeworkId";
        public static final String HOMEWORK_NAME = "name";
        public static final String HOMEWORK_DEADLINE = "deadline";
        public static final String HOMEWORK_DONE = "done";
        public static final String HOMEWORK_DESCRIPTION = "description";
        public static final String HOMEWORK_COURSE_ID = "courseId";
    }

    //Inner class that defines the table contents
    //Teachers table
    public static abstract class Teachers implements BaseColumns
    {
        //Table name
        public static final String TABLE_NAME = "teachers";
        //Columns names
        public static final String TEACHER_ID = "teacherId";
        public static final String TEACHER_FIRSTNAME = "firstName";
        public static final String TEACHER_LASTNAME = "lastName";
        public static final String TEACHER_PHONE = "phone";
        public static final String TEACHER_EMAIL = "email";
        public static final String TEACHER_DESCRIPTION = "description";

    }

    //Inner class that defines the table contents
    //Exams table
    public static abstract class Exams implements BaseColumns
    {
        //Table name
        public static final String TABLE_NAME = "exams";
        //Columns names
        public static final String EXAM_ID = "examId";
        public static final String EXAM_NAME = "name";
        public static final String EXAM_DATE = "date";
        public static final String EXAM_START = "start";
        public static final String EXAM_END = "end";
        public static final String EXAM_GRADE = "grade";
        public static final String EXAM_ROOM = "room";
        public static final String EXAM_DESCRIPTION = "description";
        public static final String EXAM_COURSE_ID = "courseId";
    }


    //Database creation sql statement courses
    public static final String CREATE_TABLE_COURSES =
            "CREATE TABLE " + Courses.TABLE_NAME+ "("
                    + Courses.COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + Courses.COURSE_NAME + " TEXT NOT NULL, "
                    + Courses.COURSE_DAY + " TEXT NOT NULL, "
                    + Courses.COURSE_START + " TEXT NOT NULL, "
                    + Courses.COURSE_END + " TEXT NOT NULL, "
                    + Courses.COURSE_COLOR + " INTEGER NULL, "
                    + Courses.COURSE_ROOM + " INTEGER NOT NULL, "
                    + Courses.COURSE_DESCRIPTION + " TEXT NULL, "
                    + Courses.COURSE_TEACHER_ID + " INTEGER NOT NULL, "
                    + " FOREIGN KEY ("+Courses.COURSE_TEACHER_ID+") REFERENCES " + Teachers.TABLE_NAME+"("+Teachers.TEACHER_ID+") ON DELETE CASCADE );";

    //Database creation sql statement teachers
    public static final String CREATE_TABLE_TEACHERS =
            "CREATE TABLE " + Teachers.TABLE_NAME+ "("
                    + Teachers.TEACHER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + Teachers.TEACHER_FIRSTNAME + " TEXT NOT NULL, "
                    + Teachers.TEACHER_LASTNAME + " TEXT NOT NULL, "
                    + Teachers.TEACHER_PHONE + " TEXT NULL, "
                    + Teachers.TEACHER_EMAIL + " TEXT NULL, "
                    + Teachers.TEACHER_DESCRIPTION + " TEXT NULL" + ");";

    //Database creation sql statement homeworks
    public static final String CREATE_TABLE_HOMEWORKS =
            "CREATE TABLE " + Homeworks.TABLE_NAME+ "("
                    + Homeworks.HOMEWORK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + Homeworks.HOMEWORK_NAME + " TEXT NOT NULL, "
                    + Homeworks.HOMEWORK_DEADLINE + " TEXT NOT NULL, "
                    + Homeworks.HOMEWORK_DONE + " INTEGER DEFAULT 0, "
                    + Homeworks.HOMEWORK_DESCRIPTION + " TEXT NULL, "
                    + Homeworks.HOMEWORK_COURSE_ID + " INTEGER NOT NULL, "
                    + " FOREIGN KEY ("+Homeworks.HOMEWORK_COURSE_ID+") REFERENCES " + Courses.TABLE_NAME+"("+Courses.COURSE_ID+") ON DELETE CASCADE );";

    //Database creation sql statement exams
    public static final String CREATE_TABLE_EXAMS =
            "CREATE TABLE " + Exams.TABLE_NAME+ "("
                    + Exams.EXAM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + Exams.EXAM_NAME + " TEXT NOT NULL, "
                    + Exams.EXAM_DATE +" TEXT NOT NULL, "
                    + Exams.EXAM_START + " TEXT NOT NULL, "
                    + Exams.EXAM_END + " TEXT NOT NULL, "
                    + Exams.EXAM_GRADE + " REAL NULL, "
                    + Exams.EXAM_ROOM + " INTEGER NOT NULL, "
                    + Exams.EXAM_DESCRIPTION + " TEXT NULL, "
                    + Exams.EXAM_COURSE_ID + " INTEGER NOT NULL, "
                    + " FOREIGN KEY ("+Exams.EXAM_COURSE_ID+") REFERENCES " + Courses.TABLE_NAME+"("+Courses.COURSE_ID+") ON DELETE CASCADE );";

    //Database drop sql statement courses
    public static final String DELETE_TABLE_COURSES = "DROP TABLE IF EXISTS " + Courses.TABLE_NAME;

    //Database drop sql statement homeworks
    public static final String DELETE_TABLE_HOMEWORKS = "DROP TABLE IF EXISTS " + Homeworks.TABLE_NAME;

    //Database drop sql statement teachers
    public static final String DELETE_TABLE_TEACHERS = "DROP TABLE IF EXISTS " + Teachers.TABLE_NAME;

    //Database drop sql statement exams
    public static final String DELETE_TABLE_EXAMS = "DROP TABLE IF EXISTS " + Exams.TABLE_NAME;
}
