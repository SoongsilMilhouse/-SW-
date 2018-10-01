package umlparser.umlparser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class CreateJavaCode {

   public UmlInfo umlInfo = null;
   public Gson gson;
   public ClassInfo classInfo;
   public InheritanceRelation inheritanceRelation;
   public ArrayList<RelationElement> inheritanceList;
   public InterfaceRelation interfaceRelation;
   public ArrayList<RelationElement> interfaceList;

   public InterfaceInfo interfaceinfo;
   public ArrayList<InterfaceInfo> interfaces;

   public CreateJavaCode() {
      gson = new Gson();

      try {
         umlInfo = gson.fromJson(new FileReader(
               "C:\\Users\\Jiyong Kim\\Desktop\\SODA조_캡스톤디자인프로젝트\\umlparser\\src\\main\\java\\umlparser\\umlparser\\umlinput.txt"),
               UmlInfo.class);

         inheritanceRelation = umlInfo.getInheritanceRealtion();
         interfaceRelation = umlInfo.getInterfaceRealtion();

      } catch (JsonSyntaxException se) {
         se.printStackTrace();
      } catch (JsonIOException ie) {
         ie.printStackTrace();
      } catch (FileNotFoundException fe) {
         fe.printStackTrace();
      }
   }

   public void makeFile() {
      for (int i = 0; i < umlInfo.getClassnum(); ++i) {
         classInfo = umlInfo.getClassinfo(i);
         makeClassFile(classInfo);
      }

      for (int i = 0; i < umlInfo.getInterfacenum(); ++i) {
         interfaceinfo = umlInfo.getInterfaceinfo(i);
         makeInterfaceFile(interfaceinfo);
      }
   }

   public void makeClassFile(ClassInfo classInfo) {
      try {
         String path = "C:\\Users\\Jiyong Kim\\Desktop\\SODA조_캡스톤디자인프로젝트\\umlparser\\src\\main\\java\\umlparser\\umlparser\\";

         File file = new File(path + classInfo.getClassname() + ".java");
         BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));

         if (file.isFile() && file.canWrite()) {

            bufferedWriter.write(classInfo.getAccess() + " class " + classInfo.getClassname());

            String string = " extends ";
            int count = 0;
            for (int i = 0; i < inheritanceRelation.getInheritancenum(); ++i) {
               RelationElement relation = inheritanceRelation.getRelationelement(i);
               if (relation.getFrom().equals(classInfo.getClassname())) {
                  string = string.concat(relation.getTo() + ",");
                  count++;
               }
            }
            if (string.charAt(string.length() - 1) == ',')
               string = string.substring(0, string.length() - 1);

            if (count != 0)
               bufferedWriter.write(string);

            string = " implements ";
            count = 0;
            for (int i = 0; i < interfaceRelation.getInterfacenum(); ++i) {
               RelationElement relation = interfaceRelation.getRelationelement(i);
               if (relation.getFrom().equals(classInfo.getClassname())) {
                  string = string.concat(relation.getTo() + ",");
                  count++;
               }
            }

            if (string.charAt(string.length() - 1) == ',')
               string = string.substring(0, string.length() - 1);

            if (count != 0)
               bufferedWriter.write(string + " {");
            else
               bufferedWriter.write(" {");
            bufferedWriter.newLine();
            bufferedWriter.newLine();

            for (int i = 0; i < classInfo.getVarinum(); ++i) {
               Variable variable = classInfo.getVarilist(i);

               bufferedWriter.write(
                     "\t" + variable.getAccess() + " " + variable.getType() + " " + variable.getName() + ";");
               bufferedWriter.newLine();
               bufferedWriter.newLine();

            }

            for (int i = 0; i < classInfo.getMethodnum(); ++i) {
               Method method = classInfo.getMethodlist(i);

               bufferedWriter
                     .write("\t" + method.getAccess() + " " + method.getretType() + " " + method.getName());

               string = "(";
               for (int j = 0; j < method.getParamnum(); ++j) {
                  FormalParameter formalParmeter = method.getformalParam(j);
                  string = string + formalParmeter.getType() + " " + formalParmeter.getvarName() + ",";
               }
               if (string.charAt(string.length() - 1) == ',')
                  string = string.substring(0, string.length() - 1);

               string = string.concat(")");
               bufferedWriter.write(string + " {");
               bufferedWriter.newLine();
               bufferedWriter.newLine();
               bufferedWriter.write("\t}");
               bufferedWriter.newLine();
               bufferedWriter.newLine();
            }
            bufferedWriter.newLine();
            bufferedWriter.newLine();
            bufferedWriter.write("}");
         }
         bufferedWriter.close();
      } catch (IOException e) {
         System.out.println(e);
      }
   }

   public void makeInterfaceFile(InterfaceInfo interfaces) {
      try {
         String path = "D:\\capston\\uml-parser-master\\uml-parser-master\\umlparser\\src\\main\\java\\umlparser\\umlparser\\umlToCode\\";

         File file = new File(path + interfaces.getInterfacename() + ".java");
         BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));

         if (file.isFile() && file.canWrite()) {

            bufferedWriter.write("public " + "interface " + interfaces.getInterfacename());

            String string = " implements ";
            int count = 0;
            for (int i = 0; i < interfaceRelation.getInterfacenum(); ++i) {
               RelationElement relation = interfaceRelation.getRelationelement(i);
               if (relation.getFrom().equals(interfaceinfo.getInterfacename())) {
                  string = string.concat(relation.getTo() + ",");
                  count++;
               }
            }

            if (string.charAt(string.length() - 1) == ',')
               string = string.substring(0, string.length() - 1);

            if (count != 0)
               bufferedWriter.write(string + " {");
            else
               bufferedWriter.write(" {");
            bufferedWriter.newLine();
            bufferedWriter.newLine();

            for (int i = 0; i < interfaces.getVarinum(); ++i) {
               Variable variable = interfaces.getVariable(i);

               bufferedWriter.write(
                     "\t" + "public static final" + " " + variable.getType() + " " + variable.getName() + ";");
               bufferedWriter.newLine();
               bufferedWriter.newLine();

            }

            for (int i = 0; i < interfaces.getMethodnum(); ++i) {
               Method method = interfaces.getMethod(i);

               bufferedWriter.write("\t" + "public abstract" + " " + method.getretType() + " " + method.getName());

               string = "(";
               for (int j = 0; j < method.getParamnum(); ++j) {
                  FormalParameter formalParmeter = method.getformalParam(j);
                  string = string + formalParmeter.getType() + " " + formalParmeter.getvarName() + ",";
               }
               if (string.charAt(string.length() - 1) == ',')
                  string = string.substring(0, string.length() - 1);

               string = string.concat(")");
               bufferedWriter.write(string + " {");
               bufferedWriter.newLine();
               bufferedWriter.newLine();
               bufferedWriter.write("\t}");
               bufferedWriter.newLine();
               bufferedWriter.newLine();
            }
            bufferedWriter.newLine();
            bufferedWriter.newLine();
            bufferedWriter.write("}");
         }
         bufferedWriter.close();
      } catch (IOException e) {
         System.out.println(e);
      }
   }

   public static void main(String[] args) {

      CreateJavaCode cjc = new CreateJavaCode();
      cjc.makeFile();
   }

}