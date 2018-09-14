package volo.heroku_volo;

import java.sql.Connection;
import java.sql.DriverManager;


public class pg {
	private static Connection pg = null;
	public static Connection pgConn(){
		if(pg!=null) {
			return pg;
		}
		try {
			Class.forName("org.postgresql.Driver");
		    String username = "vcauqapoeglqba";
		    String password = "f327482a2311368121fe323d8e9ba4d2110ba5995ffa52e021cbf80e341f17ce";
		    String dbUrl = "jdbc:postgresql://" + "ec2-54-83-29-34.compute-1.amazonaws.com" + ":" + "5432" + "/db318peca9jnl7?sslmode=require" ;

		    return DriverManager.getConnection(dbUrl, username, password);
		    
		} catch(Exception e) {
			e.printStackTrace();
		}
		return pg;
	}
}
