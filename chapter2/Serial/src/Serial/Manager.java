package Serial;

import java.io.Serializable;

public class Manager extends Employee implements Serializable {
    private Employee secretary;

    public Manager(String name, double salary, int year, int month, int day) {
        super(name, salary, year, month, day);
        this.secretary = null;
    }

    public double getSalary() { return super.getSalary(); }
    public Employee getSecretary() { return this.secretary; }
    public void setSecretary(Employee e) { this.secretary = e; }
    public String toString() { return "%s => [Manager] Secretary: %s".formatted(super.toString(), this.secretary.getName()); }
}
