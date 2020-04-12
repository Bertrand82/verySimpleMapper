package atom.tools.very.simple.generator.factoryUtil;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.TypeSpec;

import atom.tools.very.simple.generator.equals.GeneratorEquals;
import atom.tools.very.simple.generator.factory.GeneratorFactory;
import atom.tools.very.simple.generator.tostring.GeneratorToString;
import atom.tools.very.simple.mapper.p1.Info;

public class GeneratorUtil {

	
	public GeneratorUtil(Class<Info> class1) throws ClassNotFoundException {
		 TypeSpec.Builder classBuilder  =	TypeSpec.classBuilder(getClassFactoryName(class1)).addModifiers(Modifier.PUBLIC);
		GeneratorToString generatorToString = new GeneratorToString(Info.class, classBuilder);
		GeneratorFactory generatoFactory = new GeneratorFactory(Info.class,classBuilder);
		GeneratorEquals generatorEquals = new GeneratorEquals(Info.class,classBuilder);
	}

	private String getClassFactoryName(Class<Info> c) {
		
		return "Util_"+c.getSimpleName()+"";
	}

}
