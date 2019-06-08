
package konferencje;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Konferencje {

    static Connection conn = null;
    
    public static void main(String[] args) {
        // TODO code application logic here
    }
    public static Connection getConnection() throws SQLException {
		Properties ustawienia = new Properties();
		ustawienia.setProperty("userName","root");
		ustawienia.setProperty("password","");
		ustawienia.setProperty("dbms", "mysql");
		ustawienia.setProperty("portNumber", "55555");
		ustawienia.setProperty("connectionProps","konferencje");
		ustawienia.setProperty("serverName","127.0.0.1");
		try {
			File domyslne = new File("ustawienia.properties");
			if (domyslne.isFile()==false) {
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

	    return conn;
	    
	    
}
}
