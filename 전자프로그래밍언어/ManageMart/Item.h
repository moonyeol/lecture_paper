#include "Label.h"



class Item : public Label {
	string name;    //��ǰ�� �̸�
	string code;    //��ǰ�� �ڵ�
	int price;     //��ǰ�� ����
	int c_price;   //��ǰ�� ����
	int stock;     //��ǰ�� ���
	int sales_volume;   //��ǰ�� �Ǹŷ�
public:
	void item_setname();           //�̸� �ʱ�ȭ �Լ�
	void item_setcode();           //�ڵ� �ʱ�ȭ �Լ�
	void item_setprice();          //���� �ʱ�ȭ �Լ�
	void item_setc_price();        //���� �ʱ�ȭ �Լ�
	void item_setstock();          //��� �ʱ�ȭ �Լ�
	void item_sales_volume();      //�Ǹŷ� �ʱ�ȭ �Լ� (�Ǹŷ� = 0���� �ʱ�ȭ)
	int profit();   //��ǰ�� �Ǹ� ����
	int net_profit();   //��ǰ�� ������
	void print_item();  //��ǰ�� ���� ���
	int buy();         //��ǰ ����
	int get_st();       //��ǰ�� ��� ���
	int get_sv();       //��ǰ�� �Ǹŷ� ���
	void read(ifstream& in);//���� �б�
	void write(ofstream& out);//���� ����
	void edit();		//������ �Է�
	string get_name();	//��ǰ �̸� ��ȯ
};