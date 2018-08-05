#include "Label.h"



class Item : public Label {
	string name;    //상품의 이름
	string code;    //상품의 코드
	int price;     //상품의 가격
	int c_price;   //상품의 원가
	int stock;     //상품의 재고
	int sales_volume;   //상품의 판매량
public:
	void item_setname();           //이름 초기화 함수
	void item_setcode();           //코드 초기화 함수
	void item_setprice();          //가격 초기화 함수
	void item_setc_price();        //원가 초기화 함수
	void item_setstock();          //재고 초기화 함수
	void item_sales_volume();      //판매량 초기화 함수 (판매량 = 0으로 초기화)
	int profit();   //상품의 판매 수익
	int net_profit();   //상품의 순이익
	void print_item();  //상품의 정보 출력
	int buy();         //상품 구매
	int get_st();       //상품의 재고 출력
	int get_sv();       //상품의 판매량 출력
	void read(ifstream& in);//파일 읽기
	void write(ofstream& out);//파일 저장
	void edit();		//데이터 입력
	string get_name();	//상품 이름 반환
};