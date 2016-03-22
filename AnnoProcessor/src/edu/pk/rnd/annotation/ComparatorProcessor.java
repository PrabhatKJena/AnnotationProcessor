package edu.pk.rnd.annotation;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.tools.Diagnostic.Kind;
import javax.tools.FileObject;

import edu.pk.annotation.Comparator;
import edu.pk.rnd.util.Logger;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({ "edu.pk.annotation.Comparator", "edu.pk.annotation.PublicFinal" })
public class ComparatorProcessor extends AbstractProcessor {

	private Messager messager;
	private Logger logger = new Logger();
	private Filer filer;

	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		super.init(processingEnv);
		this.messager = processingEnv.getMessager();
		this.filer = processingEnv.getFiler();
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		Set<? extends Element> rootElements = roundEnv.getRootElements();
		for (Element e : rootElements) {
			logger.log("e.simpleName:" + e.getSimpleName());
			logger.log("e:" + e.toString());
		}

		for (TypeElement typeElement : annotations) {
			logger.log("typeElement : " + typeElement.toString());
			logger.log("typeElement.getQualifiedName() : " + typeElement.getQualifiedName());

			// For Comparator
			if ("edu.pk.annotation.Comparator".equals(typeElement.getQualifiedName().toString())) {
				Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(typeElement);

				for (Element element : elements) {
					// if method
					logger.log("elelemt : " + element);
					logger.log("elelemt.getKind() : " + element.getKind().toString());
					if (element.getKind() == ElementKind.METHOD) {
						ExecutableElement executableElement = (ExecutableElement) element;
						if (executableElement.getReturnType().getKind() != TypeKind.INT) {
							messager.printMessage(Kind.ERROR, "@Comparator annotated method return type must be int",
									element);
							return true;
						}

						Comparator annotation = element.getAnnotation(Comparator.class);
						String comparatorName = annotation.value();
						String comparatorMethodName = element.getSimpleName().toString();
						String processedClassName = element.getEnclosingElement().toString();
						logger.log("comparatorMethodName:" + comparatorMethodName);
						logger.log("processedClassName:" + processedClassName);
						logger.log("Comparator Name : " + comparatorName);
						if (comparatorName == null) {
							messager.printMessage(Kind.ERROR, "Value must not be empty", element);
							return true;
						}

						try {
							generateComparatorSource(comparatorName, comparatorMethodName, processedClassName);
						} catch (IOException e1) {
							e1.printStackTrace();
							logger.log("Error :"+e1);
							return false;
						}
					}
				}
			} else {
				logger.log("comparator name not matched");
			}
		}
		logger.close();
		return true;
	}

	private void generateComparatorSource(String comparatorName, String comparatorMethodName,
			String processedClassName) throws IOException {
		int lastIndexOf = processedClassName.lastIndexOf(".");
		String packageName = processedClassName.substring(0, lastIndexOf);
		String processedClass = processedClassName.substring(lastIndexOf+1);
		String compClassName = packageName + "." + comparatorName;
		FileObject sourceFile = filer.createSourceFile(compClassName);
		logger.log(sourceFile.getName());
		PrintWriter printWriter = new PrintWriter(sourceFile.openWriter());
		printWriter.append("package "+packageName+";\n");
		printWriter.append("public class "+comparatorName+" implements java.util.Comparator<"+processedClass+">{\n");
		printWriter.append("\tpublic int compare("+processedClass+" o1, "+processedClass+" o2){\n");
		printWriter.append("\t\t return o1."+comparatorMethodName+"(o2);\n");
		printWriter.append("\t}\n");
		printWriter.append("}");
		logger.log("Generated...");
		printWriter.close();
	}
}
