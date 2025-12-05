package Serial;

import java.io.Serializable;
import java.time.LocalDate;

public class Employee implements Serializable {
    private String name;
    private double salary;
    private LocalDate hireday;
    
    public Employee(String name, double salary, int year, int month, int day) {
        this.name = name;
        this.salary = salary;
        this.hireday = LocalDate.of(year, month, day);
    }

    public String getName() { return this.name; }
    public double getSalary() { return this.salary; }
    public LocalDate getHireDay() { return this.hireday; }
    public void raiseSalary(double byPercent) { this.salary += this.salary * byPercent / 100; }
    public String toString() { return "[Employee] -> Name: %s, Salary = %.2f, HireDay: %s".formatted(this.name, this.salary, this.hireday); }
}
