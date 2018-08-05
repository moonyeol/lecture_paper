package termProject;




import java.io.IOException;
import java.util.Scanner;

public class Development extends Department {

	Development(String name, String m_name, String m_id,History history) {
		this.name = name;
		this.m_name = m_name;
		this.m_id = m_id;
		this.history = history;
		
	}

	SWDeveloper developer[] = new SWDeveloper[50];
	SWTester tester[] = new SWTester[50];
	int count1 = 0, count2 = 0;
	
	void add_Employee(String Dname, String Did,int dt) throws IOException {
		if (dt == 0) {
			developer[count1] = new SWDeveloper(Dname, Did);
			count1++;
			history.add_history("신규직원 추가: "+ Dname +"("+Did+",개발자)");

		} else {
			tester[count2] = new SWTester(Dname, Did);
			count2++;
			history.add_history("신규직원 추가: "+ Dname +"("+Did+",테스터)");
		}
	}

	void move(String input_n, String input_i) throws IOException {
		int dc;
		Scanner s = new Scanner(System.in);
		System.out.println("직원의 직급을 골라주세요. (1)개발자 (2)테스터");
		dc = s.nextInt();
		if (dc == 1) {
			developer[count1] = new SWDeveloper(input_n, input_i);
			history.add_history("직원 부서 이동 "+ developer[count1].name +"("+developer[count1].id+")");
			count1++;
		} else {
			tester[count1] = new SWTester(input_n, input_i);
			history.add_history("직원 부서 이동 "+ tester[count2].name +"("+tester[count2].id+")");
			count2++;
		}
	}

	void del(int dc, int d) {
		if (d == 0) {
			for (int i = dc; i < count1 - 1; i++)
				developer[i] = developer[i + 1];
			developer[count1] = null;
			count1--;
		} else {
			for (int i = dc; i < count2 - 1; i++)
				tester[i] = tester[i + 1];
			tester[count2] = null;
			count2--;
		}

	}

	void change(int j, int d,String n_name, String n_id) throws IOException {
		if (d == 0) {
			history.add_history("직원 정보 수정"+ developer[j].name +"("+developer[j].id+") =>" + n_name +"("+n_id+")");
			developer[j].name = n_name;
			developer[j].id = n_id ;
		} else {
			history.add_history("직원 정보 수정"+ tester[j].name +"("+tester[j].id+") =>" + n_name +"("+n_id+")");
			tester[j].name = n_name;
			tester[j].id = n_id;
		}
	}


}
