package org.openintents.intentsregistry.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.openintents.intentsregistry.IntentsregistryLog;
import org.openintents.intentsregistry.net.IntentsregistryParser;

public class IntentsregistryResource {
	
	private static ArrayList<String> resources = new ArrayList<String>();
	static Map<String, String> resourcez = null;

	static {
		generateRescources(); 
		IntentsregistryParser intentList = new IntentsregistryParser();
		try {
			intentList.list( );
		} catch (Exception e) {
			IntentsregistryLog.logError(e);
		}
		
	}

	private static void generateRescources(){
		
		try {
			BufferedReader in = new BufferedReader(new FileReader("outfilename.txt"));
			String str = "";
			resourcez = new HashMap<String, String>();
			while ((str = in.readLine()) != null) {
			resources.add(str.substring(str.indexOf('=')+1, str.length()));					
			resourcez.put(str.substring(0, str.indexOf('=')), str.substring(str.indexOf('=')+1, str.length()));
			
			}

			in.close();
		} catch (IOException e) {
			IntentsregistryLog.logError(e);
		}		
		
	}
	
	/**
	 * @return the resources
	 */
	public static ArrayList<String> getResources() {
		return resources;
	}
	
	/**
	 * @return the resourcez
	 */
	public static Map<String, String> getResourcez() {
		return resourcez;
	}

}
