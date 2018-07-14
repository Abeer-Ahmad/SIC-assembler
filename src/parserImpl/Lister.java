package parserImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import parserInterafces.ILister;
import parserInterafces.IStatement;

public class Lister implements ILister {

	private String listingString;
	private String temp;
	private List<IStatement> test;
	public Lister() {
		test = new ArrayList<IStatement>();
		listingString="";
	}
	@Override
	public File generateFile(List<IStatement> statements, File fileName) throws IOException {

		for (int i = 0; i < statements.size(); i++) {
			IStatement item = statements.get(i);
			listingString += item.location();
			listingString += '\t';
			temp = (item.label());
			if(temp == null)
			  temp = "";

			if (temp.length() < 8) {
				while (temp.length() < 8) {
					temp += ' ';
				}
			}
			listingString += temp;
			listingString += "\t\t ";
			temp = item.operation();
			if(temp == null)
        temp = "";
			while (temp.length() < 6) {
				temp += ' ';
			}

			listingString += temp;
			listingString += "  ";

			temp = item.operands();
			if(temp == null)
        temp = "";

			while (temp.length() < 18) {
				temp += ' ';

			}
			listingString+=temp;
			listingString += "\t\t ";
			temp = "";
			temp = item.objectCode();
			while (temp.length() < 6) {
				temp += ' ';
			}
			listingString += temp;
			listingString += "\t";
			listingString += item.error();

			listingString += '\n';
		}
		

		FileWriter write = new FileWriter(fileName);
		write.append(listingString);
		write.close();
		
		return fileName;
	}
//	public void readExample(File SICProg) throws FileNotFoundException{
//		Scanner sc = new Scanner(SICProg);
//		while(sc.hasNextLine()){
//			String line = sc.nextLine();
//			IStatement item = new StateImpl();
//			item.setLocation(line.substring(0,4).trim());
//			item.setLabel(line.substring(6,14).trim());
//			item.setOperation(line.substring(16,22).trim());
//			item.setOperands(line.substring(24,43).trim());
//			item.setObjectCode(line.substring(43).trim());
//			test.add(item);
//		}
//		sc.close();
//	}
//	public static void main(String[]arg) throws IOException{
//		File ListingFileTry= new File("testFile.txt");
//		Lister x = new Lister();
//		x.readExample(new File("/home/youssef/Downloads/tests/Outputs/myTest.txt"));
//		x.generateFile(x.test, ListingFileTry);
//		
//	}
//	
}
