package pojo;

public class Grades {
    public String math;
    public String english;


    public Grades(String math, String english) {
        this.math = math;
        this.english = english;
    }

    public String getMath() {
        return math;
    }

    public void setMath(String math) {
        this.math = math;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }
}
