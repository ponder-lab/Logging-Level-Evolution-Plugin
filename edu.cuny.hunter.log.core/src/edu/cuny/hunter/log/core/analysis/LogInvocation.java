package edu.cuny.hunter.log.core.analysis;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.internal.corext.dom.ASTNodes;
import org.eclipse.mylyn.internal.context.core.DegreeOfInterest;

import edu.cuny.hunter.log.core.utils.LoggerNames;

@SuppressWarnings("restriction")
public class LogInvocation {

	private final MethodInvocation expression;
	private final Level logLevel;
	
	private static DegreeOfInterest degreeOfInterest;

	private static final Logger LOGGER = Logger.getLogger(LoggerNames.LOGGER_NAME);

	public LogInvocation(MethodInvocation logExpression, Level loggingLevel) {
		this.expression = logExpression;
		this.logLevel = loggingLevel;
	}

	public MethodInvocation getExpression() {
		return this.expression;
	}

	public IJavaProject getExpressionJavaProject() {
		return this.getEnclosingEclipseMethod().getJavaProject();
	}

	public MethodDeclaration getEnclosingMethodDeclaration() {
		return (MethodDeclaration) ASTNodes.getParent(this.getExpression(), ASTNode.METHOD_DECLARATION);
	}
	
	/**
	 * Through the enclosing type, I can type FQN
	 */
	public IType getEnclosingType() {
		MethodDeclaration enclosingMethodDeclaration = getEnclosingMethodDeclaration();

		if (enclosingMethodDeclaration == null)
			return null;

		IMethodBinding binding = enclosingMethodDeclaration.resolveBinding();
		return (IType) binding.getDeclaringClass().getJavaElement();
	}

	public IMethod getEnclosingEclipseMethod() {
		MethodDeclaration enclosingMethodDeclaration = this.getEnclosingMethodDeclaration();

		if (enclosingMethodDeclaration == null)
			return null;

		IMethodBinding binding = enclosingMethodDeclaration.resolveBinding();
		return (IMethod) binding.getJavaElement();
	}

	public int getStartPosition() {
		return this.expression.getStartPosition();
	}
	
	public Level getLogLevel() {
		return this.logLevel;
	}

	public void logInfo() {
		LOGGER.info("Find a log expression." + this.getExpression().toString() + " The logging level: " + getLogLevel()
				+ ". ");
	}

}
