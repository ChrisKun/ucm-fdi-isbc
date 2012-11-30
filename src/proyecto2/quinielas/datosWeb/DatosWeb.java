package proyecto2.quinielas.datosWeb;

import java.io.IOException;

public class DatosWeb {

	private static ParserWeb parserWeb;
	
	public static void main(String[] args) {
		parserWeb = new ParserWeb();
		parserWeb.ejecutarParser();
	}
}
