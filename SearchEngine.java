package project;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class SearchEngine {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/db"; //change the name of the database from 'db' to your liking 
    private static final String DB_USER = "userhttps://dev.mysql.com/downloads/connector/j/"; // update the string before 'https' , in this case it's user, but in your case u should change it 
    private static final String DB_PASSWORD = "password";   //chaneg the password here
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BOLD = "\u001B[1m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_YELLOW = "\u001B[33m";

    public Map<String, Site> search(String query) {
        Map<String, Site> result = new HashMap<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM test WHERE title LIKE ? OR description LIKE ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, "%" + query + "%");
            statement.setString(2, "%" + query + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                String link = resultSet.getString("link");
                Site site = new Site(title, description, link);
                result.put(link, site);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static void main(String[] args) {
        SearchEngine searchEngine = new SearchEngine();
        Scanner scanner = new Scanner(System.in);

        try {
            while (true) {
                System.out.print("Enter your search query (Ctrl+C to exit): ");
                String query = scanner.nextLine();

                Map<String, Site> searchResults = searchEngine.search(query);

                if (searchResults.isEmpty()) {
                    System.out.println("No results found.");
                } else {
                    System.out.println("Search results for '" + query + "':");
                    for (Site site : searchResults.values()) {
                        System.out.println(ANSI_BOLD + "Title: " + ANSI_RESET + site.getTitle());
                        System.out.println(ANSI_CYAN + "Description: " + ANSI_RESET + site.getDescription());
                        System.out.println(ANSI_YELLOW + "Link: " + ANSI_RESET + site.getLink());
                        System.out.println();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Search Engine stopped.");
        } finally {
            scanner.close();
        }
    }

    static class Site {
        private String title;
        private String description;
        private String link;

        public Site(String title, String description, String link) {
            this.title = title;
            this.description = description;
            this.link = link;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getLink() {
            return link;
        }
    }
}
