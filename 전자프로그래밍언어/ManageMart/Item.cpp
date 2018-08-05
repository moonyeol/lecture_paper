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
	cout << "상품명 : " << name << endl;
	cout << "가격 : " << price << endl;
	cout << "재고 : " << stock << endl;
	return;
}

int Item::buy()
{
	int temp;
	while (1)
	{
		cout << "해당 상품을 몇개 구매하시겠습니까?   ";
		cin >> temp;
		if (temp <= stock)
		{
			cout << "총 " << temp * price << "원 입니다."<<endl;
			stock = stock - temp;
			sales_volume = sales_volume + temp;
			break;
		}
		else
			cout << "재고가 부족합니다."<<endl;
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
	cout << "상품의 이름을 입력하세요. :";
	cin >> name;
}

void Item::item_setcode()
{
	cout << "상품의 코드를 입력하세요. : ";
		cin >> code;
}

void Item::item_setprice()
{
	cout << "상품의 판매 가격을 입력하세요. : ";
	cin >> price;
}

void Item::item_setc_price()
{
	cout << "상품의 원가를 입력하세요. : ";
	cin >> c_price;
}

void Item::item_setstock()
{
	cout << "상품의 재고를 입력하세요. : ";
	cin >> stock;
}

void Item::item_sales_volume()
{
	sales_volume = 0;
}
