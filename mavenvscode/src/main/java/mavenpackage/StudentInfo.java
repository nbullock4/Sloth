package mavenpackage;

public final class StudentInfo{
    public int studentID;
    public String major;
    public char gender;
    public int testScore;
    public int retakeScore;

    public StudentInfo(int studentID, String major, char gender, int testScore, int retakeScore){
        this.studentID = studentID;
        this.major = major;
        this.gender = gender;
        this.testScore = testScore;
        this.retakeScore = retakeScore;
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