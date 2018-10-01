package umlparser.umlparser;

public class RelationElement {
	private String from ;
	private String to;
	
	public RelationElement() {
		from = null;
		to = null;
	}
	
	public String getFrom(){
		return from;
	}
	public void setFrom(String from){
		this.from = from;
	}
	
	public String getTo(){
		return to;
	}
	public void setTo(String to){
		this.to = to;
	}
	
}