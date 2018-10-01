package umlparser.umlparser;

import java.util.ArrayList;

public class UmlInfo {
	private int totalNumOfClasses = 0;
	private int totalNumOfInterfaces = 0;
	private ArrayList<ClassInfo> classes;
	private ArrayList<InterfaceInfo> interfaces;
	private InheritanceRelation inheritanceInfo;
	private InterfaceRelation interfaceInfo;
	
	public UmlInfo() {
		totalNumOfClasses = 0;
		totalNumOfInterfaces = 0;
		classes = new ArrayList<>();
		interfaces = new ArrayList<>();
		inheritanceInfo = new InheritanceRelation();
		interfaceInfo = new InterfaceRelation();
	}
	
	public int getClassnum(){
		return totalNumOfClasses; 
	}
	public void setClassnum(int num){
		totalNumOfClasses = num;
	}
////////////////////////////////////////
	public int getInterfacenum(){
		return totalNumOfInterfaces;
	}
	public void setInterfacenum(int num){
		totalNumOfInterfaces = num;
	}
////////////////////////////////////////
	public ClassInfo getClassinfo(int index){
		return classes.get(index);
	}
	public void addClassinfo(ClassInfo classinfo){
		classes.add(classinfo);
	}
	public void setClassinfo(){
		classes = new ArrayList<ClassInfo>();
	}
////////////////////////////////////////
	public InterfaceInfo getInterfaceinfo(int index){
		return interfaces.get(index);
	}
	public void addInterfaceinfo(InterfaceInfo interfaceinfo){
		interfaces.add(interfaceinfo);
	}
	public void setInterfaceinfo(){
		interfaces = new ArrayList<InterfaceInfo>();
	}
////////////////////////////////////////
	public InheritanceRelation getInheritanceRealtion(){
		return inheritanceInfo;
	}
	public void setInheritanceRealtion(){
		inheritanceInfo = new InheritanceRelation();
	}
////////////////////////////////////////
	public InterfaceRelation getInterfaceRealtion(){
		return interfaceInfo;
	}
	public void setInterfaceRealtion(){
		interfaceInfo = new InterfaceRelation();
	}
}