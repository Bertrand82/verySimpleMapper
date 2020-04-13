package atom.tools.very.simple.generator.equals;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.MethodSpec.Builder;



public class GeneratorClassEquals {
	public static final Class[] PRIMITIVES = { Boolean.class, Integer.class, Double.class, Float.class, Long.class };

	private static final String prefixMethod = "equals_";
	private final Class clazz;
	private final List<Class> listClass = new ArrayList<Class>();
	private final List<Field> listEnum = new ArrayList<Field>();

	private final List<Field> listLPrimitive = new ArrayList<Field>();

	private final List<Field> listString = new ArrayList<Field>();

	private final List<Field> listListString = new ArrayList<Field>();

	private final List<Field> listList = new ArrayList<Field>();

	private final List<Field> listField = new ArrayList<Field>();

	public GeneratorClassEquals(Class class1) {
		this.clazz = class1;
		for (final Field field : clazz.getDeclaredFields()) {
			if (field.getType().isEnum()) {
				if (getterExistForField(clazz, field)) {
					listClass.add(field.getType());
					listEnum.add(field);
				} else {
					logMappageImpossible("A0", field);
				}

			} else if (isPrimitive(field)) {
				if (getterExistForField(clazz, field)) {
					listLPrimitive.add(field);
				} else {
					logMappageImpossible("A1", field);
				}
			} else if (field.getType().equals(String.class)) {
				if (getterExistForField(clazz, field)) {
					listString.add(field);
				} else {
					logMappageImpossible("A2", field);
				}
			} else if (field.getType().equals(Date.class)) {

			} else if (field.getType().equals(ClassLoader.class)) {

			} else if (field.getType().equals(Class.class)) {

			} else if (field.getType().getPackage() == null) {

			} else if ((field.getType().getPackage().getName() + "").startsWith("sun.reflect.")) {

			} else if (field.getType().getPackage().getName().startsWith("sun.")) {

			} else if (field.getType().isArray()) {

			} else if (field.getType().equals(List.class)) {
				if (getterExistForField(clazz, field)) {
					final Class<?> cOut = getGenericTypeFromList(field);
					if (cOut.equals(String.class)) {
						listListString.add(field);
					} else {
						listList.add(field);
						listClass.add(cOut);
					}
				} else {
					logMappageImpossible("Z3", field);
				}

			} else {
				if (getterExistForField(clazz, field)) {

					listClass.add(field.getType());
					listField.add(field);
				} else {
					logMappageImpossible("Z4", field);
				}
			}
		}
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

	public MethodSpec getMethodCreate() {
		MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(getMethodName()).
				returns(boolean.class).
				addModifiers(Modifier.PROTECTED).
				addParameter(clazz,"o1").
				addParameter(clazz,"o2").
				addModifiers(Modifier.STATIC);
		methodBuilder.beginControlFlow("if ((o1 == null) && (o2 == null))");	
		methodBuilder.addStatement("return true" );
		methodBuilder.endControlFlow();
		methodBuilder.beginControlFlow("else if (o1 == null) ");	
		methodBuilder.addStatement("return false" );
		methodBuilder.endControlFlow();
		methodBuilder.beginControlFlow("else if (o2 == null) ");	
		methodBuilder.addStatement("return false" );
		methodBuilder.endControlFlow();

		if (clazz.isEnum()) {
			processMethodCreateEnum(methodBuilder);

		} else {
		
			processString(methodBuilder);
			processPrimitive(methodBuilder);
			processEnum_(methodBuilder);
			processField(methodBuilder);
			processListString(methodBuilder);
		   processList(methodBuilder);
		}

		methodBuilder.addStatement("return true");
		return methodBuilder.build();
	}

	private void processList(Builder methodBuilder) {
		processList(methodBuilder,listList);
	}

	private void processListString(Builder methodBuilder) {
		processList(methodBuilder,listListString);
	}
	
	private void processList(Builder methodBuilder,List<Field> listField) {
				methodBuilder.addStatement("//Nombre de  List<String> :  " + listListString.size());
		   for (final Field field : listField) {
			final String name = field.getName();
			methodBuilder.addStatement("//SFF8   "+field.getType()+"   " + name);
			
			methodBuilder.beginControlFlow("if ( ( o1."+getGetter(field)+"==null) && (o2."+getGetter(field)+"==null))");
			
			methodBuilder.endControlFlow();
			methodBuilder.beginControlFlow("else if ( o1."+getGetter(field)+"==null) ");
			methodBuilder.addStatement("return false" );
			methodBuilder.endControlFlow();
			methodBuilder.beginControlFlow("else if ( o2."+getGetter(field)+"==null) ");
			methodBuilder.addStatement("return false" );
			methodBuilder.endControlFlow();
			methodBuilder.beginControlFlow("else ");
			final Class<?> c = getGenericTypeFromList(field);
			methodBuilder.addStatement( "$T<$T>  l1=  o1."+getGetter(field) ,field.getType(),c);
			methodBuilder.addStatement("$T<$T> l2 =  o2."+getGetter(field) ,field.getType(),c);
			//  
			methodBuilder.beginControlFlow(" if (l1.size()!= l2.size()) ");
			methodBuilder.addStatement("return false" );
			methodBuilder.endControlFlow();
			//		
		
			methodBuilder.beginControlFlow("for(int i=0; i< l1.size();i++)");
			if (String.class.equals(c)) {
				methodBuilder.beginControlFlow("if  (! equals_Object( l1.get(i) , l2.get(i)) )");			
				methodBuilder.addStatement("return false" );
				methodBuilder.endControlFlow();
			}else {
				methodBuilder.beginControlFlow("if  (! "+getMethodName(c)+"( l1.get(i) , l2.get(i)) )");			
				methodBuilder.addStatement("return false" );
				methodBuilder.endControlFlow();
			}
			methodBuilder.endControlFlow();
			
			methodBuilder.endControlFlow();
			
		}
	}

	private void processField(Builder methodBuilder) {
		methodBuilder.addStatement("// Nombre d'objet :  " + listField.size());
		for (final Field field : listField) {
			final String name = field.getName();
			methodBuilder.addStatement("//S5   "+field.getType()+"   " + name);
			
			methodBuilder.beginControlFlow("if (! "+getMethodName(field.getType())+"(o1."+getGetter(field)+", o2."+getGetter(field)+"))" );
			methodBuilder.addStatement("return false" );
			methodBuilder.endControlFlow();
		}
	}

	private void processEnum_(Builder methodBuilder) {
		methodBuilder.addStatement("//Nombre de  enum :  " + listEnum.size());
		for (final Field field : listEnum) {
			final String name = field.getName();
			methodBuilder.addStatement("//SS4   "+field.getType()+"   " + name);
			methodBuilder.beginControlFlow("if (!equals_Object(o1."+getGetter(field)+", o2."+getGetter(field)+"))" );
			methodBuilder.addStatement("return false" );
			methodBuilder.endControlFlow();
		}
	}

	private void processPrimitive(Builder methodBuilder) {
		methodBuilder.addStatement("//Nombre de  native :  " + listLPrimitive.size());
		for (final Field field : listLPrimitive) {
			final String name = field.getName();
			methodBuilder.addStatement("//Sww  Primitive "+field.getType()+"  "+ name);

			methodBuilder.beginControlFlow("if (!(o1."+getGetter(field)+" ==( o2."+getGetter(field)+")))" );
			methodBuilder.addStatement("return false" );
			methodBuilder.endControlFlow();
				
		}
		
	}

	private void processString(Builder methodBuilder) {
		methodBuilder.addStatement("//Nombre de  String :  " + listString.size());
		for (final Field fString : listString) {
			final String name = fString.getName();
			methodBuilder.addStatement("//SS  String " + name);

			methodBuilder.beginControlFlow("if (!equals_Object(o1."+getGetter(fString)+", o2."+getGetter(fString)+"))" );
			methodBuilder.addStatement("return false" );
			methodBuilder.endControlFlow();
				
			
		}

	}


	private void processMethodCreateEnum(Builder methodBuilder) {
		// EnumPays out = EnumPays.values()[0];
		 
		methodBuilder.beginControlFlow("if ( (o1 != o2))" );
		methodBuilder.addStatement("return false" );
		methodBuilder.endControlFlow();

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
