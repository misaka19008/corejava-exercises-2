package RandomAccess;

import java.time.LocalDate;

class Employee {
    private String name;
    private double salary;
    private LocalDate hireDay;
    public static final int NAME_SIZE = 40;
    public static final int RECORD_SIZE = 100;

    public Employee() {
        this.name = "N/A";
        this.salary = 0.0;
        this.hireDay = null;
    }

    public Employee(String name, double salary, int year, int month, int day) {
        this.name = name;
        this.salary = salary;
        this.hireDay = LocalDate.of(year, month, day);
    }

    public String getName() { return this.name; }
    public double getSalary() { return this.salary; }
    public LocalDate getHireDay() { return this.hireDay; }
    public String toString() { return "[Employee -> Name: %s, Salary = %.2f, HireDay: %s]".formatted(this.name, this.salary, this.hireDay); }
    
}