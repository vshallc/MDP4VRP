package integration;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;

public class StochasticPolynomialFunction extends PolynomialFunction{
	private static final long serialVersionUID = -4912879085296725656L;
	double sc; // coefficients for the stochastic parameter xi, xi ~ U[-1,1]
	StochasticPolynomialFunction(double[] c, double sc){
		super(c);
		this.sc = sc;
	}
	
	public PolynomialFunction getDeterminedPart() {
		return (PolynomialFunction)this;
	}
	
	public double getXiCoefficient() {
		return sc;
	}
	
	public StochasticPolynomialFunction add(final StochasticPolynomialFunction sp) {
		PolynomialFunction p = this.getDeterminedPart().add(sp.getDeterminedPart());
		return new StochasticPolynomialFunction(p.getCoefficients(), this.sc + sp.getXiCoefficient());
	}
	
}
