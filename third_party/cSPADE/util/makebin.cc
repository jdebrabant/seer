#include <cerrno>
#include <iostream>
//#include <cstdio>
#include <fstream>
//#include <strstream>
#include <sstream>
#include <cstdlib>
//#include <fcntl.h>
//#include <sys/stat.h>
//#include <unistd.h>
#include <cmath> 
using namespace std; 

#define ITSZ sizeof(int)
const int lineSize=8192;
const int wdSize=256;
ifstream fin;
ofstream fout;

void convbin(char *inBuf, int inSize)
{
	char inStr[wdSize];
	//istrstream ist(inBuf, inSize);
	stringstream ist; 
	
	ist << inBuf; 
	
	int it;
	while(ist >> inStr)
	{
		it = atoi(inStr);
		cout << it  << " ";
		fout.write((char*)&it, ITSZ);
	}
	cout << endl;
}

int main(int argc, char **argv)
{
	char inBuf[lineSize];
	int inSize;
	fin.open(argv[1]);
	if (!fin)
	{
		perror("cannot open in file");
		exit(errno);
	}
	fout.open(argv[2]);
	if (!fout)
	{
		perror("cannot open out file");
		exit(errno);
	}
	
	while(fin.getline(inBuf, lineSize))
	{
		inSize = fin.gcount();
		cout << "IN SIZE " << inSize << endl;
		convbin(inBuf, inSize);
	}
	fout.close(); 
}








