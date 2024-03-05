import java.util.List;

public class Teacher {
	private int teacherID;
    private String teacherName;
    private List<Course> courses;
    private int availableCourse;

    public Teacher(int teacherID, String teacherName, List<Course> courses, int availableCourse) {
        this.teacherID = teacherID;
        this.teacherName = teacherName;
        this.courses = courses;
        this.availableCourse = availableCourse;
    }

    public int getTeacherID() {
        return teacherID;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public int getAvailableCourse() {
        return availableCourse;
    }

    @Override
    public String toString() {
        return  "teacherID=" + teacherID +
                ", teacherName='" + teacherName + '\'' +
                ", courses=" + courses +
                ", availableCourse=" + availableCourse;
    }
}
