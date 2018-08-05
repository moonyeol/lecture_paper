package termProject;




import java.io.IOException;
import java.util.Scanner;

public class Development extends Department {

	Development(String name, String m_name, String m_id) {
		this.name = name;
		this.m_name = m_name;
		this.m_id = m_id;
		
	}
	History history = new History();

	SWDeveloper developer[] = new SWDeveloper[50];
	SWTester tester[] = new SWTester[50];
	int count1 = 0, count2 = 0;
	
	void add_Employee(String Dname, String Did) throws IOException {

		Scanner s = new Scanner(System.in);
		int dt;
		System.out.println("추가할 직원의 타입을 고르세요. (1)개발자 (2)테스터");
		dt = s.nextInt();
		if (dt == 1) {

			developer[count1] = new SWDeveloper(Dname, Did);
			count1++;
			history.add_history("신규직원 추가: "+ Dname +"("+Did+")");

		} else {
			tester[count2] = new SWTester(Dname, Did);
			count2++;
			history.add_history("신규직원 추가: "+ Dname +"("+Did+")");
		}
	}
	void add_Employee(String Dname, String Did,int dt) throws IOException {
		if (dt == 1) {
			developer[count1] = new SWDeveloper(Dname, Did);
			count1++;

		} else {
			tester[count2] = new SWTester(Dname, Did);
			count2++;
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

	void change(int j, int d) throws IOException {
		Scanner s = new Scanner(System.in);
		String n_name, n_id;
		if (d == 0) {
			System.out.println("선택한 직원 이름 : " + developer[j].name + "\tid : "
					+ developer[j].id);
			System.out.println("수정할 이름, id : ");
			n_name =s.next();
			n_id = s.nextLine();
			history.add_history("직원 정보 수정"+ developer[j].name +"("+developer[j].id+") =>" + n_name +"("+n_id+")");
			developer[j].name = n_name;
			developer[j].id = n_id ;
		} else {
			System.out.println("선택한 직원 이름 : " + tester[j].name + "\tid : "
					+ tester[j].id);
			System.out.println("수정할 이름, id : ");
			System.out.println("수정할 이름, id : ");
			n_name =s.next();
			n_id = s.nextLine();
			history.add_history("직원 정보 수정"+ tester[j].name +"("+tester[j].id+") =>" + n_name +"("+n_id+")");
			tester[j].name = s.next();
			tester[j].id = s.nextLine();
		}
	}

	void printM() {
		for (int i = 0; i < count1; i++) {
			System.out.print(developer[i].name+ "(" + developer[i].id + ") ");

		}

		for (int i = 0; i < count2; i++) {
			System.out.print(tester[i].name + "(" + tester[i].id + ") ");
		}
		System.out.println();
	}

	void searchname(String nameid) {

		String nameId = nameid;
		for (int i = 0; i < count1; i++) {
			if (nameId.equals(developer[i].name)){
				System.out.print(developer[i].name + "(" + developer[i].id
						+","+ name+ ")"+" ");
			return;
			}
		}
		for (int i = 0; i < count2; i++) {
			if (nameId.equals(tester[i].name)){
				System.out.print(tester[i].name + "(" + tester[i].id +","+ name+ ")"
						 +" ");
				return;
			}
		}
		for (int i = 0; i < count1; i++) {
			if (nameId.equals(developer[i].id)){
				System.out.print(developer[i].name + "(" + developer[i].id
						+","+ name+ ")"  +" ");
				return;
			}
		}
		for (int i = 0; i < count2; i++) {
			if (nameId.equals(tester[i].id)){
				System.out.print(tester[i].name + "(" + tester[i].id + ","+ name+")"
						+" ");
				return;
			}

		}
	}
}
