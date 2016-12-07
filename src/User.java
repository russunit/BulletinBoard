
public class User{
	
	public String name;
	public String password;
	
	public User(String n, String p)
	{
		this.name = n;
		this.password = p;
	}
	
	public boolean nameMatches(String n)
	{
		return (this.name.equalsIgnoreCase(n));
	}
	
	public boolean passwordMatches(String p)
	{
		return (this.password.equals(p));
	}

}
