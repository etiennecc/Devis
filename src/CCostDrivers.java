import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.util.ArrayList;

public class CCostDrivers {

	/*
	double m_dCout_Horaire_Etudes;
	double m_dCout_Horaire_Production;
	double m_dFIM_L;
	double m_dFIM_N;
	int m_iStart_Annee;
	int m_iStart_Mois;
	*/
	CDebug m_Debug;
	CGlobals m_Globals;
	
	ArrayList<CItem> m_ListItem;
	String m_strFileNameCostDrivers;
	//CCatalogue	m_Catalogue;
	
	// Output Xls file
	CXls m_Xls;
	
	CCostDrivers(CDebug Debug){
		/*
		m_dCout_Horaire_Etudes 		= -1;
		m_dCout_Horaire_Production 	= -1;
		m_dFIM_L 					= -1;
		m_dFIM_N 					= -1;
		m_iStart_Annee 				= 0;
		m_iStart_Mois 				= 0;
		*/
		m_Debug 					= Debug;
		m_strFileNameCostDrivers	= new String();
		//m_Item = new CItem(m_Debug);
		m_ListItem = new ArrayList<CItem>();
		//m_Catalogue = null;
		m_Globals = new CGlobals(m_Debug);
		
		m_Xls = null;

	}
	
	boolean WriteXlsForecastHeader(CXls Xls){
		if (Xls == null){
			m_Debug.PrintWarning("CForecast : WriteXlsHeader Xls = null");
			return false;
		}
		Xls.CreateRow();
		int i;
		//int j;
		int iMonthCurrent,iYearCurrent;
		String strPrintDate;
		
		//j=0;
		iYearCurrent = m_Globals.m_iStart_Annee;
		iMonthCurrent = m_Globals.m_iStart_Mois;
		/*
		Xls.m_CurrentSheet.setColumnWidth(CONSTANT.C_FORECAST+j, 20*256);
		Xls.WriteCellHeader(CONSTANT.C_FORECAST+j++, "Nom item");
		Xls.WriteCellHeader(CONSTANT.C_FORECAST+j++, " ");
		*/

		Xls.m_CurrentSheet.setColumnWidth(CONSTANT.C_FORECAST_NOM_ITEM, 35*256);
		Xls.WriteCellHeader(CONSTANT.C_FORECAST_NOM_ITEM, "Nom item");
		Xls.WriteCellHeader(CONSTANT.C_FORECAST_CATALOGUE, "Catalogue (-/o)");
		Xls.m_CurrentSheet.setColumnWidth(CONSTANT.C_FORECAST_TYPE, 20*256);
		Xls.WriteCellHeader(CONSTANT.C_FORECAST_TYPE, "Type");
		Xls.m_CurrentSheet.setColumnWidth(CONSTANT.C_FORECAST_UNITE, 15*256);
		Xls.WriteCellHeader(CONSTANT.C_FORECAST_UNITE, "Unité");

		
		for(i=0;i<m_Globals.m_iNb_Mois;i++){
			
			strPrintDate = iYearCurrent+"-"+iMonthCurrent;
			Xls.m_CurrentSheet.setColumnWidth(CONSTANT.C_FORECAST_VALUES+i, 13*256);
			Xls.WriteCellHeader(CONSTANT.C_FORECAST_VALUES+i, strPrintDate);
			
			iMonthCurrent++;
			if (iMonthCurrent > 12){
				iMonthCurrent = 1;
				iYearCurrent++;
			}
		}

/*
		for(i=0;i<m_Globals.m_iNb_Mois;i++){
			
			strPrintDate = iYearCurrent+"-"+iMonthCurrent;
			Xls.m_CurrentSheet.setColumnWidth(CONSTANT.C_FORECAST+j, 10*256);
			Xls.WriteCellHeader(CONSTANT.C_FORECAST+j++, strPrintDate);
			
			iMonthCurrent++;
			if (iMonthCurrent > 12){
				iMonthCurrent = 1;
				iYearCurrent++;
			}
		}
*/
		return true;
	}

	boolean WriteXls(CXls Xls, String strFileName) throws IOException{
		if (Xls == null){
			m_Debug.PrintWarning("CCostDrivers : WriteXls setting null pointer");
			return false;
		}
		m_Xls = Xls;
		
		// Creation des sheets
		for(int i=0;i<m_ListItem.size();i++){
			m_Xls.CreateSheetItem(m_ListItem.get(i).m_strNomItem);
			
			if ( m_ListItem.get(i).WriteXls(m_Xls) == false){
				m_Debug.PrintWarning("CCostDrivers : Failed to write Item");
				return false;
			}
		}
		
		m_Xls.CreateSheetItem("Forecast");
		if ( WriteXlsForecastHeader(Xls) == false){
			m_Debug.PrintWarning("CCostDrivers : Failed to write Item Forecast Header");
			return false;
		}
		
		for(int i=0;i<m_ListItem.size();i++){
			if ( m_ListItem.get(i).WriteXlsForecast(m_Xls) == false){
				m_Debug.PrintWarning("CCostDrivers : Failed to write Item Forecast");
				return false;
			}
		}
		
		// Ecire le fichier de sortie.
		//m_Xls.WriteToFile("Output.xlsx");
		m_Xls.WriteToFile(strFileName);
		
		return true;
	}
	
	boolean SetCatalogue(CCatalogue Catalogue){
		if (Catalogue == null){
			m_Debug.PrintWarning("CCostDriver::SetCatalogue Setting a null catalogue");
			return false;
		}
		//m_Catalogue = Catalogue;
		m_Globals.m_Catalogue = Catalogue;
		
		return true;
	}
	
	boolean ReadFile(String strFileName){
		
		File file;
		file = new File(strFileName);
		if (file.exists() == false){
			m_Debug.PrintWarning("CCostDriver:ReadFile, File "+strFileName+" does not exist");
			return false;
		}

		m_strFileNameCostDrivers = strFileName;
		
		if (m_Globals.m_Catalogue == null){
			m_Debug.PrintWarning("CCostDriver:ReadFile, Catalogue has not been set !");
			return false;
		}
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		try {
		    DocumentBuilder builder = factory.newDocumentBuilder();
		    Document document= builder.parse(new File(strFileName));
		    
		    if (document == null)
		    {
		    	m_Debug.PrintWarning("CCostDrivers:ReadFile() : Could not open file :"+strFileName);
		    	return false;
		    }
			
		    
		    // =================== Lecture du header ================= //			
			
		    m_Debug.PrintInfo("-----------------> ReadFile => Cost driver");
			Element racine = document.getDocumentElement();
			m_Debug.PrintInfo(racine.getNodeName());
			
			NodeList racineNoeuds = racine.getChildNodes();
			int nbRacineNoeuds = racineNoeuds.getLength();
			
			for (int i = 0; i<nbRacineNoeuds; i++) {	
			    if(racineNoeuds.item(i).getNodeType() == Node.ELEMENT_NODE) {
			        Node Noeud = racineNoeuds.item(i);
			        
			        if (Noeud.getNodeName() == "FIM_L")
			        {
			        	m_Globals.m_dFIM_L = Double.parseDouble(Noeud.getTextContent());
			        }
			        if (Noeud.getNodeName() == "FIM_N")
			        {
			        	m_Globals.m_dFIM_N = Double.parseDouble(Noeud.getTextContent());
			        }
			        
			        if (Noeud.getNodeName() == "FRAIS_CLIENT")
			        {
			        	m_Globals.m_dFrais_Client = Double.parseDouble(Noeud.getTextContent());
			        }
			        			        
			        if (Noeud.getNodeName() == "Start_Annee")
			        {
			        	m_Globals.m_iStart_Annee = (int) Double.parseDouble(Noeud.getTextContent());
			        }
			        if (Noeud.getNodeName() == "Start_Mois")
			        {
			        	m_Globals.m_iStart_Mois = (int) Double.parseDouble(Noeud.getTextContent());
			        }
			        if (Noeud.getNodeName() == "Nb_Mois")
			        {
			        	m_Globals.m_iNb_Mois = (int) Double.parseDouble(Noeud.getTextContent());
			        }
			        if (Noeud.getNodeName() == "Cout_Horaire_Production")
			        {
			        	m_Globals.m_dCout_Horaire_Production = Double.parseDouble(Noeud.getTextContent());
			        }
			        if (Noeud.getNodeName() == "Cout_Horaire_Etudes")
			        {
			        	m_Globals.m_dCout_Horaire_Etudes = Double.parseDouble(Noeud.getTextContent());
			        }


			        if (Noeud.getNodeName() == "Item")
			        {
			        	CItem Item;
			        	Item = new CItem(m_Debug);
			        	Item.ReadNode(Noeud);
			        	//Item.SetStartDate(m_Globals.m_iStart_Annee, m_Globals.m_iStart_Mois);
			        	Item.SetGlobals(m_Globals);
			        	Item.UpdateWithLibrary();
			        	Item.ComputeCosts();
			        	m_ListItem.add(Item);
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

		return true;
	}
	
	boolean Print(){
		m_Debug.PrintInfo(">>> ------ CCostDrivers ------ <<<");
		m_Debug.PrintInfo("Cout_Horaire_Etudes = " + m_Globals.m_dCout_Horaire_Etudes);
		m_Debug.PrintInfo("Cout_Horaire_Production = " + m_Globals.m_dCout_Horaire_Production);
		m_Debug.PrintInfo("FIM_L = " + m_Globals.m_dFIM_L);
		m_Debug.PrintInfo("FIM_N = " + m_Globals.m_dFIM_N);
		m_Debug.PrintInfo("Frais Client = " + m_Globals.m_dFrais_Client);
		m_Debug.PrintInfo("Annee = " + m_Globals.m_iStart_Annee);
		m_Debug.PrintInfo("Mois = " + m_Globals.m_iStart_Mois);
		int i;
		for(i=0;i<m_ListItem.size();i++){
			m_ListItem.get(i).Print();
		}

		return true;
	}

}
