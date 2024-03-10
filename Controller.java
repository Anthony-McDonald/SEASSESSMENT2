package softwareEngineering;

import softwareEngineering.DatabaseHandler;
import softwareEngineering.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Controller {
    private DatabaseHandler databaseHandler;
    private View view ;
    private Scanner scanner;

    public Controller(DatabaseHandler databaseHandler, View view) {
        this.databaseHandler = databaseHandler;
        this.view = view;
        this.scanner = new Scanner(System.in);
    }

    public DatabaseHandler getDatabaseHandler() {
        return databaseHandler;
    }

    public View getView() {
        return view;
    }

    public Scanner getScanner() {
        return scanner;
    }

    private void showProgramCommands() {
        System.out.println("\n" + "Welcome admin! We have a list of things you could do today");
        System.out.println("Please note that none of these commands are case or space sensitive, if you wish to:");
        System.out.println("See teaching requirements, type: 'show teaching requirements'");
        System.out.println("See suitable staff for a particular course, type: 'show suitable staff'");
        System.out.println("Assign a teacher to a course, type: 'assign a teacher'");
        System.out.println("See all assigned teaching requirements, type: 'see teaching requirements'");
        System.out.println("Edit existing teaching requirements, type: 'edit teaching requirements'");
        System.out.println("Stop the program completely, type: 'stop'" + "\n");
        System.out.println("If you want to see these options again, type: 'help");
    }

    public void startProgram() {
        System.out.println("Program has started, beep beep boop. TEACHERINATOR 9000 IS ON THE CASE");
        showProgramCommands();
        // Keeps going until it reads a 'stop command'
        while (parseInput() != 0) {
        }
    }

    public int parseInput() {
        System.out.println("Enter your command:");
        String toParse = getScanner().nextLine().toLowerCase().replace(" ", "");
        switch (toParse) {
            case "stop":
                System.out.println("Stopping the program...");
                return 0;
            case "help":
                showProgramCommands();
                return 1;
            case "showteachingrequirements":
                showMessage(getDatabaseHandler().getAllTeachingRequirements());
                return 1;
            case "showsuitablestaff":
                showSuitableStaff();
                return 1;
            case "assignateacher":
                return 1;
            case "seeteachingrequirements":
                return 1;
            case "editteachingrequirements":
                return 1;
            default:
                System.out.println("DEFAULT CALLED, BREAKING");


        }

    return 1;
    }

    private void showMessage(List<?> list) {
        if (!list.isEmpty()) {
            if (list.get(0) instanceof Teacher) {
                view.displayTeachers((List<Teacher>) list);
            } else if (list.get(0) instanceof Course) {
                view.displayTeachingRequirements((List<Course>) list);
            }
        }

    }

    private void showMessage(Teacher teacher) {
        view.displayTeacher(teacher);
    }

    private void showSuitableStaff() {
        System.out.println("Please specify the course or write 'stop' to break out of this loop");
        List<Teacher> staffToReturn = new ArrayList<>();
        String course = getScanner().nextLine().toLowerCase().replace(" ", "");;
        if (course.equals("stop")) {
            return;
        }
        boolean courseFound = false;
        for (Course courseObject : getDatabaseHandler().getAllTeachingRequirements()) {
            String courseName = courseObject.getCourseName().toLowerCase().replace(" ", "");
//            System.out.println("Comparing " + course + " to " + courseName);
            if (course.equals(courseName)) {
                courseFound = true;
                List<Teacher> teachers = databaseHandler.getAllTeachers();
                for (Teacher teacher : teachers) {
//                    System.out.println("iterating through teachers");
                    for (Course teacherCourse : teacher.getCourses()) {
//                        System.out.println("\n" + teacher.getTeacherName() + " teaches " + teacherCourse.getCourseName());
                        if (teacherCourse.getCourseName().toLowerCase().replace(" ", "").equals(course)) {
                            staffToReturn.add(teacher);
//                            System.out.println("adding " + teacher.getTeacherName() + " to to return");
                        }
                    }
                }
            }
            }
        if (!courseFound) {
            System.out.println("That is not a valid course name");
            showSuitableStaff();
            return;
        }
        showMessage(staffToReturn);
    }

    // Test method
    public static void main(String[] args) {
        View view = new View();
        DatabaseHandler databaseHandler = new DatabaseHandler("database-teacher.txt", "database-teachingRequirment.txt");
        Controller controller = new Controller(databaseHandler, view);
        controller.startProgram();
    }

}
