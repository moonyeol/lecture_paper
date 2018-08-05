package termProject;

import java.util.Calendar;
import java.io.*;
public class History {
	String path;
	History(String path){
		this.path = path;
	}
	void change_history(String path){
		this.path = path;
	}
	void add_history(String massage) throws IOException {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(cal.YEAR);
		int month = cal.get(cal.MONTH) + 1;
		int date = cal.get(cal.DATE);
		int hour = cal.get(cal.HOUR_OF_DAY);
		int min = cal.get(cal.MINUTE);
		FileWriter fout = new FileWriter(path, true);
		BufferedWriter out = new BufferedWriter(fout);
		out.write(year + "." + month + "." + date + " " + hour + ":" + min
				+ massage);
		out.newLine();
		out.close();
		fout.close();
	}
	void print_histort(String path) throws IOException{

	}
}
