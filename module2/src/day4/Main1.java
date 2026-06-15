package day4;

import java.sql.*;

public class Main1 {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/northern";
        String user = "postgres";
        String password = "12345";

        try(Connection conn = DriverManager.getConnection(url, user, password)){
            System.out.println("Connected");


            String createTable = "CREATE TABLE IF NOT EXISTS Person (" +
                    "id SERIAL PRIMARY KEY, " +
                    "fname VARCHAR(50), " +
                    "lname VARCHAR(50), " +
                    "age INT)";
            Statement stmt = conn.createStatement();
            stmt.execute(createTable);
            System.out.println("Table created!");



            String insertSQL = "INSERT INTO Person(fname, lname, age) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(insertSQL);

            ps.setString(1, "John");
            ps.setString(2, "Doe");
            ps.setInt(3, 30);

            ps.executeUpdate();
            System.out.println("Record inserted!");


            String selectSQL = "SELECT * FROM Person";
            Statement s = conn.createStatement();
            ResultSet rs = s.executeQuery(selectSQL);

            while (rs.next()) {
                int id = rs.getInt("id");
                String fname = rs.getString("fname");
                String lname = rs.getString("lname");
                int age = rs.getInt("age");

                System.out.println(id + " " + fname + " " + lname + " " + age);
            }


            String sql = "SELECT * FROM Person WHERE age > ?";
            PreparedStatement ps2 = conn.prepareStatement(sql);
            ps2.setInt(1, 20);

            ResultSet rs2 = ps2.executeQuery();
            while (rs2.next()) {
                System.out.println(rs2.getString("fname"));
            }


            String updateSQL = "UPDATE Person SET age = ? WHERE fname = ?";
            PreparedStatement ps3 = conn.prepareStatement(updateSQL);

            ps3.setInt(1, 35);
            ps3.setString(2, "Virat");

            int rows = ps3.executeUpdate();
            System.out.println("Rows updated: " + rows);


            String deleteSQL = "DELETE FROM Person WHERE fname = ?";
            PreparedStatement ps4 = conn.prepareStatement(deleteSQL);

            ps4.setString(1, "Hritik");

            int deleted = ps4.executeUpdate();
            System.out.println("Rows deleted: " + deleted);


            String countSQL = "SELECT COUNT(*) FROM Person";
            Statement st = conn.createStatement();
            ResultSet rs3 = st.executeQuery(countSQL);

            if (rs3.next()) {
                System.out.println("Total rows: " + rs3.getInt(1));
            }



            String orderSQL = "SELECT * FROM Person ORDER BY age DESC";
            ResultSet rs4 = stmt.executeQuery(orderSQL);

            while (rs4.next()) {
                System.out.println(rs4.getString("fname") + " - " + rs4.getInt("age"));
            }


            String groupSQL = "SELECT lname, COUNT(*) FROM Person GROUP BY lname";
            ResultSet rs5 = stmt.executeQuery(groupSQL);

            while (rs5.next()) {
                System.out.println(rs5.getString(1) + " -> " + rs5.getInt(2));
            }




            String likeSQL = "SELECT * FROM Person WHERE fname LIKE ?";
            PreparedStatement ps5 = conn.prepareStatement(likeSQL);

            ps5.setString(1, "K%");

            ResultSet rs6 = ps5.executeQuery();
            while (rs6.next()) {
                System.out.println(rs6.getString("fname"));
            }




            conn.setAutoCommit(false);

            try {
                PreparedStatement p1 = conn.prepareStatement(
                        "INSERT INTO Person(fname, lname, age) VALUES (?, ?, ?)");
                p1.setString(1, "Test");
                p1.setString(2, "User");
                p1.setInt(3, 99);
                p1.executeUpdate();

                conn.commit(); // save changes
            } catch (Exception e) {
                conn.rollback(); // undo if error
            }


        }catch(SQLException e){
            System.err.println("Failed");
            e.printStackTrace();
        }
    }
}
