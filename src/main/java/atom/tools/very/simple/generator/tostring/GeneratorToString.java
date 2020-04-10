package atom.tools.very.simple.generator.tostring;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import atom.tools.very.simple.generator.mapper.AbstracMethodMapper;
import atom.tools.very.simple.generator.mapper.ClassPair;
import atom.tools.very.simple.generator.mapper.JavaPoetWriter;
import atom.tools.very.simple.generator.mapper.MappageFailure;
import atom.tools.very.simple.mapper.p1.Info;

public class GeneratorToString {

	private GeneratorClassToString  clazzFactory;
	private File dirOutput = new File("GENERATED_COMPARATOR");
	private JavaPoetWriter javaPoetWritter;
	private static String packageName = "atom.generated.factory";
	private List<GeneratorClassToString> listClassFactory = new ArrayList();
	
	public GeneratorToString(Class<Info> class1) throws ClassNotFoundException {
		
		dirOutput.mkdirs();
		this.clazzFactory = new GeneratorClassToString(class1);
		processClass(this.clazzFactory);
        System.out.println(this.listClassFactory);
		javaPoetWritter = new JavaPoetWriter(dirOutput);
		javaPoetWritter.write(getJavaFileGenerator());
		System.err.println("ObjectFacrory Done  dirOutput exists  : " + dirOutput.exists() + " | path  : " + dirOutput.getAbsolutePath());
	}

	private void processClass(final GeneratorClassToString c) throws ClassNotFoundException {

		listClassFactory.add(c);
		c.process();
		for (final Class cl : c.getListClass()) {
			if (!isProcessed(cl)) {
				processClass(new GeneratorClassToString(cl));
			}
		}
	}

	private boolean isProcessed(Class  cp) {
		for (GeneratorClassToString c : this.listClassFactory) {
			if (c.getClazz().equals(cp)) {
				return true;
			}
		}
		return false;
	}

	public JavaFile getJavaFileGenerator() {

		final TypeSpec.Builder classBuilder = TypeSpec.classBuilder(getClassFactoryName()).addModifiers(Modifier.PUBLIC);

		String comment = "Cette Class propose un factory utile pour les tests \n";
		comment += "Class : \t" + clazzFactory.getClazz().getName() + "\n";
	

	  
		comment += "\n\n ";
		classBuilder.addJavadoc(comment);
		classBuilder.addMethod(mainFactoryMethod());
		
		
		
		for (final GeneratorClassToString cp : listClassFactory) {
			classBuilder.addMethod(cp.getMethodToString());		
		}
		classBuilder.addMethod(virguleFactoryMethod());
		final TypeSpec customClassMapper = classBuilder.build();
		final JavaFile.Builder javaFileBuilder = JavaFile.builder(packageName, customClassMapper).indent("    ");

		javaFileBuilder.addFileComment("Generated by " + this.getClass().getName());
		final JavaFile javaFile = javaFileBuilder.build();
		return javaFile;
	}
	
	private String getClassFactoryName() {
	
			return "ToString_" + clazzFactory.getClazz().getSimpleName();
	}
	private MethodSpec virguleFactoryMethod( ) {
		final MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("virg").
			
				returns(String.class).
				addModifiers(Modifier.PRIVATE).
				addModifiers(Modifier.STATIC).
				addParameter(int.class, "i").
		        addParameter(int.class, "j");
		
		methodBuilder.addJavadoc("\nRajoute virgule\n");
		methodBuilder.beginControlFlow("if (i <j)");
		methodBuilder.addStatement("return \",\"");
		methodBuilder.nextControlFlow("else");
		methodBuilder.addStatement("return \"aaaaaaaaaaaaaaaaaa,bbbbbbbbb\"");
				methodBuilder.endControlFlow();
						return methodBuilder.build();
	}
	private MethodSpec mainFactoryMethod( ) {
		final MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("toString").
			
				returns(String.class).
				addModifiers(Modifier.PUBLIC).
				addModifiers(Modifier.STATIC).addParameter(clazzFactory.getClazz(), "o");
		
		methodBuilder.addJavadoc("\nSeule methode public de la classe\n");
		
		methodBuilder.addStatement("return " + clazzFactory.getMethodName() + "(o)");
		return methodBuilder.build();
	}

}
