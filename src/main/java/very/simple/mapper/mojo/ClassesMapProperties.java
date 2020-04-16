package very.simple.mapper.mojo;

import org.apache.maven.plugins.annotations.Parameter;

public class ClassesMapProperties {
	
    
	@Parameter
	private String class1;
    
    @Parameter
	private String class2;
    
    
	public ClassesMapProperties() {
		super();
	}
	public String getClass1() {
		return class1;
	}
	public void setClass1(String class1) {
		this.class1 = class1;
	}
	public String getClass2() {
		return class2;
	}
	public void setClass2(String class2) {
		this.class2 = class2;
	}
	
	@Override
	public String toString() {
		return "  class1=" + class1 + ", class2=" + class2 + "";
	}

}
