package TextFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Scanner;

public class TextFileTest {
    public static void main(String[] args) throws IOException {
        var staff = new Employee[3];
        staff[0] = new Employee("misaka15063", 27608.52, 2021, 5, 9);
        staff[1] = new Employee("misaka15064", 28000.50, 2021, 6, 17);
        staff[2] = new Employee("misaka15065", 21500.00, 2021, 4, 23);

        try (PrintWriter out = new PrintWriter("./output/employee_data.dat", StandardCharsets.UTF_8)) {
            writeData(staff, out);
        }

        try (Scanner in = new Scanner(new FileInputStream("./output/employee_data.dat"), "UTF-8")) {
            Employee[] newstaff = readData(in);
            for (Employee e : newstaff) System.out.println(e);
        }
    }

    private static void writeData(Employee[] employees, PrintWriter out) throws IOException {
        out.println(employees.length);
        for (Employee e : employees) writeEmployee(out, e);
    }

    private static Employee[] readData(Scanner in) {
        int n = in.nextInt();
        in.nextLine();
        var employees = new Employee[n];
        for (int i = 0; i < n; i++) employees[i] = readEmployee(in);
        return employees;
    }

    public static void writeEmployee(PrintWriter out, Employee e) {
        out.println("%s|%s|%s".formatted(e.getName(), e.getSalary(), e.getHireDay()));
    }

    public static Employee readEmployee(Scanner in) {
        String line = in.nextLine();
        String[] tokens = line.split("\\|");
        String name = tokens[0];
        double salary = Double.parseDouble(tokens[1]);
        LocalDate hireday = LocalDate.parse(tokens[2]);
        int year = hireday.getYear();
        int month = hireday.getMonthValue();
        int day = hireday.getDayOfMonth();
        return new Employee(name, salary, year, month, day);
    }
}

class Employee {
    private String name;
    private double salary;
    private LocalDate hireDay;

    public Employee(String name, double salary, int year, int month, int day) {
        this.name = name;
        this.salary = salary;
        this.hireDay = LocalDate.of(year, month, day);
    }

    public String getName() { return this.name; }
    public double getSalary() { return this.salary; }
    public LocalDate getHireDay() { return this.hireDay; }
    public void raiseSalary(double byPercent) { this.salary += salary * byPercent / 100; }
    
}