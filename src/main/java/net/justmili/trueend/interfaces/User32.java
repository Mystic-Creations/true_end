package net.justmili.trueend.interfaces;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;

public interface User32 extends StdCallLibrary {
	int MB_OK           = 0x00000000;
	int MB_SYSTEMMODAL  = 0x00001000;
	int MB_TOPMOST      = 0x00040000;

		User32 INSTANCE = Native.load("user32", User32.class);
		void MessageBoxA(long var1, String var3, String var4, int var5);
	}
