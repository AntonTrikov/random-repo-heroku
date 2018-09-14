package volo.heroku_volo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import volo.heroku_volo.pg;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class MyResource {

    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getIt() {
    	PreparedStatement query = null;
		String returnString = null;
		Connection conn = null;
		try {
			conn = pg.pgConn();
			query = conn.prepareStatement("select * from persons;");
			ResultSet rs = query.executeQuery();
			returnString="";
			while(rs.next()) {
				String id = rs.getString("id");
				String firstName = rs.getString("firstName");
				String lastName = rs.getString("lastName");
				String sex = rs.getString("sex");
				returnString= returnString + "<p>Person id: "+id+". First Name: "+firstName+". Last name: "+lastName+". Sex: "+sex+"</p>";
			}
			query.close();
		} catch(Exception e) {
			e.printStackTrace();
			returnString = "<p> Errore nel database </p>"+"<p>"+e+"</p>";
		}finally {
			if(conn!=null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return returnString;
    }
}
