package atom.tools.very.simple.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class UtilString {

	public static String readFile(File file) throws IOException {
		String s = "";
		BufferedReader br = new BufferedReader(new FileReader(file));
		String st;
		while ((st = br.readLine()) != null) {
			s += st;		
		}
		br.close();
		return s;
	}

	 private static Random random = new Random();
	public static String getRandomString() {
		int leftLimit = 97; // letter 'a'
	    int rightLimit = 122; // letter 'z'
	    int targetStringLength = 10;
	   
	    StringBuilder buffer = new StringBuilder(targetStringLength);
	    for (int i = 0; i < targetStringLength; i++) {
	        int randomLimitedInt = leftLimit + (int) 
	          (random.nextFloat() * (rightLimit - leftLimit + 1));
	        buffer.append((char) randomLimitedInt);
	    }
	    String generatedString = buffer.toString();
	    return generatedString;
	}
	public static String getRandom(Class<?> type) {
		if (Integer.class.equals(type)) {
			return  "" +random.nextInt();
		}
		if (String.class.equals(type)) {
			return  "" +getRandomString();
		}
		if (Double.class.equals(type)) {
			return  "" +random.nextDouble();
		}
		if (Boolean.class.equals(type)) {
			return  "" +random.nextBoolean();
		}
		return ""+random.nextInt();
	}

}
