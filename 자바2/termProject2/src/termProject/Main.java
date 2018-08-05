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
		development[0] = new Development("����1��", "�Ŵ���4", "0004");
		development[1] = new Development("����2��", "�Ŵ���5", "0005");
		development[2] = new Development("����3��", "�Ŵ���6", "0006");
		management[0] = new Management("�λ���", "�Ŵ���1", "0001");
		management[1] = new Management("�񼭽�", "�Ŵ���2", "0002");
		management[2] = new Management("�ѹ���", "�Ŵ���3", "0003");
		History history = new History();
		HRSW hrsw = new HRSW();
		// ���� �ҷ�����

		hrsw.file_in(development, management);


		while (true) {
			System.out.println("����� ����� �������ּ���.");
			System.out
					.println("(1)�ű� ���� �߰� (2)���� ���� �� ���� ���� (3)���� �μ� �̵� (4)���� �˻� (5)�����丮 ��� (6)����");
			menu = s.nextInt();
			try {
				if (menu < 1 || menu > 7)
					throw new Exception();
			} catch (Exception e) {
				System.out.println("�ٽ� ������ �ּ���.");
				continue;
			}

			switch (menu) {
			case 1:
				add_Employee(s, development, management);
				break;
			case 2:
				System.out.println("������ �̸��� ���̵��� �Է��ϼ���");
				input_n = s.next();
				input_i = s.nextLine();
				if (!check(development, management, input_n, input_i)) {
					System.out.println("������ �������� �ʽ��ϴ�.");
					break;
				}
				System.out.println("(1)���� (2)����");
				ans = s.nextInt();
				del_Employee(s, development, management, input_n, input_i, ans);
				if (ans == 2)
					history.add_history("���� ���� " + input_n + "(" + input_i
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
		// ����
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
		System.out.println("�߰��� ������ �̸��� id�� �Է��ϼ���");
		name = s.next();
		id = s.next();
		if (check(development, management, id)) {
			System.out.println("id�� ���� ������ �����մϴ�.");
			return;
		}
		System.out.println("������ �߰��� �μ��� ������");
		System.out.println("(1)�λ��� (2)�񼭽� (3)�ѹ��� (4)����1�� (5)����2�� (6)����3��");
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
		System.out.println("�̵��� ������ �̸��� ���̵� �Է��ϼ���.");
		input_n = s.next();
		input_i = s.next();
		if (!check(development, management, input_n, input_i)) {
			System.out.println("������ �������� �ʽ��ϴ�.");
			return;
		}

		System.out
				.println("�̵��� �μ��� ������. (1)�λ��� (2)�񼭽� (3)�ѹ��� (4)����1�� (5)����2�� (6)����3��");
		ans = s.nextInt();
		switch (ans) {
		case 1:
			System.out.println(management[0].m_name + "(�Ŵ���)�� ��� �Ͻðڽ��ϱ�?(y/n");
			an = s.next();
			if (an.equals("y")) {
				del_Employee(s, development, management, input_n, input_i, 2);
				management[0].move(input_n, input_i);
			} else
				return;
			break;
		case 2:
			System.out.println(management[1].m_name + "(�Ŵ���)�� ��� �Ͻðڽ��ϱ�?(y/n");
			an = s.next();
			if (an.equals("y")) {
				del_Employee(s, development, management, input_n, input_i, 2);
				management[1].move(input_n, input_i);
			} else
				return;
			break;
		case 3:
			System.out.println(management[2].m_name + "(�Ŵ���)�� ��� �Ͻðڽ��ϱ�?(y/n");
			an = s.next();
			if (an.equals("y")) {
				del_Employee(s, development, management, input_n, input_i, 2);
				management[2].move(input_n, input_i);
			} else
				return;
			break;
		case 4:
			System.out.println(development[0].m_name + "(�Ŵ���)�� ��� �Ͻðڽ��ϱ�?(y/n");
			an = s.next();
			if (an.equals("y")) {
				del_Employee(s, development, management, input_n, input_i, 2);
				development[0].move(input_n, input_i);
			} else
				return;
			break;
		case 5:
			System.out.println(development[1].m_name + "(�Ŵ���)�� ��� �Ͻðڽ��ϱ�?(y/n");
			an = s.next();
			if (an.equals("y")) {
				del_Employee(s, development, management, input_n, input_i, 2);
				development[1].move(input_n, input_i);
			} else
				return;
			break;
		case 6:
			System.out.println(development[2].m_name + "(�Ŵ���)�� ��� �Ͻðڽ��ϱ�?(y/n");
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
		System.out.print("��� �˻�(1),�μ� �˻�(2),maneger �˻�(3)");
		int a;
		String b;
		while (true) {
			a = s.nextInt();

			switch (a) {
			case 1:
				System.out.print("�˻��� ����� �̸��̳� Id�� �Է�");
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
				System.out.println("� �μ��� �˻��Ͻðڽ��ϱ�?");
				System.out
						.print("(1)�λ��� (2)�񼭽� (3)�ѹ��� (4)����1�� (5)����2�� (6)����3�� (7)����");
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
				System.out.println("� �Ŵ����� �˻��Ͻðڽ��ϱ�?");
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
