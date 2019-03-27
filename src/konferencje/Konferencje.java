
package konferencje;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Konferencje
{
    static Connection conn = null;

	public static void main(String[] args)
	{
        // TODO code application logic here
		Connection connection = null;
		try
		{
			connection = getConnection();
		}
		catch (SQLException e)
		{
			System.out.println("Cannot connect do database");
			e.printStackTrace();
		}

		if(connection!=null)
			System.out.println("Connected to database");
	}

    public static Connection getConnection() throws SQLException {

		Properties ustawienia=new Properties();

		try // try to read from file
		{
			ustawienia.load(new FileInputStream("ustawienia.properties"));
		}
		catch (IOException e)   // no file exists
		{
			System.out.println("No configuration file found!");

			File f = new File("ustawienia.properties");   // create new file and load default configuration
			try
			{
				FileOutputStream output = new FileOutputStream(f);
				ustawienia.setProperty("userName", "root");
				ustawienia.setProperty("password", "");
				ustawienia.setProperty("DBURL", "jdbc:mysql://127.0.0.1:55555/konferencje?useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift" +
						"=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
				ustawienia.store(output, "Database connection paramaters");

				System.out.println("Created configuration file!");
			}
			catch (IOException el)
			{
				el.printStackTrace();
			}
		}

		String DBURL    = (String) ustawienia.get("DBURL");
		String userName = (String) ustawienia.get("userName");
		String password = (String) ustawienia.get("password");

		return DriverManager.getConnection(DBURL, userName, password);

		/*
		ustawienia.setProperty("userName","root");
		ustawienia.setProperty("password","");
		ustawienia.setProperty("dbms", "mysql");
		ustawienia.setProperty("portNumber", "55555");
		ustawienia.setProperty("connectionProps","konferencje");
		ustawienia.setProperty("serverName","127.0.0.1");

		try {
			File domyslne = new File("ustawienia.properties");
			if (domyslne.isFile()==false) {

				System.out.println("No config file");

				FileOutputStream out = new FileOutputStream("ustawienia.properties");
				ustawienia.storeToXML(out, null);
				out.close();
			}
			FileInputStream in = new FileInputStream("ustawienia.properties");
			ustawienia.loadFromXML(in);
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (conn == null) {
			conn = DriverManager.getConnection("jdbc:" + 
			ustawienia.getProperty("dbms") + "://" + ustawienia.getProperty("serverName") + ":" + 
			ustawienia.getProperty("portNumber") + "/" + ustawienia.getProperty("connectionProps"),"root",""); 
	    }

	    return conn;*/
	}
}
