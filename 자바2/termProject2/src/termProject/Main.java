package termProject;

import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.*;

public class Main {

	public static void main(String agrs[]) throws IOException {
		Scanner s = new Scanner(System.in);
		int menu, ans;
		String input_n, input_i;
		Development[] development = new Development[3];
		Management[] management = new Management[3];
		development[0] = new Development("개발1팀", "매니저4", "0004");
		development[1] = new Development("개발2팀", "매니저5", "0005");
		development[2] = new Development("개발3팀", "매니저6", "0006");
		management[0] = new Management("인사팀", "매니저1", "0001");
		management[1] = new Management("비서실", "매니저2", "0002");
		management[2] = new Management("총무팀", "매니저3", "0003");
		History history = new History();
		HRSW hrsw = new HRSW();
		// 파일 불러오기

		hrsw.file_in(development, management);


		while (true) {
			System.out.println("사용할 기능을 선택해주세요.");
			System.out
					.println("(1)신규 직원 추가 (2)직원 삭제 및 직원 수정 (3)직원 부서 이동 (4)직원 검색 (5)히스토리 출력 (6)종료");
			menu = s.nextInt();
			try {
				if (menu < 1 || menu > 7)
					throw new Exception();
			} catch (Exception e) {
				System.out.println("다시 선택해 주세요.");
				continue;
			}

			switch (menu) {
			case 1:
				add_Employee(s, development, management);
				break;
			case 2:
				System.out.println("직원의 이름과 아이디을 입력하세요");
				input_n = s.next();
				input_i = s.nextLine();
				if (!check(development, management, input_n, input_i)) {
					System.out.println("직원이 존재하지 않습니다.");
					break;
				}
				System.out.println("(1)수정 (2)삭제");
				ans = s.nextInt();
				del_Employee(s, development, management, input_n, input_i, ans);
				if (ans == 2)
					history.add_history("직원 삭제 " + input_n + "(" + input_i
							+ ")");
				break;
			case 3:
				move(s, development, management);
				break;
			case 4:
				search(s, development, management);
				break;
			case 5:
				print_History();
				break;
			default:
				break;
			}
			if (menu == 6)
				break;
		}
		// 저장
		hrsw.file_out(development, management);

	}

	static boolean check(Development[] development, Management[] management,
			String input_i) {

		int count, count1, count2;

		for (int i = 0; i < 3; i++) {
			count = management[i].count;
			for (int j = 0; j < count; j++)
				if (input_i.equals(management[i].staff[j].id)) {
					return true;
				}
		}
		for (int i = 0; i < 3; i++) {
			count1 = development[i].count1;
			count2 = development[i].count2;
			for (int j = 0; j < count1; j++)
				if (input_i.equals(development[i].developer[j].id)) {
					return true;
				}
			for (int j = 0; j < count2; j++)
				if (input_i.equals(development[i].tester[j].id)) {
					return true;
				}
		}
		return false;
	}

	static boolean check(Development[] development, Management[] management,
			String input_n, String input_i) {

		int count, count1, count2;

		for (int i = 0; i < 3; i++) {
			count = management[i].count;
			for (int j = 0; j < count; j++)
				if (input_n.equals(management[i].staff[j].name)
						&& input_i.equals(management[i].staff[j].id)) {
					return true;
				}
		}
		for (int i = 0; i < 3; i++) {
			count1 = development[i].count1;
			count2 = development[i].count2;
			for (int j = 0; j < count1; j++)
				if (input_n.equals(development[i].developer[j].name)
						&& input_i.equals(development[i].developer[j].id)) {
					return true;
				}
			for (int j = 0; j < count2; j++)
				if (input_n.equals(development[i].tester[j].name)
						&& input_i.equals(development[i].tester[j].id)) {
					return true;
				}
		}
		return false;
	}

	static void del_Employee(Scanner s, Development[] development,
			Management[] management, String input_n, String input_i, int ans)
			throws IOException {

		int count, count1, count2;

		for (int i = 0; i < 3; i++) {
			count = management[i].count;
			for (int j = 0; j < count; j++)
				if (input_n.equals(management[i].staff[j].name)
						&& input_i.equals(management[i].staff[j].id)) {
					if (ans == 1) {
						management[i].change(j);
						return;
					} else {
						management[i].del(j);
						return;
					}

				}
		}
		for (int i = 0; i < 3; i++) {
			count1 = development[i].count1;
			count2 = development[i].count2;
			for (int j = 0; j < count1; j++)
				if (input_n.equals(development[i].developer[j].name)
						&& input_i.equals(development[i].developer[j].id)) {
					if (ans == 1) {
						development[i].change(j, 0);
						return;
					} else {
						development[i].del(j, 0);
						return;
					}

				}
			for (int j = 0; j < count2; j++)
				if (input_n.equals(development[i].tester[j].name)
						&& input_i.equals(development[i].tester[j].id)) {
					if (ans == 1) {
						development[i].change(j, 0);
						return;
					} else {
						development[i].del(j, 0);
						return;
					}
				}
		}
	}

	static void add_Employee(Scanner s, Development[] development,
			Management[] management) throws IOException {
		int c;
		String name, id;
		System.out.println("추가할 직원의 이름과 id를 입력하세요");
		name = s.next();
		id = s.next();
		if (check(development, management, id)) {
			System.out.println("id가 같은 직원이 존재합니다.");
			return;
		}
		System.out.println("직원을 추가할 부서를 고르세요");
		System.out.println("(1)인사팀 (2)비서실 (3)총무팀 (4)개발1팀 (5)개발2팀 (6)개발3팀");
		c = s.nextInt();
		switch (c) {
		case 1:
			management[0].add_Employee(name, id);
			break;
		case 2:
			management[1].add_Employee(name, id);
			break;
		case 3:
			management[2].add_Employee(name, id);
			break;
		case 4:
			development[0].add_Employee(name, id);
			break;
		case 5:
			development[1].add_Employee(name, id);
			break;
		case 6:
			development[2].add_Employee(name, id);
			break;
		}
	}

	static void move(Scanner s, Development[] development,
			Management[] management) throws IOException {
		String input_n, input_i;
		int ans;
		String an;
		System.out.println("이동할 직원의 이름과 아이디를 입력하세요.");
		input_n = s.next();
		input_i = s.next();
		if (!check(development, management, input_n, input_i)) {
			System.out.println("직원이 존재하지 않습니다.");
			return;
		}

		System.out
				.println("이동할 부서를 고르세요. (1)인사팀 (2)비서실 (3)총무팀 (4)개발1팀 (5)개발2팀 (6)개발3팀");
		ans = s.nextInt();
		switch (ans) {
		case 1:
			System.out.println(management[0].m_name + "(매니저)가 허락 하시겠습니까?(y/n");
			an = s.next();
			if (an.equals("y")) {
				del_Employee(s, development, management, input_n, input_i, 2);
				management[0].move(input_n, input_i);
			} else
				return;
			break;
		case 2:
			System.out.println(management[1].m_name + "(매니저)가 허락 하시겠습니까?(y/n");
			an = s.next();
			if (an.equals("y")) {
				del_Employee(s, development, management, input_n, input_i, 2);
				management[1].move(input_n, input_i);
			} else
				return;
			break;
		case 3:
			System.out.println(management[2].m_name + "(매니저)가 허락 하시겠습니까?(y/n");
			an = s.next();
			if (an.equals("y")) {
				del_Employee(s, development, management, input_n, input_i, 2);
				management[2].move(input_n, input_i);
			} else
				return;
			break;
		case 4:
			System.out.println(development[0].m_name + "(매니저)가 허락 하시겠습니까?(y/n");
			an = s.next();
			if (an.equals("y")) {
				del_Employee(s, development, management, input_n, input_i, 2);
				development[0].move(input_n, input_i);
			} else
				return;
			break;
		case 5:
			System.out.println(development[1].m_name + "(매니저)가 허락 하시겠습니까?(y/n");
			an = s.next();
			if (an.equals("y")) {
				del_Employee(s, development, management, input_n, input_i, 2);
				development[1].move(input_n, input_i);
			} else
				return;
			break;
		case 6:
			System.out.println(development[2].m_name + "(매니저)가 허락 하시겠습니까?(y/n");
			an = s.next();
			if (an.equals("y")) {
				del_Employee(s, development, management, input_n, input_i, 2);
				development[2].move(input_n, input_i);
			} else
				return;
			break;
		}
	}

	static void print_History() throws IOException {
		FileReader fin = new FileReader("history.txt");
		int c;
		while ((c = fin.read()) != -1)
			System.out.print((char) c);
		fin.close();
	}

	static void search(Scanner s, Development[] development,
			Management[] management) {
		System.out.print("사원 검색(1),부서 검색(2),maneger 검색(3)");
		int a;
		String b;
		while (true) {
			a = s.nextInt();

			switch (a) {
			case 1:
				System.out.print("검색할 사원의 이름이나 Id를 입력");
				b = s.next();
				while (true) {
					for (int i = 0; i < 3; i++) {
						development[i].searchname(b);
						management[i].searchname(b);
					}
					System.out.println();
					break;
				}

				break;

			case 2:
				System.out.println("어떤 부서를 검색하시겠습니까?");
				System.out
						.print("(1)인사팀 (2)비서실 (3)총무팀 (4)개발1팀 (5)개발2팀 (6)개발3팀 (7)종료");
				a = s.nextInt();
				while (true) {
					switch (a) {
					case 1:
						System.out.print(management[0].m_name + " ");
						management[0].printM();
						break;
					case 2:
						System.out.print(management[1].m_name + " ");
						management[1].printM();
						break;
					case 3:
						System.out.print(management[2].m_name + " ");
						management[2].printM();
						break;
					case 4:
						System.out.print(development[0].m_name + " ");
						development[0].printM();
						break;
					case 5:
						System.out.print(development[1].m_name + " ");
						development[1].printM();
						break;
					case 6:
						System.out.print(development[2].m_name + " ");
						development[2].printM();
						break;
					}
					break;

				}
				break;

			case 3:
				System.out.println("어떤 매니저를 검색하시겠습니까?");
				System.out.print("(1)"+management[0].m_name +" (2)"+management[1].m_name + " (3)"+management[2].m_name + " (4)"+development[0].m_name + " (5)"
						+development[1].m_name + " (6)"+development[2].m_name);
				a = s.nextInt();
				while (true) {
					switch (a) {
					case 1:
						management[0].printM();
						break;
					case 2:
						management[1].printM();
						break;
					case 3:
						management[2].printM();
						break;
					case 4:
						development[0].printM();
						break;
					case 5:
						development[1].printM();
						break;
					case 6:
						development[2].printM();
						break;
					}
					break;
				}
			}
			break;
		}
	}
}
