#include <iomanip>
#include <cstdio>
#include <cstdlib>
#include <iostream>
#include <string>
#include <conio.h>

using namespace std;

int main()
{
	string id="open";
	
	while(id!="exit")
	{
		cout << "Podaj id dispensera: ";
		cin >> id;
	
		int sum=0;
		int control=124;
	
		for(size_t i=0;i<id.length();i++)
		{
			int number = id[i]-48;
			number*=control*(i+1);
			if(number>999) number=number%1000;
			sum+=number;
			control+=9;	
		}
	
		if(sum<1000) sum*=control;
		sum = sum % 1000;
		if(sum==0)
		{
			sum = control * id.length() * (id.length() % 10 + 10);
			sum = sum % 1000;
		}
		cout << "Suma kontrolna wynosi: " << sum << endl;
	}
	return EXIT_SUCCESS;
}
