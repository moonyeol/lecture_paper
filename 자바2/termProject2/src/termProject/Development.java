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
		System.out.println("�߰��� ������ Ÿ���� ������. (1)������ (2)�׽���");
		dt = s.nextInt();
		if (dt == 1) {

			developer[count1] = new SWDeveloper(Dname, Did);
			count1++;
			history.add_history("�ű����� �߰�: "+ Dname +"("+Did+")");

		} else {
			tester[count2] = new SWTester(Dname, Did);
			count2++;
			history.add_history("�ű����� �߰�: "+ Dname +"("+Did+")");
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
		System.out.println("������ ������ ����ּ���. (1)������ (2)�׽���");
		dc = s.nextInt();
		if (dc == 1) {
			developer[count1] = new SWDeveloper(input_n, input_i);
			history.add_history("���� �μ� �̵� "+ developer[count1].name +"("+developer[count1].id+")");
			count1++;
		} else {
			tester[count1] = new SWTester(input_n, input_i);
			history.add_history("���� �μ� �̵� "+ tester[count2].name +"("+tester[count2].id+")");
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
			System.out.println("������ ���� �̸� : " + developer[j].name + "\tid : "
					+ developer[j].id);
			System.out.println("������ �̸�, id : ");
			n_name =s.next();
			n_id = s.nextLine();
			history.add_history("���� ���� ����"+ developer[j].name +"("+developer[j].id+") =>" + n_name +"("+n_id+")");
			developer[j].name = n_name;
			developer[j].id = n_id ;
		} else {
			System.out.println("������ ���� �̸� : " + tester[j].name + "\tid : "
					+ tester[j].id);
			System.out.println("������ �̸�, id : ");
			System.out.println("������ �̸�, id : ");
			n_name =s.next();
			n_id = s.nextLine();
			history.add_history("���� ���� ����"+ tester[j].name +"("+tester[j].id+") =>" + n_name +"("+n_id+")");
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
