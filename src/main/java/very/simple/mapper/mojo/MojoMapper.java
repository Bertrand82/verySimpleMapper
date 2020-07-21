package very.simple.mapper.mojo;


import java.io.File;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import atom.tools.very.simple.generator.mapper.GeneratorMapper;


@Mojo(name = "map")
public class MojoMapper extends AbstractMojo {

	/**
	 * The greeting to display.
	 */
	@Parameter(property = "list.map")
	private List<ClassesMapProperties> mapClasses;

	@Parameter(defaultValue="${project.build.directory}/generated-sources/simple-mapper", required=false)
	private File dirTarget;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		System.err.println("dirTarget  : "+dirTarget.getAbsolutePath());
		if (mapClasses == null) {
			getLog().error("MojoMapper " + mapClasses);
			return;
		}
		getLog().info("MojoMapper mapClasses.size size :" + mapClasses.size());
		for (ClassesMapProperties cmp : mapClasses) {
			getLog().info("MojoMapper "+cmp);
			try {
				mapGenerate(cmp);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public List<ClassesMapProperties> getMapClasses() {
		return mapClasses;
	}

	public void setMapClasses(List<ClassesMapProperties> mapClasses) {
		this.mapClasses = mapClasses;
	}
	
	public  void  mapGenerate(ClassesMapProperties cmp) throws Exception {
		System.out.println("------------------------  start ");
		final GeneratorMapper generator = new GeneratorMapper(cmp.getClass1(), cmp.getClass2(),dirTarget);
		File dirOutputMapper = generator.getDirOutput();
		getLog().info("dirOutPut "+dirOutputMapper);
		
	}

	public File getDirTarget() {
		return dirTarget;
	}

	public void setDirTarget(File dirTarget) {
		this.dirTarget = dirTarget;
	}



}  
