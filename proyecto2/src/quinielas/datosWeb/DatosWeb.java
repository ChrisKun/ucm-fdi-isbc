package src.quinielas.datosWeb;


public class DatosWeb {

	private static ParserWeb parserWeb;
	
	public static void main(String[] args) {
		parserWeb = new ParserWeb();
		parserWeb.ejecutarParser();
	}
}
