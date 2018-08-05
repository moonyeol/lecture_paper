package termProject;


import java.io.IOException;
import java.util.Scanner;


public class Management extends Department {

	Management(String name, String m_name, String m_id,History history) {
		this.name = name;
		this.m_name = m_name;
		this.m_id = m_id;
		this.history = history;
	}

	Staff staff[] = new Staff[50];
	int count = 0;

	void add_Employee(String Dname, String Did) throws IOException  {

		staff[count] = new Staff(Dname, Did);
		count++;
		history.add_history("신규직원 추가: "+ Dname +"("+Did+")");

	}
	void move(String input_n, String input_i) throws IOException{

		staff[count] = new Staff(input_n, input_i);
		history.add_history("직원 부서 이동 "+ staff[count].name +"("+staff[count].id+")");
		count++;


	}
	void del(int dc) {
		for (int i = dc; i < count; i++)
			staff[i] = staff[i + 1];
		staff[count] = null;
		count--;
	}

	void change(int j,String n_name, String n_id) throws IOException {
		history.add_history("직원 정보 수정"+ staff[j].name +"("+staff[j].id+") =>" + n_name +"("+n_id+")");
		staff[j].name = n_name;
		staff[j].id = n_id;

	}
}
