package atom.tools.very.simple.generator.tostring;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.MethodSpec.Builder;


import atom.tools.very.simple.util.UtilEnumeration;

public class GeneratorClassToString {
	public static final Class[] PRIMITIVES = { Boolean.class, Integer.class, Double.class, Float.class, Long.class };

	private static final String prefixMethod = "toString_";
	private final Class clazz;
	private final List<Class> listClass = new ArrayList<Class>();
	private final List<Field> listFields = new ArrayList<Field>();

	public GeneratorClassToString(Class class1) {
		this.clazz = class1;
		for (final Field field : clazz.getDeclaredFields()) {
			if (getterExistForField(clazz, field)) {
				Class c = getClassCustom(field);
				System.out.println("---------------- '----------------------------------------------"+field.getName()+"    "+getterExistForField(clazz, field)+"   "+c);

				if (c != null) {
					listClass.add(c);
				}
				listFields.add(field);
			} else {
				logMappageImpossible("A0", field);
			}
		}
	}

	private Class getClassCustom(Field field) {
		if (!getterExistForField(clazz, field)) {
			return null;
		} else if (field.getType().equals(List.class)) {
			final Class<?> cOut = getGenericTypeFromList(field);
			return cOut;
		} else {
			return getClassCustom(field.getType());
		}
	}

	private Class getClassCustom(Class type) {
		if(type == null) {
		}else if (type.isEnum()) {
		} else if (isPrimitive(type)) {
		} else if (type.equals(String.class)) {
		} else if (type.equals(Date.class)) {
		} else if (type.equals(ClassLoader.class)) {
		} else if (type.equals(Class.class)) {
		} else if (type.getPackage() == null) {
		} else if ((type.getPackage().getName() + "").startsWith("sun.reflect.")) {
		} else if (type.getPackage().getName().startsWith("sun.")) {
		} else if (type.isArray()) {
		} else if (type.equals(List.class)) {
		} else {
			return type;
		}
		return null;
	}

	List<String> listFailures = new ArrayList<String>();

	private void logMappageImpossible(String comment, Field fieldOut) {
		String s = comment + "  " + fieldOut.getType() + "  " + fieldOut.getName();
		System.err.println(s);
		listFailures.add(s);
	}

	protected Class<?> getGenericTypeFromList(final Field field) {
		final ParameterizedType listType = (ParameterizedType) field.getGenericType();
		final Class<?> c = (Class<?>) listType.getActualTypeArguments()[0];
		return c;

	}

	protected boolean isPrimitive(Field fieldOut) {
		Class cl = fieldOut.getType();
		return isPrimitive(cl);
	}

	protected boolean isPrimitive(Class cl) {

		if (cl.isPrimitive()) {
			return true;
		}
		for (Class c : PRIMITIVES) {
			if (c.equals(cl)) {
				return true;
			}
		}
		return false;
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

	/**
	 * @param pName
	 * @return
	 */
	private static String capitalizeFirstLetter(final String str) {
		final String s = str.substring(0, 1).toUpperCase() + str.substring(1);
		return s;
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

	public void process() {

	}

	public List<Class> getListClass() {
		return listClass;
	}

	public Class getClazz() {
		return clazz;
	}

	private String getMethodName(Class c) {
		return prefixMethod + c.getSimpleName();
	}

	public String getMethodName() {
		return getMethodName(clazz);
	}

	public MethodSpec getMethodToString() {
		MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(getMethodName()).returns(String.class).addModifiers(Modifier.PROTECTED).addParameter(clazz, "o").addModifiers(Modifier.STATIC);
		methodBuilder.addStatement("int i=0");
		methodBuilder.addStatement("String s =  \"{\"");
		int i=0;
		for (Field f : listFields) {
			i++;
			if (f.getType().equals(String.class)) {
				methodBuilder.addStatement(" s +="+getKey(f)+"  \\\"\" + o."+getGetter(f)+"+\"\\\" "+virgule(i)+"\\n\"");
			}else if (isPrimitive(f)) {
				methodBuilder.addStatement(" s += "+getKey(f)+"\" + o."+getGetter(f)+"+\""+virgule(i)+"\\n\"");
			}else if (Date.class.equals(f.getType())) {
				methodBuilder.addStatement(" s += "+getKey(f)+" \\\"\" + o."+getGetter(f)+"+\"\\\\\\\" "+virgule(i)+"\\n\"");
			}else if (List.class.equals(f.getType())){
				methodBuilder.addStatement("i=0");
				Class c =getClassCustom(f);
				methodBuilder.addStatement(" s +="+getKey(f)+"[  \"");
				methodBuilder.beginControlFlow("if(  o."+getGetter(f)+"== null)");
				
				methodBuilder.addStatement(" s += \"\\n\"");
				methodBuilder.nextControlFlow("else");
				methodBuilder.beginControlFlow("for( $T x : o."+getGetter(f)+")",c);
				
				methodBuilder.beginControlFlow("if( i > 0)");
				methodBuilder.addStatement("s +=\",\\n\" ");
				methodBuilder.endControlFlow();
				methodBuilder.addStatement("i++");
				
				if (String.class.equals(c)) {
					methodBuilder.addStatement(" s +=\" \\\"\"+x +\"\\\"\"");
				}else {
					methodBuilder.addStatement("s += "+getMethodName(c)+"( x)+\"\\\"\"");
				}
				methodBuilder.endControlFlow();
				methodBuilder.endControlFlow();
				methodBuilder.addStatement(" s +=\"]"+virgule(i)+"\\n  \"");
			}else if (f.getType().isEnum()){
				methodBuilder.addStatement(" s += "+getKey(f)+" \\\"\" + o."+getGetter(f)+"+\"\\\" "+virgule(i)+"\\n\"");
			}else {
				methodBuilder.addStatement(" s +="+getKey(f)+"  \" +"+getMethodName(f.getType())+" (o."+getGetter(f)+")"+"+\""+virgule(i)+"\\n\"");
			}
		}
		methodBuilder.addStatement("s +=  \"}\"");
		methodBuilder.addStatement("return s");
		return methodBuilder.build();
	}
	
	private String virgule(int i) {
	    if(i <listFields.size()) {
	    	return ",";
	    }else {
	    	return" ";
	    }
		
	}

	private String getKey(Field f) {
		String s ="\"\\\""+f.getName()+"\\\" : ";
		return s;
	}

	Random random = new Random();

	private void processMethodCreateEnum(Builder methodBuilder) {
		// EnumPays out = EnumPays.values()[0];
		int max = UtilEnumeration.getSizeEnum(clazz);
		int i = random.nextInt(max);
		methodBuilder.addStatement("$T out = $T.values ()[" + i + "]", clazz, clazz);

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
	

}
