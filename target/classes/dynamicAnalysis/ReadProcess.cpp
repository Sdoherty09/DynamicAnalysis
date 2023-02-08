#include <iostream>
#include <windows.h>
#include <memoryapi.h>

using namespace std;

void EnableDebugPriv()
{
	HANDLE hToken;
	LUID luid;
	TOKEN_PRIVILEGES tkp;

	OpenProcessToken(GetCurrentProcess(), TOKEN_ADJUST_PRIVILEGES | TOKEN_QUERY, &hToken);

	LookupPrivilegeValue(NULL, SE_DEBUG_NAME, &luid);

	tkp.PrivilegeCount = 1;
	tkp.Privileges[0].Luid = luid;
	tkp.Privileges[0].Attributes = SE_PRIVILEGE_ENABLED;

	AdjustTokenPrivileges(hToken, false, &tkp, sizeof(tkp), NULL, NULL);

	CloseHandle(hToken);
}

int main()
{
	EnableDebugPriv();
	MEMORY_BASIC_INFORMATION mbi; //mbi used as register for assigning in query
	HANDLE hProcess = OpenProcess(PROCESS_ALL_ACCESS, FALSE, 16824); //process using pid
	DWORD oldprotect; //given the value of old protect
	BYTE* buffer; //string for buffer
	int addr; //need to find what this gets assigned to
	VirtualQueryEx(hProcess, NULL, (PMEMORY_BASIC_INFORMATION)&mbi, sizeof(mbi)); //early assign of mbi, equals 28 (size)
	addr = (int)mbi.BaseAddress; //BaseAddress gives 0, shouldnt be correct
	int check = VirtualProtectEx(hProcess, 0, mbi.RegionSize, PAGE_EXECUTE_READWRITE, &oldprotect);
	int temp;
	SIZE_T size;
	while (temp = VirtualQueryEx(hProcess, (LPCVOID)addr, (PMEMORY_BASIC_INFORMATION)&mbi, mbi.RegionSize)) //will not work for 64 bit programs (Invalid access to memory location.)
	{
		int check = VirtualProtectEx(hProcess, (LPVOID)addr, mbi.RegionSize, PAGE_EXECUTE_READWRITE, &oldprotect); //issue here, error 487: ERROR_INVALID_ADDRESS, address incrementing seems valid. 100% relation to whether memory gets read
		if (check != 0)
		{
			buffer = new BYTE[mbi.RegionSize];
			ReadProcessMemory(hProcess, (LPVOID)addr, buffer, mbi.RegionSize, &size); //not filling entire buffer
			VirtualProtectEx(hProcess, (LPVOID)addr, mbi.RegionSize, oldprotect, &oldprotect);
			cout << buffer;
		}
		addr += mbi.RegionSize;
	}
	CloseHandle(hProcess);

}
