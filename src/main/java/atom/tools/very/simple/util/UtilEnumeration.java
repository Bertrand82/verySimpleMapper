package atom.tools.very.simple.util;

public class UtilEnumeration {

	
	

	
	public static int getSizeEnum(Class<?> enumClass) {     
        	return enumClass.getFields().length;
    }
}
