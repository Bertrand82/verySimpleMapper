package atom.tools.very.simple.mapper;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import atom.tools.very.simple.util.UtilString;

public class Compilator {

	private static File classes = new File("target/classes");
	
	public static Class<?> compile(File file, String className) throws Exception {
		String s = UtilString.readFile(file);
		return compile(s, className);
	}

	public static Class<?> compile(String classStr, String className) throws Exception {
		JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
		
		classes.mkdir();
	
		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
		JavaFileObject javaFileObject = new JavaSourceFromString(className, classStr);
		List<? extends JavaFileObject> listJavaFileObject = Arrays.asList(javaFileObject);

		List<String> options = new ArrayList<String>();
		options.add("-d");
		options.add(classes.getAbsolutePath());
		options.add("-verbose");

		CompilationTask task = javaCompiler.getTask(null, null, diagnostics, options, null, listJavaFileObject);

		boolean success = task.call();

		for (Diagnostic<?> diagnostic : diagnostics.getDiagnostics()) {
			System.out.println(toString(diagnostic));
		}

		System.out.println("Compile Success: " + success + "   for " + className);
		System.out.println("Diagnostic  : " + diagnostics.getDiagnostics());

		if (success) {			
			Class<?> clazz = null;
			try {
				clazz = Class.forName(className);
			} catch (ClassFormatError e) {
				System.out.println("ClassFormatError " + e.getMessage());
			} catch (ClassNotFoundException e) {
				System.out.println("ClassNotFoundException " + e.getMessage());
			}
			System.out.println("clazz " + clazz);
			return clazz;
		} else {
			return null;
		}
	}

	private static String toString(Diagnostic<?> diagnostic) {
		String s = "";
		s += diagnostic.getCode();
		s += diagnostic.getKind();
		s += diagnostic.getPosition();
		s += diagnostic.getStartPosition();
		s += diagnostic.getEndPosition();
		s += diagnostic.getSource();
		s += diagnostic.getMessage(null);
		return s;
	}
}

class JavaSourceFromString extends SimpleJavaFileObject {
	final String code;

	JavaSourceFromString(String name, String code) {
		super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
		this.code = code;
	}

	@Override
	public CharSequence getCharContent(boolean ignoreEncodingErrors) {
		return code;
	}
}