package atom.tools.very.simple.generator.factory;

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

import atom.tools.very.simple.mapper.p1.Info;
import atom.tools.very.simple.util.UtilEnumeration;
import atom.tools.very.simple.util.UtilString;

public class GeneratorClassFactory {
	public static final Class[] PRIMITIVES = { Boolean.class, Integer.class, Double.class, Float.class, Long.class };

	private static final String prefixMethod = "create_";
	private final Class clazz;
	private final List<Class> listClass = new ArrayList<Class>();
	private final List<Field> listEnum = new ArrayList<Field>();

	private final List<Field> listLPrimitive = new ArrayList<Field>();

	private final List<Field> listString = new ArrayList<Field>();

	private final List<Field> listListString = new ArrayList<Field>();

	private final List<Field> listList = new ArrayList<Field>();

	private final List<Field> listField = new ArrayList<Field>();

	public GeneratorClassFactory(Class<Info> class1) {
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
		MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(getMethodName()).returns(clazz).addModifiers(Modifier.PROTECTED).addModifiers(Modifier.STATIC);

		if (clazz.isEnum()) {
			processMethodCreateEnum(methodBuilder);

		} else {
			methodBuilder.addStatement("$T out =  new $T ()", clazz, clazz);
			processString(methodBuilder);
			processPrimitive(methodBuilder);
			processEnum_(methodBuilder);
			processField(methodBuilder);
			processListString(methodBuilder);
			processList(methodBuilder);
		}

		methodBuilder.addStatement("return out");
		return methodBuilder.build();
	}

	private void processList(Builder methodBuilder) {
		methodBuilder.addStatement("//Nombre de  list :  " + listList.size());
		for (final Field field : listList) {
			final String name = field.getName();
			methodBuilder.addStatement("//S8   "+field.getType()+"   " + name);
			final Class<?> c = getGenericTypeFromList(field);
			methodBuilder.addStatement("out."+getSetter(field)+"(new $T<$T>())",ArrayList.class,c);
			methodBuilder.addStatement("out."+getGetter(field)+".add("+getMethodName(c)+"())");
			methodBuilder.addStatement("out."+getGetter(field)+".add("+getMethodName(c)+"())");
					
			//out.getLs().add("xxxxx");
		}
	}

	private void processListString(Builder methodBuilder) {
		methodBuilder.addStatement("//Nombre de  List<String> :  " + listListString.size());
		for (final Field field : listListString) {
			final String name = field.getName();
			methodBuilder.addStatement("//S8   "+field.getType()+"   " + name);
			methodBuilder.addStatement("out."+getSetter(field)+"(new $T<String>())",ArrayList.class);
			methodBuilder.addStatement("out."+getGetter(field)+".add(\""+UtilString.getRandomString()+"\")");
			methodBuilder.addStatement("out."+getGetter(field)+".add(\""+UtilString.getRandomString()+"\")");
			//out.getLs().add("xxxxx");
		}
	}

	private void processField(Builder methodBuilder) {
		methodBuilder.addStatement("// Nombre d'objet :  " + listField.size());
		for (final Field field : listField) {
			final String name = field.getName();
			methodBuilder.addStatement("//S5   "+field.getType()+"   " + name);
			methodBuilder.addStatement("out."+getSetter(field)+"("+getMethodName(field.getType())+"())");
		}
	}

	private void processEnum_(Builder methodBuilder) {
		methodBuilder.addStatement("//Nombre de  enum :  " + listEnum.size());
		for (final Field field : listEnum) {
			final String name = field.getName();
			methodBuilder.addStatement("//S4   "+field.getType()+"   " + name);
			methodBuilder.addStatement("out."+getSetter(field)+"("+getMethodName(field.getType())+"())");
		}
	}

	private void processPrimitive(Builder methodBuilder) {
		methodBuilder.addStatement("//Nombre de  native :  " + listLPrimitive.size());
		for (final Field field : listLPrimitive) {
			final String name = field.getName();
			methodBuilder.addStatement("//S3   "+field.getType()+"   " + name);
			methodBuilder.addStatement("out."+getSetter(field)+"("+UtilString.getRandom(field.getType())+")");
		}
		
	}

	private void processString(Builder methodBuilder) {
		methodBuilder.addStatement("//Nombre de  String :  " + listString.size());
		for (final Field fString : listString) {
			final String name = fString.getName();
			methodBuilder.addStatement("//SS  String " + name);
			methodBuilder.addStatement("out."+getSetter(fString)+"(\""+UtilString.getRandomString()+"\")");
		}

	}

	Random random = new Random();
	private void processMethodCreateEnum(Builder methodBuilder) {
		// EnumPays out = EnumPays.values()[0];
		int max = UtilEnumeration.getSizeEnum(clazz);
		int i = random.nextInt(max);
		methodBuilder.addStatement("$T out = $T.values ()["+i+"]", clazz, clazz);

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
