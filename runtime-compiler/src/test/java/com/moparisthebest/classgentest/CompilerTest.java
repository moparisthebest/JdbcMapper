package com.moparisthebest.classgentest;

import com.moparisthebest.classgen.Compiler;
import com.moparisthebest.classgen.MultiCompiler;
import com.moparisthebest.classgen.StringJavaFileObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This is a sample program that shows how to generate java source code, compile, and run it, all at runtime and
 * without ever touching the filesystem.  It can be used for much more complicated (and usefull) things, perhaps
 * like creating Pojo instances from ResultSets. :)
 * <p>
 * You need tools.jar on your classpath, and this class in the same package as well:
 * public interface Calculator {
 * public double calc(double a, double b);
 * public java.util.Date whenGenerated();
 * }
 * <p>
 * The original idea was taken from:
 * http://mindprod.com/jgloss/javacompiler.html#SAMPLECODE
 *
 * @author moparisthebest
 */
public class CompilerTest {

	@Test
	public void testSingle() throws InterruptedException {
		final Compiler compiler = new Compiler();
		final Calculator mult1 = genCalc(compiler, "Multiply", "a * b");
		final Calculator hyp = genCalc(compiler, "Hypotenuse", "Math.sqrt( a*a + b*b )");
		final Calculator mult2 = genCalc(compiler, "Multiply", "a * b * 2");
		final Calculator mult3 = genCalc(compiler, "Multiply", "a * b * 3");
		final Calculator mult4 = genCalc(compiler, "Multiply", "a * b * 4");
		assertEquals(12.0, mult1.calc(3.0, 4.0), 0.01);
		assertEquals(5.0, hyp.calc(3.0, 4.0), 0.01);
		assertEquals(24.0, mult2.calc(3.0, 4.0), 0.01);
		assertEquals(48.0, mult3.calc(4.0, 4.0), 0.01);
		assertEquals(48.0, mult4.calc(3.0, 4.0), 0.01);
	}

	@Test
	public void testMulti() throws InterruptedException {
		final MultiCompiler compiler = new MultiCompiler();
		final Calculator mult1 = genCalc(compiler, "Multiply", "a * Var.var", "Var", "4.0");
		final Calculator hyp = genCalc(compiler, "Hypotenuse", "Math.sqrt( a*a + Var.var*Var.var )", "Var", "4.0");
		final Calculator mult2 = genCalc(compiler, "Multiply2", "a * Var.var * 2", "Var", "4.0");
		final Calculator mult3 = genCalc(compiler, "Multiply3", "a * Var.var * 3", "Var", "4.0");
		final Calculator mult4 = genCalc(compiler, "Multiply", "a * Var.var * 4", "Var", "4.0");
		assertEquals(mult1.calc(3.0, 4.0), 12.0, 0.01);
		assertEquals(hyp.calc(3.0, 4.0), 5.0, 0.01);
		assertEquals(mult2.calc(3.0, 4.0), 24.0, 0.01);
		assertEquals(mult3.calc(4.0, 4.0), 48.0, 0.01);
		assertEquals(48.0, mult4.calc(3.0, 4.0), 0.01);
	}

	private static String writeCalculator(String className, String expression) {
		String packageName = null;
		final int lastIndex = className.lastIndexOf(".");
		if (lastIndex != -1) {
			packageName = className.substring(0, lastIndex);
			className = className.substring(lastIndex + 1);
		}

		return (packageName == null ? "" : "package " + packageName + ";\n") +
				"import java.util.Date;\n" +
				"public final class " + className + " implements " + Calculator.class.getName() + " {\n" +
				"  public double calc(double a, double b) {\n" +
				"    return " + expression + ";\n" +
				"  }\n" +
				"  public Date whenGenerated() {\n" +
				"    return new Date(" + System.currentTimeMillis() + "L);\n" +
				"  }\n" +
				"}\n";
	}

	private static StringJavaFileObject writeCalculatorOb(String className, String expression) {
		return new StringJavaFileObject(className, writeCalculator(className, expression));
	}

	private static StringJavaFileObject writeVar(String className, String expression) {
		String packageName = null;
		final int lastIndex = className.lastIndexOf(".");
		if (lastIndex != -1) {
			packageName = className.substring(0, lastIndex);
			className = className.substring(lastIndex + 1);
		}

		return new StringJavaFileObject(className, (packageName == null ? "" : "package " + packageName + ";\n") +
				"public final class " + className + " {\n" +
				"  public static final double var = " + expression + ";\n" +
				"}\n");
	}

	public static Calculator genCalc(final Compiler compiler, final String className, final String expression) {
		// compose text of Java program on the fly.
		final String calc = writeCalculator(className, expression);
		/*
		System.out.println("calc:");
		System.out.println(calc);
		*/
		return compiler.compile(className, calc);
	}

	public static Calculator genCalc(final MultiCompiler compiler, final String className, final String expression, final String varClass, final String varExpression) {
		// compose text of Java program on the fly.
		final StringJavaFileObject calc = writeCalculatorOb(className, expression);
		final StringJavaFileObject var = writeVar(varClass, varExpression);
		/*
		System.out.println("calc:");
		System.out.println(calc.getCharContent(true));
		System.out.println("var:");
		System.out.println(var.getCharContent(true));
		*/
		return compiler.compile(className, calc, var);
	}

	public static double runCalc(final String className, final String expression, final double a, final double b) {
		final Calculator calc = genCalc(new Compiler(), className, expression);
		double ret = calc.calc(a, b);
		System.out.printf("%s.calc( %f, %f ) is : %f\n", className, a, b, ret);
		System.out.printf("%s generated on: %s\n", className, calc.whenGenerated());
		return ret;
	}
}
