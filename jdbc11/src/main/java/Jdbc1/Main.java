package Jdbc1;

import java.sql.*;
import java.util.Random;
import java.util.Scanner;

public class Main {
    // CREATE DATABASE mydb;
    static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/estate?serverTimezone=Europe/Kiev";
    static final String DB_USER = "root";
    static final String DB_PASSWORD = "Chuharkin94";

    static final String SQL_GETALL = "SELECT * FROM flats";
    static final String SQL_GETONEBED = "select * from flats where bed = '1'";
    static final String SQL_GETTWOBED = "select * from flats where bed = '2'";
    static final String SQL_GETPRICELESS = "select * from flats where price <= '25000'";
    static final String SQL_GETPRICEBETWEEN = "select * from flats where price >= '25000' && price <= '30000'";

    static Connection conn;

    public static void main(String[] args) throws SQLException {

        try (Scanner sc = new Scanner(System.in)) {
            conn = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
            initDB();
            addFlat("Saltov", "Praci 52", "53", "2","23500");
            addFlat("Soln", "Ukrain 14", "84", "3","26800");
            addFlat("Zalut", "Kropiv 56", "35", "1","20100");
            addFlat("Komin", "Peremogy 22", "57", "2","24600");
            addFlat("Indust", "Nezlam 18", "61", "3","26800");
            addFlat("Komin", "Naukov 69", "36", "1","24000");
            addFlat("Zalut", "Stadio 12", "49", "2","25450");
            addFlat("Saltov", "Uzhviy 97", "53", "2","24800");
            addFlat("Indust", "Heroiv 51", "72", "3","31150");
            addFlat("Soln", "Yarosha 44", "33", "1","22650");
            addFlat("Saltov", "Troley 35", "29", "1","25100");

            while( true ) {
                System.out.println("1: view all flats");
                System.out.println("2: 1 bedroom");
                System.out.println("3: 2 bedroom");
                System.out.println("4: price until 25000$");
                System.out.println("5: price from 25000$ to 30000$");
                System.out.print("-> ");

                String s = sc.nextLine();
                switch (s) {
                    case "1":
                        viewFlats(SQL_GETALL);
                        break;
                    case "2":
                        viewFlats(SQL_GETONEBED);
                        break;
                    case "3":
                        viewFlats(SQL_GETTWOBED);
                        break;
                    case "4":
                        viewFlats(SQL_GETPRICELESS);
                        break;
                    case "5":
                        viewFlats(SQL_GETPRICEBETWEEN);
                        break;
                    default:
                        return;
                }
            }
        } finally {
            if (conn != null) conn.close();
        }
    }

    private static void initDB() throws SQLException {
        Statement st = conn.createStatement();
        try {
            st.execute("DROP TABLE IF EXISTS flats");
            st.execute("CREATE TABLE flats (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "district VARCHAR(128) default null," +
                    "address VARCHAR(128) default NULL," +
                    "m2 VARCHAR(128) default NULL," +
                    "bed VARCHAR(128) default null," +
                    "price varchar(128) default null)");
        } finally {
            st.close();
        }
    }

    private static void viewFlats( String req ) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(req);
        try {
            // table of data representing a database result set,
            // ps.setFetchSize(100);
            ResultSet rs = ps.executeQuery();

            try {
                // can be used to get information about the types and properties of the columns in a ResultSet object
                ResultSetMetaData md = rs.getMetaData();

                for (int i = 1; i <= md.getColumnCount(); i++)
                    System.out.print(md.getColumnName(i) + "\t\t\t\t\t\t\t");
                System.out.println();

                while (rs.next()) {
                    for (int i = 1; i <= md.getColumnCount(); i++) {
                        System.out.print(rs.getString(i) + "\t\t\t\t\t\t\t");
                    }
                    System.out.println();
                }
            } finally {
                rs.close(); // rs can't be null according to the docs
            }
        } finally {
            ps.close();
        }
    }

    private static void addFlat( String district, String address, String area, String bed, String price ) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("INSERT INTO flats (district, address, m2, bed, price) VALUES(?, ?, ?, ?, ?)");
        try {
            ps.setString(1, district);
            ps.setString(2, address);
            ps.setString(3, area);
            ps.setString(4, bed);
            ps.setString(5, price);
            ps.executeUpdate(); // for INSERT, UPDATE & DELETE
        } finally {
            ps.close();
        }
    }


}
