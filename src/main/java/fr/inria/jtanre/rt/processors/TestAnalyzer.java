package fr.inria.jtanre.rt.processors;

import java.util.List;
import java.util.Map;

import fr.inria.astor.core.entities.ProgramVariant;
import fr.inria.astor.core.faultlocalization.entity.SuspiciousCode;
import fr.inria.astor.core.solutionsearch.extension.AstorExtensionPoint;
import fr.inria.jtanre.rt.core.Classification;
import fr.inria.jtanre.rt.core.GenericTestAnalysisResults;
import fr.inria.jtanre.rt.core.ProgramModel;
import fr.inria.jtanre.rt.core.ResultMap;
import spoon.reflect.code.CtStatement;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtExecutable;

/**
 * 
 * @author Matias Martinez
 *
 */
public interface TestAnalyzer<T, C, MC> extends AstorExtensionPoint {

	public List<T> findElements(Map<String, List<?>> previousPartialResults, List<CtStatement> stmts,
			CtExecutable testMethodModel, List<CtClass> allClasses);

	public Classification<C> classifyElements(ResultMap<Classification<?>> previousDynamic,
			/* CtClass */MC testClassdModel, CtExecutable testMethodModel,
			Map<String, SuspiciousCode> mapCacheSuspicious, CtStatement stmts, List<T> retrievedElementsFromStatic);

	public void labelTest(GenericTestAnalysisResults analysisResult, List<T> staticAnalysis, Classification<C> dynamic,
			ResultMap<List<?>> statics, ResultMap<Classification<?>> dynamics);

	/**
	 * 
	 * IIf the analyzer does not refactor, simply returns null or an empty list.
	 * 
	 * @return
	 */
	public List<ProgramVariant> refactor(ProgramModel model, CtClass aTestModelCtClass,
			GenericTestAnalysisResults analysisResult, List<T> staticAnalysis, Classification<C> dynamic,
			ResultMap<List<?>> statics, ResultMap<Classification<?>> dynamics);
}
