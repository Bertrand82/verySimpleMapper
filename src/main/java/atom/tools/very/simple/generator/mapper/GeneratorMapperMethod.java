package atom.tools.very.simple.generator.mapper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.MethodSpec;

public class GeneratorMapperMethod extends AbstracMethodMapper {

	private final List<Field> listList = new ArrayList<Field>();

	private final List<Field> listListString = new ArrayList<Field>();
	private final List<Field> listLPrimitive = new ArrayList<Field>();

	private final List<Field> listString = new ArrayList<Field>();

	private final List<Field> listField = new ArrayList<Field>();
	private final List<Field> listEnum = new ArrayList<Field>();

	public GeneratorMapperMethod(ClassPair cPair)  {
		super(cPair);
		System.err.println("xxxxxx  out :: "+cPair.classOut.getName());
		for (final Field fieldOut :  classPair.classOut.getDeclaredFields()) {
			final Field fieldIn = getFieldFromClass( classPair.classIn, fieldOut.getName());
			if (fieldOut.getType().isEnum()) {
				if (getterExistForField( classPair.classIn, fieldOut)) {
					final ClassPair cp = new ClassPair(fieldIn.getType(), fieldOut.getType());
					listClassPair.add(cp);
					listEnum.add(fieldOut);
				} else {
					logMappageImpossible("A0", fieldIn, fieldOut);
				}

			} else if (isPrimitive(fieldOut)) {
				if (getterExistForField( classPair.classOut, fieldOut)) {
					listLPrimitive.add(fieldOut);
				} else {
					logMappageImpossible("A1", fieldIn, fieldOut);
				}
			} else if (fieldOut.getType().equals(String.class)) {
				if (getterExistForField( classPair.classIn, fieldOut)) {
					listString.add(fieldOut);
				} else {
					logMappageImpossible("A2", fieldIn, fieldOut);
				}
			} else if (fieldOut.getType().equals(Date.class)) {

			} else if (fieldOut.getType().equals(ClassLoader.class)) {

			} else if (fieldOut.getType().equals(Class.class)) {

			} else if (fieldOut.getType().getPackage() == null) {

			} else if ((fieldOut.getType().getPackage().getName() + "").startsWith("sun.reflect.")) {

			} else if (fieldOut.getType().getPackage().getName().startsWith("sun.")) {

			} else if (fieldOut.getType().isArray()) {

			} else if (fieldOut.getType().equals(List.class)) {
				if (getterExistForField( classPair.classIn, fieldOut)) {
					final Class<?> cOut = getGenericTypeFromList(fieldOut);
					final Class<?> cIn = getGenericTypeFromList(fieldIn);
					if (cOut.equals(String.class)) {
						listListString.add(fieldOut);
					} else {
						listList.add(fieldOut);
						final ClassPair cp = new ClassPair(cIn, cOut);
						listClassPair.add(cp);
					}
				} else {
					logMappageImpossible("A3", fieldIn, fieldOut);
				}

			} else {
				if (getterExistForField( classPair.classOut, fieldOut)) {
					final ClassPair cp = new ClassPair(fieldIn.getType(), fieldOut.getType());
					listClassPair.add(cp);
					listField.add(fieldOut);
				} else {
					logMappageImpossible("A4", fieldIn, fieldOut);
				}
			}
		}
	}

	@Deprecated
	private MethodSpec getMethodCompare() {
		final MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(prefixMethod +  classPair.classOut.getSimpleName()).addParameter( classPair.classOut, "out").addParameter( classPair.classIn, "in").addModifiers(Modifier.PUBLIC);
		methodBuilder.beginControlFlow("if (out == null)");
		methodBuilder.addStatement("return");
		methodBuilder.endControlFlow();
		methodBuilder.beginControlFlow("if (in == null)");
		methodBuilder.addStatement("return");
		methodBuilder.endControlFlow();

		methodBuilder.addStatement("//Nombre de  String :  " + listString.size());
		for (final Field fString : listString) {
			final String name = fString.getName();
			methodBuilder.addStatement("//SS  String " + name);
			methodBuilder.beginControlFlow("if ((out." + getGetter(fString) + "== null) &&  (in." + getGetter(fString) + "!= null))");
			methodBuilder.addStatement("String value =in." + getGetter(fString));
			methodBuilder.beginControlFlow("if ( this.rapportManager.isNonMapped(value))");
			methodBuilder.addStatement("out." + getSetter(fString) + "(value)");
			methodBuilder.endControlFlow();
			methodBuilder.endControlFlow();
		}

		for (final Field field : listField) {
			methodBuilder.addStatement("//FF  " + field.getName() + "  " + field.getType().getSimpleName());
			methodBuilder.addStatement("out." + getSetter(field) + "(" + prefixMethod + field.getType().getSimpleName() + "(out." + ", in." + getGetter(field) + "))");

		}

		for (final Field field : listLPrimitive) {
			methodBuilder.addStatement("//PP  " + field.getName() + "  " + field.getType().getSimpleName());
			methodBuilder.addStatement(prefixMethod + field.getType().getSimpleName() + "(out." + getGetter(field) + ", in." + getGetter(field) + ")");

		}

		for (final Field field : listList) {
			methodBuilder.addStatement("//LL  " + field.getName() + "  list " + getGenericTypeFromList(field));
			final Class<?> c = getGenericTypeFromList(field);
			final String getter = getGetter(field);
			methodBuilder.beginControlFlow("for(int i=0; i<in." + getter + ".size();i++)");
			methodBuilder.addStatement(prefixMethod + c.getSimpleName() + "( out." + getter + ".get(i), in." + getter + ".get(i))");
			methodBuilder.endControlFlow();
		}

		for (final Field field : listListString) {
			methodBuilder.addStatement("//LS  " + field.getName() + "  list " + getGenericTypeFromList(field) + " No Implemented");

		}

		return methodBuilder.build();

	}

	public MethodSpec getMethodMapper() {
		final MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(getMethodName())

				.addParameter( classPair.classIn, "in").returns( classPair.classOut).addModifiers(Modifier.PROTECTED).addModifiers(Modifier.STATIC);
		methodBuilder.addStatement("$T out =  new $T() ",  classPair.classOut,  classPair.classOut);
		for (final Field fString : listString) {
			final String name = fString.getName();
			methodBuilder.addComment(" String " + name);
			String v = getMapPrimitive(fString);
			methodBuilder.addStatement(v);
		}

		for (final Field field : listLPrimitive) {
			final String name = field.getName();
			methodBuilder.addComment("PP1 Primitive  " + field.getType().getName() + "   " + name);
			String v = getMapPrimitive(field);
			methodBuilder.addStatement(v);
		}
		for (final Field field : listField) {
			methodBuilder.addComment("FF1  " + field.getName() + "  " + field.getType().getSimpleName());
			methodBuilder.addStatement("out." + getSetter(field) + "(" + prefixMethod + field.getType().getSimpleName() + "( in." + getGetter(field) + "))");

		}

		for (final Field field : listList) {
			methodBuilder.addComment("LLA1  " + field.getName() + "  list " + getGenericTypeFromList(field));
			final Class<?> cOut = getGenericTypeFromList(field);
			Field fieldIn = getFieldFromClass(classPair.classIn, field.getName());// TODO a deporter dans classPAir method getFieldInByName(String name)
			final Class<?> cIn = getGenericTypeFromList(fieldIn);
			String ls = "ls_" + field.getName();
			methodBuilder.addStatement("$T <$T> " + ls + " = new $T<$T>()", List.class, cOut, ArrayList.class, cOut);
			final String getter = getGetter(field);
			methodBuilder.beginControlFlow("if(in." + getter + " != null)");
			methodBuilder.beginControlFlow("for($T o : in." + getter + ")", cIn);
			String ss = prefixMethod + cOut.getSimpleName() + "( o )";
			methodBuilder.addStatement(ls + ".add(" + ss + ")");
			methodBuilder.endControlFlow();
			methodBuilder.endControlFlow();
			String setter = "out." + getSetter(field) + "(" + ls + ")";

		}

		for (final Field field : listListString) {
			methodBuilder.addComment("//LS1  " + field.getName() + "  list  String" + getGenericTypeFromList(field) + " ");
			String ls = "ls_" + field.getName();
			methodBuilder.addStatement("$T <String>" + ls + " = new $T<String>()", List.class, ArrayList.class);
			final String getter = getGetter(field);
			methodBuilder.beginControlFlow("for(String s : in." + getter + ")");
			methodBuilder.addStatement(ls + ".add(s)");
			methodBuilder.endControlFlow();
			String setter = "out." + getSetter(field) + "(" + ls + ")";
			methodBuilder.addStatement(setter);
		}

		for (final Field field : listEnum) {
			methodBuilder.addComment("//LENUM  " + field.getName() + " No Implemented ");
		}
		methodBuilder.addStatement("return out");
		return methodBuilder.build();
	}

}
