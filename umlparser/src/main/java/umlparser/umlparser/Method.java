package umlparser.umlparser;
import java.util.ArrayList;

public class Method {
	private String access ;
	private String name;
	private String returnType;
	private int paramNum;
	private ArrayList<FormalParameter> formalParameter;
	
	
	public Method() {
		access = null;
		name = null;
		returnType = null;
		paramNum = 0;
		formalParameter = new ArrayList<>();
	}
	
	public String getAccess(){
		return access;
	}
	public void setAccess(String access){
		this.access = access;
	}
	
	public String getretType(){
		return returnType;
	}
	public void setretType(String type){
		this.returnType = type;
	}
	
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}

	public int getParamnum(){
		return paramNum;
	}
	public void setParamnum(int num){
		this.paramNum = num;
	}

	public FormalParameter getformalParam(int index){
		return formalParameter.get(index);
	}
	public void addformalParam(FormalParameter formalparameter){
		formalParameter.add(formalparameter);
	}
	public void setformalParam(){
		formalParameter = new ArrayList<FormalParameter>();
	}
}