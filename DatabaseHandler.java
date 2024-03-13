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

public class DatabaseHandler {
    private List<Teacher> teachers = new ArrayList<>();
    private List<Course> teachingRequirements = new ArrayList<>(); 
    private Map<String, Course> courseMap = new HashMap<>(); 

    public DatabaseHandler(String teachersFilePath, String coursesFilePath) {
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
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        List<String> lines = this.getLines(fileName);
        lines.add(content);
        String text = String.join("\n", lines);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
            bw.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> getLines(String fileName) {
        File file = new File(fileName);
        List<String> lines = new ArrayList<String>();
        // get all lines from the file
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            if (!file.exists()) {
                return null;
            }
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            return null;
        }
        return lines;
    }

    public void deleteLineInAssignmentFile(String content, String fileName) {
        List<String> lines = this.getLines(fileName);

        // loop through the list to find the line
        for (String line : lines) {
            if (line.equals(content)) {
                lines.remove(line);
                break;
            }
        }

        // write new text
        File file = new File(fileName);
        String text = String.join("\n", lines);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addAssignment(String fileName, Teacher teacher, Course course) {
        String line = this.formatAssignment(teacher, course);
        this.writeToAssignmentFile(line, fileName);
    }

    public List<Map<Teacher, Course>> getAllAssignments(String filePath) {
        List<Map<Teacher, Course>> result = new ArrayList<Map<Teacher, Course>>();
        List<String> lines = this.getLines(filePath);
        if (lines == null) {
            return result;
        }
        for (String line : lines) {
            String[] parts = line.replaceAll("[{}]", "").split("; ");
            if (parts.length < 2) {
                continue;
            }
            int teacherID = Integer.parseInt(parts[0].split(": ")[1]);
            int courseID = Integer.parseInt(parts[1].split(": ")[1]);

            Teacher teacher = this.findTeacherFromID(teacherID);
            Course course = this.findCourseFromID(courseID);
            Map<Teacher, Course> map = new HashMap<Teacher, Course>();
            map.put(teacher, course);
            result.add(map);
        }
        return result;
    }

    public void deleteAssignment(String fileName, Teacher teacher, Course course) {
        String line = this.formatAssignment(teacher, course);
        this.deleteLineInAssignmentFile(line, fileName);
    }

    private String formatAssignment(Teacher teacher, Course course) {
        return String.format("{teacherID: %d; courseID: %d}", teacher.getTeacherID(), course.getCourseID());
    }

    public Teacher findTeacherFromID(int id) {
        for (Teacher teacher : this.teachers) {
            if (teacher.getTeacherID() == id) {
                return teacher;
            }
        }
        return null;
    }

    public Course findCourseFromID(int id) {
        for (Course course : this.teachingRequirements) {
            if (course.getCourseID() == id) {
                return course;
            }
        }
        return null;
    }

    // Test method
    /*public static void main(String[] args) {
    	databaseHandler dbHandler = new databaseHandler("database-teacher.txt", "database-teachingRequirment.txt");
        //dbHandler.writeToAssignmentFile("New assignment data", "database-assignment.txt");
        
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
    }*/
}
