package softwareEngineering;

import softwareEngineering.Course;
import softwareEngineering.DatabaseHandler;
import softwareEngineering.Teacher;
import softwareEngineering.Controller;

import java.util.List;

public class View {
    
    public void displayTeachingRequirements(List<Course> courses){
        for (Course course : courses) {
            System.out.println("Course: " + course.getCourseName() + ", Requirements: " + course.getRequiredTeachers());
        }
    }

    public void displaySuitableTeachers(List<Teacher> teachers){
        System.out.println("Suitable Teachers: ");
    }

    public void displayTeachers(List<Teacher> teachers){
        for (Teacher teacher : teachers) {
            this.displayTeacher(teacher);
        }
    }

    public void displayTeacher(Teacher teacher) {
        System.out.println(teacher.getTeacherName());
    }

    public void displayText(String string) {
        System.out.println(string);
    }

    public void showProgramCommands() {
        System.out.println("\n" + "Welcome admin! We have a list of things you could do today");
        System.out.println("Please note that none of these commands are case or space sensitive, if you wish to:");
        System.out.println("See teaching requirements, type: 'show teaching requirements'");
        System.out.println("See suitable staff for a particular course, type: 'show suitable staff'");
        System.out.println("Assign a teacher to a course, type: 'assign a teacher'");
        System.out.println("See all assigned teaching requirements, type: 'see assigned teaching requirements'");
        System.out.println("Edit existing teaching requirements, type: 'edit teaching requirements'");
        System.out.println("Stop the program completely, type: 'stop'" + "\n");
        System.out.println("If you want to see these options again, type: 'help");
    }

}


