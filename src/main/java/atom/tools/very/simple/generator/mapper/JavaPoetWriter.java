package atom.tools.very.simple.generator.mapper;



import java.io.File;
import java.io.IOException;

import com.squareup.javapoet.JavaFile;

/**
 * @author c82bgui
 *
 */
public class JavaPoetWriter {

	private final File dir;

	public JavaPoetWriter(File dir) {
		this.dir = dir;
		dir.mkdirs();
	}

	/**
	 * @param pJavafile
	 */
	public void write(final JavaFile javafile) {
		try {



			javafile.writeTo(dir);
			
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public File getDir() {
		return dir;
	}

}


