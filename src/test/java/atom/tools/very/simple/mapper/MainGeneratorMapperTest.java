package atom.tools.very.simple.mapper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import atom.tools.very.simple.generator.mapper.GeneratorMapper;

public class MainGeneratorMapperTest {

	/**
	 * Generation du code permettant de verifier qu'aucun String n'a été oublié. La
	 * classe générée est incorporée dans le projet bm1atom-tarif-ws
	 *
	 * @param s
	 * @throws Exception
	 */
	public static void main(final String[] s) throws Exception {
		main();
	}
	
	public static void main() throws Exception {
		(new MainGeneratorMapperTest()) .mapGenerate();
	}

	@Test
	public void mapTest() throws Exception {
		mapGenerate();
	}
	
	public  Class  mapGenerate() throws Exception {
		final Class clazzIn = atom.tools.very.simple.mapper.p1.Info.class;
		final Class clazzOut = atom.tools.very.simple.mapper.p2.Info.class;
		return mapGenerate(clazzIn, clazzOut);
	}
	
	public  Class  mapGenerate(Class clazzIn, Class clazzOut) throws Exception {
		System.out.println("MainComparator start ");
		final GeneratorMapper generator = new GeneratorMapper(clazzOut, clazzIn);
		File dirOutputMapper = generator.getDirOutput();
		assertTrue(dirOutputMapper.exists());
		List<File> listJavaFiles = getJavaFileFromDir(dirOutputMapper);
		assertTrue(listJavaFiles.size() > 0);
		for (File f : listJavaFiles) {
			assertTrue(f.exists());
		}
		for (File file : listJavaFiles) {
			String className = getClassName(dirOutputMapper, file);			
			Class clazz = Compilator.compile(file, className);
			assertNotNull(clazz);
			return clazz;
		}
		return null;
	}

	private static String getClassName(File dirGenerated, File javaFile) {
		String s = "";
		try {
			s = javaFile.getCanonicalPath();
			s = s.replace(dirGenerated.getCanonicalPath(), "");
			s = s.substring(1);
			s = s.replace("\\", ".");
			s = s.replace("/", ".");
			s = s.replace(".java", "");
			return s;
		} catch (Exception e) {
			e.printStackTrace();
			return s;
		}
	}

	private List<File> getJavaFileFromDir(File dirOutput) {
		List<File> list = new ArrayList<File>();
		;
		for (File f : dirOutput.listFiles()) {
			if (f.isDirectory()) {
				list.addAll(getJavaFileFromDir(f));
			} else if (f.getName().endsWith(".java")) {
				list.add(f);
			}
		}
		return list;
	}
}
