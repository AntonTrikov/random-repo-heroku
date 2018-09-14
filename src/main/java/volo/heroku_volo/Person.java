package volo.heroku_volo;

public class Person {
	private int id;
	private String firstName;
	private String lastName;
	private char sex;
	
	public Person(int id, String firstName, String lastName, char sex) {
		this.id=id;
		this.firstName=firstName;
		this.lastName=lastName;
		this.sex= sex;
	}
	
	public String toJson() {
		return "{"+
				"\"id\": "+this.id+","+
				"\"firstName\": \""+this.firstName+"\","+
				"\"lastName\": \""+this.lastName+"\","+
				"\"sex\": \""+this.sex+"\" }";
				
	}
	public int getId(){
		return this.id;
	}
	public void setId(int id){
		this.id = id;
	}
	
	public String getFirstName(){
		return this.firstName;
	}
	public void setFirstName(String firstName){
		this.firstName = firstName;
	}
	public String getLastName(){
		return this.lastName;
	}
	public void setLastName(String lastName){
		this.lastName = lastName;
	}
	public char getSex() {
		return this.sex;
	}
	public void setSex(char sex){
		this.sex = sex;
	}
}