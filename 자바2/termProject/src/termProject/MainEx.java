package termProject;

import javax.swing.*;

import java.io.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.filechooser.*;

public class MainEx extends JFrame {
	Container contentPane;
	Development[] development = null;
	Management[] management = null;
	History history = null;
	HRSW hrsw = null;
	String path = null;

	MyModalDialog1 dialog1;

	JLabel J1, J2, J3, J4, ch;
	JButton b1, b2;
	JTextField tf1, tf2, tf3, tf4;
	JTextArea ta;
	String[] department = { "인사팀", "비서실", "총무팀", "개발1팀", "개발2팀", "개발3팀" };
	String[] man = { "개발자", "테스터" };
	String[] cho = { "직원 부서 이동", "직원 정도 변경" };
	JComboBox C1 = new JComboBox(department);
	JComboBox C2 = new JComboBox(man);
	JComboBox C3 = new JComboBox(cho);
	JScrollPane scroll;

	MainEx() throws FileNotFoundException {

		super("직원관리 프로그램");

		development = new Development[3];
		management = new Management[3];
		hrsw = new HRSW();
		path = "history.txt";
		history = new History(path);
		development[0] = new Development("개발1팀", "매니저4", "0004", history);
		development[1] = new Development("개발2팀", "매니저5", "0005", history);
		development[2] = new Development("개발3팀", "매니저6", "0006", history);
		management[0] = new Management("인사팀", "매니저1", "0001", history);
		management[1] = new Management("비서실", "매니저2", "0002", history);
		management[2] = new Management("총무팀", "매니저3", "0003", history);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		dialog1 = new MyModalDialog1(this, "직원 검색");

		J1 = new JLabel("직원 ID");
		J2 = new JLabel("직원 이름");
		J3 = new JLabel("수정할 ID");
		J4 = new JLabel("수정할 이름");
		ch = new JLabel("이미 존재하는 직원입니다");
		b1 = new JButton("확인");
		b2 = new JButton("취소");

		tf1 = new JTextField(5);
		tf2 = new JTextField(5);
		tf3 = new JTextField(5);
		tf4 = new JTextField(5);
		ta = new JTextArea();
		scroll = new JScrollPane(ta);
		contentPane = getContentPane();
		contentPane.setLayout(new FlowLayout());
		creatMenu();
		setSize(300, 300);
		setVisible(true);
	}

	void creatMenu() {
		JMenuBar mb = new JMenuBar();
		JMenu[] menu = new JMenu[4];
		JMenuItem[] menuitem1 = new JMenuItem[2];
		JMenuItem[] menuitem2 = new JMenuItem[4];
		JMenuItem[] menuitem3 = new JMenuItem[2];
		String[] menutitle = { "Database", "Operations", "History", "Quit" };
		String[] menuitemtile1 = { "Load", "Save" };
		String[] menuitemtile2 = { "신규직원 추가", "직원정보 삭제", "직원정보 변경", "직원 검색" };
		String[] menuitemtile3 = { "Change the Log File", "Show the Log File" };
		for (int i = 0; i < 4; i++) {
			menu[i] = new JMenu(menutitle[i]);
			mb.add(menu[i]);
		}
		for (int i = 0; i < 2; i++) {
			menuitem1[i] = new JMenuItem(menuitemtile1[i]);
			menuitem1[i].addActionListener(new MenuActionListenr());
			menu[0].add(menuitem1[i]);
		}
		for (int i = 0; i < 4; i++) {
			menuitem2[i] = new JMenuItem(menuitemtile2[i]);
			menuitem2[i].addActionListener(new MenuActionListenr());
			menu[1].add(menuitem2[i]);
		}
		for (int i = 0; i < 2; i++) {
			menuitem3[i] = new JMenuItem(menuitemtile3[i]);
			menuitem3[i].addActionListener(new MenuActionListenr());
			menu[2].add(menuitem3[i]);
		}
		JMenuItem item = new JMenuItem("Quit");
		item.addActionListener(new MenuActionListenr());
		menu[3].add(item);
		setJMenuBar(mb);

	}

	class MenuActionListenr implements ActionListener {
		int de, dev;

		public void actionPerformed(ActionEvent e) {
			try {
				String cmd = e.getActionCommand();
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"txt파일", "txt");
				chooser.setFileFilter(filter);
				String filepath;
				int ret;
				if (cmd.equals("Load")) {
					ret = chooser.showOpenDialog(null);
					if (ret != JFileChooser.APPROVE_OPTION) {
						JOptionPane.showMessageDialog(null, "파일을 선택하지 않았습니다.",
								"경고", JOptionPane.WARNING_MESSAGE);
						return;
					}
					filepath = chooser.getSelectedFile().getPath();

					hrsw.file_in(development, management, filepath);
					
				} else if (cmd.equals("Save")) {

					ret = chooser.showSaveDialog(null);
					filepath = chooser.getSelectedFile().getPath();
					hrsw.file_out(development, management, filepath);
				} else if (cmd.equals("Quit")) {
					System.exit(0);
				} else if (cmd.equals("Change the Log File")) {
					ret = chooser.showOpenDialog(null);
					if (ret != JFileChooser.APPROVE_OPTION) {
						JOptionPane.showMessageDialog(null, "파일을 선택하지 않았습니다.",
								"경고", JOptionPane.WARNING_MESSAGE);
						return;
					}
					path = chooser.getSelectedFile().getPath();
					history.change_history(path);
				} else if (cmd.equals("Show the Log File")) {
					contentPane.removeAll();
					contentPane.repaint();
					ta.setText(readAll(path));
					scroll.setPreferredSize(new Dimension(300, 100));
					add(scroll);
					setVisible(true);

				} else if (cmd.equals("신규직원 추가")) {
					contentPane.removeAll();
					contentPane.repaint();
					add(J1);
					add(tf1);
					add(J2);
					add(tf2);
					add(C1);
					add(C2);
					C2.setVisible(false);
					add(b1);
					add(b2);
					
					C1.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							JComboBox cb = (JComboBox) e.getSource();
							int index = cb.getSelectedIndex();
							de = index;
							if (index < 3)
								C2.setVisible(false);
							else if (index > 2) {
								C2.setVisible(true);
								C2.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										JComboBox cc = (JComboBox) e
												.getSource();
										dev = cc.getSelectedIndex();
									}
								});
							}
						}
					});
					b1.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							if (de < 3)
								try {

									management[de].add_Employee(tf2.getText(),
											tf1.getText());
								} catch (IOException e1) {

								}
							else if (de > 2) {
								try {
									development[de - 3].add_Employee(
											tf2.getText(), tf1.getText(), dev);

								} catch (IOException e1) {

								}
							}

						}
					});

					b2.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							setVisible(false);
						}
					});

					setVisible(true);

				} else if (cmd.equals("직원정보 삭제")) {
					contentPane.removeAll();
					contentPane.repaint();
					add(J1);
					add(tf1);
					add(J2);
					add(tf2);
					add(b1);
					add(b2);
					b1.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							try {
								del_Employee(tf1.getText(), tf2.getText());
								history.add_history("직원 삭제 " + tf2.getText() + "(" + tf1.getText()
										+ ")");
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

						}
					});
					b2.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							setVisible(false);
						}
					});
					setVisible(true);
				} else if (cmd.equals("직원정보 변경")) {
					contentPane.removeAll();
					contentPane.repaint();
					add(C3);
					C3.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							
							JComboBox cb = (JComboBox) e.getSource();
							int index = cb.getSelectedIndex();
							if (index == 0) {
								contentPane.removeAll();
								contentPane.repaint();
								
								add(J1);
								add(tf1);
								add(J2);
								add(tf2);
								add(C1);
								add(b1);
								add(b2);
								C1.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										JComboBox cb = (JComboBox) e
												.getSource();
										int index = cb.getSelectedIndex();
										de = index;
									}
								});
								b1.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										try {
											move(tf1.getText(), tf2.getText(),
													de);
										} catch (IOException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
									}
								});
								setVisible(true);
							} else  {
								contentPane.removeAll();
								contentPane.repaint();
								
								add(J1);
								add(tf1);
								add(J2);
								add(tf2);
								add(J3);
								add(tf3);
								add(J4);
								add(tf4);
								add(b1);
								add(b2);
								b1.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										try {
											change_Employee(tf1.getText(),
													tf2.getText(),
													tf3.getText(),
													tf4.getText());
										} catch (IOException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
									}
								});
								setVisible(true);
							}
						}
					});

					b1.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {

							if (de < 3) {

								try {
									for (int i = 0; i < 3; i++) {
										for (int j = 0; j < management[i].count; j++)
											if (tf1.getText()
													.equals(management[i].staff[j].name)
													&& tf2.getText()
															.equals(management[i].staff[j].id)) {
												management[i].del(j);
												break;
											}
									}

									management[de].move(tf2.getText(),
											tf1.getText());
								} catch (IOException e1) {

								}
							} else {

								try {
									for (int i = 0; i < 3; i++) {
										for (int j = 0; j < development[i].count1; j++)
											if (tf1.getText()
													.equals(development[i].developer[j].name)
													&& tf2.getText()
															.equals(development[i].developer[j].id)) {

												development[i].del(j, 0);
												break;
											}

									}
									for (int i = 0; i < 3; i++) {
										for (int j = 0; j < development[i].count2; j++)
											if (tf1.getText()
													.equals(development[i].tester[j].name)
													&& tf2.getText()
															.equals(development[i].tester[j].id)) {
												development[i].del(j, 0);
												;
											}
									}
									development[de].move(tf2.getText(),
											tf1.getText());
								} catch (IOException e1) {

								}

							}

						}
					});

					b2.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							setVisible(false);
						}
					});
					setVisible(true);
				} else {
					dialog1.setVisible(true);
				}
			} catch (NumberFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}



	class MyModalDialog1 extends JDialog { // //직원검색 다이알로그
		JLabel J1 = new JLabel("직원 ID");
		JLabel J2 = new JLabel("직원 이름");
		JLabel J3 = new JLabel();
		JTextField tf1 = new JTextField(5);
		JTextField tf2 = new JTextField(5);
		JButton b1 = new JButton("확인");
		JButton b2 = new JButton("취소");

		public MyModalDialog1(JFrame frame, String title) {
			super(frame, title, true);
			setLayout(new FlowLayout());
			add(J1);
			add(tf1);
			add(J2);
			add(tf2);
			add(b1);
			add(b2);
			add(J3);
			setSize(170, 200);

			b1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					for (int i = 0; i < 3; i++) {
						String searchId = tf1.getText();
						String searchName = tf2.getText();
						for (int j = 0; j < management[i].count; j++) {
							if (searchId.equals(management[i].staff[j].name)
									&& searchName
											.equals(management[i].staff[j].id)) {
								J3.setText(management[i].staff[j].name + "("
										+ management[i].staff[j].id + ","
										+ management[i].name + ")" + " ");

								return;
							}
						}

						for (int j = 0; j < development[i].count1; j++) {
							if (searchId
									.equals(development[i].developer[j].name)
									&& searchName
											.equals(development[i].developer[j].id)) {
								J3.setText(development[i].developer[j].name
										+ "(" + development[i].developer[j].id
										+ "," + development[i].name + ")" + " ");
								return;
							}
						}
						for (int j = 0; j < development[i].count2; j++) {
							if (searchId.equals(development[i].tester[j].name)
									&& searchName
											.equals(development[i].tester[j].id)) {
								J3.setText(development[i].tester[j].name + "("
										+ development[i].tester[j].id + ","
										+ development[i].name + ")" + " ");
								return;
							}
						}
					}
				}
			});
			b2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setVisible(false);
				}
			});
		}

	}



	public static void main(String[] args) throws FileNotFoundException {

		new MainEx();

	}
	String readAll(String path) {
		int c;
		StringBuffer sb = new StringBuffer();
		try {
			InputStreamReader r = new InputStreamReader(
					new FileInputStream(path));
			while ((c = r.read()) != -1)
				sb.append((char) c);
			r.close();
			return new String(sb);
		} catch (Exception e) {
			return "";
		}
	}

	void del_Employee(String input_n, String input_i) throws IOException {

		int count, count1, count2;

		for (int i = 0; i < 3; i++) {
			count = management[i].count;
			for (int j = 0; j < count; j++)
				if (input_n.equals(management[i].staff[j].name)
						&& input_i.equals(management[i].staff[j].id)) {

					management[i].del(j);
					return;

				}
		}
		for (int i = 0; i < 3; i++) {
			count1 = development[i].count1;
			count2 = development[i].count2;
			for (int j = 0; j < count1; j++)
				if (input_n.equals(development[i].developer[j].name)
						&& input_i.equals(development[i].developer[j].id)) {

					development[i].del(j, 0);
					return;

				}
			for (int j = 0; j < count2; j++)
				if (input_n.equals(development[i].tester[j].name)
						&& input_i.equals(development[i].tester[j].id)) {
					development[i].del(j, 0);
					return;

				}
		}
	}

	void change_Employee(String input_n, String input_i, String n_name,
			String n_id) throws IOException {

		int count, count1, count2;

		for (int i = 0; i < 3; i++) {
			count = management[i].count;
			for (int j = 0; j < count; j++)
				if (input_n.equals(management[i].staff[j].name)
						&& input_i.equals(management[i].staff[j].id)) {
					management[i].change(j, n_name, n_id);
					return;

				}
		}
		for (int i = 0; i < 3; i++) {
			count1 = development[i].count1;
			count2 = development[i].count2;
			for (int j = 0; j < count1; j++)
				if (input_n.equals(development[i].developer[j].name)
						&& input_i.equals(development[i].developer[j].id)) {
					development[i].change(j, 0, n_name, n_id);
					return;
				}
			for (int j = 0; j < count2; j++)
				if (input_n.equals(development[i].tester[j].name)
						&& input_i.equals(development[i].tester[j].id)) {
					development[i].change(j, 1, n_name, n_id);
					return;
				}
		}
	}

	void move(String input_n, String input_i, int ans) throws IOException {

		switch (ans) {
		case 1:
			del_Employee(input_n, input_i);
			management[0].move(input_n, input_i);
			break;
		case 2:
			del_Employee(input_n, input_i);
			management[1].move(input_n, input_i);
			break;
		case 3:
			del_Employee(input_n, input_i);
			management[2].move(input_n, input_i);

			break;
		case 4:
			del_Employee(input_n, input_i);
			development[0].move(input_n, input_i);
			break;
		case 5:
			del_Employee(input_n, input_i);
			development[1].move(input_n, input_i);

			break;
		case 6:
			del_Employee(input_n, input_i);
			development[2].move(input_n, input_i);
			break;
		}
	}
}