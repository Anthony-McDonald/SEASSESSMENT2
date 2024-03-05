public class Config {
    private String teachersFilePath = "database-teacher.txt";
    private String courseFilePath = "database-teachingRequirment.txt";
    private String assignmentsFilePath = "database-assignment.txt";

    public String getTeachersFilePath() {
        return this.teachersFilePath;
    }

    public String getCoursesFilePath() {
        return this.courseFilePath;
    }

    public String getAssignmentsFilePath() {
        return this.assignmentsFilePath;
    }
}
