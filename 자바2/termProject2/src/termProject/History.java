package termProject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

public class History {
	void add_history(String massage) throws IOException {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(cal.YEAR);
		int month = cal.get(cal.MONTH) + 1;
		int date = cal.get(cal.DATE);
		int hour = cal.get(cal.HOUR_OF_DAY);
		int min = cal.get(cal.MINUTE);
		FileWriter fout = new FileWriter("history.txt", true);
		BufferedWriter out = new BufferedWriter(fout);
		out.write(year + "." + month + "." + date + " " + hour + ":" + min
				+ massage);
		out.newLine();
		out.close();
		fout.close();
	}

}
