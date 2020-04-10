package atom.tools.very.simple.generator.mapper;

import java.util.ArrayList;
import java.util.List;

import com.squareup.javapoet.MethodSpec;

public class ClassPair {

    public Class classIn;

    public Class classOut;

    boolean isAlreadyProcessed = false;

    public ClassPair() {
    }

    public ClassPair(final Class pClassIn, final Class pClassOut) {
        super();
        classIn = pClassIn;
        classOut = pClassOut;
    }

    /**
     * @return Attribut {@link #isAlreadyProcessed}
     */
    public boolean isAlreadyProcessed() {
        return isAlreadyProcessed;
    }

    /**
     * @param pIsAlreadyProcessed Valeur à affecter à l'attribut {@link #isAlreadyProcessed}
     */
    public void setAlreadyProcessed(final boolean pIsAlreadyProcessed) {
        isAlreadyProcessed = pIsAlreadyProcessed;
    }

	public boolean isEnum() {
	   if (classIn.isEnum()) {
		   return true;
	   }
		return classOut.isEnum();
	}

	public ClassPair getSymetrique() {	
		return new ClassPair(classOut, classIn);
	}

	 private  AbstracMethodMapper  methodsMapper_1  ; 
	 private  AbstracMethodMapper  methodsMapper_2  ; 
	 
	public void process() {
		methodsMapper_1 = AbstracMethodMapper.createMapper(this);		
		methodsMapper_2= AbstracMethodMapper.createMapper(this.getSymetrique());		
	}

	public List<ClassPair> getListClassPair() {
		List<ClassPair> list = new ArrayList<ClassPair>();
		list.addAll(methodsMapper_1.getListClassPair());
		list.addAll(methodsMapper_2.getListClassPair());
		return list;
	}

	public AbstracMethodMapper getMethodsMapper_1() {
		return methodsMapper_1;
	}

	public AbstracMethodMapper getMethodsMapper_2() {
		return methodsMapper_2;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((classIn == null) ? 0 : classIn.hashCode());
		result = prime * result + ((classOut == null) ? 0 : classOut.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClassPair other = (ClassPair) obj;
		if (classIn == null) {
			if (other.classIn != null)
				return false;
		} else if (!classIn.equals(other.classIn))
			return false;
		if (classOut == null) {
			if (other.classOut != null)
				return false;
		} else if (!classOut.equals(other.classOut))
			return false;
		return true;
	}

	public boolean equalsAsymetrique(ClassPair cp) {
		if (this.equals(cp)) {
			return true;
		}
		if (this.getSymetrique().equals(cp)) {
			return true;
		}
		return false;
	}

	


	
}



