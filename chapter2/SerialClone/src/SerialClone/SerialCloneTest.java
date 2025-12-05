package SerialClone;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;

public class SerialCloneTest {
    public static void main(String[] args) throws CloneNotSupportedException {
        Employee harry = new Employee("Harry Hacker", 35000, 1989, 10, 1);
        Employee harry2 = (Employee) harry.clone();
        System.out.println(harry);
        System.out.println(harry2);
    }
}

class SerialCloneable implements Cloneable, Serializable {
    public Object clone() throws CloneNotSupportedException {
        try {
            ByteArrayOutputStream byteout = new ByteArrayOutputStream();
            try (ObjectOutputStream out = new ObjectOutputStream(byteout)) {
                out.writeObject(this);
            }
            try (ByteArrayInputStream bytein = new ByteArrayInputStream(byteout.toByteArray())) {
                ObjectInputStream in = new ObjectInputStream(bytein);
                return in.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            CloneNotSupportedException e2 = new CloneNotSupportedException();
            e2.initCause(e);
            throw e2;
        }
    }
}

class Employee extends SerialCloneable {
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
    public String toString() { return "%s => [Name: %s, Salary = %s, HireDay: %s]".formatted(getClass().getName(), this.name, this.salary, this.hireday); }
}
