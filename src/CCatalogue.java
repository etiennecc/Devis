import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import org.xml.sax.SAXException;
import java.io.File;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import java.util.*;

public class CCatalogue {

	// Nombre d'éléments
	//int nbElements;
	
	// Eléments de paramétrage
	CMapComposant m_MapComposant;
	CDebug m_Debug;
	
	/*public CCatalogue(){
		m_Debug = null;
		m_MapComposant = new CMapComposant(null);		
	}
	*/
	// Constructeur de classe
	public CCatalogue(CDebug Debug)
	{
		m_Debug = Debug;
		m_MapComposant = new CMapComposant(Debug);
	}
	
	// Pour affichage
	public void Print()
	{
		m_Debug.PrintInfo(">> CATALOGUE <<");		
		m_MapComposant.Print();
	}
	
	public CComposant FindComposant(String strRef){
		if (strRef == ""){
			m_Debug.PrintWarning("CCatalogue : FindComposant => Empty string");
			return null;
		}
		CComposant Composant;
		
		Composant = m_MapComposant.get(strRef);
		
		if (Composant == null){
			m_Debug.PrintWarning("CCatalogue : FindComposant Ref "+strRef+" could not be found !");
			return null;
		}

		return Composant;
	}
	
	// Lecture du fichier XML contenant les paramètres
	public boolean ReadFile(String strFileName)
	{
		File file;
		file = new File(strFileName);
		if (file.exists() == false){
			m_Debug.PrintWarning("CCatalogue, File "+strFileName+" does not exist");;
			return false;
		}

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		try {
		    DocumentBuilder builder = factory.newDocumentBuilder();
		    Document document= builder.parse(new File(strFileName));
		    
		    if (document == null)
		    {
		    	m_Debug.PrintWarning("CCatalogue:ReadFile() : Could not open file :"+strFileName);
		    	return false;
		    }
			
		    
		    // =================== Lecture du header ================= //			
			
			Element racine = document.getDocumentElement();
			//m_Debug.PrintInfo(racine.getNodeName());
			
			NodeList racineNoeuds = racine.getChildNodes();
			int nbRacineNoeuds = racineNoeuds.getLength();
			
			for (int i = 0; i<nbRacineNoeuds; i++) {	
			    if(racineNoeuds.item(i).getNodeType() == Node.ELEMENT_NODE) {
			        Node Noeud = racineNoeuds.item(i);
			        /*
			        if (Noeud.getNodeName() == "FIM_L")
			        {
			        	m_dFIM_L = Double.parseDouble(Noeud.getTextContent());
			        }
			        if (Noeud.getNodeName() == "FIM_N")
			        {
			        	m_dFIM_N = Double.parseDouble(Noeud.getTextContent());
			        }
			        if (Noeud.getNodeName() == "Cout_Horaire_Production")
			        {
			        	m_dCout_Horaire_Production = Double.parseDouble(Noeud.getTextContent());
			        }
			        if (Noeud.getNodeName() == "Cout_Horaire_Etudes")
			        {
			        	m_dCout_Horaire_Etudes = Double.parseDouble(Noeud.getTextContent());
			        }
			        */
			        if (Noeud.getNodeName() == "LISTE")
			        {
						NodeList NoeudsComposants = Noeud.getChildNodes();
						int nbNoeudsComposants = NoeudsComposants.getLength();
						for (int j = 0; j<nbNoeudsComposants; j++) {	
						    if(NoeudsComposants.item(j).getNodeType() == Node.ELEMENT_NODE) {
						        Node NoeudComposant = NoeudsComposants.item(j);
						        //Composant.ReadNode(NoeudComposant);
						        //Composant.Print();
						        m_MapComposant.AddComposant(NoeudComposant);
						    }
						}
			        }		        
			    }				
			}
		}
		catch (final ParserConfigurationException e) {
		    e.printStackTrace();
		}
		catch (final SAXException e) {
		    e.printStackTrace();
		}
		catch (final IOException e) {
		    e.printStackTrace();
		}
		
		//m_Debug.PrintInfo("Catalogue::ReadFile , Catalogue content is :");
		//Print();

		return true;
	}


public boolean Copy(CCatalogue Catalogue)
{
	CComposant lComposant, lComposantCopy;
	if (Catalogue == null)
	{
		m_Debug.PrintWarning("CCatalogue Copy : Catalogue == null");
		return false;
	}
	
	m_Debug = Catalogue.m_Debug;
	m_MapComposant.m_Debug = m_Debug;
	// Ensure the hashmap is empty
	m_MapComposant.clear();
	
	Iterator<String> keySetIterator = Catalogue.m_MapComposant.keySet().iterator();

	while(keySetIterator.hasNext()){
	  String key = keySetIterator.next();
	  
	  //m_Debug.PrintInfo("ITERATOR !");
	  
	  // Pointeur sur composant à copier
	  lComposant = Catalogue.m_MapComposant.get(key);
	  
	  // Verifier que le composant a été trouvé
	  if (lComposant == null){
		  m_Debug.PrintWarning("CCatalogue Copy : Could not iterate, find composant");
		  return false;
	  }
	  
	  // Instancier une copie
	  lComposantCopy = new CComposant(m_Debug);
	  
	  // Effectuer la copie
	  lComposantCopy.Copy(lComposant);
	  
	  //m_Debug.PrintInfo("COmposant copie :");
	  //lComposantCopy.Print();
	  
	  // Ajouter le copie dans la map
	  if ( m_MapComposant.AddComposant(lComposantCopy) == false ){
		  m_Debug.PrintWarning("CCatalogue Copy : AddComposant failed");
		  return false;
	  }
	  
	  //m_Debug.PrintInfo("Taille map :" + m_MapComposant.size());
	  
	  //System.out.println("key: " + key + " value: " + map.get(key));
	}

	//m_Debug.PrintInfo("Catalogue::Copy function :");
	//Print();

	return true;
}

public boolean ResetValues()
{
	CComposant lComposant;
	
	Iterator<String> keySetIterator = m_MapComposant.keySet().iterator();

	while(keySetIterator.hasNext()){
	  String key = keySetIterator.next();
	  
	  // Pointeur sur composant à copier
	  lComposant = m_MapComposant.get(key);
	  
	  // Verifier que le composant a été trouvé
	  if (lComposant == null){
		  m_Debug.PrintWarning("CCatalogue ResetQuantity : Could not iterate, find composant");
		  return false;
	  }
	  
	  //lComposant.m_dQuantite = 0;
	  lComposant.SetToZero();
	  

	}

	return true;
}

}