package RandomAccess;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.LocalDate;

public class RandomAccessTest {
    public static void main(String[] args) throws IOException {
        Employee[] staff = new Employee[3];
        staff[0] = new Employee("misaka10000", 15810.32, 2023, 7, 5);
        staff[1] = new Employee("misaka10001", 20344.28, 2021, 5, 29);
        staff[2] = new Employee("misaka10002", 24088.15, 2024, 9, 19);

        try (DataOutputStream out = new DataOutputStream(new FileOutputStream("./output/employee.dat"))) {
            for (Employee e : staff) writeData(out, e);
        }

        try (RandomAccessFile in = new RandomAccessFile("./output/employee.dat", "r")) {
            int n = (int) (in.length() / Employee.RECORD_SIZE);
            Employee[] newStaff = new Employee[n];
            for (int i = n - 1; i >= 0; i--) {
                newStaff[i] = new Employee();
                in.seek(i * Employee.RECORD_SIZE);
                newStaff[i] = readData(in);
            }
            for (Employee e : newStaff) System.out.println(e);
        }
    }

    public static void writeData(DataOutput out, Employee e) throws IOException {
        DataIO.writeFixedString(e.getName(), Employee.NAME_SIZE, out);
        out.writeDouble(e.getSalary());

        LocalDate hireday = e.getHireDay();
        out.writeInt(hireday.getYear());
        out.writeInt(hireday.getMonthValue());
        out.writeInt(hireday.getDayOfMonth());
    }

    public static Employee readData(DataInput in) throws IOException {
        String name = DataIO.readFixedString(Employee.NAME_SIZE, in);
        double salary = in.readDouble();
        int y = in.readInt();
        int m = in.readInt();
        int d = in.readInt();
        return new Employee(name, salary, y, m, d);
    }
}