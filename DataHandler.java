import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class processes data used in the program.
 */
public class DataHandler {
    private Config config;
    private databaseHandler dbHandler;
    
    public DataHandler(Config config) {
        this.config = config;
        this.dbHandler = new databaseHandler(config.getTeachersFilePath(), config.getCoursesFilePath());
    }

    public List<Teacher> getAllTeachers() {
        return this.dbHandler.getAllTeachers();
    }

    public List<Course> getAllTeachingRequirements() {
        return this.dbHandler.getAllTeachingRequirements();
    }

    public List<Map<Teacher, Course>> getAllAssignments() {
        return this.dbHandler.getAllAssignments(config.getAssignmentsFilePath());
    }

    /**
     * Find a list of teachers who can teach the course
     * @param course
     * @return suitable teachers
     */
    public List<Teacher> getSuitableTeachers(Course course) {
        List<Teacher> allTeachers =  this.dbHandler.getAllTeachers();
        List<Teacher> availableTeacher = new ArrayList<Teacher>();
        for (Teacher teacher : allTeachers) {
            if (this.canTeacherTeach(teacher, course)) {
                availableTeacher.add(teacher);
            }
        }
        return availableTeacher;
    }

    /**
     * Assign a teacher to a course
     * @param teacher
     * @param course
     * @return 0 if successful; -1 if the teacher is not in the database;
     *         -2 if the course is not in the database; -3 if the teacher
     *         cannot teach this course
     */
    public int assignTeacher(Teacher teacher, Course course) {
        boolean isValidTeacher = this.verifyTeacherInDatabase(teacher);
        if (isValidTeacher == false) {
            return -1;
        }
        boolean isValidCourse = this.verifyCourseInDatabase(course);
        if (isValidCourse == false) {
            return -2;
        }
        if (this.canTeacherTeach(teacher, course) == false) {
            return -3;
        }
        this.dbHandler.addAssignment(this.config.getAssignmentsFilePath(), teacher, course);
        return 0;
    }

    /**
     * Find courses that already have teachers assigned to
     * @return a map of courses paired with a list of teacher assigned to
     *         the course
     */
    public Map<Course, List<Teacher>> getAssignedCourse() {
        Map<Course, List<Teacher>> result = new HashMap<Course, List<Teacher>>();
        List<Map<Teacher, Course>> allAssignments = this.dbHandler.getAllAssignments(this.config.getAssignmentsFilePath());
        for (Map<Teacher, Course> map : allAssignments) {
            List<Entry<Teacher, Course>> mapList = new ArrayList<Entry<Teacher, Course>>(map.entrySet());
            Teacher teacher = mapList.get(0).getKey();
            Course course = mapList.get(0).getValue();
            if (result.containsKey(course)) {
                result.get(course).add(teacher);
            } else {
                List<Teacher> teacherList = new ArrayList<Teacher>();
                teacherList.add(teacher);
                result.put(course, teacherList);
            }
        }

        return result;
    }

    /**
     * Find courses that are assigned to the given teacher
     * @param inputTeacher
     * @return a list of courses that this teacher will teach
     */
    public List<Course> getTeacherAssignedCourses(Teacher inputTeacher) {
        List<Course> result = new ArrayList<Course>();
        List<Map<Teacher, Course>> allAssignments = this.dbHandler.getAllAssignments(this.config.getAssignmentsFilePath());
        for (Map<Teacher, Course> map : allAssignments) {
            List<Entry<Teacher, Course>> mapList = new ArrayList<Entry<Teacher, Course>>(map.entrySet());
            Teacher teacher = mapList.get(0).getKey();
            Course course = mapList.get(0).getValue();
            if (teacher.equals(inputTeacher)) {
                result.add(course);
            }
        }

        return result;
    }

    /**
     * Delete an assignment
     * @param teacher
     * @param course
     * @return 0 if successful; -1 if the teacher is not in the database;
     *         -2 if the course is not in the database
     */
    public int deleteAssignment(Teacher teacher, Course course) {
        boolean isValidTeacher = this.verifyTeacherInDatabase(teacher);
        if (isValidTeacher == false) {
            return -1;
        }
        boolean isValidCourse = this.verifyCourseInDatabase(course);
        if (isValidCourse == false) {
            return -2;
        }
        this.dbHandler.deleteAssignment(this.config.getAssignmentsFilePath(), teacher, course);
        return 0;
    }

    /**
     * Verify if a given teacher is in the database
     * @param teacher
     * @return true if in; false if not
     */
    private boolean verifyTeacherInDatabase(Teacher teacher) {
        List<Teacher> allTeachers =  this.dbHandler.getAllTeachers();
        return allTeachers.contains(teacher);
    }

    /**
     * Verify if a given course is in the database
     * @param course
     * @return true if in; false if not
     */
    private boolean verifyCourseInDatabase(Course course) {
        List<Course> allCourses = this.dbHandler.getAllTeachingRequirements();
        return allCourses.contains(course);
    }

    /**
     * Verify if a given teacher can teach a given course
     * @param teacher
     * @param course
     * @return true if can; false if cannot
     */
    private boolean canTeacherTeach(Teacher teacher, Course course) {
        if (teacher.getCourses().contains(course) == false) {
            return false;
        }
        if (this.getTeacherAssignedCourses(teacher).size() >= teacher.getAvailableCourse()) {
            return false;
        }
        
        if (this.getTeacherAssignedCourses(teacher).contains(course)) {
            return false;
        }
        
        return true;
    }

    public Teacher findTeacherFromID(int id) {
        return this.dbHandler.findTeacherFromID(id);
    }

    public Course findCourseFromID(int id) {
        return this.dbHandler.findCourseFromID(id);
    }

    // example
    public static void main(String[] args) {
        Config config = new Config();
        DataHandler dHandler = new DataHandler(config);
        
        // find a list of teacher who can teach this course
        Course course = dHandler.findCourseFromID(3);
        System.out.println("Teachers who can teach " + course.getCourseName() + ":");
        for (Teacher t : dHandler.getSuitableTeachers(course)) {
            System.out.println(String.format("\t - %s, ID: %s", t.getTeacherName(), t.getTeacherID()));
        }

        // assign a teacher to a course
        Teacher teacher = dHandler.findTeacherFromID(2);
        int isSuccess = dHandler.assignTeacher(teacher, course);
        System.out.println("The assignment is " + (isSuccess == 0 ? "successful" : "unsuccessful"));

        // find current assignment
        System.out.println("Current assignments:");
        for (Map<Teacher, Course> map : dHandler.getAllAssignments()) {
            List<Entry<Teacher, Course>> mapList = new ArrayList<Entry<Teacher, Course>>(map.entrySet());
            Teacher tt = mapList.get(0).getKey();
            Course cc = mapList.get(0).getValue();
            System.out.println(String.format("\t - %s, %s", tt.getTeacherName(), cc.getCourseName()));
        }
        
    }
}
