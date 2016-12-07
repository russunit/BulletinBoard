import java.util.Date;


public class Message {
	
	public String content;
	public String topic;
	public String title;
	public User author;
	public Date date;
	
	public Message(String c, String to, String ti, User a, Date d)
	{
		this.content = c;
		this.topic = to;
		this.title = ti;
		this.author = a;
		this.date = d;
	}
	
	public boolean titleMatches(String t)
	{
		return this.title.equals(t);
	}
	
	public String displayString()
	{
		String s = "" + title + "\r\n" + topic + "\r\n" + author.name + "\r\n" + date.toString() + "\r\n------------\r\n" + content + "\r\n";
		return s;
		
	}

}
