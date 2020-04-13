package atom.tools.very.simple.generator.factoryUtil;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.TypeSpec;

import atom.tools.very.simple.generator.equals.GeneratorEquals;
import atom.tools.very.simple.generator.factory.GeneratorFactory;
import atom.tools.very.simple.generator.tostring.GeneratorToString;


public class GeneratorUtil {

	
	public GeneratorUtil(Class class1) throws ClassNotFoundException {
		 TypeSpec.Builder classBuilder  =	TypeSpec.classBuilder(getClassFactoryName(class1)).addModifiers(Modifier.PUBLIC);
		GeneratorToString generatorToString = new GeneratorToString(class1, classBuilder);
		GeneratorFactory generatoFactory = new GeneratorFactory(class1,classBuilder);
		GeneratorEquals generatorEquals = new GeneratorEquals(class1,classBuilder);
	}

	private String getClassFactoryName(Class  c) {
		
		return "Util_"+c.getSimpleName()+"";
	}

}
