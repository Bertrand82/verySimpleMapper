package atom.tools.very.simple.generator.mapper;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.MethodSpec;

public class GeneratorEnum extends AbstracMethodMapper {

	public GeneratorEnum(ClassPair cPair) {
		super(cPair);
	}

	@Override
	public MethodSpec getMethodMapper() {
		final MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(getMethodName())
				
				.addParameter( classPair.classIn, "in").returns( classPair.classOut).addModifiers(Modifier.PROTECTED).addModifiers(Modifier.STATIC);
		methodBuilder.addStatement("$T out =  null ", classPair.classOut);
		if( classPair.classOut.equals( classPair.classIn)) {
			methodBuilder.addStatement("$T out = in",  classPair.classOut);
		}else if ( classPair.classOut.isEnum()){
			methodBuilder.beginControlFlow("for($T   p :$T.values())", classPair.classOut, classPair.classOut);
			methodBuilder.beginControlFlow("if (p.name().equals(\"\"+in))");
			methodBuilder.addStatement(" out = p");
			methodBuilder.endControlFlow();			
			methodBuilder.endControlFlow();
		}else if ( classPair.classOut.equals(String.class)){
			methodBuilder.addStatement(" out = \"\"+in");
		}else {
			this.logMappageImpossible("Enum001", null, null);
		}
	
		methodBuilder.addStatement("return out");
		return methodBuilder.build();
	}

}
