package atom.tools.very.simple.mapper;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;

import atom.tools.very.simple.util.UtilString;

public class Compilator {

	public static Class  compile(File file, String className) throws Exception {
		String s = UtilString.readFile(file);
		return compile(s, className);
	}

	public static Class  compile(String s, String className) throws Exception {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
		JavaFileObject file = new JavaSourceFromString(className, s);
		Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(file);
		CompilationTask task = compiler.getTask(null, null, diagnostics, null, null, compilationUnits);

		boolean success = task.call();
		for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
			System.out.println(toString(diagnostic));
		}
		System.out.println("Compile Success: " + success+"   for "+className);

		if (success) {
			Class clazz=null;
			try {
				clazz = Class.forName(className);
			} catch (ClassNotFoundException e) {
				System.out.println("ClassNotFoundException "+e.getMessage());
			}
			return clazz;
		}else {
			return null;
		}
	}

	private static String toString(Diagnostic diagnostic) {
		String s ="";
		s+=diagnostic.getCode();
		s+=diagnostic.getKind();
		s+=diagnostic.getPosition();
		s+=diagnostic.getStartPosition();
		s+=diagnostic.getEndPosition();
		s+=diagnostic.getSource();
		s+=diagnostic.getMessage(null);
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