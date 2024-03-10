package softwareEngineering;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class Controller {
    private DataHandler dataHandler;
    private View view ;
    private Scanner scanner;

    public Controller(DataHandler dataHandler, View view) {
        this.dataHandler = dataHandler;
        this.view = view;
        this.scanner = new Scanner(System.in);
    }

    public DataHandler getDataHandler() {
        return dataHandler;
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
        System.out.println("See all assigned teaching requirements, type: 'see assigned teaching requirements'");
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
                showMessage(getDataHandler().getAllTeachingRequirements());
                return 1;
            case "showsuitablestaff":
                showSuitableStaff();
                return 1;
            case "assignateacher":
                assignTeacher();
                return 1;
            case "seeassignedteachingrequirements":
                showTeachingRequirementsSated();
                return 1;
            case "editteachingrequirements":
                editTeachingRequirements();
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
        String course = getScanner().nextLine().toLowerCase().replace(" ", "");;
        if (course.equals("stop")) {
            return;
        }
        Course courseFound = null;
        for (Course courseObject : getDataHandler().getAllTeachingRequirements()) {
            String courseName = courseObject.getCourseName().toLowerCase().replace(" ", "");
//            System.out.println("Comparing " + course + " to " + courseName);
            if (course.equals(courseName)) {
                courseFound = courseObject;
            }
        }
        if (courseFound == null) {
            System.out.println("That is not a valid course name");
            showSuitableStaff();
            return;
        }
        showMessage(getDataHandler().getSuitableTeachers(courseFound));
    }
    private void showSuitableStaff(String courseInput) {
        List<Teacher> staffToReturn = new ArrayList<>();
//            System.out.println("Comparing " + course + " to " + courseName);
                List<Teacher> teachers = getDataHandler().getAllTeachers();
                for (Teacher teacher : teachers) {
//                    System.out.println("iterating through teachers");
                    for (Course teacherCourse : teacher.getCourses()) {
//                        System.out.println("\n" + teacher.getTeacherName() + " teaches " + teacherCourse.getCourseName());
                        if (teacherCourse.getCourseName().toLowerCase().replace(" ", "").equals(courseInput)) {
                            staffToReturn.add(teacher);
//                            System.out.println("adding " + teacher.getTeacherName() + " to to return");
                        }
                    }
                }
        showMessage(staffToReturn);
    }

    public void assignTeacher() {
        System.out.println("Please specify the course or write 'stop' to break out of this loop");
        String course = getScanner().nextLine().toLowerCase().replace(" ", "");
        String courseCapitalised = course.substring(0, 1).toUpperCase() + course.substring(1);

        if (course.equals("stop")) {
            return;
        }
        boolean courseValid = false;
        Course courseToAssign = null;
        for (Course indivCourse : getDataHandler().getAllTeachingRequirements()) {
            if (indivCourse.getCourseName().toLowerCase().replace(" ", "").equals(course)) {
                courseValid = true;
                courseToAssign = indivCourse;
            }
        }
        if (!courseValid) {
            System.out.println("Course name isn't in the list of courses, please try again");
            assignTeacher();
            return;
        }

        System.out.println("Teachers who can teach " + courseCapitalised + " are as follows:");
        showSuitableStaff(course);
        System.out.println("Which teacher would you like to assign to teach " + courseCapitalised + "?");
        String teacherName = getScanner().nextLine().toLowerCase().replace(" ", "");

        int isSuccess = -1;
        for (Teacher teacher : getDataHandler().getAllTeachers()) {
            int teacherMaxCourseNumber = teacher.getAvailableCourse();
            int teacherCurrentCourseNumber = getDataHandler().getTeacherAssignedCourses(teacher).size();
            if (teacher.getTeacherName().toLowerCase().replace(" ", "").equals(teacherName) && (teacherCurrentCourseNumber < teacherMaxCourseNumber)) {
                isSuccess = getDataHandler().assignTeacher(teacher, courseToAssign);
//                System.out.println("The assignment is " + (isSuccess == 0 ? "successful" : "unsuccessful"));
            }
        }
    }

    public void showTeachingRequirementsSated() {
        for (Map<Teacher, Course> map : getDataHandler().getAllAssignments()) {
            List<Map.Entry<Teacher, Course>> mapList = new ArrayList<Map.Entry<Teacher, Course>>(map.entrySet());
            Teacher tt = mapList.get(0).getKey();
            Course cc = mapList.get(0).getValue();
            view.displayText(String.format("\t - %s, %s", tt.getTeacherName(), cc.getCourseName()));
//            System.out.println(String.format("\t - %s, %s", tt.getTeacherName(), cc.getCourseName()));
        }
    }

    public void editTeachingRequirements() {
        Teacher teacherToChange = null;
        Course courseToChange = null;
        System.out.println("Do you want to change the 'teacher' or 'course'? Type stop to break loop");
        String input = getScanner().nextLine().toLowerCase().replace(" ", "");

        if (input.equals("stop")) {
            return;
        }

        if (input.equals("teacher")) {
            for (Teacher teacher : getDataHandler().getAllTeachers()) {
                System.out.println(teacher.getTeacherName());
            }
            System.out.println("What teacher do you want to change?");
            String teacherInput = getScanner().nextLine().toLowerCase().replace(" ", "");
            for (Teacher teacher : getDataHandler().getAllTeachers()) {
//                System.out.println("Comparing " + teacher.getTeacherName().toLowerCase().replace(" ", "") + " to " + teacherInput);
                if (teacher.getTeacherName().toLowerCase().replace(" ", "").equals(teacherInput)) {
                    teacherToChange = teacher;
                    System.out.println("Teacher name is valid, here is this teacher's currently assigned course(s):");
                    for (Course course : getDataHandler().getTeacherAssignedCourses(teacher)) {
                        System.out.println(course.getCourseName());
                    }
                    System.out.println("Do you wish to add a course, or remove a course from this list?");
                    String addOrRemove = getScanner().nextLine().toLowerCase().replace(" ", "");
                    System.out.println("Which course?");
                    String courseToAlter = getScanner().nextLine().toLowerCase().replace(" ", "");
                    boolean courseValid = false;
                    for (Course course : getDataHandler().getAllTeachingRequirements()) {
                        if (course.getCourseName().toLowerCase().replace(" ", "").equals(courseToAlter)) {
                            courseToChange = course;
                            courseValid = true;
                            System.out.println("course is valid");
                        }
                    }
                    if (courseValid) {
                        if (addOrRemove.equals("add")) {
                            System.out.println("Assigning teacher...");
                            getDataHandler().assignTeacher(teacherToChange, courseToChange);
                            return;
                        } else if (addOrRemove.equals("remove")) {
                            System.out.println("Unassigning teacher...");
                            getDataHandler().deleteAssignment(teacherToChange, courseToChange);
                            return;
                        }
                    } else {
                        System.out.println("Course is not valid, restarting process");
                        editTeachingRequirements();
                        return;
                    }
                }

            }

            if (teacherToChange == null ) {
                System.out.println("Teacher name is not valid, restarting process");

                editTeachingRequirements();
                return;
            }
        }
        if (input.equals("course")) {
            System.out.println("What course do you want to change?");
            String courseInput = getScanner().nextLine().toLowerCase().replace(" ", "");
            System.out.println("User inputted " + courseInput + " unsure what to do with this atm");

        }
    }

    // Test method
    public static void main(String[] args) {
        View view = new View();

        DataHandler dataHandler = new DataHandler(new Config());
        Controller controller = new Controller(dataHandler, view);
        controller.startProgram();
    }

}
