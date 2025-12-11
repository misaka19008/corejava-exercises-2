package Query;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class QueryTest {
    private static Scanner in;
    private static ArrayList<String> authors = new ArrayList<>();
    private static ArrayList<String> publishers = new ArrayList<>();

    private static final String allQuery = "SELECT Price, Title FROM Books";
    private static final String priceUpdate = """
           UPDATE Books SET Price = ? WHERE Publisher_ID = (
                SELECT Publisher_ID FROM Publishers WHERE Name = ?
           )       
    """;
    private static final String publisherQuery = """
           SELECT Books.Price, Books.Title FROM Books, Publishers
           WHERE Books.Publisher_ID = Publishers.Publisher_ID
             AND Publishers.Name = ?        
    """;
    private static final String authorQuery = """
           SELECT Books.Price, Books.Title FROM Books, BooksAuthors, Authors
           WHERE Authors.Author_ID = BooksAuthors.Author_ID
             AND BooksAuthors.ISBN = Books.ISBN
             AND Authors.Name = ?        
    """;
    private static final String authorPublisherQuery = """
           SELECT Books.Price, Books.Title
           FROM Books, BooksAuthors, Authors, Publishers
           WHERE Authors.Author_ID = BooksAuthors.Author_ID
             AND BooksAuthors.ISBN = Books.ISBN
             AND Books.Publisher_ID = Publishers.Publisher_ID
             AND Authors.Name = ?
             AND Publishers.Name = ?
    """;

    public static void main(String[] args) throws IOException {
        try (Connection conn = getConnection()) {
            in = new Scanner(System.in);
            authors.add("Any");
            publishers.add("Any");
            try (Statement stat = conn.createStatement()) {
                String query = "SELECT Name FROM Authors";
                try (ResultSet rs = stat.executeQuery(query)) {
                    while (rs.next()) authors.add(rs.getString(1));
                }

                query = "SELECT Name FROM Publishers";
                try (ResultSet rs = stat.executeQuery(query)) {
                    while (rs.next()) publishers.add(rs.getString(1));
                }
            }

            boolean done = false;
            while (!done) {
                System.out.print("Q)uery C)hange prices E)xit: ");
                String input = in.next().toUpperCase();
                if (input.equals("Q")) executeQuery(conn);
                else if (input.equals("C")) changePrices(conn);
                else done = true;
            }
        } catch (SQLException e) {
            for (Throwable t : e) System.out.println(t.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException, IOException {
        Properties props = new Properties();
        try (Reader in = Files.newBufferedReader(Path.of("database.properties"), StandardCharsets.UTF_8)) {
            props.load(in);
        }

        String drivers = props.getProperty("jdbc.drivers");
        if (drivers != null) System.setProperty("jdbc.drivers", drivers);
        String jdbc_url = props.getProperty("jdbc.url");
        String username = props.getProperty("jdbc.username");
        String password = props.getProperty("jdbc.password");

        return DriverManager.getConnection(jdbc_url, username, password);
    }

    public static String select(String prompt, List<String> options) {
        while (true) {
            System.out.println(prompt);
            for (int i = 0; i < options.size(); i++) System.out.println("%2d) %s".formatted(i + 1, options.get(i)));
            int sel = in.nextInt();
            if (sel > 0 && sel <= options.size()) return options.get(sel - 1);
        }
    }

    public static void changePrices(Connection conn) throws SQLException {
        String publisher = select("Publishers: ", publishers.subList(1, publishers.size()));
        System.out.print("Change price by: ");
        double priceChange = in.nextDouble();
        PreparedStatement stat = conn.prepareStatement(priceUpdate);
        stat.setDouble(1, priceChange);
        stat.setString(2, publisher);
        int r = stat.executeUpdate();
        System.out.println("%d records updated.".formatted(r));
    }

    private static void executeQuery(Connection conn) throws SQLException {
        String author = select("Authors: ", authors);
        String publisher = select("Publishers: ", publishers);
        PreparedStatement stat;

        if (!author.equals("Any") && !publisher.equals("Any")) {
            stat = conn.prepareStatement(authorPublisherQuery);
            stat.setString(1, author);
            stat.setString(2, publisher);
        } else if (!author.equals("Any") && publisher.equals("Any")) {
            stat = conn.prepareStatement(authorQuery);
            stat.setString(1, author);
        } else if (author.equals("Any") && !publisher.equals("Any")) {
            stat = conn.prepareStatement(publisherQuery);
            stat.setString(1, publisher);
        } else stat = conn.prepareStatement(allQuery);

        try (ResultSet rs = stat.executeQuery()) {
            while (rs.next()) System.out.println("%s, %s".formatted(rs.getString(1), rs.getString(2)));
        }
    }
}
