package p2;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Disco implements Comparable<Disco> {

	private String langs;
	public String idd;
	private String titulo;
	private String interprete;
	private ArrayList<String> premios;
	private ArrayList<Cancion> cancion;

	public Disco(Node node) {

		this.idd = ((Element) node).getAttribute("idd");

		if (((Element) node).getAttribute("langs").isEmpty()) {
			this.langs = ((Element) node.getParentNode()).getAttribute("lang");
		} else {
			this.langs = ((Element) node).getAttribute("langs");
		}
		this.titulo = ((Element) node).getElementsByTagName("Titulo").item(0).getTextContent();
		this.interprete = ((Element) node).getElementsByTagName("Interprete").item(0).getTextContent();

		// aqui compruebo los premios y los instancio
		NodeList ispremio = ((Element) node).getElementsByTagName("Premios");
		this.premios = new ArrayList<>();
		if (ispremio.getLength() != 0) {

			NodeList premio = ((Element) ispremio.item(0)).getElementsByTagName("Premio");
			for (int temp = 0; temp < premio.getLength(); temp++) {
				Node nNode = premio.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					premios.add(((Element) nNode).getTextContent());

				}
			}

		}
	}

	public String getLangs() {
		return langs;
	}

	public String getIdd() {
		return idd;
	}

	public String getTitulo() {
		return titulo;
	}

	public String getInterprete() {
		return interprete;
	}

	public ArrayList<String> getPremios() {
		return premios;
	}

	public ArrayList<Cancion> getCancion() {
		return cancion;
	}

	@Override
	public int compareTo(Disco o) {
		if (getInterprete().compareTo(o.getInterprete()) == 0) {
			return getTitulo().compareTo(o.getTitulo());
		}

		else
			return getInterprete().compareTo(o.getInterprete());

	}

}
