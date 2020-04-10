package atom.tools.very.simple.generator.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import com.squareup.javapoet.MethodSpec;

public abstract class AbstracMethodMapper {

	public static final Class[] PRIMITIVES = { Boolean.class, Integer.class, Double.class, Float.class, Long.class };
	protected final List<ClassPair> listClassPair = new ArrayList<ClassPair>();
	protected ClassPair classPair;

	private List<MappageFailure> listMappageFailure = new ArrayList<MappageFailure>();

	
	
	public AbstracMethodMapper(ClassPair cPair) {
		cPair.setAlreadyProcessed(true);
		this.classPair = cPair;
		
	}
	/**
	 * @param pField1
	 * @return
	 */
	protected static String getGetter(final Field field) {
		final String name = capitalizeFirstLetter(field.getName());
		final String methodName = "get" + name + "()";
		return methodName;
	}

	public ClassPair getClassPair() {
		return classPair;
	}

	protected boolean isPrimitive(Field fieldOut) {
		Class clazz = fieldOut.getType();
		if (clazz.isPrimitive()) {
			return true;
		}
		for (Class c : PRIMITIVES) {
			if (c.equals(clazz)) {
				return true;
			}
		}
		return false;
	}

	protected void logMappageImpossible(String from, Field fieldIn, Field fieldOut) {
		System.err.println(from + " Mappage non resolu " +classPair.classIn.getName() + "   " + fieldIn + "  " + fieldOut);
		MappageFailure mf = new MappageFailure(from, this.classPair, fieldIn, fieldOut);
		listMappageFailure.add(mf);
	}

	public List<MappageFailure> getListMappageFailure() {
		return listMappageFailure;
	}

	public List<ClassPair> getListClassPair() {
		return listClassPair;
	}

	protected Class<?> getGenericTypeFromList(final Field field) {
		final ParameterizedType listType = (ParameterizedType) field.getGenericType();
		final Class<?> c = (Class<?>) listType.getActualTypeArguments()[0];
		return c;
	
	}

	protected boolean getterExistForField(final Class pClazz, final Field field) {
		final String getter = getGetter(field).replace("()", "");
		for (final Method m : pClazz.getMethods()) {
			if (m.getName().equals(getter)) {
				return true;
			}
		}
		return false;
	}

	protected Field getFieldFromClass(final Class clazz, final String fieldName) {
		for (final Field f : clazz.getDeclaredFields()) {
			if (f.getName().equals(fieldName)) {
				return f;
			}
		}
		return null;
	}

	/**
	 * @param pField1
	 * @return
	 */
	protected static String getSetter(final Field field) {
		final String name = capitalizeFirstLetter(field.getName());
		final String methodName = "set" + name;
		return methodName;
	}

	/**
	 * @param pField1
	 * @return
	 */
	private static String getIser(final Field field) {
		final String name = capitalizeFirstLetter(field.getName());
		final String methodName = "is" + name + "()";
		return methodName;
	}

	/**
	 * @param pName
	 * @return
	 */
	private static String capitalizeFirstLetter(final String str) {
		final String s = str.substring(0, 1).toUpperCase() + str.substring(1);
		return s;
	}


	protected String getMapPrimitive(Field field) {
		String s;
		if (isBoolean(field)) {
			String v = "in." + getIser(field);
			s = "out." + getSetter(field) + "(" + v + ")";
		} else {
			String v = "in." + getGetter(field);
			s = "out." + getSetter(field) + "(" + v + ")";
		}
		return s;
	}

	private boolean isBoolean(Field field) {
		if (field.getType().equals(boolean.class)) {
			return true;
		} else if (field.getType().equals(Boolean.class)) {
			return true;
		}
		return false;
	}
	public  abstract MethodSpec getMethodMapper() ;
	public static AbstracMethodMapper createMapper(ClassPair cPair) {
		if (cPair.isEnum()) {
			return new GeneratorEnum(cPair);
		}else {
			return new GeneratorMapperMethod(cPair);
		}
	
	}
	protected static String prefixMethod = "map_";



	public String getMethodName() {
		return prefixMethod + this.classPair.classOut.getSimpleName();
	}
}