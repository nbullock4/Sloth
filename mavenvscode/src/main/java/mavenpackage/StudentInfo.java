package mavenpackage;

public final class StudentInfo{
    public int studentID;
    public String major;
    public char gender;

    public StudentInfo(int studentID, String major, char gender){
        this.studentID = studentID;
        this.major = major;
        this.gender = gender;
    }
    public StudentInfo() {}

    void setStudentID(int studentID){
        this.studentID = studentID;
    }
    void setMajor(String major){
        this.major = major;
    }
    void setGender(char gender){
        this.gender = gender;
    }
}