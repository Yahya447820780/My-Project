import java.util.Scanner;

public class StudentMarks {

    static Scanner sc = new Scanner(System.in);
    static String studentName = "";
    static String studentID = "";
    static int numCourses = 0;
    static double[] marks;
    static boolean detailsEntered = false;
    static boolean marksEntered = false;

    public static void main(String[] args) {
        int choice = 0;
        while (choice != 5) {
            printMenu();
            System.out.print("Enter your choice: ");
            while (!sc.hasNextInt()) {
                System.out.println("Invalid input.");
                sc.next();
                System.out.print("Enter your choice: ");
            }
            choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1: enterStudentDetails(); break;
                case 2:
                    if (!detailsEntered) System.out.println("Enter details first.");
                    else { enterMarks(); displayMarksInfo(); }
                    break;
                case 3:
                    if (!marksEntered) System.out.println("Enter marks first.");
                    else displayMarksInfo();
                    break;
                case 4:
                    if (!detailsEntered) System.out.println("Enter details first.");
                    else displayStudentDetails();
                    break;
                case 5: System.out.println("Goodbye!"); break;
                default: System.out.println("Invalid choice. Please enter 1-5.");
            }
        }
    }

    static void printMenu() {
        System.out.println("Menu:");
        System.out.println("1. Enter Student Details");
        System.out.println("2. Enter Marks");
        System.out.println("3. Display Marks Info.");
        System.out.println("4. Display Student Details");
        System.out.println("5. Exit");
    }

    static void enterStudentDetails() {
        System.out.print("Enter Student Name: ");
        studentName = toTitleCase(sc.nextLine().trim());
        while (true) {
            System.out.print("Enter ID Number: ");
            studentID = sc.nextLine().trim();
            if (studentID.matches("4\\d{8}")) break;
            else System.out.println("Invalid ID. Must start with 4 and be 9 digits.");
        }
        while (true) {
            System.out.print("How many courses last semester? ");
            if (sc.hasNextInt()) {
                numCourses = sc.nextInt(); sc.nextLine();
                if (numCourses >= 1 && numCourses <= 5) break;
                else System.out.println("Courses must be between 1 and 5.");
            } else { System.out.println("Invalid input."); sc.nextLine(); }
        }
        marks = new double[numCourses];
        detailsEntered = true; marksEntered = false;
    }

    static void enterMarks() {
        System.out.println("Enter Marks for " + numCourses + " subjects:");
        for (int i = 0; i < numCourses; i++) {
            while (true) {
                System.out.print("  Mark " + (i+1) + ": ");
                if (sc.hasNextDouble()) {
                    double m = sc.nextDouble(); sc.nextLine();
                    if (m >= 0 && m <= 100) { marks[i] = m; break; }
                    else System.out.println("  Mark must be 0–100.");
                } else { System.out.println("  Invalid input."); sc.nextLine(); }
            }
        }
        marksEntered = true;
    }

    static void displayMarksInfo() {
        double total = 0, max = marks[0], min = marks[0];
        for (double m : marks) { total += m; if (m > max) max = m; if (m < min) min = m; }
        double avg = total / numCourses;
        System.out.printf("Avg: %.1f%n", avg);
        System.out.println("===========================");
        System.out.printf("%-8s %-8s %-8s %-8s%n", "Course", "Mark", "P/F", "Note");
        System.out.println("===========================");
        for (int i = 0; i < numCourses; i++) {
            String pf = marks[i] >= 60 ? "Pass" : "Fail";String note = (marks[i]==max && marks[i]==min) ? "Max/Min"
                        : marks[i]==max ? "Max" : marks[i]==min ? "Min" : "--";
            System.out.printf("%-8d %-8.1f %-8s %-8s%n", (i+1), marks[i], pf, note);
        }
        System.out.println("===========================");
    }

    static void displayStudentDetails() {
        System.out.println("Name:   " + studentName);
        System.out.println("KKU ID: " + studentID);
        if (marksEntered) displayMarksInfo();
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