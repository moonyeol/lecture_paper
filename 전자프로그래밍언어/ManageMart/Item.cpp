#define _CRT_SECURE_NO_WARNINGS
#include <iostream>
#include "ITEM.h"
using namespace std;

int Item::profit()
{
	int result = price * sales_volume;
	return result;
}

int Item::net_profit()
{
	int result = c_price * sales_volume;
	return result;
}

void Item::edit() {
	item_setname();
	item_setcode();
	item_setprice();
	item_setc_price();
	item_setstock();
	item_sales_volume();
}

void Item::print_item()
{
	cout << "��ǰ�� : " << name << endl;
	cout << "���� : " << price << endl;
	cout << "��� : " << stock << endl;
	return;
}

int Item::buy()
{
	int temp;
	while (1)
	{
		cout << "�ش� ��ǰ�� � �����Ͻðڽ��ϱ�?   ";
		cin >> temp;
		if (temp <= stock)
		{
			cout << "�� " << temp * price << "�� �Դϴ�."<<endl;
			stock = stock - temp;
			sales_volume = sales_volume + temp;
			break;
		}
		else
			cout << "��� �����մϴ�."<<endl;
	}
	return temp * price;
}
int Item::get_st()
{
	return stock;

}
int Item::get_sv()
{
	return sales_volume;
}

void Item::read(ifstream& in) {
	char buf[256];
	in.getline(buf, 256, '\n');
	name = strtok(buf, "\t");
	code = strtok(NULL, "\t");
	Label::L_name = strtok(NULL, "\t");
	price = stoi(strtok(NULL, "\t"));
	stock = stoi(strtok(NULL, "\n"));
}

void Item::write(ofstream& out) {
	out <<"\n"<< name << "\t" << code << "\t" << Label::L_name << "\t" << price << "\t" << stock;
}

string Item::get_name() {
	return name;
}

void Item::item_setname()
{
	cout << "��ǰ�� �̸��� �Է��ϼ���. :";
	cin >> name;
}

void Item::item_setcode()
{
	cout << "��ǰ�� �ڵ带 �Է��ϼ���. : ";
		cin >> code;
}

void Item::item_setprice()
{
	cout << "��ǰ�� �Ǹ� ������ �Է��ϼ���. : ";
	cin >> price;
}

void Item::item_setc_price()
{
	cout << "��ǰ�� ������ �Է��ϼ���. : ";
	cin >> c_price;
}

void Item::item_setstock()
{
	cout << "��ǰ�� ��� �Է��ϼ���. : ";
	cin >> stock;
}

void Item::item_sales_volume()
{
	sales_volume = 0;
}
