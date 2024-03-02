package softwareEngineering;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class databaseHandler {
    private List<Teacher> teachers = new ArrayList<>();
    private List<Course> teachingRequirements = new ArrayList<>(); 
    private Map<String, Course> courseMap = new HashMap<>(); 

    public databaseHandler(String teachersFilePath, String coursesFilePath) {
        loadCoursesFromFile(coursesFilePath);
        loadTeachersFromFile(teachersFilePath);
    }

    private void loadTeachersFromFile(String filePath) {
    	try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.replaceAll("[{}]", "").split("; ");
                int teacherID = Integer.parseInt(parts[0].split(": ")[1]);
                String teacherName = parts[1].split(": ")[1];
                String[] courseNames = parts[2].split(": ")[1].split(", ");
                int availableCourse = Integer.parseInt(parts[3].split(": ")[1]);
                List<Course> courses = new ArrayList<>();
                for (String name : courseNames) {
                    if (courseMap.containsKey(name)) {
                        courses.add(courseMap.get(name));
                    } else {
                        System.out.println("Warning: Course " + name + " not found for teacher " + teacherName);
                    }
                }
                teachers.add(new Teacher(teacherID, teacherName, courses, availableCourse));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCoursesFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.replaceAll("[{}]", "").split("; ");
                int courseID = Integer.parseInt(parts[0].split(": ")[1]);
                String courseName = parts[1].split(": ")[1];
                int requiredTeachers = Integer.parseInt(parts[2].split(": ")[1]);
             // After creating the new Course object
                Course course = new Course(courseID, courseName, requiredTeachers);
                teachingRequirements.add(course);
                courseMap.put(courseName, course);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Teacher> getAllTeachers() {
        return teachers;
    }

    public List<Course> getAllTeachingRequirements() {
        return teachingRequirements;
    }

    public void writeToAssignmentFile(String content, String fileName) {
        File file = new File(fileName);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            if (!file.exists()) {
                file.createNewFile();
            }
            bw.write(content);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Test method
    public static void main(String[] args) {
    	databaseHandler dbHandler = new databaseHandler("C:\\Users\\HP\\Desktop\\database\\database-teacher.txt", "C:\\Users\\HP\\Desktop\\database\\database-teachingRequirment.txt");
        dbHandler.writeToAssignmentFile("New assignment data", "C:\\Users\\HP\\Desktop\\database\\database-assignment.txt");
        
        System.out.println("All Teachers:");
        List<Teacher> allTeachers = dbHandler.getAllTeachers();
        for (Teacher teacher : allTeachers) {
            System.out.println(teacher.toString());
        }


        System.out.println("\nAll Teaching Requirements:");
        List<Course> allCourses = dbHandler.getAllTeachingRequirements();
        for (Course course : allCourses) {
            System.out.println(course.toString());
        }
    }
}
