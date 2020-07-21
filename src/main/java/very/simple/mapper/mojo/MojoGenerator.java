package very.simple.mapper.mojo;

import java.io.File;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import atom.tools.very.simple.generator.equals.GeneratorEquals;
import atom.tools.very.simple.generator.factory.GeneratorFactory;
import atom.tools.very.simple.generator.factoryUtil.GeneratorUtil;
import atom.tools.very.simple.generator.tostring.GeneratorToString;





@Mojo(name = "generateUtil")
public class MojoGenerator extends AbstractMojo{

	@Parameter(defaultValue="${project.build.directory}/generated-sources/simple-util", required=false)
	private File dirTarget;
	
	@Parameter(property = "list.classes")
	private List<String> listClasses;

	public MojoGenerator() {	
	}

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		System.err.println("dirTarget  : "+dirTarget.getAbsolutePath());
		if (listClasses == null) {
			getLog().error("MojoGenerator  listClasses :" + listClasses);
			return;
		}
		getLog().info("MojoGenerator listClasses.size size :" + listClasses.size());
		for (String  cmp : listClasses) {
			getLog().info("MojoGenerator "+cmp);
			try {
				generate(cmp);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void generate(String className) {
		try {
			Class clazz = MojoGenerator.class.getClassLoader().loadClass(className);
			
			GeneratorUtil geberatorUtil = new GeneratorUtil(clazz);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
