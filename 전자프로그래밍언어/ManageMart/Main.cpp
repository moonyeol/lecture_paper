#include "Item.h"
	vector<string> n_label;
	vector<Item*> l_items;


//품목 추가 함수
void add_label(int a)
{
	if (a >= 9)
	{
		cout << "더 이상 품목을 추가할 수 없습니다." << endl;
		return;
	}
	string temp;
	cout << "추가할 품목의 이름은 ? :";
	cin >> temp;
	n_label.push_back(temp);
}
//품목 출력 함수
void show_label(int a)
{
	cout << "**********" << endl;
	for (int i = 0; i < a; i++)
		cout <<i+1<<'.'<< n_label.at(i) << endl;
	cout << "**********" << endl;

}
//품목 검색 함수, 해당 품목의 인덱스를 반환
int search_label(string name)
{
	int i;
	for (i = 0; i <n_label.size();i++) 
		if (n_label.at(i) == name) 
			return i;
	cout << "해당 품목은 존재하지 않습니다."<<endl;
	return -1;
}
//물품 검색 함수, 해당 물품의 인덱스를 반환
int search_item(string name)
{
	for (int i=0; i <l_items.size();i++)
		if(l_items.at(i)->get_name()==name)
			return i;
	cout << "해당 상품은 존재하지 않습니다."<<endl;
	return -1;
}
//통계 함수, 품목 -> 물품 -> 해당 물품의 통계 순의 출력
void stat() {
	for (int i = 0; i < n_label.size(); i++) {
		cout << n_label.at(i) << endl;
		for (int j = 0; j < l_items.size(); j++) {
			if (n_label.at(i) == l_items.at(j)->get_label())
			{
				cout << "상품명 : "<<l_items.at(j)->get_name() << endl;
				cout << "재고 :   " << l_items.at(j)->get_st() << endl;
				cout << "판매량 : " << l_items.at(j)->get_sv() << endl;
				cout << "수익 : " << l_items.at(j)->profit() << endl;
				cout << "순수익 : " << l_items.at(j)->net_profit() << endl;

			}
		}
	}
}
//품목 삭제시 해당 품목에 해당하는 상품을 검색
int serch_item_label(string name, int i)            
{
	for (i; i < l_items.size(); i++)
		if (l_items.at(i)->get_label() == name)
			return i;
	return -1;
}
//품목 삭제 함수
void delete_label(int a)
{
	vector<string>::iterator iter;
	string tmp;
	int t = 0;
	int count = 0;
	int l = 0;
	if (a <= 0)
	{
		cout << "더 이상 품목을 삭제할 수 없습니다." << endl;
		return;
	}
	show_label(a);
	cout << "삭제할 품목 이름을 입력해주세요.";
	cin >> tmp;
	t = search_label(tmp);
	if (t == -1)
		return;
	n_label.erase(n_label.begin() + t);
	while (1){								//지운 품목의 물품들을 삭제
		l = serch_item_label(tmp, count);
		if (l == -1)
			break;
		delete(l_items.at(l));				//객체 해지
		l_items.erase(l_items.begin() + l);//벡터에서 지운 객체의 자리를 지움
		count = count + 1;
	}
}
//상품 추가 함수
void add_item()
{
	int l;
	Item *temp = new Item();
	cout << "추가하려는 상품의 정보를 입력하시오." << endl;
	temp->edit();
	cout << "상품의 품목을 고르세요" << endl;
	show_label(n_label.size());
	cin >> l;
	temp->label_setname(n_label.at(l-1));
	l_items.push_back(temp);
}
//물품 삭제 함수
 void delete_item()                                                    
{
	string tmp;
	int t;
	cout << "삭제하려는 상품을 입력하시오." << endl;
	cin >> tmp;
	t = search_item(tmp);
	if (t == -1)
		return;
	delete(l_items.at(t));		//객체 해지
	l_items.erase(l_items.begin() +t);
} 
//물품 정보 수정 함수
 void edit_item() {
	 string tmp;
	 int t;
	 cout << "수정하려는 상품을 입력하세요." << endl;
	 cin >> tmp;
	 t = search_item(tmp);
	 if (t == -1)
		 return;
	 cout << "새로운 정보를 입력하세요." << endl;
	 l_items.at(t)->edit();
	 cout << "수정되었습니다. " << endl;
 }
 //품목, 물품을 전부 출력
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

	n_label.reserve(10);   //품목은 최대 10개
	int total_price = 0;   //장바구니의 가격
	int command, command2;
	char yn;
	string buy_item;
	int t,k=0;
	int count = 0,cc=0;
	//파일 읽기
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

	//n_label에 품목이름을 넣어줌
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


	//구현부
	while (1) {
		cout << "(1) 고객모드  (2) 관리모드  (3) 종료" << endl;
		cin >> command;
		if (command < 1 && command>3)
			continue;

		else if (command == 1) {		//고객 모드
			while (1) {
				cout << "1. 상품 선택  2. 계산  3. 상품 검색" << endl;
				cin >> command2;
				if (command2 == 1) {					//상품 선택
					print_all();						//상품 출력
					cout << "어떤것을 구매하시겠습니까?";
					cin >> buy_item;
					t = search_item(buy_item);
					if (t != -1)
						total_price += l_items.at(t)->buy();
				}
				else if (command2 == 2) {				//계산
					cout << "총 " << total_price << "원 나왔습니다." << endl;
					total_price = 0;
					cout << "쇼핑을 그만하시겠습니까?[y/n]" << endl;
					cin >> yn;
					if (yn == 'y')
						break;
				}
				else if (command2 == 3) {				//상품 검색
					cout << "찾으시는 물건 이름을 입력하세요." << endl;
					cin >> buy_item;
					t = search_item(buy_item);
					if (t != -1)
						l_items.at(t)->print_item();	//해당 상품 출력
				}

			}
		}

		else if (command == 2) {					//관리자모드
			while (1) {
				cout << "1.품목추가  2.품목삭제  3.상품추가  4.상품삭제  5.상품수정  6.통계보기  7.모드" << endl;
				cin >> command2;
				switch (command2) {
				case 1:
					add_label(n_label.size());       //품목 추가
					show_label(n_label.size());          //추가한 후 현재의 품목 나열
					break;
				case 2:
					delete_label(n_label.size());    //품목 삭제
					break;
				case 3:
					add_item();                      //상품 추가
					break;
				case 4:
					delete_item();				//상품삭제
					break;
				case 5:
					edit_item();				//상품정보수정
					break;
				case 6:
					stat();						//통계보기
					break;
				case 7:
					k = 1;						//탈출조건
					break;
				}
				if (k == 1) break;			//루프 탈출
			}
		}
		else if (command == 3)				//프로그램 종료(루프 탈출)
			break;
	}
	//파일 저장
	out.open("itemlist.txt");
	for(int i =0; i<l_items.size();i++){
		l_items.at(i)->write(out);
	}
	out.close();
	//동적할당된 객체 해지
	for (int i = 0; i < l_items.size();i++) {
		delete(l_items.at(i));
	}
	l_items.clear();
	n_label.clear();
}