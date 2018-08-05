#include "Item.h"
	vector<string> n_label;
	vector<Item*> l_items;


//ǰ�� �߰� �Լ�
void add_label(int a)
{
	if (a >= 9)
	{
		cout << "�� �̻� ǰ���� �߰��� �� �����ϴ�." << endl;
		return;
	}
	string temp;
	cout << "�߰��� ǰ���� �̸��� ? :";
	cin >> temp;
	n_label.push_back(temp);
}
//ǰ�� ��� �Լ�
void show_label(int a)
{
	cout << "**********" << endl;
	for (int i = 0; i < a; i++)
		cout <<i+1<<'.'<< n_label.at(i) << endl;
	cout << "**********" << endl;

}
//ǰ�� �˻� �Լ�, �ش� ǰ���� �ε����� ��ȯ
int search_label(string name)
{
	int i;
	for (i = 0; i <n_label.size();i++) 
		if (n_label.at(i) == name) 
			return i;
	cout << "�ش� ǰ���� �������� �ʽ��ϴ�."<<endl;
	return -1;
}
//��ǰ �˻� �Լ�, �ش� ��ǰ�� �ε����� ��ȯ
int search_item(string name)
{
	for (int i=0; i <l_items.size();i++)
		if(l_items.at(i)->get_name()==name)
			return i;
	cout << "�ش� ��ǰ�� �������� �ʽ��ϴ�."<<endl;
	return -1;
}
//��� �Լ�, ǰ�� -> ��ǰ -> �ش� ��ǰ�� ��� ���� ���
void stat() {
	for (int i = 0; i < n_label.size(); i++) {
		cout << n_label.at(i) << endl;
		for (int j = 0; j < l_items.size(); j++) {
			if (n_label.at(i) == l_items.at(j)->get_label())
			{
				cout << "��ǰ�� : "<<l_items.at(j)->get_name() << endl;
				cout << "��� :   " << l_items.at(j)->get_st() << endl;
				cout << "�Ǹŷ� : " << l_items.at(j)->get_sv() << endl;
				cout << "���� : " << l_items.at(j)->profit() << endl;
				cout << "������ : " << l_items.at(j)->net_profit() << endl;

			}
		}
	}
}
//ǰ�� ������ �ش� ǰ�� �ش��ϴ� ��ǰ�� �˻�
int serch_item_label(string name, int i)            
{
	for (i; i < l_items.size(); i++)
		if (l_items.at(i)->get_label() == name)
			return i;
	return -1;
}
//ǰ�� ���� �Լ�
void delete_label(int a)
{
	vector<string>::iterator iter;
	string tmp;
	int t = 0;
	int count = 0;
	int l = 0;
	if (a <= 0)
	{
		cout << "�� �̻� ǰ���� ������ �� �����ϴ�." << endl;
		return;
	}
	show_label(a);
	cout << "������ ǰ�� �̸��� �Է����ּ���.";
	cin >> tmp;
	t = search_label(tmp);
	if (t == -1)
		return;
	n_label.erase(n_label.begin() + t);
	while (1){								//���� ǰ���� ��ǰ���� ����
		l = serch_item_label(tmp, count);
		if (l == -1)
			break;
		delete(l_items.at(l));				//��ü ����
		l_items.erase(l_items.begin() + l);//���Ϳ��� ���� ��ü�� �ڸ��� ����
		count = count + 1;
	}
}
//��ǰ �߰� �Լ�
void add_item()
{
	int l;
	Item *temp = new Item();
	cout << "�߰��Ϸ��� ��ǰ�� ������ �Է��Ͻÿ�." << endl;
	temp->edit();
	cout << "��ǰ�� ǰ���� ������" << endl;
	show_label(n_label.size());
	cin >> l;
	temp->label_setname(n_label.at(l-1));
	l_items.push_back(temp);
}
//��ǰ ���� �Լ�
 void delete_item()                                                    
{
	string tmp;
	int t;
	cout << "�����Ϸ��� ��ǰ�� �Է��Ͻÿ�." << endl;
	cin >> tmp;
	t = search_item(tmp);
	if (t == -1)
		return;
	delete(l_items.at(t));		//��ü ����
	l_items.erase(l_items.begin() +t);
} 
//��ǰ ���� ���� �Լ�
 void edit_item() {
	 string tmp;
	 int t;
	 cout << "�����Ϸ��� ��ǰ�� �Է��ϼ���." << endl;
	 cin >> tmp;
	 t = search_item(tmp);
	 if (t == -1)
		 return;
	 cout << "���ο� ������ �Է��ϼ���." << endl;
	 l_items.at(t)->edit();
	 cout << "�����Ǿ����ϴ�. " << endl;
 }
 //ǰ��, ��ǰ�� ���� ���
 void print_all() {
	 for (int i = 0; i < n_label.size();i++) {
		 cout <<endl << n_label.at(i) << endl;
		 for (int j = 0; j < l_items.size();j++) {
			 if (n_label.at(i) == l_items.at(j)->get_label())
				 l_items.at(j)->print_item();
		 }
	 }
 }

int main() {

	n_label.reserve(10);   //ǰ���� �ִ� 10��
	int total_price = 0;   //��ٱ����� ����
	int command, command2;
	char yn;
	string buy_item;
	int t,k=0;
	int count = 0,cc=0;
	//���� �б�
	ifstream in;
	ofstream out;
	in.open("itemlist.txt", ios::in);
	Item* tmp;
	char buf[256];
	in.getline(buf,256, '\n');
	while (!in.eof()) {
		tmp = new Item();
		tmp->read(in);
		l_items.push_back(tmp);
		cc++;
	}
	in.close();

	//n_label�� ǰ���̸��� �־���
	for (int i = 0; i<l_items.size(); i++) {
		count = 0;
		for (int j = 0; j < n_label.size(); j++)
			if (l_items.at(i)->get_label() == n_label.at(j))
				break;
			else
				count++;
		if (count == n_label.size())
			n_label.push_back(l_items.at(i)->get_label());
	}


	//������
	while (1) {
		cout << "(1) �����  (2) �������  (3) ����" << endl;
		cin >> command;
		if (command < 1 && command>3)
			continue;

		else if (command == 1) {		//�� ���
			while (1) {
				cout << "1. ��ǰ ����  2. ���  3. ��ǰ �˻�" << endl;
				cin >> command2;
				if (command2 == 1) {					//��ǰ ����
					print_all();						//��ǰ ���
					cout << "����� �����Ͻðڽ��ϱ�?";
					cin >> buy_item;
					t = search_item(buy_item);
					if (t != -1)
						total_price += l_items.at(t)->buy();
				}
				else if (command2 == 2) {				//���
					cout << "�� " << total_price << "�� ���Խ��ϴ�." << endl;
					total_price = 0;
					cout << "������ �׸��Ͻðڽ��ϱ�?[y/n]" << endl;
					cin >> yn;
					if (yn == 'y')
						break;
				}
				else if (command2 == 3) {				//��ǰ �˻�
					cout << "ã���ô� ���� �̸��� �Է��ϼ���." << endl;
					cin >> buy_item;
					t = search_item(buy_item);
					if (t != -1)
						l_items.at(t)->print_item();	//�ش� ��ǰ ���
				}

			}
		}

		else if (command == 2) {					//�����ڸ��
			while (1) {
				cout << "1.ǰ���߰�  2.ǰ�����  3.��ǰ�߰�  4.��ǰ����  5.��ǰ����  6.��躸��  7.���" << endl;
				cin >> command2;
				switch (command2) {
				case 1:
					add_label(n_label.size());       //ǰ�� �߰�
					show_label(n_label.size());          //�߰��� �� ������ ǰ�� ����
					break;
				case 2:
					delete_label(n_label.size());    //ǰ�� ����
					break;
				case 3:
					add_item();                      //��ǰ �߰�
					break;
				case 4:
					delete_item();				//��ǰ����
					break;
				case 5:
					edit_item();				//��ǰ��������
					break;
				case 6:
					stat();						//��躸��
					break;
				case 7:
					k = 1;						//Ż������
					break;
				}
				if (k == 1) break;			//���� Ż��
			}
		}
		else if (command == 3)				//���α׷� ����(���� Ż��)
			break;
	}
	//���� ����
	out.open("itemlist.txt");
	for(int i =0; i<l_items.size();i++){
		l_items.at(i)->write(out);
	}
	out.close();
	//�����Ҵ�� ��ü ����
	for (int i = 0; i < l_items.size();i++) {
		delete(l_items.at(i));
	}
	l_items.clear();
	n_label.clear();
}