package umlparser.umlparser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import com.google.gson.*;



public class Umlparser {

	public static void main(String[] args) throws Exception {
		if (args[0].equals("class")) {
			ParseEngine pe = new ParseEngine(args[1], args[2]);
			System.out.println(args[0]);
			System.out.println(args[1]);
			System.out.println(args[2]);
			
			
			String str = pe.start();
			System.out.println("YUML FORMAT : " + str);
			String json = makeJsonFile(str);
			System.out.println("JSON FORMAT : " + json);
			
			File file = new File("C:\\Users\\Jiyong Kim\\Desktop\\SODA조_캡스톤디자인프로젝트\\umlparser\\src\\main\\java\\umlparser\\umlparser\\input.txt");
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
			
			if(file.isFile() && file.canWrite()) {
				bufferedWriter.write(json);
				bufferedWriter.close();
			}
		} else if (args[0].equals(("seq"))) {
			//ParseSeqEngine pse = new ParseSeqEngine(args[1], args[2], args[3], args[4]);
			//pse.start();
		} else {
			System.out.println("Invalid keyword " + args[0]);
		}
	}

	public static String makeJsonFile(String str) {
		UmlInfo umlInfo = new UmlInfo();

		ArrayList<String> classArray = new ArrayList<>();
		ArrayList<String> interfaceArray = new ArrayList<>();
		String[] eachClassArray = str.split(",");
		ArrayList<String> extendsArray = new ArrayList<String>();
		ArrayList<String> implementsArray = new ArrayList<String>();
	
		String json = "";
		int classCount = 0;
		int interfaceCount = 0;
		int i, j, k;

		// "totalNumOfClasses"占쏙옙 占쏙옙占쏙옙 classCount占쏙옙 占쏙옙占쏙옙占싼댐옙. (占쏙옙占쏙옙占쏙옙占쏙옙 클占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙)
		for (String s : eachClassArray) {
			// comma(,) 2占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙李� 占쌍깍옙 占쏙옙占쏙옙占쏙옙 isEmpty占쏙옙 占써서 占쏙옙占쏙옙占쏙옙 占쌍댐옙 占싶몌옙 classArray占쏙옙 占쌍는댐옙.
			if (!s.isEmpty()) {
				// 占쏙옙占� 표占쏙옙 "-^"占쏙옙 占쏙옙占쏙옙占쏙옙占쏙옙 占십댐옙 占쏙옙占� (占쏙옙 占쏙옙占쏙옙占쏙옙트占쏙옙占쏙옙 占쏙옙占쏙옙占� "-^"占쏙옙 표占쏙옙占싼댐옙. 占쏙옙) B extends A --> [B] -^
				// [A])
				if (!s.contains("-^") && !s.contains("-.-^") && !s.contains("interface")) {
					classArray.add(s);
					classCount++;
				}
				// 占쏙옙占쏙옙占� 占쏙옙占쏙옙占싹댐옙 占쏙옙占�
				else if (s.contains("-.-^"))
					implementsArray.add(s);
				else if (s.contains("-^"))
					extendsArray.add(s);
				else if (s.contains("<<interface>>")) {
					interfaceArray.add("[" + s.substring(15));
					interfaceCount++;
				}
			}
		}
		
		// umlInfo 占쏙옙占쏙옙 relation 占십깍옙화 ... 占쏙옙占쏙옙占쌔억옙占쏙옙. ( 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙 占싱삼옙)
		umlInfo.setInheritanceRealtion();
		umlInfo.setInterfaceRealtion();
		

		umlInfo.setClassnum(classCount);
		umlInfo.setInterfacenum(interfaceCount);

		
		umlInfo.getInheritanceRealtion().setInheritancenum(extendsArray.size());
		umlInfo.getInterfaceRealtion().setInterfacenum(implementsArray.size());

		
		
		for (i = 0; i < extendsArray.size(); i++) {
			String elementArray[] = extendsArray.get(i).split("\\-\\^");
			RelationElement relationElement = new RelationElement();
			
			int startIndex = elementArray[0].indexOf('[');
			int endIndex = elementArray[0].indexOf(']');
			
			relationElement.setFrom(elementArray[0].substring(startIndex + 1, endIndex));
			
			startIndex = elementArray[1].indexOf('[');
			endIndex = elementArray[1].indexOf(']');

			relationElement.setTo(elementArray[1].substring(startIndex + 1, endIndex));
			
			umlInfo.getInheritanceRealtion().addRelationelement(relationElement);
		}
		
		for (i = 0; i < implementsArray.size(); i++) {
			String elementArray[] = implementsArray.get(i).split("\\-\\.\\-\\^");
			RelationElement relationElement = new RelationElement();

			int startIndex = elementArray[0].indexOf('[');
			int endIndex = elementArray[0].indexOf(']');
			
			relationElement.setFrom(elementArray[0].substring(startIndex + 1, endIndex));
			
			startIndex = elementArray[1].indexOf(';');
			endIndex = elementArray[1].indexOf(']');

			
			relationElement.setTo(elementArray[1].substring(startIndex + 1, endIndex));
			
			umlInfo.getInterfaceRealtion().addRelationelement(relationElement);
		}
		
		for (i = 0; i < classArray.size(); i++) {
			ClassInfo classInfo = new ClassInfo();

			String elementArray[] = classArray.get(i).split("\\|");
			String className = elementArray[0].trim().substring(1, elementArray[0].length());
			int totalNumOfVariables = 0;
			int totalNumOfMethods = 0;

			// elementArray.length > 1占쏙옙 占쏙옙占� --> 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占싹댐옙 占쏙옙占�
			if (elementArray.length > 1)
				totalNumOfVariables = elementArray[1].trim().split(";").length;

			// commaCount != 0占쏙옙 占쏙옙占� --> 占쌨소드가 占쏙옙占쏙옙占싹댐옙 占쏙옙占�
			if (elementArray.length > 2)
				totalNumOfMethods = elementArray[2].trim().split(";").length;

			classInfo.setVarinum(totalNumOfVariables);
			classInfo.setMethodnum(totalNumOfMethods);
			classInfo.setClassname(className);
			classInfo.setAccess("public");

			// 占쏙옙占쏙옙("variables") 占쌩곤옙占싹댐옙 占쏙옙占쏙옙 (elementArray.length > 1占쏙옙 占쏙옙占� --> 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占싹댐옙 占쏙옙占�)
			if (elementArray.length > 1) {
				for (String elem : elementArray[1].split(";")) {
					elem = elem.replaceAll(" ", "");
					Variable variable = new Variable();
					String[] tmp = elem.trim().split(":");

					// set variable access modifier
					if (tmp[0].charAt(0) == '+') {
						variable.setAccess("public");
					} else if (tmp[0].charAt(0) == '-') {
						variable.setAccess("private");
					}

					// set variable name
					variable.setName(tmp[0].substring(1));

					// set variable type
					if (tmp[1].equals("int]"))
						tmp[1] = tmp[1].replaceAll("\\]", "");
					variable.setType(tmp[1]);

					classInfo.addVarilist(variable);
				}
			}

			// 占쌨소듸옙("methods") 占쌩곤옙占싹댐옙 占쏙옙占쏙옙
			if (elementArray.length > 2) {
				elementArray[2] = elementArray[2].replaceAll("]$", "").replaceAll(" ", "");
				String methodInfo = elementArray[2];
				// 占쏙옙占쏙옙 ')' 占쏙옙 占쏙옙占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙타占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙 (array[0] --> access, name, param 占쏙옙占쏙옙, array[1] -->
				// 占쏙옙占쏙옙타占쏙옙)
				String[] methodInfoArray = methodInfo.trim().split(";");

				// 占쌨소드가 1占쏙옙占쏙옙 占쏙옙占�
				if (methodInfoArray.length == 1) {
					Method method = new Method();

					int startIndex, endIndex;
					startIndex = methodInfoArray[0].indexOf("(");
					endIndex = methodInfoArray[0].indexOf(")");

					// set method access modifier
					if (methodInfoArray[0].charAt(0) == '+') {
						method.setAccess("public");
					} else if (methodInfoArray[0].charAt(0) == '-') {
						method.setAccess("private");
					}

					// set method name
					method.setName(methodInfoArray[0].substring(1, startIndex));

					// set return type
					method.setretType(methodInfoArray[0].substring(endIndex + 2));

					// set param num
					int paramNum = 0;
					for (int index = 0; index < methodInfoArray[0].substring(startIndex, endIndex).length(); index++)
						if (methodInfoArray[0].substring(startIndex, endIndex).charAt(index) == ':')
							paramNum++;

					method.setParamnum(paramNum);

					// set formal param 
					FormalParameter formalParameter = new FormalParameter();

					formalParameter
							.setvarName(methodInfoArray[0].substring(startIndex + 1, endIndex).split(":")[0]);
					formalParameter.setType(methodInfoArray[0].substring(startIndex, endIndex).split(":")[1]);
					method.addformalParam(formalParameter);

					classInfo.addMethodlist(method);

				}
				// 占쌨소드가 2占쏙옙 占싱삼옙占쏙옙 占쏙옙占�
				else {
					for (j = 0; j < methodInfoArray.length; j++) {
						Method method = new Method();
						int paramNum = 0;

						if (methodInfoArray.length > 1) {
							int startIndex, endIndex;

							startIndex = methodInfoArray[j].indexOf("(");
							endIndex = methodInfoArray[j].indexOf(")");

							// set method access modifier
							if (methodInfoArray[j].charAt(0) == '+') {
								method.setAccess("public");
							} else if (methodInfoArray[j].charAt(0) == '-') {
								method.setAccess("private");
							}

							// set method name
							method.setName(methodInfoArray[j].substring(1, startIndex));

							// set return type
							method.setretType(methodInfoArray[j].substring(endIndex + 2));

							// set param num
							for (int index = 0; index < methodInfoArray[j].substring(startIndex, endIndex)
									.length(); index++)
								if (methodInfoArray[j].substring(startIndex, endIndex).charAt(index) == ':')
									paramNum++;

							method.setParamnum(paramNum);

							// set formal param
							String[] formalParamArray = methodInfoArray[j].substring(startIndex + 1, endIndex)
									.split("\\.");

							for (int index = 0; index < paramNum; index++) {
								FormalParameter formalParameter = new FormalParameter();

								formalParameter.setvarName(formalParamArray[index].split(":")[0]);
								formalParameter.setType(formalParamArray[index].split(":")[1]);

								method.addformalParam(formalParameter);
							}

							classInfo.addMethodlist(method);
						}
					}
				}
			}
			umlInfo.addClassinfo(classInfo);
		}
		
		for (i = 0; i < interfaceArray.size(); i++) {
			InterfaceInfo interfaceInfo = new InterfaceInfo();

			String elementArray[] = interfaceArray.get(i).split("\\|");
			String interfaceName = elementArray[0].trim().substring(1, elementArray[0].length());
			int totalNumOfVariables = 0;
			int totalNumOfMethods = 0;

			// elementArray.length > 1占쏙옙 占쏙옙占� --> 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占싹댐옙 占쏙옙占�
			if (elementArray.length > 1)
				totalNumOfVariables = elementArray[1].trim().split(";").length;

			// commaCount != 0占쏙옙 占쏙옙占� --> 占쌨소드가 占쏙옙占쏙옙占싹댐옙 占쏙옙占�
			if (elementArray.length > 2)
				totalNumOfMethods = elementArray[2].trim().split(";").length;

			interfaceInfo.setVarinum(totalNumOfVariables);
			interfaceInfo.setMethodnum(totalNumOfMethods);
			interfaceInfo.setInterfacename(interfaceName);

			// 占쏙옙占쏙옙("variables") 占쌩곤옙占싹댐옙 占쏙옙占쏙옙 (elementArray.length > 1占쏙옙 占쏙옙占� --> 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占싹댐옙 占쏙옙占�)
			if (elementArray.length > 1) {
				for (String elem : elementArray[1].split(";")) {
					elem = elem.replaceAll(" ", "");
					Variable variable = new Variable();
					String[] tmp = elem.trim().split(":");

					/*// set variable access modifier
					if (tmp[0].charAt(0) == '+') {
						variable.setAccess("public");
					} else if (tmp[0].charAt(0) == '-') {
						variable.setAccess("private");
					}*/

					// set variable name
					variable.setName(tmp[0].substring(1));

					// set variable type
					variable.setType(tmp[1]);

					interfaceInfo.addVariable(variable);
				}
			}

			// 占쌨소듸옙("methods") 占쌩곤옙占싹댐옙 占쏙옙占쏙옙
			if (elementArray.length > 2) {
				elementArray[2] = elementArray[2].replaceAll("]$", "").replaceAll(" ", "");
				String methodInfo = elementArray[2];
				// 占쏙옙占쏙옙 ')' 占쏙옙 占쏙옙占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙타占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙 (array[0] --> access, name, param 占쏙옙占쏙옙, array[1] -->
				// 占쏙옙占쏙옙타占쏙옙)
				String[] methodInfoArray = methodInfo.trim().split(";");

				// 占쌨소드가 1占쏙옙占쏙옙 占쏙옙占�
				if (methodInfoArray.length == 1) {
					Method method = new Method();

					int startIndex, endIndex;
					startIndex = methodInfoArray[0].indexOf("(");
					endIndex = methodInfoArray[0].indexOf(")");

					/*// set method access modifier
					if (methodInfoArray[0].charAt(0) == '+') {
						method.setAccess("public");
					} else if (methodInfoArray[0].charAt(0) == '-') {
						method.setAccess("private");
					}*/

					// set method name
					method.setName(methodInfoArray[0].substring(1, startIndex));

					// set return type
					method.setretType(methodInfoArray[0].substring(endIndex + 2));

					// set param num
					int paramNum = 0;
					for (int index = 0; index < methodInfoArray[0].substring(startIndex, endIndex).length(); index++)
						if (methodInfoArray[0].substring(startIndex, endIndex).charAt(index) == ':')
							paramNum++;

					method.setParamnum(paramNum);

					// set formal param
					FormalParameter formalParameter = new FormalParameter();

					formalParameter
							.setvarName(methodInfoArray[0].substring(startIndex + 1, endIndex).split(":")[0]);
					formalParameter.setType(methodInfoArray[0].substring(startIndex, endIndex).split(":")[1]);
					method.addformalParam(formalParameter);

					interfaceInfo.addMethod(method);

				}
				// 占쌨소드가 2占쏙옙 占싱삼옙占쏙옙 占쏙옙占�
				else {
					for (j = 0; j < methodInfoArray.length; j++) {
						Method method = new Method();
						int paramNum = 0;

						if (methodInfoArray.length > 1) {
							int startIndex, endIndex;

							startIndex = methodInfoArray[j].indexOf("(");
							endIndex = methodInfoArray[j].indexOf(")");

							/*// set method access modifier
							if (methodInfoArray[j].charAt(0) == '+') {
								method.setAccess("public");
							} else if (methodInfoArray[j].charAt(0) == '-') {
								method.setAccess("private");
							}*/

							// set method name
							method.setName(methodInfoArray[j].substring(1, startIndex));

							// set return type
							method.setretType(methodInfoArray[j].substring(endIndex + 2));

							// set param num
							for (int index = 0; index < methodInfoArray[j].substring(startIndex, endIndex)
									.length(); index++)
								if (methodInfoArray[j].substring(startIndex, endIndex).charAt(index) == ':')
									paramNum++;

							method.setParamnum(paramNum);

							// set formal param
							String[] formalParamArray = methodInfoArray[j].substring(startIndex + 1, endIndex)
									.split("\\.");

							for (int index = 0; index < paramNum; index++) {
								FormalParameter formalParameter = new FormalParameter();

								formalParameter.setvarName(formalParamArray[index].split(":")[0]);
								formalParameter.setType(formalParamArray[index].split(":")[1]);

								method.addformalParam(formalParameter);
							}

							interfaceInfo.addMethod(method);
						}
					}
				}
			}
			umlInfo.addInterfaceinfo(interfaceInfo);
		}

		Gson gson = new Gson();
		String jsonText = gson.toJson(umlInfo);
		

		return jsonText;
	}
}