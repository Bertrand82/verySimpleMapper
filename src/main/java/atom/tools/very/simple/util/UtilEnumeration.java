package atom.tools.very.simple.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class UtilEnumeration {

	
	

	
	public static int getSizeEnum(Class<?> enumClass) {     
        	return enumClass.getFields().length;
    }
}
