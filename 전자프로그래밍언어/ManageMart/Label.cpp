#include "Label.h"

void Label::print_label() {
	cout << "ǰ�� �� : " << L_name;
}
void Label::label_setname(string name)
{
	L_name = name;
}
string Label::get_label() {
	return L_name;
}