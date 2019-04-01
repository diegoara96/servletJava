package p2;
import java.util.ArrayList;
import java.util.Comparator;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Cancion implements Comparable<Cancion> {

	private String idc;
	private String titulo;
	private int duracion;
	private String genero;
	private String comentario;
	private Version version;
	private ArrayList<String> premios;
	 
	public Cancion() {
		this.idc="idc idc";
		this.titulo="titulo cancion";
		this.duracion=55;
		this.genero="pop";
		
	}
	
	public Cancion (Node node) {
		
		this.idc = ((Element)node).getAttribute("idc");
		this.titulo = ((Element)node).getElementsByTagName("Titulo").item(0).getTextContent();
		this.duracion = Integer.parseInt(((Element)node).getElementsByTagName("Duracion").item(0).getTextContent());
		this.genero = ((Element)node).getElementsByTagName("Genero").item(0).getTextContent();
		
		
		if(((Element)node).getElementsByTagName("Version").getLength()>0) {
			this.version = new Version(((Element)node).getElementsByTagName("Version").item(0));
		}
		
		this.premios=new ArrayList<>();
		NodeList ispremio = ((Element) node.getParentNode()).getElementsByTagName("Premios");
		
		if (ispremio.getLength() != 0) {

			NodeList premio = ((Element) ispremio.item(0)).getElementsByTagName("Premio");
			for (int temp = 0; temp < premio.getLength(); temp++) {
				Node nNode = premio.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					premios.add(((Element) nNode).getTextContent());

				}
			}

		}
		
		
		NodeList desc =((Element)node).getChildNodes();
		String coment="";
		this.comentario="";
		for (int temp = 0; temp < desc.getLength(); temp++) {
			Node nNode = desc.item(temp);
			
			if (nNode.getNodeType() == Node.TEXT_NODE) {
				coment=coment+nNode.getNodeValue().trim();
				
			}
		}
		this.comentario=coment;
		
		
		
		
		
		
		
		
		
	}

	public String getPremios() {
		String premio="";
		if(premios.size()>0) {
		for(int i=0;i<premios.size();i++) {
			premio=premio.concat(" "+premios.get(i));
		}	
		}
		
		
		return premio;
	}

	public String getIdc() {
		return idc;
	}

	public String getTitulo() {
		return titulo;
	}

	public int getDuracion() {
		return duracion;
	}

	public String getGenero() {
		return genero;
	}

	public String getComentario() {
		return comentario;
	}

	public Version getVersion() {
		return version;
	}

	@Override
	public int compareTo(Cancion o) {
		  
               
          return  getTitulo().compareTo(o.getTitulo())*(-1);
           
	}

	

	


	
	
	
}

class Version {
	private String titulo;
	private String idc;
	private String iml;
	
	
	
	public Version(Node nodo) {
		
		
		if(((Element)nodo).getElementsByTagName("Idc").getLength()>0) {
			this.idc=((Element)nodo).getElementsByTagName("Idc").item(0).getTextContent();
		}
		else {
			this.titulo=((Element)nodo).getElementsByTagName("Titulo").item(0).getTextContent();
		}
		
		this.iml=((Element)nodo).getElementsByTagName("IML").item(0).getTextContent();
	}



	public String getTitulo() {
		return titulo;
	}



	public String getIdc() {
		return idc;
	}



	public String getIml() {
		return iml;
	}
		
}
class ComparatorDuracion implements Comparator<Cancion>{

	@Override
	public int compare(Cancion o1, Cancion o2) {
		if(o1.getDuracion()>o2.getDuracion()) return 1;
	
	
	else if(o1.getDuracion()<o2.getDuracion()) return -1;
	else return o1.getIdc().compareTo(o2.getIdc());
		}
	
	
}
