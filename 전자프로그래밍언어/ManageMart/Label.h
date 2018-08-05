#include <iostream>
#include <vector>
#include <fstream>
#include <string>

using namespace std;

class Label {
protected:
	string L_name;
public:
	void label_setname(string name);
	void print_label();
	string get_label();
};