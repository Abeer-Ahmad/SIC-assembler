package parserImpl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import parserInterafces.IStatement;

public class Assembler {

	public Assembler(List<IStatement> code) throws IOException {
		getObjectFile(code);
	}

	public File getObjectFile(List<IStatement> code) throws IOException {
		String out = "";
		out += new HeaderRecord(code.get(0), code.get(code.size() - 1)) + "\n";

		int n = code.size() - 1;
		for (int i = 1; i < n; i++) {
			TextRecord tr = new TextRecord(code.get(i++));
			while (i < n && tr.push(code.get(i)))
				i++;
			i--;
			if(tr.toString().length() > 0)
			out += tr + "\n";
		}
		out += new EndRecord(code.get(0), null) + "\n";
		File Obj = new File("ObjectCode.obj");
		FileWriter writ = new FileWriter(Obj);
		writ.write(out);
		writ.close();
		return Obj;
	}
}