package fr.inria.jtanre.rt.elements;

import java.util.ArrayList;
import java.util.List;

import spoon.reflect.code.CtReturn;
import spoon.reflect.declaration.CtElement;

/**
 * 
 * @author Matias Martinez
 *
 */
public class Skip extends TestElement {

	protected CtReturn executedReturn;
	protected List<TestElement> notExecutedTestElements = new ArrayList<>();

	public Skip(CtReturn executedReturn) {
		super("Skip");
		this.executedReturn = executedReturn;
	}

	public CtReturn getExecutedReturn() {
		return executedReturn;
	}

	public void setExecutedReturn(CtReturn executedReturn) {
		this.executedReturn = executedReturn;
	}

	public List<TestElement> getNotExecutedTestElements() {
		return notExecutedTestElements;
	}

	public void setNotExecutedTestElements(List<TestElement> notExecutedTestElements) {
		this.notExecutedTestElements = notExecutedTestElements;
	}

	@Override
	public CtElement getElement() {
		return executedReturn;
	}

}