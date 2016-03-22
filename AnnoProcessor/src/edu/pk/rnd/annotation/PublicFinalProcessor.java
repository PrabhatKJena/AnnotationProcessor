package edu.pk.rnd.annotation;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("edu.pk.annotation.PublicFinal")
public class PublicFinalProcessor extends AbstractProcessor {

	private Messager messager;

	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		super.init(processingEnv);
		this.messager = processingEnv.getMessager();
	}

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		for (TypeElement typeElement : annotations) {
			Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(typeElement);
			for (Element element : elements) {
				Set<Modifier> modifiers = element.getModifiers();
				if (!modifiers.contains(Modifier.FINAL)) {
					messager.printMessage(Diagnostic.Kind.ERROR, "Field isn't final", element);
				}else if (!modifiers.contains(Modifier.PUBLIC)) {
					messager.printMessage(Diagnostic.Kind.ERROR, "Field isn't public", element);
				}
			}
		}
		return true;
	}
}
