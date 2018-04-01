import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.util.ArrayList;
//import org.apache.poi.xssf.usermodel.*;
//import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class CItem {
	String m_strNomItem;
	CDebug m_Debug;
	ArrayList<CGroupe> m_ListGroupe;
	CForecast m_Forecast;
	CGlobals m_Globals;
	CGlobals m_Locals;

	//Couts du groupe
	double m_Cout_Achats;
	double m_Cout_Frais;
	double m_Cout_MO;
	double m_Cout_FIM;
	double m_Cout_Marge_Estimation;
	double m_Cout_Total;
	
	double m_MO_h_e_Tot;
	double m_MO_h_p_Tot;
	double m_dQuantite;


	CItem(CDebug Debug){
		m_strNomItem = new String();
		m_Debug = Debug;
		m_ListGroupe = new ArrayList<CGroupe>();
		m_Forecast = new CForecast(m_Debug);
		m_Locals = new CGlobals(m_Debug);
		m_Globals = null;
		m_dQuantite = 1;
		ResetCout();

	}
	
	void ResetCout(){
		m_Cout_Achats = 0; 
		m_Cout_Frais = 0;
		m_Cout_MO = 0;
		m_Cout_FIM = 0;
		m_Cout_Marge_Estimation = 0;
		m_Cout_Total = 0;		
		m_MO_h_e_Tot = 0;
		m_MO_h_p_Tot = 0;
	}


	boolean WriteXlsForecast(CXls Xls) throws IOException{
		if (Xls == null){
			m_Debug.PrintWarning("CItem : WriteXlsForecast setting null pointer");
			return false;
		}
		if (m_Forecast.WriteXls(Xls,this) == false){
			m_Debug.PrintWarning("CItem : WriteXlsForecast Could not write forecast");
			return false;
		}
		return true;
	}
	boolean WriteXls(CXls Xls) throws IOException{
		if (Xls == null){
			m_Debug.PrintWarning("CItem : WriteXls setting null pointer");
			return false;
		}
		
		Xls.SetSheetToOnePage();

		Xls.CreateRow();

        Cell titleCell_NomItem = Xls.m_CurrentRow.createCell(0);
        titleCell_NomItem.setCellValue(">>>> " + m_strNomItem + " <<<<");
        titleCell_NomItem.setCellStyle(Xls.m_styles.get("header_yellow"));

		Xls.CreateRow();

        Cell titleCell = Xls.m_CurrentRow.createCell(0);
        titleCell.setCellValue("Hypotheses :");
        titleCell.setCellStyle(Xls.m_styles.get("header_yellow"));
        Xls.m_CurrentSheet.setColumnWidth(0, 20*256);
        
        
        Xls.CreateRow();
        Xls.WriteCellString(0, "FIM L :");
        Xls.WriteCellPercent(1, m_Globals.m_dFIM_L/100);
        Xls.CreateRow();
        Xls.WriteCellString(0, "FIM N	 :");
        Xls.WriteCellPercent(1, m_Globals.m_dFIM_N/100);
        Xls.CreateRow();
        Xls.WriteCellString(0, "Cout Horaire Etude	 :");
        Xls.WriteCellRate(1, m_Globals.m_dCout_Horaire_Etudes);
        Xls.CreateRow();
        Xls.WriteCellString(0, "Cout Horaire Production	 :");
        Xls.WriteCellRate(1, m_Globals.m_dCout_Horaire_Production);

		for(int i=0;i<m_ListGroupe.size();i++){
			if ( m_ListGroupe.get(i).WriteXls(Xls) == false){
				m_Debug.PrintWarning("CItem :: Failed write XLS Gropue");
				return false;
			}
		}
		
        double dL_MO_h_e_Tot;
        double dL_MO_h_p_Tot;
        double dL_Cout_MO;
        double dL_Cout_Achats;
        double dL_Cout_FIM;
        double dL_Cout_Marge_Estimation;
        double dL_Cout_Frais;
        double dL_Cout_Total;
        double dl_Coeff;

		// Write the total of the item
        int k=CONSTANT.C_COMPOSANT;
        Xls.CreateRow();
        Xls.CreateRow();
                
        Xls.WriteCellHeader(k++,"TOTAL");
        Xls.WriteCellHeaderHeure(k++,m_MO_h_e_Tot);
        dL_MO_h_e_Tot = m_MO_h_e_Tot;
        Xls.WriteCellHeaderHeure(k++,m_MO_h_p_Tot);
        dL_MO_h_p_Tot = m_MO_h_p_Tot;
        Xls.WriteCellHeaderEuro(k++,m_Cout_MO);
        dL_Cout_MO = m_Cout_MO;
        Xls.WriteCellHeaderEuro(k++,m_Cout_Achats);
        dL_Cout_Achats = m_Cout_Achats;
        Xls.WriteCellHeaderEuro(k++,m_Cout_FIM);
        dL_Cout_FIM = m_Cout_FIM;
        Xls.WriteCellHeader(k++,"");
        Xls.WriteCellHeader(k++,"NA");
        Xls.WriteCellHeaderEuro	(k++,m_Cout_Marge_Estimation);
        dL_Cout_Marge_Estimation = m_Cout_Marge_Estimation;
        Xls.WriteCellHeaderEuro(k++,m_Cout_Frais);
        dL_Cout_Frais = m_Cout_Frais;
        Xls.WriteCellHeader(k++,"NA");
        Xls.WriteCellHeader(k++,"");
        Xls.WriteCellHeaderEuro(k++,m_Cout_Total);
        dL_Cout_Total = m_Cout_Total;
        
		// Write the total of the item
        k=CONSTANT.C_COMPOSANT;
        Xls.CreateRow();
        
        dl_Coeff = (1+ (m_Locals.m_dFrais_Client/100));
        Xls.WriteCellHeader(k++,"TOTAL (avec Frais " + m_Locals.m_dFrais_Client + " %)" );
        dL_MO_h_e_Tot *= dl_Coeff;
        Xls.WriteCellHeaderHeure(k++,dL_MO_h_e_Tot);
        
        dL_MO_h_p_Tot *= dl_Coeff;
        Xls.WriteCellHeaderHeure(k++,dL_MO_h_p_Tot);
        
        dL_Cout_MO *= dl_Coeff;
        Xls.WriteCellHeaderEuro(k++,dL_Cout_MO);
        
        dL_Cout_Achats *= dl_Coeff;
        Xls.WriteCellHeaderEuro(k++,dL_Cout_Achats);
        
        dL_Cout_FIM *= dl_Coeff;
        Xls.WriteCellHeaderEuro(k++,dL_Cout_FIM);
        
        Xls.WriteCellHeader(k++,"");
        Xls.WriteCellHeader(k++,"NA");
        
        dL_Cout_Marge_Estimation *= dl_Coeff;
        Xls.WriteCellHeaderEuro	(k++,dL_Cout_Marge_Estimation);
        
        dL_Cout_Frais *= dl_Coeff;
        Xls.WriteCellHeaderEuro(k++,dL_Cout_Frais);
        
        Xls.WriteCellHeader(k++,"NA");
        Xls.WriteCellHeader(k++,"");
        
        dL_Cout_Total *= dl_Coeff;
        Xls.WriteCellHeaderEuro(k++,dL_Cout_Total);
        
		// Write the total of the item
        k=CONSTANT.C_COMPOSANT;
        Xls.CreateRow();
        
        dl_Coeff = m_dQuantite;
        Xls.WriteCellHeader(k++,"TOTAL (Q=" + dl_Coeff + ")" );
        dL_MO_h_e_Tot *= dl_Coeff;
        Xls.WriteCellHeaderHeure(k++,dL_MO_h_e_Tot);
        
        dL_MO_h_p_Tot *= dl_Coeff;
        Xls.WriteCellHeaderHeure(k++,dL_MO_h_p_Tot);
        
        dL_Cout_MO *= dl_Coeff;
        Xls.WriteCellHeaderEuro(k++,dL_Cout_MO);
        
        dL_Cout_Achats *= dl_Coeff;
        Xls.WriteCellHeaderEuro(k++,dL_Cout_Achats);
        
        dL_Cout_FIM *= dl_Coeff;
        Xls.WriteCellHeaderEuro(k++,dL_Cout_FIM);
        
        Xls.WriteCellHeader(k++,"");
        Xls.WriteCellHeader(k++,"NA");
        
        dL_Cout_Marge_Estimation *= dl_Coeff;
        Xls.WriteCellHeaderEuro	(k++,dL_Cout_Marge_Estimation);
        
        dL_Cout_Frais *= dl_Coeff;
        Xls.WriteCellHeaderEuro(k++,dL_Cout_Frais);
        
        Xls.WriteCellHeader(k++,"NA");
        Xls.WriteCellHeader(k++,"");
        
        dL_Cout_Total *= dl_Coeff;
        Xls.WriteCellHeaderEuro(k++,dL_Cout_Total);

        
        /*
        // Prochaine ligne pour indiquer
        int iColumn = k-3;
        Xls.CreateRow();
        Xls.WriteCellHeader(iColumn++,"Frais Client");
        Xls.WriteCellHeader(iColumn++,m_Locals.m_dFrais_Client + " %");
        Xls.WriteCellHeaderEuro(iColumn++,m_Cout_Total * (1+ (m_Locals.m_dFrais_Client/100)));
        
        //
        iColumn = k-3;
        Xls.CreateRow();
        Xls.WriteCellHeader(iColumn++," x Quantite ");
        Xls.WriteCellHeader(iColumn++,m_dQuantite);
        Xls.WriteCellHeaderEuro(iColumn++,m_dQuantite * m_Cout_Total * (1+ (m_Locals.m_dFrais_Client/100)));
		*/
        //Xls.SetSheetToOnePage();
   		return true;
	}
	boolean ComputeCosts(){
		ResetCout();
		for (int i=0 ; i < m_ListGroupe.size(); i++){
			m_ListGroupe.get(i).ComputeCosts();
			
			m_Cout_Achats += m_ListGroupe.get(i).m_Cout_Achats; 
			m_Cout_Frais += m_ListGroupe.get(i).m_Cout_Frais;
			m_Cout_MO += m_ListGroupe.get(i).m_Cout_MO;
			m_Cout_FIM += m_ListGroupe.get(i).m_Cout_FIM;
			m_Cout_Marge_Estimation += m_ListGroupe.get(i).m_Cout_Marge_Estimation;
			m_Cout_Total += m_ListGroupe.get(i).m_Cout_Total;	
			
			m_MO_h_e_Tot += m_ListGroupe.get(i).m_MO_h_e_Tot;
			m_MO_h_p_Tot += m_ListGroupe.get(i).m_MO_h_p_Tot;

		}
		
		
		//m_Debug.PrintInfo("==========> Catalogue count");
		//m_Locals.m_CatalogueCount.Print();
		
		return true;
	}
	
	boolean SetGlobals(CGlobals Globals){
		if (Globals == null){
			m_Debug.PrintWarning("SetGlobals == null");
			return false;
		}
		m_Globals = Globals;
		
		// Initialisation du catalogue qui comptabilisera le nombre d'occurence.
		m_Locals.m_CatalogueCount = new CCatalogue(m_Debug);
		m_Locals.m_CatalogueCount.Copy(m_Globals.m_Catalogue);
		CComposant ComposantAutre = new CComposant(m_Debug);
		ComposantAutre.SetGlobals(m_Locals);
		ComposantAutre.m_strDescriptif = "Hors Catalogue";
		ComposantAutre.m_strReference = CONSTANT.C_STR_REF_HORS_CATALOGUE;
		ComposantAutre.m_strCommentaire = "Couts Hors Catalogue";
		//ComposantAutre.m_dQuantite = 0;
		m_Locals.m_CatalogueCount.m_MapComposant.AddComposant(ComposantAutre);
		m_Locals.m_CatalogueCount.ResetValues();
		
		m_Locals.SmartCopy(m_Globals);
		m_Forecast.SetGlobals(m_Locals);
		for (int i=0 ; i < m_ListGroupe.size(); i++){
			m_ListGroupe.get(i).SetGlobals(m_Locals);
		}
		return true;
	}
	
	boolean UpdateWithLibrary(){
		if (m_Globals.m_Catalogue == null){
			m_Debug.PrintWarning("CItem::UpdateWithLibrary Catalogue is not set !");
			return false;
		}
		
		for(int i=0;i<m_ListGroupe.size();i++){
			if (m_ListGroupe.get(i).UpdateWithLibrary() == false){
				return false;
			}
		}
		
		return true;
	}
	
	boolean ReadNode(Node Noeud){
    	
		NodeList Noeuds = Noeud.getChildNodes();
		int nbNoeuds = Noeuds.getLength();
		for (int j = 0; j<nbNoeuds; j++) {	
		    if(Noeuds.item(j).getNodeType() == Node.ELEMENT_NODE) {
		        Node NoeudElement = Noeuds.item(j);
		        
		        if (NoeudElement.getNodeName() == "Nom_Item")
		        {
		        	m_strNomItem = NoeudElement.getTextContent();
		        }

		        if (NoeudElement.getNodeName() == "Groupe")
		        {
		        	CGroupe Groupe;
		        	Groupe = new CGroupe(m_Debug);
		        	Groupe.ReadNode(NoeudElement);
		        	m_ListGroupe.add(Groupe);
		        }
		        
		        if (NoeudElement.getNodeName() == "Forecast")
		        {
		        	m_Forecast.ReadNode(NoeudElement);
		        }
		        
		        if (NoeudElement.getNodeName() == "FIM_L")
		        {
		        	m_Locals.m_dFIM_L = Double.parseDouble(NoeudElement.getTextContent());
		        }
		        if (NoeudElement.getNodeName() == "FIM_N")
		        {
		        	m_Locals.m_dFIM_N = Double.parseDouble(NoeudElement.getTextContent());
		        }
		        if (NoeudElement.getNodeName() == "Start_Annee")
		        {
		        	m_Locals.m_iStart_Annee = (int) Double.parseDouble(NoeudElement.getTextContent());
		        }
		        if (NoeudElement.getNodeName() == "Start_Mois")
		        {
		        	m_Locals.m_iStart_Mois = (int) Double.parseDouble(NoeudElement.getTextContent());
		        }
		        if (NoeudElement.getNodeName() == "Cout_Horaire_Production")
		        {
		        	m_Locals.m_dCout_Horaire_Production = Double.parseDouble(NoeudElement.getTextContent());
		        }
		        if (NoeudElement.getNodeName() == "Cout_Horaire_Etudes")
		        {
		        	m_Locals.m_dCout_Horaire_Etudes = Double.parseDouble(NoeudElement.getTextContent());
		        }
		        
		        if (NoeudElement.getNodeName() == "Quantite")
		        {
		        	m_dQuantite = Double.parseDouble(NoeudElement.getTextContent());
		        }
		        

		        //Composant.ReadNode(NoeudComposant);
		        //Composant.Print();
		        //**m_MapComposant.AddComposant(NoeudComposant);
		    }
		}
			
		return true;
	}
	
	boolean Print(){
		
		m_Debug.PrintInfo(">>>>>>>>>>>>><<<<<<<<<<<<<<");
		m_Debug.PrintInfo(">>> ------ CItem ------ <<<");
		m_Debug.PrintInfo(">>>>>>>>>>>>><<<<<<<<<<<<<<");
		m_Debug.PrintInfo("NomItem="+m_strNomItem);
		m_Debug.PrintInfo("Quantite = "+m_dQuantite);
		m_Locals.PrintGlobalsOf("CItem");
		int i;
		for(i=0;i<m_ListGroupe.size();i++){
			m_ListGroupe.get(i).Print();
		}
		
		m_Debug.PrintInfo("Comptage items du catalogue pour cet Item :");
		m_Locals.m_CatalogueCount.Print();
		
		m_Forecast.Print();

		return true;
	}

}
