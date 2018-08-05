package termProject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

public class HRSW {
	void file_in(Development[] development, Management[] management, String file)
			throws NumberFormatException, IOException {
		FileReader fin = new FileReader(file);
		BufferedReader in = new BufferedReader(fin);
		StringTokenizer st = null;
		String ss;

		int t;
		while ((ss = in.readLine()) != null) {
			st = new StringTokenizer(ss, " ");
			t = Integer.parseInt(st.nextToken());
			if (t < 3) {
				management[t].add_Employee(st.nextToken(), st.nextToken());

			} else {
				if (Integer.parseInt(st.nextToken()) == 0) {
					development[t - 3].add_Employee(st.nextToken(),
							st.nextToken(), 1);
				} else {
					development[t - 3].add_Employee(st.nextToken(),
							st.nextToken(), 0);
				}
			}
		}
	}

	void file_out(Development[] development, Management[] management,
			String file) throws IOException {
		FileWriter fout = new FileWriter(file+".txt");
		BufferedWriter out = new BufferedWriter(fout);

		for (int j = 0; j < 3; j++) {
			for (int i = 0; i < management[j].count; i++) {
				
				out.write(Integer.toString(j));
				out.write(" ");
				out.write(management[j].staff[i].name);
				out.write(" ");
				out.write(management[j].staff[i].id);
				out.newLine();
			}
		}
		for (int j = 0; j < 3; j++) {
			for (int i = 0; i < development[j].count1; i++) {
				out.write(Integer.toString(i+3));
				out.write(" ");
				out.write('0');
				out.write(" ");
				out.write(development[j].developer[i].name);
				out.write(" ");
				out.write(development[j].developer[i].id);
				out.newLine();
			}
			for (int i = 0; i < development[j].count2; i++) {
				out.write(Integer.toString(j+3));
				out.write(" ");
				out.write('1');
				out.write(" ");
				out.write(development[j].tester[i].name);
				out.write(" ");
				out.write(development[j].tester[i].id);
				out.newLine();
			}
		}
		out.close();
		fout.close();
	}
}
