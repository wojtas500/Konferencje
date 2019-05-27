package SQLhandling;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class QueryHandler
{
    public static void execute(String query)
    {
        //System.out.println(query);
        Statement stmt;
        try
        {
            Connection connection = DBConnection.GetInstance().getConnection();
            stmt = connection.createStatement();
            stmt.executeUpdate(query);
            stmt.close();
        }
        catch (SQLException e)
        {
            System.out.println("Incorrect query!");
            e.printStackTrace();
        }
    }
}

