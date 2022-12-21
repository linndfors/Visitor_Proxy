package org.example;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PipedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.sql.*;

public class PDLReader {
    public static <SQL> void main(String[] args) throws IOException {
        String API_KEY = "03f8d36d258ec253eea3cf2de09d45ad085fcf7e489748adcd479e46f261737e";
        String my_domain = "newhomesource.com";
        try {
            Class.forName("org.sqlite.JDBC");
            String dbURL = "jdbc:sqlite:product.db";
            Connection conn = DriverManager.getConnection(dbURL);

            try {
                String sql = "create table domains (domain varchar(20), info varchar(20))";
                Statement statement = conn.createStatement();
                statement.executeUpdate(sql);
            }
            catch (Exception ex) {

            }

            PreparedStatement prepared = conn.prepareStatement("select domain, info from domains where domain=?;");
            prepared.setString(1, my_domain);
            ResultSet result = prepared.executeQuery();
            if(!result.isBeforeFirst()){
                System.out.println("No Data Found"); //data not exist
                System.out.println("Start parsing...");
                String query = URLEncoder.encode("SELECT NAME FROM COMPANY WHERE WEBSITE=" + "'" + my_domain + "'", StandardCharsets.UTF_8);
                URL url = new URL("https://api.peopledatalabs.com/v5/company/search?sql=" + query);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("X-Api-Key", API_KEY);
                connection.connect();
                String text = new Scanner(connection.getInputStream()).useDelimiter("\\Z").next();
                JSONObject jsonObject = new JSONObject(text);
                System.out.println(jsonObject);
                PreparedStatement statement = conn.prepareStatement("insert into domains values(?, ?);");
                statement.setString(1, my_domain);
                statement.setString(2, jsonObject.toString());
                statement.addBatch();
                conn.setAutoCommit(false);
                statement.executeBatch();
                conn.setAutoCommit(true);
            }
            else {
                System.out.println("data is already in base");
                while (result.next()) {
                    System.out.println(result.getString("info"));
                }
            }

        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
