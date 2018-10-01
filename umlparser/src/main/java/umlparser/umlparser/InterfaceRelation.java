package umlparser.umlparser;

import java.util.ArrayList;

public class InterfaceRelation {
	private int totalNumOfInterface;
	private ArrayList<RelationElement> relation;
	
	public InterfaceRelation() {
		totalNumOfInterface = 0;
		relation = new ArrayList<>();
	}
	
	public int getInterfacenum(){
		return totalNumOfInterface;
	}
	public void setInterfacenum(int num){ 
		totalNumOfInterface = num;
	}

	public RelationElement getRelationelement(int index){
		return relation.get(index);
	}
	public void addRelationelement(RelationElement element){
		relation.add(element);
	}
	public void setRelationelement(){
		relation = new ArrayList<RelationElement>();
	}
}
