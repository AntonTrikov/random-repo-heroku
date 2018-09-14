package volo.heroku_volo;


import java.io.StringReader;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBElement;

import javax.ws.rs.core.UriInfo;
import javax.json.*;
import javax.json.JsonReader;
import java.sql.*;

import volo.heroku_volo.Person;
import volo.heroku_volo.pg;

@Path("/person")
public class MyResource {
	/*@POST
	public Response createCustomer(String is) {
		Person person = readPerson(is);
		String id = readId(is);
        PersonDao.instance.getModel().put(id, person);
		return Response.created(URI.create("person/" + id)).build();
	}
	*/
	/*@GET
	@Path("{id}")
    public Response getPerson(@PathParam("id") String id) {
        Person person = PersonDao.instance.getModel().get(id);
        if(person==null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }else{
        	return Response.ok(person.toJson()).build();
        }
    }
	*/
	/*
	@PUT
    public Response putPerson(String is) {
        Person person = readPerson(is);
        return putAndGetResponse(person);
    }
	*/
	@DELETE
	@Path("{id}")
    public Response deletePerson(@PathParam("id") String id) throws Exception{
		PreparedStatement query = null;
		Response response=null;
		Connection conn = null;
		try {
			conn = pg.pgConn();
			String q = "select count(*) from persons where id="+id;
			query = conn.prepareStatement(q);
			ResultSet rs = query.executeQuery();
			int rowcount = 0;
			while(rs.next()) {
				rowcount=rs.getInt("count");
			}
			if(rowcount==0) {
				return Response.status(Response.Status.NOT_FOUND).build();
			}
			q = "delete from persons where id="+id;
			PreparedStatement testo = conn.prepareStatement(q);
			int result = testo.executeUpdate();
			testo.close();
			query.close();
		} catch(Exception e) {
			e.printStackTrace();
			response=Response.serverError().build();;
		}finally {
			if(conn!=null) {
				response = Response.ok().build();
				conn.close();
			}
		}
		return response;
    }
	
	
	
	protected Person readPerson(String s) {
			JsonReader jsonReader = Json.createReader(new StringReader(s));
			JsonObject o = jsonReader.readObject();
			int id = Integer.parseInt(o.getString("id"));
			String firstName = o.getString("firstName");
			String lastName = o.getString("lastName");
			String sex = o.getString("sex");
			Person person = new Person(id, firstName, lastName, sex.charAt(0));			
			return person;
		}
	protected String readId(String s) {
		JsonReader jsonReader = Json.createReader(new StringReader(s));
		JsonObject o = jsonReader.readObject();
		return o.getString("id");
	}
	
	@Path("/database")
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String returnDatabaseStatus(){
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
	@PUT
    public Response putPerson(String is) throws Exception{
		Person person = readPerson(is);
		String firstName = person.getFirstName();
		String lastName = person.getLastName();
		int id=person.getId();
		char sex = person.getSex();
		PreparedStatement query = null;
		Response response=null;
		Connection conn = null;
		try {
			conn = pg.pgConn();
			String q = "select count(*) from persons where id="+id;
			query = conn.prepareStatement(q);
			ResultSet rs = query.executeQuery();
			int rowcount = 0;
			while(rs.next()) {
				rowcount=rs.getInt("count");
			}
			if(rowcount==0) {
				return Response.status(Response.Status.NOT_FOUND).build();
			}
			q = "update persons set firstname='"+firstName+"',lastname='"+lastName+"',sex='"+sex+"' where id="+id;
			PreparedStatement testo = conn.prepareStatement(q);
			int result = testo.executeUpdate();
			testo.close();
			query.close();
		} catch(Exception e) {
			e.printStackTrace();
			response=Response.serverError().build();;
		}finally {
			if(conn!=null) {
				response = Response.created(URI.create("person/" + id)).build();
				conn.close();
			}
		}
		return response;
    }
	@GET
	@Path("{id}")
    public Response getPerson(@PathParam("id") String id) throws Exception {
		PreparedStatement query = null;
		Connection conn = null;
		String firstName=null;String lastName=null; char sex=' ';
		Response response=null;
		try {
			conn = pg.pgConn();
			query = conn.prepareStatement("select * from persons where id="+id);
			ResultSet rs = query.executeQuery();
			while(rs.next()) {
				firstName = rs.getString("firstname");
				lastName = rs.getString("lastname");
				sex = rs.getString("sex").charAt(0);
			}
			query.close();
		} catch(Exception e) {
			e.printStackTrace();
			response=Response.status(Response.Status.NOT_FOUND).build();
		}finally {
			if(conn!=null){
				Person person = new Person(Integer.parseInt(id),firstName,lastName,sex);
				conn.close();
				response=Response.ok(person.toJson()).build();
			}
		}
		return response;
    }
	@POST
	public Response createCustomer(String is) throws Exception{
		Person person = readPerson(is);
		//int id = person.getId();
		String firstName = person.getFirstName();
		String lastName = person.getLastName();
		char sex = person.getSex();
		PreparedStatement query = null;
		Response response=null;
		Connection conn = null;
		int id=0;
		try {
			conn = pg.pgConn();
			String q = "insert into persons(firstname,lastname,sex) values('"+firstName+"','"+lastName+"','"+sex+"')";
			query = conn.prepareStatement(q);
			int rs = query.executeUpdate();
			q = "select max(id) from persons";
			PreparedStatement testo = conn.prepareStatement(q);
			ResultSet result = testo.executeQuery();
			while(result.next()) {
				id = result.getInt("max");
			}
			testo.close();
			query.close();
		} catch(Exception e) {
			e.printStackTrace();
			response=Response.serverError().build();;
		}finally {
			if(conn!=null) {
				response = Response.created(URI.create("person/" + id)).build();
				conn.close();
			}
		}
		return response;
	}
	/*@POST
	public Response createCustomer(String is) {
		Person person = readPerson(is);
		String id = readId(is);
        PersonDao.instance.getModel().put(id, person);
		return Response.created(URI.create("person/" + id)).build();
	}
	*/
}
	

