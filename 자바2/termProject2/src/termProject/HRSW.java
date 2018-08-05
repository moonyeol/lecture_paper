package termProject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

public class HRSW {
	void file_in(Development[] development, Management[] management) throws NumberFormatException, IOException {
		FileReader fin = null;
		BufferedReader in = null;
		StringTokenizer st = null;
		String ss;
		for (int j = 0; j < 3; j++) {
			fin = new FileReader("management" + j + ".txt");
			in = new BufferedReader(fin);
			int i = 0;
			while ((ss = in.readLine()) != null) {
				st = new StringTokenizer(ss, " ");
				management[j].add_Employee(st.nextToken(),st.nextToken());
				i++;
			}

		}

		for (int j = 0; j < 3; j++) {
			int i = 0, k = 0;
			fin = new FileReader("development" + j + ".txt");
			in = new BufferedReader(fin);
			ss = in.readLine();
			st = new StringTokenizer(ss, " ");
			while ((ss = in.readLine()) != null) {
				st = new StringTokenizer(ss, " ");
				if (Integer.parseInt(st.nextToken()) == 0) {
					development[j].add_Employee(st.nextToken(),st.nextToken(),1);
					j++;
				} else {
					development[k].add_Employee(st.nextToken(),st.nextToken(),0);
					k++;
				}
			}
		}
	}

	void file_out(Development[] development, Management[] management)
			throws IOException {
		FileWriter fout = null;
		BufferedWriter out = null;
		for (int j = 0; j < 3; j++) {
			fout = new FileWriter("development" + j + ".txt");
			out = new BufferedWriter(fout);
			for (int i = 0; i < development[j].count1; i++) {
				out.write('0');
				out.write(" ");
				out.write(development[j].developer[i].name);
				out.write(" ");
				out.write(development[j].developer[i].id);
				out.newLine();
			}
			for (int i = 0; i < development[j].count2; i++) {
				out.write('1');
				out.write(" ");
				out.write(development[j].tester[i].name);
				out.write(" ");
				out.write(development[j].tester[i].id);
				out.newLine();
			}
			out.close();
			fout.close();
		}
		for (int j = 0; j < 3; j++) {
			fout = new FileWriter("management" + j + ".txt");
			out = new BufferedWriter(fout);
			for (int i = 0; i < management[j].count; i++) {
				out.write(management[j].staff[i].name);
				out.write(" ");
				out.write(management[j].staff[i].id);
				out.newLine();
			}
			out.close();
			fout.close();
		}
	}
}
