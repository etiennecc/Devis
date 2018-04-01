import java.util.*;
import org.w3c.dom.Node;

//public class CMapComposant extends HashMap<String,CComposant>{
public class CMapComposant extends LinkedHashMap<String,CComposant>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4482783128616635116L;
	CDebug m_Debug;
	// Constructor
	CMapComposant(CDebug Debug){
		m_Debug = Debug;
	}
	
	boolean AddComposant(Node noeud){
		CComposant Composant;
		Composant = new CComposant(m_Debug);
		Composant.ReadNode(noeud);
		
		put(Composant.m_strReference,Composant);
		
		return true;
	}
	
	boolean AddComposant(CComposant Composant){
		if (Composant == null){
			m_Debug.PrintWarning("CMapComposant :: AddComposant Composant == null");
			return false;
		}
		
		put(Composant.m_strReference,Composant);
		
		return true;
	}
	
	boolean Print(){

		CComposant Composant;
		Composant = new CComposant(m_Debug);
		String key;
		
		if (isEmpty() == true){
			m_Debug.PrintWarning("WARNING : Map est vide !!!!!!");
			return false;
		}
		
		Iterator<String> iter = keySet().iterator();
		
		m_Debug.PrintInfo(">>>>>>>>>>>>>>>>>>>> CMAP COMPOSANT <<<<<<<<<<<<<<<<<<<<<");
		
		while (iter.hasNext()){
			key = iter.next();
			
			Composant = get(key);
			
			if ( Composant == null){
				m_Debug.PrintWarning("CMapComposant::Print Pointeur null sur key =" + key);
				return false;
			}
			
			Composant.Print();
		}
		
		return true;
	}

}
