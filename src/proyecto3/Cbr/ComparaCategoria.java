package Cbr;

import jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;

/**
 * Clase encargada de comparar las categorias de dos objetos
 * @author Alvaro
 *
 */
public class ComparaCategoria implements LocalSimilarityFunction {

	public double compute(Object o1, Object o2) throws jcolibri.exception.NoApplicableSimilarityFunctionException{
		if ((o1 == null) || (o2 == null))
			return 0;
		if (!(o1 instanceof java.lang.String))
			throw new jcolibri.exception.NoApplicableSimilarityFunctionException(this.getClass(), o1.getClass());
		if (!(o2 instanceof java.lang.String))
			throw new jcolibri.exception.NoApplicableSimilarityFunctionException(this.getClass(), o2.getClass());
		
		String cadena1 = (String)o1;
		String cadena2 = (String)o2;
		
		String[] c1 = cadena1.split(";");
		String[] c2 = cadena2.split(";");
		
		int maxLong = c1.length > c2.length ? c1.length : c2.length;
		double similitud = 0;
		// Por cada coincidencia sumamos 1
		for(String c: c1) {
			for(String cc: c2) {
				if (c.equals(cc))
					similitud++;
			}
		}		
		// Como no hay categorias repetidas en cada producto la division : similitud/maxLong nunca deberia ser mayor de 1
		// Por si acaso hacemos esta comprobacion para no devolver algo mayor que 1 en ningun caso.
		similitud = similitud/maxLong < 1 ? similitud/maxLong : 1;
		
		return similitud;
	}
	
	/** Applicable to String */
	public boolean isApplicable(Object o1, Object o2)
	{
		if((o1==null)&&(o2==null))
			return true;
		else if(o1==null)
			return o2 instanceof String;
		else if(o2==null)
			return o1 instanceof String;
		else
			return (o1 instanceof String)&&(o2 instanceof String);
	}

}
