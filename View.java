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


}


