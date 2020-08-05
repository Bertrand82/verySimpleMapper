package atom.tools.very.simple.mapper;

import atom.tools.very.simple.generator.equals.GeneratorEquals;
import atom.tools.very.simple.generator.factory.GeneratorFactory;
import atom.tools.very.simple.generator.factoryUtil.GeneratorUtil;
import atom.tools.very.simple.generator.tostring.GeneratorToString;
import atom.tools.very.simple.mapper.p1.Info;

public class MainGeneratorFactory {

	public MainGeneratorFactory() {
		}

	public static void main(String[] args) throws Exception {
		GeneratorToString generatorToString = new GeneratorToString(Info.class);
		GeneratorFactory generatoFactory = new GeneratorFactory(Info.class);
		GeneratorEquals generatorEquals = new GeneratorEquals(Info.class);
		GeneratorUtil generatorUtil = new GeneratorUtil(Info.class);
		MainGeneratorMapperTest.main();
	}

}
