package atom.tools.very.simple.generator.mapper;

import java.lang.reflect.Field;

public class MappageFailure {

	String from;
	ClassPair classPair;
	Field fieldIn;
	Field fieldOut;
	
	public MappageFailure(String from, ClassPair classPair, Field fieldIn, Field fieldOut) {
		super();
		this.from = from;
		this.classPair = classPair;
		this.fieldIn = fieldIn;
		this.fieldOut = fieldOut;
	}

	@Override
	public String toString() {
		String s = "Failure:  "+from;
		s += " \t| class :  "+classPair.classOut.getSimpleName();
		s += " \t| fieldOut ";
		if (fieldOut == null) {
			s += " Missing  ";
		}else {
			s+=toString(fieldOut.getType().getSimpleName(),6)+ "  \t"+toString(fieldOut.getName(),6);
		}
		s += " \t| fieldIn ";
		if (fieldIn == null) {
			s += " Missing  ";
		}else {
			s+=toString(fieldIn.getType().getSimpleName(),6) +"  \t"+toString(fieldIn.getName(),6);
		}
		return s;
	}
	
	private String  toString (String s,int n) {
		s=""+s;
		while(s.length()< n) {
			s+=" ";
		}
		return  s;
		
	}

	

}
