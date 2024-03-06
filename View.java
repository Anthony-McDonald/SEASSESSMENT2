import softwareEngineering.Course;
import softwareEngineering.databaseHandler;

public class View {
    
    public void displayTeachingRequirements(){
        databaseHandler dbHandler = new databaseHandler();
        List<Course> courses = dbHandler.getAllTeachingRequirements();
        for (Course course : courses) {
            System.out.println("Course: " + course.getCourseName() + ", Requirements: " + course.getRequiredTeachers());
        }
    }

    public void displaySuitableTeachers(){
        System.out.println("Suitable Teachers: ");
    }

    public void displayAllTeachers(){
        List<Teacher> teachers = dbHandler.getAllTeachers();
        for (Teacher teacher : teachers) {
            System.out.println(teacher.getName());
        }
    }
}
