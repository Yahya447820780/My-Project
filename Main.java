import java.util.ArrayList;
import java.util.Scanner;

abstract class Student {
    private String name;
    private String id;
    private double[] marks;
    private int numCourses;

    public Student(String name, String id, int numCourses) {
        this.name = name;
        this.id = id;
        this.numCourses = numCourses;
        this.marks = new double[numCourses];
    }

    public String getName() { return name; }
    public String getId() { return id; }
    public double[] getMarks() { return marks; }
    public int getNumCourses() { return numCourses; }

    public void setMark(int index, double mark) {
        if (index >= 0 && index < numCourses && mark >= 0 && mark <= 100)
            marks[index] = mark;
    }

    public double getAverage() {
        double total = 0;
        for (double m : marks) total += m;
        return total / numCourses;
    }

    public abstract double getPassingGrade();

    public String getType() {
        return this instanceof GraduateStudent ? "Graduate" : "Undergraduate";
    }
}

class UndergraduateStudent extends Student {
    public UndergraduateStudent(String name, String id, int numCourses) {
        super(name, id, numCourses);
    }
    public double getPassingGrade() { return 60.0; }
}

class GraduateStudent extends Student {
    public GraduateStudent(String name, String id, int numCourses) {
        super(name, id, numCourses);
    }
    public double getPassingGrade() { return 80.0; }
}

public class Main {

    static Scanner sc = new Scanner(System.in);
    static ArrayList<Student> students = new ArrayList<>();

    public static void main(String[] args) {
        int choice = 0;
        while (choice != 6) {
            printMenu();
            System.out.print("Enter your choice: ");
            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.\n");
                continue;
            }
            switch (choice) {
                case 1: addStudent(); break;
                case 2: enterMarks(); break;
                case 3: displayMarksInfo(); break;
                case 4: displayStudentDetails(); break;
                case 5: modifyMark(); break;
                case 6: System.out.println("Goodbye!"); break;
                default: System.out.println("Invalid choice.\n");
            }
        }
    }

    static void printMenu() {
        System.out.println("Menu:");
        System.out.println("1. Add Student");
        System.out.println("2. Enter Marks");
        System.out.println("3. Display Marks Info");
        System.out.println("4. Display Student Details");
        System.out.println("5. Modify Mark");
        System.out.println("6. Exit");
    }

    static void addStudent() {
        System.out.print("Enter Student Name: ");
        String name = toTitleCase(sc.nextLine().trim());

        String id;
        while (true) {
            System.out.print("Enter ID (9 digits starting with 4): ");
            id = sc.nextLine().trim();
            if (id.matches("4\\d{8}")) break;
            else System.out.println("Invalid ID.");
        }

        System.out.print("Type (1=Undergraduate, 2=Graduate): ");
        String type = sc.nextLine().trim();

        int courses;
        while (true) {
            try {
                System.out.print("Number of courses (1-5): ");
                courses = Integer.parseInt(sc.nextLine().trim());
                if (courses >= 1 && courses <= 5) break;
                else System.out.println("Must be 1-5.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
            }
        }

        Student s = type.equals("2")
            ? new GraduateStudent(name, id, courses)
            : new UndergraduateStudent(name, id, courses);

        students.add(s);
        System.out.println("Student added: " + name + "\n");
    }

    static void enterMarks() {
        Student s = selectStudent();
        if (s == null) return;System.out.println("Enter " + s.getNumCourses() + " marks:");
        for (int i = 0; i < s.getNumCourses(); i++) {
            while (true) {
                try {
                    System.out.print("  Mark " + (i+1) + ": ");
                    double m = Double.parseDouble(sc.nextLine().trim());
                    if (m >= 0 && m <= 100) { s.setMark(i, m); break; }
                    else System.out.println("  Must be 0-100.");
                } catch (NumberFormatException e) {
                    System.out.println("  Invalid input.");
                }
            }
        }
        System.out.println("Marks entered.\n");
    }

    static void displayMarksInfo() {
        Student s = selectStudent();
        if (s == null) return;
        double[] marks = s.getMarks();
        double max = marks[0], min = marks[0];
        for (double m : marks) {
            if (m > max) max = m;
            if (m < min) min = m;
        }
        System.out.printf("Avg: %.1f | Pass Grade: %.0f%%%n", s.getAverage(), s.getPassingGrade());
        System.out.println("===========================");
        System.out.printf("%-8s %-8s %-8s %-8s%n","Course","Mark","P/F","Note");
        System.out.println("===========================");
        for (int i = 0; i < s.getNumCourses(); i++) {
            String pf = marks[i] >= s.getPassingGrade() ? "Pass" : "Fail";
            String note = (marks[i]==max && marks[i]==min) ? "Max/Min"
                        : marks[i]==max ? "Max" : marks[i]==min ? "Min" : "--";
            System.out.printf("%-8d %-8.1f %-8s %-8s%n", (i+1), marks[i], pf, note);
        }
        System.out.println("===========================\n");
    }

    static void displayStudentDetails() {
        Student s = selectStudent();
        if (s == null) return;
        System.out.println("Name:   " + s.getName());
        System.out.println("ID:     " + s.getId());
        System.out.println("Type:   " + s.getType());
        System.out.printf("Avg:    %.1f%n%n", s.getAverage());
    }

    static void modifyMark() {
        Student s = selectStudent();
        if (s == null) return;
        try {
            System.out.print("Enter course number to modify (1-" + s.getNumCourses() + "): ");
            int idx = Integer.parseInt(sc.nextLine().trim()) - 1;
            System.out.print("Enter new mark: ");
            double newMark = Double.parseDouble(sc.nextLine().trim());
            if (newMark < 0 || newMark > 100) throw new IllegalArgumentException("Mark out of range.");
            s.setMark(idx, newMark);
            System.out.println("Mark updated.\n");
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.\n");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage() + "\n");
        }
    }

    static Student selectStudent() {
        if (students.isEmpty()) {
            System.out.println("No students added yet.\n");
            return null;
        }
        System.out.println("Select student:");
        for (int i = 0; i < students.size(); i++)
            System.out.println("  " + (i+1) + ". " + students.get(i).getName());
        try {
            int idx = Integer.parseInt(sc.nextLine().trim()) - 1;
            if (idx >= 0 && idx < students.size()) return students.get(idx);
        } catch (NumberFormatException e) {}
        System.out.println("Invalid selection.\n");
        return null;
    }

    static String toTitleCase(String input) {
        String[] words = input.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String w : words) {
            if (w.length() > 0) {
                sb.append(Character.toUpperCase(w.charAt(0)));
                sb.append(w.substring(1).toLowerCase()).append(" ");
            }
        }
        return sb.toString().trim();
    }
}
