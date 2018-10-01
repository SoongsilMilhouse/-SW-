package umlparser.umlparser;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Native;

import com.google.gson.*;

import javax.imageio.ImageIO;
import javax.security.auth.login.CredentialNotFoundException;
//import org.json.*;
import javax.swing.*;


import java.util.*;

public class Frame {
	JFrame frame;
	JPanel editPanel;
	JPanel umlPanel;
	JPanel codePanel;
	Container contentPane;
	//ArrayList<objectInfo> objectList;
	ClassInfo classClick;
	DrawPanel drawPanel;
	boolean isShift = false;
	String cName="";
	String mName="";
	TextArea code;
	UmlInfo umlInfo = null;
	
	int count = 0;
	
	Frame(){
		frame = new JFrame();
		contentPane = frame.getContentPane();
		umlPanel = new JPanel();
		this.GUI();
		
		
		Dimension res = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(res.width,res.height);
		frame.setTitle("UMLgenerator");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void GUI(){
				buttonAction action = new buttonAction();
				drawPanel =new DrawPanel();
				
				//2개의 panel붙이기
				editPanel = new JPanel();
				codePanel = new JPanel();
				
				//editPanel에 버튼 3개 붙이기
				editPanel.setBackground(Color.ORANGE);
				
				JButton objectButton = new JButton("Object");
				JButton UtoCButton = new JButton("UML->CODE");
				JButton CtoUButton = new JButton("UML<-CODE");
				JButton JavaButton = new JButton("addJava");
				
				editPanel.setLayout(new GridLayout(6,0,10,10));
				editPanel.add(objectButton);
				editPanel.add(UtoCButton);
				editPanel.add(CtoUButton);
				editPanel.add(JavaButton);
				
				//버튼에 각각 ActionListener달기
				objectButton.addActionListener(action);
				UtoCButton.addActionListener(action);
				CtoUButton.addActionListener(action);
				JavaButton.addActionListener(action);
				
				drawPanel.addMouseListener(new mouseAction());
				drawPanel.addMouseMotionListener(new mouseAction());
				
				//codePanel
				codePanel.setBackground(Color.BLUE);
				codePanel.setLayout(new FlowLayout());
				code = new TextArea(35,50);
				JScrollPane scrollPane = new JScrollPane(code);
				codePanel.add(scrollPane);
				
				contentPane.add(editPanel,"West");
				contentPane.add(drawPanel,"Center");
				contentPane.add(codePanel,"East");
	}
	
	public class buttonAction implements ActionListener{
		
		ParseEngine pe ;
		public void actionPerformed(ActionEvent e){
			JButton b=(JButton)e.getSource();
			if(b.getText().equals("Object")){
				//처음 그릴때 umlInfo객체 생성
				if(umlInfo == null){
//					umlInfo = new UmlInfo();
//					umlInfo.setClassinfo();
//					umlInfo.setInterfaceinfo();
//					umlInfo.setInheritanceRealtion();
//					umlInfo.setInterfaceRealtion();
//					umlInfo.getClassinfo(0).setVarilist();
//					umlInfo.getClassinfo(0).setMethodlist();
					
					
					contentPane.getParent().repaint();
				}
				else{
					ClassInfo classinfo = new ClassInfo();
					classinfo.setVarinum(0);
					classinfo.setMethodnum(0);
					classinfo.setVarilist();
					classinfo.setMethodlist();
					classinfo.setClassname("new Obj");
					classinfo.setAccess("public");
					umlInfo.addClassinfo(classinfo);
					umlInfo.setClassnum(umlInfo.getClassnum()+1);
					contentPane.repaint();
				}
			}
			else if(b.getText().equals("UML->CODE")){
				
				if (umlInfo != null) {
					Gson gson = new Gson();
					String umlout = gson.toJson(umlInfo);
					try {
						BufferedWriter out = new BufferedWriter(new FileWriter("D:\\capston\\uml-parser-master\\uml-parser-master\\umlparser\\src\\main\\java\\umlparser\\umlparser\\umlinput.txt"));
						out.write(umlout); 
						out.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				CreateJavaCode cjc = new CreateJavaCode();
				cjc.makeFile();
				try {
					pe.cuArray = pe.getCuArray("D:\\capston\\uml-parser-master\\uml-parser-master\\umlparser\\src\\main\\java\\umlparser\\umlparser\\umlToCode\\");
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				code.setText("");
				for(int k=0;k<pe.cuArray.size();++k) {
					code.append(pe.cuArray.get(k).toString());
					code.append("\n");
				}
				contentPane.getParent().repaint();
			}
			else if(b.getText().equals("UML<-CODE")){
				//textArea에 코드가 있을 경우 : Code->UML
				String codeText = code.getText();
					
					try {
						
							Umlparser ups = new Umlparser();
							pe = new ParseEngine("D:\\capston\\uml-parser-master\\uml-parser-master\\umlparser\\src\\main\\java\\umlparser\\umlparser\\code","diagram");
							String str = pe.start();
							System.out.println("���� ���ڿ�: " + str);
							String json = ups.makeJsonFile(str);
							
							File file = new File("D:\\capston\\uml-parser-master\\uml-parser-master\\umlparser\\src\\main\\java\\umlparser\\umlparser\\umlinput.txt");
							BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
							
							if(file.isFile() && file.canWrite()) {
								bufferedWriter.write(json);
								bufferedWriter.close();
							}
						 
						
						Gson gson = new Gson();
						umlInfo = gson.fromJson(new FileReader("D:\\capston\\uml-parser-master\\uml-parser-master\\umlparser\\src\\main\\java\\umlparser\\umlparser\\umlinput.txt"), UmlInfo.class);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				contentPane.getParent().repaint();
			}
			else if(b.getText().equals("addJava")){
				if(!code.getText().equals("")){
					
					try {
						String cod = code.getText();
						String[] word = cod.split("class|interface");
						int index = word[1].indexOf("{");

						System.out.println(word[1].substring(1, index));
						String className = word[1].substring(1, index).split(" ")[0];
						BufferedWriter out = new BufferedWriter(new FileWriter("D:\\capston\\uml-parser-master\\uml-parser-master\\umlparser\\src\\main\\java\\umlparser\\umlparser\\code\\" + className +".java"));
						count++;
						out.write(code.getText()); out.newLine();
						out.close();
						
					} catch (JsonSyntaxException | JsonIOException | IOException e1) {
						e1.printStackTrace();
					}
				}
				contentPane.getParent().repaint();
			}
		}

	}
	
	public class DrawPanel extends JPanel{
		int x,y,w,h;
		
		public void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			this.removeAll();
			
			//이미지로 그리는 방법
			if(umlInfo != null){
				BufferedImage img = null;
				try {
					img = ImageIO.read(new File("D:\\capston\\uml-parser-master\\uml-parser-master\\umlparser\\src\\main\\java\\umlparser\\umlparser\\classImg.png"));
				} 
				catch (IOException e) { e.printStackTrace();}
				
				//class개수만큼 만들기
				for(int i=0;i<umlInfo.getClassnum();i++){
					x = umlInfo.getClassinfo(i).x ;
					y = umlInfo.getClassinfo(i).y ;
					w = umlInfo.getClassinfo(i).width ;
					h = umlInfo.getClassinfo(i).height;
					
					//uml틀
					g2.drawImage(img, x, y, w, h,this);
					
					//class이름 text
					JLabel classlabel = new JLabel();
					classlabel.setText("Class name");
					classlabel.setBounds(x+20, y, 100, 30);
					this.add(classlabel);
					
					//class이름
					JLabel classnamelabel = new JLabel();
					if(umlInfo.getClassinfo(i).getAccess() == "private")
						classnamelabel.setForeground(Color.RED);
					else
						classnamelabel.setForeground(Color.BLUE);
					classnamelabel.setText(umlInfo.getClassinfo(i).getClassname());
					classnamelabel.setBounds(x+100, y, 100, 30);
					this.add(classnamelabel);
					

					//변수가 있으면
					if(umlInfo.getClassinfo(i).getVarinum() != 0){
						//변수 이름 text
						JLabel varilabel = new JLabel();
//						varilabel.addActionListener();
						varilabel.setText("Variable");
						varilabel.setBounds(x+20, y+30, 100, 30);
						this.add(varilabel);
						
						for(int j=0;j<umlInfo.getClassinfo(i).getVarinum();j++){
							JLabel varinamelabel = new JLabel();
							if(umlInfo.getClassinfo(i).getVarilist(j).getAccess().equals("private"))
								varinamelabel.setForeground(Color.red);
							else
								varinamelabel.setForeground(Color.blue);
							varinamelabel.setText(umlInfo.getClassinfo(i).getVarilist(j).getType()+" "+
									umlInfo.getClassinfo(i).getVarilist(j).getName());
							varinamelabel.setBounds(x+100, y+30, 100, 30);
							y = y+30;
							this.add(varinamelabel);
						}
					}
					
					//메소드가 있으면
					if(umlInfo.getClassinfo(i).getMethodnum() != 0){
						//메소드 이름 text
						JLabel methodlabel = new JLabel();
						methodlabel.setText("Method");
						methodlabel.setBounds(x+20, y+30, 100, 30);
						this.add(methodlabel);
						
						//메소드 이름
						for(int j=0;j<umlInfo.getClassinfo(i).getMethodnum();j++){
							JLabel methodbutton = new JLabel();
							if(umlInfo.getClassinfo(i).getMethodlist(j).getAccess().equals("private"))
								methodbutton.setForeground(Color.red);
							else
								methodbutton.setForeground(Color.blue);
							methodbutton.setText(umlInfo.getClassinfo(i).getMethodlist(j).getName());
							methodbutton.setBounds(x+100, y+30,50,30);
							y = y+30;
							this.add(methodbutton);
						}
					}
				}

				//상속관계 표현
				if(umlInfo.getInheritanceRealtion().getInheritancenum() != 0){
					int fx = 0,fy = 0,tx = 0,ty = 0;
					int fw=0,fh=0,tw=0,th=0;
					for(int i=0;i<umlInfo.getInheritanceRealtion().getInheritancenum();i++){
						//어떤 클래스사이의 관계인지 찾기
						for(int j=0;j<umlInfo.getClassnum();j++){
							if(umlInfo.getInheritanceRealtion().getRelationelement(i).getFrom().equals(umlInfo.getClassinfo(j).getClassname())){
								fx = umlInfo.getClassinfo(j).x +umlInfo.getClassinfo(j).width;
								fy = umlInfo.getClassinfo(j).y +umlInfo.getClassinfo(j).height/2;
								fw = umlInfo.getClassinfo(j).width;
								fh = umlInfo.getClassinfo(j).height;
							}
							else if(umlInfo.getInheritanceRealtion().getRelationelement(i).getTo().equals(umlInfo.getClassinfo(j).getClassname())){
								tx =  umlInfo.getClassinfo(j).x;
								ty = umlInfo.getClassinfo(j).y + umlInfo.getClassinfo(j).height/2;
								tw = umlInfo.getClassinfo(j).width;
								th = umlInfo.getClassinfo(j).height;
							}
						}
						
						if(tx>=fx){
							int[] px={tx-10,tx,tx-10};
							int[] py={ty-10,ty,ty+10};
							g2.drawLine(fx, fy, tx, ty);
							g2.drawPolyline(px, py, 3);
						}
						else{
							fx = fx-fw;
							tx = tx+tw; 
							int[] px={tx+10,tx,tx+10};
							int[] py={ty-10,ty,ty+10};
							g2.drawLine(fx, fy, tx, ty);
							g2.drawPolyline(px, py, 3);
						}
					}
				}
			}
			this.setVisible(true);
		}
		
	}
	
	
	
	public boolean isThere(int x, int y){
		if(umlInfo != null) {
		for(int i=0;i<umlInfo.getClassnum();i++){
			if(x >= umlInfo.getClassinfo(i).x && x <= umlInfo.getClassinfo(i).x+umlInfo.getClassinfo(i).width &&
					y >= umlInfo.getClassinfo(i).y && y <= umlInfo.getClassinfo(i).y+umlInfo.getClassinfo(i).height){
				classClick = umlInfo.getClassinfo(i);
	            return true;
	         }
		}
		}
	      return false;
	 }
	
public class mouseAction implements MouseListener,MouseMotionListener{
	int shx,shy;
	int classIndex;
	ObjDialog ObjDialog;
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			if(isThere(e.getX(), e.getY())){
				ObjDialog = new ObjDialog(frame, "class", true);
				ObjDialog.setBounds(0, 0, 350, 500);
				ObjDialog.setVisible(true);
		   }
		}
	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		if(isShift == false && isThere(e.getX(), e.getY())){
			isShift = true;
			shx = e.getX();
			shy = e.getY();
//			for(int i=0;i<umlInfo.getClassnum();i++){
//				if(classClick.equals(umlInfo.getClassinfo(i))){
//					classIndex = i;
//					break;
//				}
//			}
	   }
	}

	public void mouseReleased(MouseEvent e) {
		if(isShift == true){
			if(e.getX()>=shx){
				if(e.getY()>=shy)
				{
					classClick.x += e.getX()-shx;
					classClick.y += e.getY()-shy;
				}
				else{
					classClick.x += e.getX()-shx;
					classClick.y -= (shy-e.getY());
				}
			}
			else{
				if(e.getY()>=shy)
				{
					classClick.x -= (shx-e.getX());
					classClick.y += e.getY()-shy;
				}
				else{
					classClick.x -= (shx-e.getX());
					classClick.y -= (shy-e.getY());
				}
			}
		}
		
		contentPane.getParent().repaint();
		isShift = false;
		classClick = null;
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseDragged(MouseEvent e) {
		
	}

	public void mouseMoved(MouseEvent e) {
		
	}
}

	class ObjDialog extends JDialog{
		int tmpy=0;
		String etmp = "";
		JLabel cname = new JLabel("클래스 이름");
		JTextField className = new JTextField(10);
		JRadioButton []cRadio = new JRadioButton[2];
		JLabel mname = new JLabel("메소드 이름");
		JTextField methodName;
		JButton madd = new JButton("메소드 추가");
		JLabel vname = new JLabel("변수 이름");
		JTextField variName;
		JButton vadd = new JButton("변수 추가");
		JButton abutton = new JButton("확인");
		JLabel ename = new JLabel("상속");
		JTextField extendName;
		ArrayList<JTextField> chmethod = new ArrayList<JTextField>();
		ArrayList<JTextField> chvariable = new ArrayList<JTextField>();
		
		
		
		public ObjDialog(JFrame frame, String title,boolean modal) {
			super(frame,title,true);
			this.setLayout(null);
			
			tmpy = 10;
			// class
			this.add(cname);
			cname.setBounds(20, tmpy, 100, 20);
			className.setText(classClick.getClassname());
			this.add(className);
			className.setBounds(100, tmpy, 100, 20);

			tmpy += 40;
			ButtonGroup g = new ButtonGroup();
			cRadio[0] = new JRadioButton("Public");
			cRadio[0].setBounds(50, tmpy, 100, 20);
			cRadio[1] = new JRadioButton("Private");
			cRadio[1].setBounds(150, tmpy, 100, 20);
			if (classClick.getAccess().equals("public")) {
				cRadio[0].setSelected(true);
			} else {
				cRadio[1].setSelected(true);
			}
			g.add(cRadio[0]);
			g.add(cRadio[1]);
			this.add(cRadio[0]);
			this.add(cRadio[1]);

			// methods
			this.add(mname);
			tmpy += 40;
			mname.setBounds(20, tmpy, 100, 20);
			for (int i = 0; i < classClick.getMethodnum(); i++) {
				methodName = new JTextField(10);
				String tmp = classClick.getMethodlist(i).getAccess() + " " + classClick.getMethodlist(i).getretType()
						+ " " + classClick.getMethodlist(i).getName();

				//매개변수가 있는 경우
				if (classClick.getMethodlist(i).getParamnum() != 0) {
					tmp += "(";
					for (int j = 0; j < classClick.getMethodlist(i).getParamnum(); j++) {
						tmp += classClick.getMethodlist(i).getformalParam(j).getType() + " "
								+ classClick.getMethodlist(i).getformalParam(j).getvarName() + ",";
					}
					tmp = tmp.substring(0, tmp.length() - 1);
					tmp += ")";
				}
				//매개변수가 없는 경우
				else{
					tmp += "()";
				}
				methodName.setText(tmp);
				chmethod.add(methodName);
				this.add(methodName);
				methodName.setBounds(100, tmpy, 200, 20);
				tmpy += 30;
			}
			this.add(madd);
			madd.setBounds(100, tmpy, 200, 20);
			tmpy+=30;

			// 변수
			this.add(vname);
			tmpy += 40;
			vname.setBounds(20, tmpy, 100, 20);
			for (int i = 0; i < classClick.getVarinum(); i++) {
				String vtmp = classClick.getVarilist(i).getAccess() + " " + classClick.getVarilist(i).getType() + " "
						+ classClick.getVarilist(i).getName();
				variName = new JTextField(10);
				variName.setText(vtmp);
				chvariable.add(variName);
				this.add(variName);
				variName.setBounds(100, tmpy, 200, 20);
				tmpy += 30;
			}
			this.add(vadd);
			vadd.setBounds(100, tmpy, 200, 20);
			tmpy+=30;
			
			//상속
			this.add(ename);
			tmpy+=40;
			ename.setBounds(20,tmpy,100,20);
			extendName = new JTextField(10);
			if(umlInfo.getInheritanceRealtion() != null){
				for (int i = 0; i < umlInfo.getInheritanceRealtion().getInheritancenum(); i++) {
					if (umlInfo.getInheritanceRealtion().getRelationelement(i).getFrom()
							.equals(classClick.getClassname())) {
						etmp =umlInfo.getInheritanceRealtion().getRelationelement(i).getTo();
						extendName.setText(etmp);
						break;
					}
				}
			}
			this.add(extendName);
			extendName.setBounds(100,tmpy,200,20);

			tmpy += 40;
			this.add(abutton);
			abutton.setBounds(60, tmpy, 200, 20);

			

			abutton.addActionListener(new ActionListener() {
				int mindex = 0;
				int vindex = 0;
				@Override
				public void actionPerformed(ActionEvent e) {
					//className바뀐 경우
					if(!classClick.getClassname().equals(className.getText())){
						for(int i=0;i<umlInfo.getInheritanceRealtion().getInheritancenum();i++){
							for(int j=0;j<umlInfo.getClassnum();j++){
								if(umlInfo.getInheritanceRealtion().getRelationelement(i).getFrom().equals(classClick.getClassname())){
									umlInfo.getInheritanceRealtion().getRelationelement(i).setFrom(className.getText());
								}
								if(umlInfo.getInheritanceRealtion().getRelationelement(i).getTo().equals(classClick.getClassname())){
									umlInfo.getInheritanceRealtion().getRelationelement(i).setTo(className.getText());
								}
							}
						}
						classClick.setClassname(className.getText());
					}
					
					//class access바뀐 경우
					//public인 경우
					if(cRadio[0].isSelected()){
						classClick.setAccess("public");
					}
					//private인 경우
					else{
						classClick.setAccess("private");
					}
					
					//메소드 바뀐 경우
					classClick.setMethodlist();
					classClick.setMethodnum(0);
					for(int i=0;i<chmethod.size();i++){
						//메소드를 지웠을 경우
						if(chmethod.get(i).getText().equals("")){
							mindex = i;
							continue;
						}
						//메소드가 변경되었을 경우
						else{
							String str = chmethod.get(i).getText();
							classClick.setMethodnum(classClick.getMethodnum()+1);
							classClick.addMethodlist(new Method());
							classClick.getMethodlist(mindex).setAccess(str.split(" ")[0]);
							classClick.getMethodlist(mindex).setretType(str.split(" ")[1]);
							str =str.split(" ",3)[2];
							classClick.getMethodlist(mindex).setName(str.split("\\(")[0]);
							str = str.split("\\(")[1];
							str = str.substring(0, str.length()-1);
							
							//메소드의 매개변수가 없을 경우
							if(str.equals("")){
								classClick.getMethodlist(mindex).setParamnum(0);
								classClick.getMethodlist(mindex).setformalParam();
							}
							//매개변수가 있는 경우
							else{
								int paramnum = str.split(",").length;
								classClick.getMethodlist(mindex).setParamnum(paramnum);
								classClick.getMethodlist(mindex).setformalParam();
								for(int j=0;j<paramnum;j++){
									String pstr = str.split(",")[j];
									classClick.getMethodlist(mindex).addformalParam(new FormalParameter());
									classClick.getMethodlist(mindex).getformalParam(j).setType(pstr.split(" ")[0]);
									classClick.getMethodlist(mindex).getformalParam(j).setvarName(pstr.split(" ")[1]);
								}
							}
							mindex++;
						}
					}
					
					//변수 바뀐 경우
					classClick.setVarilist();
					classClick.setVarinum(0);
					for(int i=0;i<chvariable.size();i++){
						if(chvariable.get(i).getText().equals("")){
							vindex = i;
							continue;
						}
						else{
							String vstr = chvariable.get(i).getText();
							classClick.setVarinum(classClick.getVarinum()+1);
							classClick.addVarilist(new Variable());
							classClick.getVarilist(vindex).setAccess(vstr.split(" ")[0]);
							classClick.getVarilist(vindex).setType(vstr.split(" ")[1]);
							classClick.getVarilist(vindex).setName(vstr.split(" ")[2]);
							vindex ++;
						}
					}
					
					//상속하는 경우(나중에: getinheritancerelation객체 없는 경우도 처리)
					boolean isExtend = false;
					for (int i = 0; i < umlInfo.getInheritanceRealtion().getInheritancenum(); i++) {
						if (umlInfo.getInheritanceRealtion().getRelationelement(i).getFrom()
								.equals(classClick.getClassname())) {
							if (extendName.getText().equals("")) {
								umlInfo.getInheritanceRealtion()
										.setInheritancenum(umlInfo.getInheritanceRealtion().getInheritancenum() - 1);
								umlInfo.getInheritanceRealtion().removeRelationelement(i);
							} else {
								umlInfo.getInheritanceRealtion().getRelationelement(i).setTo(extendName.getText());
							}
							isExtend = true;
						}
					}
					if (isExtend == false && !extendName.getText().equals("")) {
						RelationElement element = new RelationElement();
						element.setFrom(classClick.getClassname());
						element.setTo(extendName.getText());
						umlInfo.getInheritanceRealtion().addRelationelement(element);
						umlInfo.getInheritanceRealtion().setInheritancenum(umlInfo.getInheritanceRealtion().getInheritancenum()+1);
					}
					
					contentPane.repaint();
					setVisible(false);
					setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				}
			});
			
			vadd.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					setVisible(false);
					VariDialog variDialog = new VariDialog(frame, "variable", true);
					variDialog.setBounds(0, 0,250, 200);
					variDialog.setAlwaysOnTop(true);
					//variDialog.setSize(200, 150);
					variDialog.setVisible(true);
					
				}
			});
			
			madd.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					setVisible(false);
					MethodDialog methodDialog = new MethodDialog(frame, "method", true);
					methodDialog.setBounds(0, 0, 250, 200);
					methodDialog.setAlwaysOnTop(true);
					//methodDialog.setSize(200, 150);
					methodDialog.setVisible(true);
				}
			});
			
		}
	}
	
	class VariDialog extends JDialog{
		JLabel access = new JLabel("Access");
		JTextField accesstext = new JTextField(10);
		JLabel type = new JLabel("Type");
		JTextField typetext = new JTextField(10);
		JLabel name = new JLabel("Name");
		JTextField nametext = new JTextField(10);
		JButton vadd = new JButton("추가");
		
		public VariDialog(JFrame frame, String string, boolean b) {
			super(frame,string,true);
			this.setLayout(null);
			this.setSize(100, 300);
			
			this.add(access);
			access.setBounds(20,20,100,20);
			this.add(accesstext);
			accesstext.setBounds(100, 20, 100,20);
			
			this.add(type);
			type.setBounds(20, 40, 100,20);
			this.add(typetext);
			typetext.setBounds(100, 40, 100,20);
			
			this.add(name);
			name.setBounds(20, 60, 100,20);
			this.add(nametext);
			nametext.setBounds(100, 60, 100,20);
			
			this.add(vadd);
			vadd.setBounds(60,80,100,20);
			
			vadd.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if(!accesstext.getText().equals("") && !typetext.getText().equals("") && !nametext.getText().equals("")){
						Variable vari = new Variable();
						vari.setAccess(accesstext.getText());
						vari.setType(typetext.getText());
						vari.setName(nametext.getText());
						classClick.addVarilist(vari);
						classClick.setVarinum(classClick.getVarinum()+1);
					}
					setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					contentPane.repaint();
					setVisible(false);
				}
			});
		}
	}
		
		class MethodDialog extends JDialog{
			JLabel access = new JLabel("Access");
			JTextField accesstext = new JTextField(10);
			JLabel type = new JLabel("Returntype");
			JTextField typetext = new JTextField(10);
			JLabel name = new JLabel("Name");
			JTextField nametext = new JTextField(10);
			JLabel para = new JLabel("Parameter");
			JTextField paratext = new JTextField(10);
			JButton madd = new JButton("추가");
			
			public MethodDialog(JFrame frame, String string, boolean b) {
				super(frame,string,true);
				this.setLayout(null);
				this.setPreferredSize(new Dimension(500, 600));
				this.setResizable(true);
				this.pack();
				//this.setSize(600, 900);
				
				//this.setBounds(100, 100, 600, 900);
				this.add(access);
				access.setBounds(20,20,100,20);
				this.add(accesstext);
				accesstext.setBounds(100, 20, 100,20);
				
				this.add(type);
				type.setBounds(20, 40, 100,20);
				this.add(typetext);
				typetext.setBounds(100, 40, 100,20);
				
				this.add(name);
				name.setBounds(20, 60, 100,20);
				this.add(nametext);
				nametext.setBounds(100, 60, 100,20);
				
				this.add(para);
				para.setBounds(20, 80, 100,20);
				this.add(paratext);
				paratext.setBounds(100, 80, 100,20);
				
				this.add(madd);
				madd.setBounds(60,100,100,20);
				
				madd.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						if(!accesstext.getText().equals("") && !typetext.getText().equals("") && !nametext.getText().equals("")){
							Method meth = new Method();
							meth.setAccess(accesstext.getText());
							meth.setretType(typetext.getText());
							meth.setName(nametext.getText());

							classClick.addMethodlist(meth);
							classClick.setMethodnum(classClick.getMethodnum()+1);
							
							//파라미터가 없을 경우
							if(paratext.getText().equals("")){
								meth.setParamnum(0);
							}
							else{
								String parastr = paratext.getText();
								int parasize = parastr.split(",").length;
								meth.setParamnum(parasize);
								meth.setformalParam();
								for(int i=0;i<parasize;i++){
									String paratmp = parastr.split(",")[i];
									meth.addformalParam(new FormalParameter());
									meth.getformalParam(i).setType(paratmp.split(" ")[0]);
									meth.getformalParam(i).setvarName(paratmp.split(" ")[1]);
								}
							}
							
						}
						setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
						contentPane.repaint();
						setVisible(false);
					}
				});
			}
		
	}
	
}



	

