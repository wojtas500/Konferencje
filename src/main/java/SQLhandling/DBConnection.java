package SQLhandling;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection
{
    private static DBConnection instance;
    private Connection connection;

    public Connection getConnection()
    {
        return connection;
    }

    public static DBConnection GetInstance() throws SQLException
    {
        if(instance == null || instance.getConnection().isClosed())
            instance = new DBConnection();
        return instance;
    }

    private DBConnection() throws SQLException
    {
        Logger logger = LogManager.getLogger(DBConnection.class);
        Properties properties=new Properties();

        try // try to read from file
        {
            properties.load(new FileInputStream("dbconnection.properties"));
        }
        catch (IOException e)   // no file exists
        {
            // log error
            logger.warn("No configuration file found. Loading default configuration");

            File f = new File("dbconnection.properties");   // create new file and load default configuration
            try
            {
                FileOutputStream output = new FileOutputStream(f);
                properties.setProperty("userName", "root");
                properties.setProperty("password", "");
                properties.setProperty("DBURL", "jdbc:mysql://127.0.0.1:55555/conferences?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift" +
                        "=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
                properties.store(output, "Database connection paramaters");
            }
            catch (IOException el)
            {
                el.printStackTrace();
            }
        }

        String DBURL    = (String) properties.get("DBURL");
        String userName = (String) properties.get("userName");
        String password = (String) properties.get("password");

        this.connection = DriverManager.getConnection(DBURL, userName, password);

        logger.trace("Connection parameters loaded properly");
    }
}