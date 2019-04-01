package p2;

import java.io.File;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Diego Araujo Novoa
 *
 */
public class Sint143P2 extends HttpServlet {

	public static ArrayList<SAXParseException> listaWarning = new ArrayList<>();
	static String clave = "12345asdfg";
	static String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
	static String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
	static String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
	static ArrayList<SAXParseException> listaErrores = new ArrayList<>();
	static ArrayList<SAXParseException> listaFatalErrores = new ArrayList<>();
	static TreeMap<Integer, Document> documentos = new TreeMap<>();
	static ArrayList<String> listadocumentos = new ArrayList<>();
	static ArrayList<String> listaXML = new ArrayList<>();
	static Document documentoex;
	static String comentarioex;
	static String langex;
	static String generoex;
	static String uri_inicial = "http://gssi.det.uvigo.es/users/agil/public_html/SINT/18-19/";

	// MÉTODO DE INICIALIZACIÓN

	public void init(ServletConfig config) throws ServletException {

		String XML = "http://gssi.det.uvigo.es/users/agil/public_html/SINT/18-19/iml2001.xml";
		listaXML.add(XML);

		for (int i = 0; i < listaXML.size(); i++) {

			leerXML(listaXML.get(i), config);
		}
		String ex1="http://gssi.det.uvigo.es/users/agil/public_html/ex2.xml";
		leerex(ex1,config);
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		if (req.getParameter("auto") != null && req.getParameter("auto").equals("si")) {
			res.setContentType("text/xml");

			res.setCharacterEncoding("UTF-8");
			PrintWriter out = res.getWriter();

			out.println("<?xml version='1.0' encoding='UTF-8' ?>");
			String contrasena = req.getParameter("p");

			String pfase = req.getParameter("pfase");
			if (contrasena == null || contrasena.equals("null")) {
				out.println("<wrongRequest>no passwd</wrongRequest>");

			} else if (!contrasena.equals(clave)) {
				out.println("<wrongRequest>bad passwd</wrongRequest>");

			}

			else {
				if (pfase == null) {
					pfase = "01";
				}
				switch (pfase) {
				case "01":
					fase1ConsultaXML(req, res);
					break;
				case "02":
					erroresXML(req, res);
					break;
				case "11":
					fase2anioXML(req, res);
					break;
				case "12":
					if (req.getParameter("panio") == null || req.getParameter("panio").equals("null")) {
						out.println("<wrongRequest>no param:panio</wrongRequest>");

					} else {
						fase3discoXML(req, res);
					}
					break;
				case "13":
					if (req.getParameter("panio") == null || req.getParameter("panio").equals("null")) {
						out.println("<wrongRequest>no param:panio</wrongRequest>");

					} else if (req.getParameter("pidd") == null || req.getParameter("pidd").equals("null")) {
						out.println("<wrongRequest>no param:pidd</wrongRequest>");

					} else {
						fase4cancionXML(req, res);
					}

					break;
				case "14":
					if (req.getParameter("panio") == null || req.getParameter("panio").equals("null")) {
						out.println("<wrongRequest>no param:panio</wrongRequest>");

					} else if (req.getParameter("pidd") == null || req.getParameter("pidd").equals("null")) {
						out.println("<wrongRequest>no param:pidd</wrongRequest>");

					} else if (req.getParameter("pidc") == null || req.getParameter("pidc").equals("null")) {
						out.println("<wrongRequest>no param:pidc</wrongRequest>");

					} else {
						fase5respuestaXML(req, res);
					}

					break;
				default:
					fase1ConsultaXML(req, res);
					break;
				}
			}

		} else {

			res.setContentType("text/html");
			PrintWriter out = res.getWriter();
			out.println("<html>");
			out.println("<head>");
			out.println("<link href=\"p2/iml.css\" rel=\"stylesheet\" >");
			out.println("<title>SERVICIO DE CONSULTA</title>");
			out.println("</head>");
			out.println("<body>");
			out.println("<div id=\"centro\">");
			String contrasena = req.getParameter("p");

			String pfase = req.getParameter("pfase");
			if (contrasena == null || contrasena.equals("null")) {
				out.println("<h3>Falta contraseña</h3>");

			} else if (!contrasena.equals(clave)) {
				out.println("<h3>Contraseña erronea</h3>");

			}

			else {
				if (pfase == null) {
					pfase = "01";
				}

				switch (pfase) {

				case "01":
					fase1Consulta(req, res);
					break;
				case "02":
					errores(req, res);
					break;
				case "11":
					fase2anio(req, res);
					break;
				case "12":
					if (req.getParameter("panio") == null || req.getParameter("panio").equals("null")) {
						out.println("<h3>Falta ANIO</h3>");
						fase2anio(req, res);
					} else {
						fase3disco(req, res);
					}
					break;
				case "13":
					if (req.getParameter("panio") == null || req.getParameter("panio").equals("null")) {
						out.println("<h3>Falta ANIO</h3>");
						fase2anio(req, res);
					} else if (req.getParameter("pidd") == null || req.getParameter("pidd").equals("null")) {
						out.println("<h3>Falta IDD</h3>");
						fase3disco(req, res);
					} else {
						fase4cancion(req, res);
					}

					break;
				case "14":
					if (req.getParameter("panio") == null || req.getParameter("panio").equals("null")) {
						out.println("<h3>Falta ANIO</h3>");
						fase2anio(req, res);
					} else if (req.getParameter("pidd") == null || req.getParameter("pidd").equals("null")) {
						out.println("<h3>Falta IDD</h3>");
						fase3disco(req, res);
					} else if (req.getParameter("pidc") == null || req.getParameter("pidc").equals("null")) {
						out.println("<h3>Falta IDC</h3>");
						fase4cancion(req, res);
					} else {
						fase5respuesta(req, res);
					}

					break;
				default:
					fase1Consulta(req, res);
					break;
				}
			}
		}
	}

	/**
	 * Funcion encargada de la primera consulta pfase 01 o sin pfase HTML
	 *
	 * @param req
	 *            HttpServletRequest
	 * @param res
	 *            HttpServletResponse
	 * @throws IOException
	 */
	public static void fase1Consulta(HttpServletRequest req, HttpServletResponse res) throws IOException {
		PrintWriter out = res.getWriter();
		String contrasena = req.getParameter("p");
		out.println("<div id=\"centrada\">");
		out.println("<form method='GET' action='?pfase=11'>");

		out.println("<h1>SERVICIO DE CONSULTA DE INFORMACIÓN MUSICAL</h1>");
		out.println("<a href=\"?pfase=02&p=" + contrasena + "\">Consultar errores</a> <br>");
		out.println(
				"<input type='radio' name='consulta' value='1' checked> Consulta 1: Canciones de un interprete que duran menos que una dada<br>");

		out.println("<input type='hidden' name='pfase' value='11'>");
		out.println("<input type='hidden' name='p' value=" + contrasena + ">");
		out.println("<input type='submit'  value='Enviar'>");
		out.println("</form>");
		out.println("</div>");

out.println("<h2>"+langex+"</h2>");

out.println("<h2>"+generoex+"</h2>");

out.println("<h2>"+comentarioex+"</h2>");

		out.println("<h2>DIEGO ARAUJO NOVOA</h2>");
		out.println("</div>");
		out.println("</body>");
		out.println("</html>");

	}

	/**
	 * Funcion encargada de la primera consulta pfase
	 *
	 * @param req
	 *            HttpServletRequest
	 * @param res
	 *            HttpServletResponse
	 * @throws IOException
	 */
	public static void fase1ConsultaXML(HttpServletRequest req, HttpServletResponse res) throws IOException {
		PrintWriter out = res.getWriter();
		out.println("<service>");
		out.println("<status>OK</status>");
		out.println("</service>");

	}

	/**
	 * Funcion encargada de la segunda consulta, sacar años pfase 11
	 *
	 * @param req
	 *            HttpServletRequest
	 * @param res
	 *            HttpServletResponse
	 * @throws IOException
	 */
	public static void fase2anio(HttpServletRequest req, HttpServletResponse res) throws IOException {
		PrintWriter out = res.getWriter();
		String contrasena = req.getParameter("p");
		out.println("<div id=\"centrada\">");
		out.println("<form method='GET' action='?pfase=12'>");

		out.println("<h1>Consulta 1</h1>");
		out.println("<h2>Selecciona un año:</h2>");

		out.println("<input type='hidden' name='pfase' value='12'>");
		out.println("<input type='hidden' name='p' value=" + contrasena + ">");
		ArrayList<String> anios = getC1Anios();
		for (int i = 0; i < anios.size(); i++) {
			if (i == 0) {
				out.println("<input type='radio' name='panio' value='" + anios.get(i) + "' checked>" + (i + 1) + ".-"
						+ anios.get(i) + "<br>");
			} else {
				out.println("<input type='radio' name='panio' value='" + anios.get(i) + "'>" + (i + 1) + ".-"
						+ anios.get(i) + "<br>");

			}
		}

		out.println("<input type='submit' name='submit' value='Enviar'>");
		out.println("<input type='submit' value='Atrás' onClick='form.pfase.value=01'>");
		out.println("<input type='submit' value='Inicio' onClick='form.pfase.value=01'>");
		out.println("</form>");
		out.println("</div>");
		out.println("<h2>DIEGO ARAUJO NOVOA</h2>");
		out.println("</div>");
		out.println("</body>");
		out.println("</html>");

	}

	/**
	 * Funcion encargada de la segunda consulta, sacar años XML pfase 11
	 *
	 * @param req
	 *            HttpServletRequest
	 * @param res
	 *            HttpServletResponse
	 * @throws IOException
	 */
	public static void fase2anioXML(HttpServletRequest req, HttpServletResponse res) throws IOException {

		PrintWriter out = res.getWriter();
		out.println("<anios>");

		ArrayList<String> anios = getC1Anios();
		for (int i = 0; i < anios.size(); i++) {

			out.println("    <anio>" + anios.get(i) + "</anio> ");

		}

		out.println("</anios>");

	}

	/**
	 * Funcion encargada de la tercera consulta, sacar discos de un año dado pfase
	 * 12
	 *
	 * @param req
	 *            HttpServletRequest
	 * @param res
	 *            HttpServletResponse
	 * @throws IOException
	 */
	public static void fase3disco(HttpServletRequest req, HttpServletResponse res) throws IOException {
		PrintWriter out = res.getWriter();
		String contrasena = req.getParameter("p");
		String anio = req.getParameter("panio");
		out.println("<form method='GET' action='?pfase=13'>");
		out.println("<input type='hidden' name='pfase' value='13'>");

		anio = req.getParameter("panio");
		out.println("<div id=\"izquierda\" text-align: \"left\">");
		out.println("<h1>Consulta 1:Año=" + anio + "</h1>");// consulta 1: Año=2004
		out.println("<h2>Selecciona un Disco:</h2>");

		ArrayList<Disco> discos = getC1Discos(anio);
		Collections.sort(discos);
		for (int i = 0; i < discos.size(); i++) {
			out.println("<input type='radio' name='pidd' value='" + discos.get(i).getIdd() + "' checked>" + (i + 1)
					+ ".-<b>Título</b>= '" + discos.get(i).getTitulo() + "' --<b>IDD</b>='" + discos.get(i).getIdd()
					+ "'---<b>Interprete</b>= '" + discos.get(i).getInterprete() + "' --<b>Idiomas</b>='"
					+ discos.get(i).getLangs() + "'<br>");
		}

		out.println("<input type='hidden' name='p' value=" + contrasena + ">");
		out.println("<input type='hidden' name='panio' value=" + anio + ">");
		out.println("<input type='submit' name='submit' value='Enviar'>");
		out.println("<input type='submit' value='Atrás' onClick='form.pfase.value=11'>");
		out.println("<input type='submit' value='Inicio' onClick='form.pfase.value=01'>");
		out.println("</form>");
		out.println("</div>");
		out.println("<h2>DIEGO ARAUJO NOVOA</h2>");
		out.println("</div>");
		out.println("</body>");
		out.println("</html>");
	}

	/**
	 * Funcion encargada de la tercera consulta, sacar discos de un año dado XML
	 * pfase 12
	 *
	 * @param req
	 *            HttpServletRequest
	 * @param res
	 *            HttpServletResponse
	 * @throws IOException
	 */
	public static void fase3discoXML(HttpServletRequest req, HttpServletResponse res) throws IOException {
		PrintWriter out = res.getWriter();
		String anio = req.getParameter("panio");
		out.println("<discos>");

		ArrayList<Disco> discos = getC1Discos(anio);
		Collections.sort(discos);
		for (int i = 0; i < discos.size(); i++) {
			out.println("<disco idd='" + discos.get(i).getIdd() + "' interprete='" + discos.get(i).getInterprete()
					+ "' langs='" + discos.get(i).getLangs() + "'>" + discos.get(i).getTitulo() + "</disco>");
		}

		out.println("</discos>");

	}

	/**
	 * Funcion encargada de la cuarta consulta, sacar canciones usando un
	 * disco(pidd) y un año(panio) dado pfase 13
	 *
	 * @param req
	 *            HttpServletRequest
	 * @param res
	 *            HttpServletResponse
	 * @throws IOException
	 */
	public static void fase4cancion(HttpServletRequest req, HttpServletResponse res) throws IOException {
		PrintWriter out = res.getWriter();
		String contrasena = req.getParameter("p");
		String anio = req.getParameter("panio");
		out.println("<form method='GET' action='?pfase=14'>");
		String idd = req.getParameter("pidd");
		out.println("<h1>Consulta1: Año= " + anio + ", Disco=" + idd + "</h1>"); // meterle año y idd del disco
		out.println("<h2>Selecciona una Canción:</h2>");
		out.println("<input type='hidden' name='panio' value=" + anio + ">");
		out.println("<input type='hidden' name='pidd' value=" + idd + ">");
		out.println("<input type='hidden' name='pfase' value='14'>");
		out.println("<input type='hidden' name='p' value=" + contrasena + ">");
		ArrayList<Cancion> cancion = getC1Canciones(anio, idd);
		cancion.sort(new ComparatorDuracion());
		for (int i = 0; i < cancion.size(); i++) {
			out.println("<input type='radio' name='pidc' value='" + cancion.get(i).getIdc() + "' checked>" + (i + 1)
					+ ".-<b>Título</b>= '" + cancion.get(i).getTitulo() + "' ---<b>IDC</b>='" + cancion.get(i).getIdc()
					+ "'---<b>Genero</b>='" + cancion.get(i).getGenero() + "'---<b>Duracion</b>='"
					+ cancion.get(i).getDuracion() + "' seg<br>");
		}

		out.println("<input type='submit' name='submit' value='Enviar'>");
		out.println("<input type='submit' value='Atrás' onClick='form.pfase.value=12'>");
		out.println("<input type='submit' value='Inicio' onClick='form.pfase.value=01'>");
		out.println("</form>");
		out.println("<h2>DIEGO ARAUJO NOVOA</h2>");
		out.println("</div>");
		out.println("</body>");
		out.println("</html>");

	}

	/**
	 * Funcion encargada de la cuarta consulta, sacar canciones usando un
	 * disco(pidd) y un año(panio) dado pfase 13
	 *
	 * @param req
	 *            HttpServletRequest
	 * @param res
	 *            HttpServletResponse
	 * @throws IOException
	 */
	public static void fase4cancionXML(HttpServletRequest req, HttpServletResponse res) throws IOException {

		PrintWriter out = res.getWriter();
		out.println("<canciones>");
		String anio = req.getParameter("panio");
		String idd = req.getParameter("pidd");

		ArrayList<Cancion> cancion = getC1Canciones(anio, idd);
		cancion.sort(new ComparatorDuracion());
		for (int i = 0; i < cancion.size(); i++) {
			out.println("<cancion idc='" + cancion.get(i).getIdc() + "' genero='" + cancion.get(i).getGenero()
					+ "' duracion='" + cancion.get(i).getDuracion() + "'>" + cancion.get(i).getTitulo() + "</cancion>");
		}

		out.println("</canciones>");
	}

	/**
	 * Funcion encargada de la respuesta, nos da las canciones de un interprete que
	 * dura menos que una dada usa pidc,pidd,panio
	 *
	 * @param req
	 *            HttpServletRequest
	 * @param res
	 *            HttpServletResponse
	 * @throws IOException
	 */
	public static void fase5respuesta(HttpServletRequest req, HttpServletResponse res) throws IOException {
		PrintWriter out = res.getWriter();
		String contrasena = req.getParameter("p");
		String anio = req.getParameter("panio");
		String idd = req.getParameter("pidd");
		String idc = req.getParameter("pidc");
		out.println("<form method='GET' action='?pfase=14'>");

		out.println("<h1>Respuesta  Año= " + anio + ", IDD=" + idd + ", IDC=" + idc + "</h1>");

		ArrayList<Cancion> cancion = getC1Resultado(anio, idd, idc);
		Collections.sort(cancion);
		for (int i = 0; i < cancion.size(); i++) {
			if (cancion.get(i).getPremios().isEmpty()) {
				out.println((i + 1) + ".-<b>Titulo</b>= " + cancion.get(i).getTitulo() + " ---<b>Descripcion=</b> "
						+ cancion.get(i).getComentario() + "<br>");
			} else {
				out.println((i + 1) + ".-<b>Titulo</b>= " + cancion.get(i).getTitulo() + " ---<b>Descripcion</b>= "
						+ cancion.get(i).getComentario() + " ---<b>Premios</b>= " + cancion.get(i).getPremios()
						+ "<br>");
			}

		}

		out.println("<input type='hidden' name='panio' value=" + anio + ">");
		out.println("<input type='hidden' name='pidd' value=" + idd + ">");

		out.println("<input type='hidden' name='pidc' value=" + idc + ">");
		out.println("<input type='hidden' name='pfase' value='14'>");
		out.println("<input type='hidden' name='p' value=" + contrasena + ">");
		out.println("<input type='submit' value='Atrás' onClick='form.pfase.value=13'>");
		out.println("<input type='submit' value='Inicio' onClick='form.pfase.value=01'>");
		out.println("</form>");
		out.println("<h2>DIEGO ARAUJO NOVOA</h2>");
		out.println("</div>");
		out.println("</body>");
		out.println("</html>");

	}

	/**
	 * Funcion encargada de la respuesta, nos da las canciones de un interprete que
	 * dura menos que una dada usa pidc,pidd,panio
	 *
	 * @param req
	 *            HttpServletRequest
	 * @param res
	 *            HttpServletResponse
	 * @throws IOException
	 */
	public static void fase5respuestaXML(HttpServletRequest req, HttpServletResponse res) throws IOException {
		PrintWriter out = res.getWriter();
		String anio = req.getParameter("panio");
		String idd = req.getParameter("pidd");
		String idc = req.getParameter("pidc");

		ArrayList<Cancion> cancion = getC1Resultado(anio, idd, idc);
		Collections.sort(cancion);

		out.println("<songs>");

		for (int i = 0; i < cancion.size(); i++) {

			out.println("<song descripcion='" + cancion.get(i).getComentario() + "' premios='"
					+ cancion.get(i).getPremios() + "'>" + cancion.get(i).getTitulo() + "</song>");

		}

		out.println("</songs>");

	}

	/**
	 * Funcion encargada de sacar los errores de los documentos leidos 3 tipos de
	 * errores, WARNIGS,ERRORS,FATALERRORS
	 *
	 * @param req
	 *            HttpServletRequest
	 * @param res
	 *            HttpServletResponse
	 * @throws IOException
	 */

	public static void errores(HttpServletRequest req, HttpServletResponse res) throws IOException {
		PrintWriter out = res.getWriter();
		String contrasena = req.getParameter("p");
		out.println("<div id=\"centrada\">");
		out.println("<form method='GET' action='?pfase=01'>");

		out.println("<h2>Consultar errores</h2>");
		ArrayList<String> lwarning = new ArrayList<>();

		for (int r = 0; r < listaWarning.size(); r++) {
			if (!lwarning.contains(listaWarning.get(r).getSystemId()))
				lwarning.add(listaWarning.get(r).getSystemId());
		}
		out.println("<h3>Se han encontrado: " + lwarning.size() + "  ficheros con WARNING</h3>");
		if (!listaWarning.isEmpty()) {
			boolean sysid = false;
			warXML: for (int a = 0; a < listaXML.size(); a++) {
				sysid = false;
				for (int i = 0; i < listaWarning.size(); i++) {

					if (listaWarning.get(i).getSystemId().contains(listaXML.get(a))) {
						if (sysid == false) {
							out.println("<h4><b>" + listaXML.get(a) + "</b></h4><br>");
							sysid = true;
						}
						out.println(listaWarning.get(i) + "<br>");
						if (listaWarning.size() > i + 1) {
							if (listaWarning.get(i).getLineNumber() == listaWarning.get(i + 1).getLineNumber()) {
								out.println(listaWarning.get(i + 1) + "<br>");

							}
						}
						continue warXML;
					}
				}

			}
		}

		ArrayList<String> lfaterror = new ArrayList<>();

		for (int r = 0; r < listaFatalErrores.size(); r++) {
			if (!lfaterror.contains(listaFatalErrores.get(r).getSystemId()))
				lfaterror.add(listaFatalErrores.get(r).getSystemId());
		}
		out.println("<h3>Se han encontrado: " + lfaterror.size() + "  ficheros con FATALERROR</h3>");
		if (!listaFatalErrores.isEmpty()) {
			boolean sysid = false;
			otroXml: for (int a = 0; a < listaXML.size(); a++) {

				sysid = false;
				for (int i = 0; i < listaFatalErrores.size(); i++) {
					String prueba1 = listaFatalErrores.get(i).getSystemId();
					if (prueba1.contains(listaXML.get(a))) {

						if (sysid == false) {
							out.println("<h4><b>" + listaXML.get(a) + "</b></h4><br>");
							sysid = true;
						}

						out.println(listaFatalErrores.get(i) + "<br>");
						if (listaFatalErrores.size() > i + 1) {
							if (listaFatalErrores.get(i).getLineNumber() == listaFatalErrores.get(i + 1)
									.getLineNumber()) {
								out.println(listaFatalErrores.get(i + 1) + "<br>");

							}
						}

						continue otroXml;

					}
				}

			}

		}

		ArrayList<String> lerror = new ArrayList<>();

		for (int r = 0; r < listaErrores.size(); r++) {
			if (!lerror.contains(listaErrores.get(r).getSystemId()))
				lerror.add(listaErrores.get(r).getSystemId());
		}
		out.println("<h3>Se han encontrado: " + lerror.size() + "  ficheros con ERRORES</h3>");

		if (!listaErrores.isEmpty()) {
			boolean sysid = false;
			otroXml: for (int a = 0; a < listaXML.size(); a++) {

				sysid = false;
				for (int i = 0; i < listaErrores.size(); i++) {
					String prueba1 = listaErrores.get(i).getSystemId();
					if (prueba1.contains(listaXML.get(a))) {

						if (sysid == false) {
							out.println("<h4><b>" + listaXML.get(a) + "</b></h4><br>");
							sysid = true;
						}

						out.println(listaErrores.get(i) + "<br>");
						if (listaErrores.size() > i + 1) {
							if (listaErrores.get(i).getLineNumber() == listaErrores.get(i + 1).getLineNumber()) {
								out.println(listaErrores.get(i + 1) + "<br>");

							}
						}

						continue otroXml;

					}
				}

			}

		}

		out.println("<input type='hidden' name='pfase' value='02'>");
		out.println("<input type='hidden' name='p' value=" + contrasena + ">");
		out.println("<input type='submit' value='Atrás' onClick='form.pfase.value=01'>");
		out.println("<input type='submit' value='Inicio' onClick='form.pfase.value=01'>");
		out.println("</form>");
		out.println("</div>");
		out.println("<h2>DIEGO ARAUJO NOVOA</h2>");
		out.println("</div>");
		out.println("</body>");
		out.println("</html>");

	}

	/**
	 * Funcion encargada de sacar los errores de los documentos leidos 3 tipos de
	 * errores, WARNIGS,ERRORS,FATALERRORS
	 *
	 * @param req
	 *            HttpServletRequest
	 * @param res
	 *            HttpServletResponse
	 * @throws IOException
	 */
	public static void erroresXML(HttpServletRequest req, HttpServletResponse res) throws IOException {
		PrintWriter out = res.getWriter();
		out.println("<errores>");
		out.println("<warnings>");

		if (!listaWarning.isEmpty()) {
			boolean sysid = false;
			warXML: for (int a = 0; a < listaXML.size(); a++) {
				sysid = false;
				for (int i = 0; i < listaWarning.size(); i++) {

					if (listaWarning.get(i).getSystemId().contains(listaXML.get(a))) {
						if (sysid == false) {
							out.println("<file>" + listaXML.get(a) + "</file>");
							sysid = true;
						}

						if (listaWarning.size() > i + 1) {
							if (listaWarning.get(i).getLineNumber() == listaWarning.get(i + 1).getLineNumber()) {
								out.println("<cause>" + listaWarning.get(i));

								out.println(listaWarning.get(i + 1) + "</cause>");

							} else {
								out.println("<cause>" + listaWarning.get(i) + "</cause>");
							}
						}
						continue warXML;
					}
				}

			}
		}
		out.println("</warnings>");

		out.println("<errors>");
		if (!listaErrores.isEmpty()) {
			boolean sysid = false;
			otroXml: for (int a = 0; a < listaXML.size(); a++) {

				sysid = false;
				for (int i = 0; i < listaErrores.size(); i++) {
					String prueba1 = listaErrores.get(i).getSystemId();
					if (prueba1.contains(listaXML.get(a))) {

						if (sysid == false) {
							out.println("<error>");
							out.println("<file>" + listaXML.get(a) + "</file>");
							sysid = true;

						}


						if (listaErrores.size() > i + 1) {
							if (listaErrores.get(i).getLineNumber() == listaErrores.get(i + 1).getLineNumber()) {

								out.println("<cause>" + listaErrores.get(i));
								out.println(listaErrores.get(i + 1) + "</cause>");
							} else {
								out.println("<cause>" + listaErrores.get(i) + "</cause>");

							}
						}
						else {
							out.println("<cause>" + listaErrores.get(i) + "</cause>");

						}

						out.println("</error>");
						continue otroXml;

					}
				}

			}

		}

		out.println("</errors>");

		out.println("<fatalerrors>");
		if (!listaFatalErrores.isEmpty()) {
			boolean sysid = false;
			otroXml: for (int a = 0; a < listaXML.size(); a++) {

				sysid = false;
				for (int i = 0; i < listaFatalErrores.size(); i++) {
					String prueba1 = listaFatalErrores.get(i).getSystemId();
					if (prueba1.contains(listaXML.get(a))) {

						if (sysid == false) {
							out.println("<fatalerror>");

							out.println("<file>" + listaXML.get(a) + "</file>");
							sysid = true;
						}

						if (listaFatalErrores.size() > i + 1) {
							if (listaFatalErrores.get(i).getLineNumber() == listaFatalErrores.get(i + 1)
									.getLineNumber()) {
								out.println("<cause>" + listaFatalErrores.get(i));
								out.println(listaFatalErrores.get(i + 1) + "</cause>");

							}
						} else {
							out.println("<cause>" + listaFatalErrores.get(i) + "</cause>");
						}

						out.println("</fatalerror>");
						continue otroXml;

					}
				}

			}

		}

		out.println("</fatalerrors>");

		out.println("</errores>");
	}

	/**
	 * Funcion encargada de leer los xml
	 *
	 * @param XML
	 * @param config
	 */
	public static void leerXML(String XML, ServletConfig config) {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(true);

		dbf.setNamespaceAware(true);
		dbf.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
		String URL_XSD = config.getServletContext().getRealPath("p2/iml.xsd");

		File fileSchema = new File(URL_XSD);

		dbf.setAttribute(JAXP_SCHEMA_SOURCE, fileSchema);

		DocumentBuilder db;

		Document doc = null;
		try {
			db = dbf.newDocumentBuilder();

			IML_ErrorHandler errorHandler = new IML_ErrorHandler();
			db.setErrorHandler(errorHandler);

			if (XML.startsWith("http")) {
				doc = db.parse(new URL(XML).openStream(), XML);
			} else
				doc = db.parse(new URL(uri_inicial + XML).openStream(), XML);
			if (errorHandler.getError()) {
				errorHandler.setError();
				return;

			}

			listadocumentos.add(XML);

			// comprobar que no haya habido errores

			XPathFactory xpathFactory = XPathFactory.newInstance();
			XPath xpath = xpathFactory.newXPath();

			NodeList nlAnios = (NodeList) xpath.evaluate("/Songs/Anio", doc, XPathConstants.NODESET);

			NodeList iml = (NodeList) xpath.evaluate("/Songs/Pais/Disco/Cancion/Version/IML", doc,
					XPathConstants.NODESET);

			documentos.put(Integer.parseInt(nlAnios.item(0).getTextContent()), doc);

			for (int i = 0; i < iml.getLength(); i++) {

				String aux;
				if (!iml.item(i).getTextContent().startsWith("http")) {
					aux = uri_inicial + iml.item(i).getTextContent();
				} else
					aux = iml.item(i).getTextContent();
				if (!listaXML.contains(aux)) {
					listaXML.add(aux);

				}

			}

		} catch (ParserConfigurationException e) {

		} catch (SAXException e) {

		} catch (IOException e) {

		} catch (XPathExpressionException e) {

		}

	}


	public static void leerex(String XML, ServletConfig config) {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(true);

		dbf.setNamespaceAware(true);

		DocumentBuilder db;


		try {
			db = dbf.newDocumentBuilder();




				documentoex = db.parse(new URL(XML).openStream(), XML);




			// comprobar que no haya habido errores

			XPathFactory xpathFactory = XPathFactory.newInstance();
			XPath xpath = xpathFactory.newXPath();
//aqui saco la lang
			NodeList langlist = (NodeList) xpath.evaluate("/examen/genero/@lang", documentoex, XPathConstants.NODESET);
			langex=langlist.item(0).getNodeValue();

//aqui saco el genero
			NodeList generolist = (NodeList) xpath.evaluate("/examen/genero", documentoex,
					XPathConstants.NODESET);
	generoex=((Element)generolist.item(0)).getTextContent();
//aqui guardo el comentario
					NodeList comentario = (NodeList) xpath.evaluate("/examen", documentoex,
							XPathConstants.NODESET);

							NodeList desc =((Element)comentario.item(0)).getChildNodes();
							String coment="";
							comentarioex="";
							for (int temp = 0; temp < desc.getLength(); temp++) {
								Node nNode = desc.item(temp);

								if (nNode.getNodeType() == Node.TEXT_NODE) {
									coment=coment+nNode.getNodeValue().trim();

								}
							}
							comentarioex=coment;


		} catch (ParserConfigurationException e) {

		} catch (SAXException e) {

		} catch (IOException e) {

		} catch (XPathExpressionException e) {

		}

	}


	// METODOS OBLIGATORIOS
	public static ArrayList<String> getC1Anios() {
		ArrayList<String> anios = new ArrayList<>();
		for (Entry<Integer, Document> entry : documentos.entrySet()) {
			anios.add(entry.getKey().toString());

		}
		return anios;
	}

	public static ArrayList<Disco> getC1Discos(String anio) {
		Document doc = documentos.get(Integer.parseInt(anio));
		ArrayList<Disco> lista_discos = new ArrayList<>();
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();

		NodeList discos = null;

		try {

			discos = (NodeList) xpath.evaluate("/Songs/Pais/Disco", doc, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {

			e.printStackTrace();
		}

		for (int i = 0; i < discos.getLength(); i++) {
			Disco disco = new Disco(discos.item(i));
			if(disco.getLangs().contains(langex)){
				lista_discos.add(disco);
			}

		}

		return lista_discos;
	}

	public static ArrayList<Cancion> getC1Canciones(String anio, String idd) {

		Document doc = documentos.get(Integer.parseInt(anio));
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		ArrayList<Cancion> canciones = new ArrayList<>();
		try {
			NodeList listaCancion = (NodeList) xpath.evaluate("/Songs/Pais/Disco[@idd='" + idd + "']/Cancion", doc,
					XPathConstants.NODESET);

			for (int i = 0; i < listaCancion.getLength(); i++) {
				Cancion cancion = new Cancion(listaCancion.item(i));
				if(!cancion.getGenero().contains(generoex)){
						canciones.add(cancion);
				}

			}

		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return canciones;
	}

	public static ArrayList<Cancion> getC1Resultado(String anio, String idd, String idc) {

		Document doc = documentos.get(Integer.parseInt(anio));
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		ArrayList<Cancion> canciones = new ArrayList<>();

		try {
			NodeList duracioNodo = (NodeList) xpath.evaluate("/Songs/Pais/Disco/Cancion[@idc='" + idc + "']/Duracion",
					doc, XPathConstants.NODESET);

			NodeList nodecancion = (NodeList) xpath.evaluate("/Songs/Pais/Disco/Cancion[@idc='" + idc + "']", doc,
					XPathConstants.NODESET);
			Node inter = ((Element) nodecancion.item(0).getParentNode()).getElementsByTagName("Interprete").item(0);

			String interprete = inter.getTextContent();
			int duracion = Integer.parseInt(duracioNodo.item(0).getTextContent());

			ArrayList<String> keys = getC1Anios();

			for (int i = 0; i < keys.size(); i++) {

				doc = documentos.get(Integer.parseInt(keys.get(i)));

				NodeList duracionMenor = (NodeList) xpath.evaluate("/Songs/Pais/Disco[Interprete='" + interprete
						+ "']/Cancion[Duracion[text() < " + duracion + "]]", doc, XPathConstants.NODESET);
				for (int a = 0; a < duracionMenor.getLength(); a++) {

					canciones.add(new Cancion(duracionMenor.item(a)));

				}

			}

		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return canciones;
	}

}

class IML_ErrorHandler extends DefaultHandler {
	boolean error = false;

	public IML_ErrorHandler() {
	}

	public void warning(SAXParseException spe) {
		// spe.printStackTrace();
		error = true;

		Sint143P2.listaWarning.add(new SAXParseException(spe.getMessage(), spe.getPublicId(), spe.getSystemId(),
				spe.getLineNumber(), spe.getColumnNumber(), spe.getException()));
	}

	public void error(SAXParseException spe) {

		// spe.printStackTrace();
		error = true;
		Sint143P2.listaErrores.add(new SAXParseException(spe.getMessage(), spe.getPublicId(), spe.getSystemId(),
				spe.getLineNumber(), spe.getColumnNumber(), spe.getException()));
	}

	public void fatalError(SAXParseException spe) throws SAXParseException {
		// spe.printStackTrace();
		error = true;
		Sint143P2.listaFatalErrores.add(new SAXParseException(spe.getMessage(), spe.getPublicId(), spe.getSystemId(),
				spe.getLineNumber(), spe.getColumnNumber(), spe.getException()));
	}

	public boolean getError() {
		return error;
	}

	public void setError() {
		error = false;
		return;
	}
}
