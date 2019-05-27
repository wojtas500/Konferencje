package SQLhandling;

import java.sql.*;
import java.util.ArrayList;

public class Selector
{
    public static ArrayList<ArrayList<String>> select(String query)
    {
        //System.out.println(query);
        ArrayList<ArrayList<String>> returned = new ArrayList<>();

        try
        {
            Connection connection = DBConnection.GetInstance().getConnection();
            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery(query);

            ResultSetMetaData metadata = rs.getMetaData();
            int columnCount = metadata.getColumnCount();

            while (rs.next())
            {
                ArrayList<String> row = new ArrayList<>(columnCount);
                int i = 1;
                while (i <= columnCount)
                {
                    row.add(rs.getString(i++));
                }
                returned.add(row);
            }
        }
        catch (SQLException e)
        {
            System.out.println("Incorrect query!");
            e.printStackTrace();
        }
        return returned;
    }
}
