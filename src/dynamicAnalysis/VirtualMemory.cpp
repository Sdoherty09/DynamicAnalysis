#include <jni.h>        // JNI header provided by JDK
#include <stdio.h>      // C Standard IO Header
#include "dynamicAnalysis_VirtualMemory.h"   // Generated
#include <iostream>
#include <windows.h>
#include <memoryapi.h>
#include <vector>

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

JNIEXPORT jcharArray JNICALL Java_dynamicAnalysis_VirtualMemory_scanProcess(JNIEnv *, jobject, jint processId)
{
	EnableDebugPriv();
	MEMORY_BASIC_INFORMATION64 mbi;
	HANDLE hProcess = OpenProcess(PROCESS_ALL_ACCESS, FALSE, (int) processId);
	DWORD oldprotect;
	BYTE* buffer;
	int addr;
	int check = VirtualProtectEx(hProcess, (LPVOID)0, mbi.RegionSize, PAGE_EXECUTE_READWRITE, &oldprotect);
	SIZE_T size;
	int signedSize = 0;
	vector<BYTE> regionData;
	while (VirtualQueryEx(hProcess, (LPVOID)addr, (PMEMORY_BASIC_INFORMATION)&mbi, sizeof(mbi)))
	{
		signedSize = static_cast<int>(mbi.RegionSize);
		int check = VirtualProtectEx(hProcess, (LPVOID)addr, mbi.RegionSize, PAGE_EXECUTE_READWRITE, &oldprotect);
		if (check)
		{
			try
			{
				buffer = new BYTE[signedSize];
				ReadProcessMemory(hProcess, (LPCVOID)addr, buffer, mbi.RegionSize, &size);
				for (int index = 0; index < signedSize; index++)
				{
					regionData.push_back(buffer[index]);
				}		
			}
			catch (bad_alloc e)
			{
				break;
			}
		}
		addr = (addr + mbi.RegionSize);
	}
	char* total = new char[regionData.size()];
	int index = 0;
	try
	{
		for (auto i = regionData.begin(); i < regionData.end(); ++i)
		{
			total[index] = *i;
			index++;
		}
	}
	catch (bad_alloc e) {}
	CloseHandle(hProcess);
	return (jcharArray)total;
}