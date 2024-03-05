public class Course {
    private int courseID;
    private String courseName;
    private int requiredTeachers;

    public Course(int courseID, String courseName, int requiredTeachers) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.requiredTeachers = requiredTeachers;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getRequiredTeachers() {
        return requiredTeachers;
    }

    public void setRequiredTeachers(int requiredTeachers) {
        this.requiredTeachers = requiredTeachers;
    }

    @Override
    public String toString() {
        return "CourseID: " + courseID +
               ", CourseName: " + courseName +
               ", RequiredTeachers: " + requiredTeachers;
    }
}
