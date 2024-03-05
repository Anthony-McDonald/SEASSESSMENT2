import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

    public int assignTeacher(Teacher teacher, Course course) {
        int isValidTeacher = this.verifyTeacherInDatabase(teacher);
        if (isValidTeacher == 1) {
            return 1;
        }
        int isValidCourse = this.verifyCourseInDatabase(course);
        if (isValidCourse == 1) {
            return 1;
        }
        if (this.canTeacherTeach(teacher, course) == false) {
            return 1;
        }
        this.dbHandler.addAssignment(this.config.getAssignmentsFilePath(), teacher, course);
        return 0;
    }

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

    public int deleteAssignment(Teacher teacher, Course course) {
        int isValidTeacher = this.verifyTeacherInDatabase(teacher);
        if (isValidTeacher == 1) {
            return 1;
        }
        int isValidCourse = this.verifyCourseInDatabase(course);
        if (isValidCourse == 1) {
            return 1;
        }
        this.dbHandler.deleteAssignment(this.config.getAssignmentsFilePath(), teacher, course);
        return 0;
    }

    private int verifyTeacherInDatabase(Teacher teacher) {
        List<Teacher> allTeachers =  this.dbHandler.getAllTeachers();
        if (allTeachers.contains(teacher) == false) {
            return 1;
        }
        return 0;
    }

    private int verifyCourseInDatabase(Course course) {
        List<Course> allCourses = this.dbHandler.getAllTeachingRequirements();
        if (allCourses.contains(course) == false) {
            return 1;
        }
        return 0;
    }

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

    public static void main(String[] args) {
        Config config = new Config();
        DataHandler dHandler = new DataHandler(config);
        
        Course course = dHandler.findCourseFromID(3);
        for (Teacher t : dHandler.getSuitableTeachers(course)) {
            System.out.println(t.getTeacherID());
        }

        Teacher teacher = dHandler.findTeacherFromID(2);
        dHandler.assignTeacher(teacher, course);
        for (Map<Teacher, Course> map : dHandler.getAllAssignments()) {
            List<Entry<Teacher, Course>> mapList = new ArrayList<Entry<Teacher, Course>>(map.entrySet());
            Teacher tt = mapList.get(0).getKey();
            Course cc = mapList.get(0).getValue();
            System.out.println(tt.getTeacherID() + ", " + cc.getCourseID());
        }
        
    }
}
