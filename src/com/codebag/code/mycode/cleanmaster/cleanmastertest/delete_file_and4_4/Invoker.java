package com.codebag.code.mycode.cleanmaster.cleanmastertest.delete_file_and4_4;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.codebag.bag.MyCode;
import com.codebag.bag.Entry;
import com.codebag.bag.main.InovkedViewActivity;

public class Invoker extends MyCode {

//	String path = "/storage/extSdCard/system.txt";//S4 old
	String path = "/storage/extSdCard/system.txt";//S4
//	String path = "/storage/ext_sd/system.txt";//htc/
//	String path = "/storage/sdcard1/system.txt";//honor
	
	public Invoker(InovkedViewActivity context) {
		super(context);
	}
	
	@Entry
	public void deleteFile_mediaFile() {
//		boolean result = MediaFileUtil.deleteFile(getActivity().getContentResolver(), path);
//		Log.addLog(this, "result=" + result);
	}
	
	@Entry
	public void deleteFile_normal() {
		File file = new File(path);
		file.delete();
	}
	
	@Entry
	public void deleteFile_runTime() {
		List<String> commands = new ArrayList<String>();
		commands.add("rm");
		commands.add(path);
		
		ProcessBuilder pb = new ProcessBuilder(commands);
		try {
			pb.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Entry
	public void deleteFile_runTime_su() {
		List<String> commands = new ArrayList<String>();
		commands.add("su");
		commands.add("|");
		commands.add("rm");
		commands.add(path);
		
		ProcessBuilder pb = new ProcessBuilder(commands);
		try {
			pb.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
