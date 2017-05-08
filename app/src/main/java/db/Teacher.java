package db;

/**
 * Created by Rafael Peixoto on 06.04.2017.
 */

public class Teacher {

    //private variables
    private int teacherId;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String description;

    //constructor
    public Teacher(int teacherId, String firstName, String lastName, String phone, String email, String description)
    {
        this.teacherId = teacherId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.description = description;
    }

    //empty constructor
    public Teacher()
    {

    }

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }


    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Teacher)){
            return false;
        }
        Teacher t = (Teacher) obj;
        if(this.teacherId == t.getTeacherId()){
            return true;
        }
        return false;
    }

}
