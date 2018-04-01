import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.ArrayList;
import java.util.Iterator;

public class CForecast extends ArrayList<Double> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8989987166092283613L;
	
	CDebug m_Debug;
	int m_iStart_Annee;
	int m_iStart_Mois;
	//String m_strItemName;
	CGlobals m_Globals;

	CForecast(CDebug Debug){
		m_iStart_Annee 				= 0;
		m_iStart_Mois 				= 0;
		m_Debug = Debug;
		//m_strItemName = "NO NAME";
		m_Globals = null;
	}
	
	/*
	boolean SetStartDate(int iAnnee,int iMois){
		m_iStart_Annee 				= iAnnee;
		m_iStart_Mois 				= iMois;
		return true;
	}
	*/
	
/*	
	boolean WriteXlsForecastHeader(CXls Xls){
		if (Xls == null){
			m_Debug.PrintWarning("CForecast : WriteXlsHeader Xls = null");
			return false;
		}
		Xls.CreateRow();
		int i,j,iMonthCurrent,iYearCurrent;
		String strPrintDate;
		
		j=0;
		iYearCurrent = m_Globals.m_iStart_Annee;
		iMonthCurrent = m_Globals.m_iStart_Mois;
		
		Xls.WriteCellHeader(CONSTANT.C_FORECAST+j++, "Nom item");
		
		for(i=0;i<m_Globals.m_iNb_Mois;i++){
			
			strPrintDate = iYearCurrent+"-"+iMonthCurrent;
			Xls.WriteCellHeader(CONSTANT.C_FORECAST+j++, strPrintDate);
			iMonthCurrent++;
			if (iMonthCurrent > 12){
				iMonthCurrent = 0;
				iYearCurrent++;
			}
		}

		return true;
	}
*/
	
	boolean WriteXlsLineHeader(CXls Xls, CItem Item, String strType, String strCatalogue, String strUnit){
		if (Xls == null){
			m_Debug.PrintWarning("CForecast::WriteXls Xls ==null");
			return false;
		}
		if (Item == null){
			m_Debug.PrintWarning("CForecast::WriteXls Item ==null");
			return false;
		}
		
		Xls.CreateRow();
		Xls.WriteCellHeader(CONSTANT.C_FORECAST_NOM_ITEM, Item.m_strNomItem);
		Xls.WriteCellHeader(CONSTANT.C_FORECAST_TYPE, strType);
		Xls.WriteCellHeader(CONSTANT.C_FORECAST_CATALOGUE, strCatalogue);
		Xls.WriteCellHeader(CONSTANT.C_FORECAST_UNITE, strUnit);
		
		return true;
	}
	
	boolean WriteXlsLineValues(CXls Xls, CItem Item, double iCoeff){
		if (Xls == null){
			m_Debug.PrintWarning("CForecast::WriteXlsLineValues Xls ==null");
			return false;
		}
		if (Item == null){
			m_Debug.PrintWarning("CForecast::WriteXlsLineValues Item ==null");
			return false;
		}

		int i;
		for(i=0;(i<size()) && (i < m_Globals.m_iNb_Mois);i++){
			Xls.WriteCellValue(CONSTANT.C_FORECAST_VALUES+i, get(i)*iCoeff);
		}
		for(i=size();i<m_Globals.m_iNb_Mois;i++){
			Xls.WriteCellValue(CONSTANT.C_FORECAST_VALUES+i, 0);
		}
		
		return true;
	}

	boolean WriteXlsLineHour(CXls Xls, CItem Item, double iCoeff){
		if (Xls == null){
			m_Debug.PrintWarning("CForecast::WriteXlsLineHour Xls ==null");
			return false;
		}
		if (Item == null){
			m_Debug.PrintWarning("CForecast::WriteXlsLineHour Item ==null");
			return false;
		}

		int i;
		for(i=0;(i<size()) && (i < m_Globals.m_iNb_Mois);i++){
			Xls.WriteCellHour(CONSTANT.C_FORECAST_VALUES+i, get(i)*iCoeff);
		}
		for(i=size();i<m_Globals.m_iNb_Mois;i++){
			Xls.WriteCellHour(CONSTANT.C_FORECAST_VALUES+i, 0);
		}
		
		return true;
	}

	boolean WriteXlsLineEuro(CXls Xls, CItem Item, double iCoeff){
		if (Xls == null){
			m_Debug.PrintWarning("CForecast::WriteXlsLineEuro Xls ==null");
			return false;
		}
		if (Item == null){
			m_Debug.PrintWarning("CForecast::WriteXlsLineEuro Item ==null");
			return false;
		}

		int i;
		for(i=0;(i<size()) && (i < m_Globals.m_iNb_Mois);i++){
			Xls.WriteCellEuro(CONSTANT.C_FORECAST_VALUES+i, get(i)*iCoeff);
		}
		for(i=size();i<m_Globals.m_iNb_Mois;i++){
			Xls.WriteCellEuro(CONSTANT.C_FORECAST_VALUES+i, 0);
		}
		
		return true;
	}

	boolean WriteXlsCatalogue(CXls Xls, CItem Item){
		if (Xls == null){
			m_Debug.PrintWarning("CForecast::WriteXlsCatalogue Xls ==null");
			return false;
		}
		if (Item == null){
			m_Debug.PrintWarning("CForecast::WriteXlsCatalogue Item ==null");
			return false;
		}
		
		CCatalogue Catalogue;
		
		Catalogue = Item.m_Locals.m_CatalogueCount;
		
		if ( Catalogue == null){
			m_Debug.PrintWarning("CForecast::WriteXlsCatalogue Catalogue ==null !");
			return false;
		}
		
		CComposant lComposant;
		
		Iterator<String> keySetIterator = Catalogue.m_MapComposant.keySet().iterator();

		while(keySetIterator.hasNext()){
		  String key = keySetIterator.next();
		  
		  // Pointeur sur composant à copier
		  lComposant = Catalogue.m_MapComposant.get(key);
		  
		  // Verifier que le composant a été trouvé
		  if (lComposant == null){
			  m_Debug.PrintWarning("CForecast::WriteXlsCatalogue : Could not iterate, find composant");
			  return false;
		  }
		  
			WriteXlsLineHeader(Xls,Item,lComposant.m_strReference,"o","Q");
			WriteXlsLineValues(Xls,Item,lComposant.m_dQuantite);

			WriteXlsLineHeader(Xls,Item,lComposant.m_strReference,"o","MO-Et (h)");
			WriteXlsLineHour(Xls,Item,lComposant.m_dMO_h_etude);

			WriteXlsLineHeader(Xls,Item,lComposant.m_strReference,"o","MO-Pr (h)");
			WriteXlsLineHour(Xls,Item,lComposant.m_dMO_h_production);

			WriteXlsLineHeader(Xls,Item,lComposant.m_strReference,"o","MO (€)");
			WriteXlsLineEuro(Xls,Item,lComposant.m_Cout_MO);

			WriteXlsLineHeader(Xls,Item,lComposant.m_strReference,"o","Achats (€)");
			WriteXlsLineEuro(Xls,Item,lComposant.m_Cout_Achats);

			WriteXlsLineHeader(Xls,Item,lComposant.m_strReference,"o","FIM (€)");
			WriteXlsLineEuro(Xls,Item,lComposant.m_Cout_FIM);

			WriteXlsLineHeader(Xls,Item,lComposant.m_strReference,"o","PAI (€)");
			WriteXlsLineEuro(Xls,Item,lComposant.m_Cout_Marge_Estimation);

			WriteXlsLineHeader(Xls,Item,lComposant.m_strReference,"o","Frais (€)");
			WriteXlsLineEuro(Xls,Item,lComposant.m_Cout_Frais);

			WriteXlsLineHeader(Xls,Item,lComposant.m_strReference,"o","Cout Total(€)");
			WriteXlsLineEuro(Xls,Item,lComposant.m_Cout_Total);
	
		}


		return true;
	}
	
	boolean WriteXls(CXls Xls, CItem Item){
		if (Xls == null){
			m_Debug.PrintWarning("CForecast : WriteXls Xls = null");
			return false;
		}
		
		//int i,j;
		
		
		WriteXlsLineHeader(Xls,Item,"-","-","Q");
		WriteXlsLineValues(Xls,Item,1);

		WriteXlsLineHeader(Xls,Item,"-","-","MO-Et (h)");
		WriteXlsLineHour(Xls,Item,Item.m_MO_h_e_Tot);

		WriteXlsLineHeader(Xls,Item,"-","-","MO-Pr (h)");
		WriteXlsLineHour(Xls,Item,Item.m_MO_h_p_Tot);

		WriteXlsLineHeader(Xls,Item,"-","-","MO (€)");
		WriteXlsLineEuro(Xls,Item,Item.m_Cout_MO);

		WriteXlsLineHeader(Xls,Item,"-","-","Achats (€)");
		WriteXlsLineEuro(Xls,Item,Item.m_Cout_Achats);

		WriteXlsLineHeader(Xls,Item,"-","-","FIM (€)");
		WriteXlsLineEuro(Xls,Item,Item.m_Cout_FIM);

		WriteXlsLineHeader(Xls,Item,"-","-","PAI (€)");
		WriteXlsLineEuro(Xls,Item,Item.m_Cout_Marge_Estimation);

		WriteXlsLineHeader(Xls,Item,"-","-","Frais (€)");
		WriteXlsLineEuro(Xls,Item,Item.m_Cout_Frais);

		WriteXlsLineHeader(Xls,Item,"-","-","Cout Total(€)");
		WriteXlsLineEuro(Xls,Item,Item.m_Cout_Total);
		
		WriteXlsCatalogue(Xls,Item);
		
			
		
		/*
		// Write Quantities first
		Xls.CreateRow();
		j=0;
		Xls.WriteCellHeader(CONSTANT.C_FORECAST_NOM_ITEM+j++, Item.m_strNomItem);
		Xls.WriteCellHeader(CONSTANT.C_FORECAST_NOM_ITEM+j++, "Q");
		for(i=0;i<size();i++,j++){
			Xls.WriteCellValue(CONSTANT.C_FORECAST_NOM_ITEM+j, get(i));
		}
		for(i=size();i<m_Globals.m_iNb_Mois;i++,j++){
			Xls.WriteCellValue(CONSTANT.C_FORECAST_NOM_ITEM+j, 0);
		}
		
		
		// Write hours etudes
		Xls.CreateRow();
		j=0;
		Xls.WriteCellHeader(CONSTANT.C_FORECAST_NOM_ITEM+j++, Item.m_strNomItem);
		Xls.WriteCellHeader(CONSTANT.C_FORECAST_NOM_ITEM+j++, "MO-Et (h)");
		for(i=0;i<size();i++,j++){
			Xls.WriteCellHour(CONSTANT.C_FORECAST_NOM_ITEM+j, get(i)*Item.m_MO_h_e_Tot);
		}
		for(i=size();i<m_Globals.m_iNb_Mois;i++,j++){
			Xls.WriteCellHour(CONSTANT.C_FORECAST_NOM_ITEM+j, 0);
		}

		// Write hours production
		Xls.CreateRow();
		j=0;
		Xls.WriteCellHeader(CONSTANT.C_FORECAST_NOM_ITEM+j++, Item.m_strNomItem);
		Xls.WriteCellHeader(CONSTANT.C_FORECAST_NOM_ITEM+j++, "MO-Pr (h)");
		for(i=0;i<size();i++,j++){
			Xls.WriteCellHour(CONSTANT.C_FORECAST_NOM_ITEM+j, get(i)*Item.m_MO_h_p_Tot);
		}
		for(i=size();i<m_Globals.m_iNb_Mois;i++,j++){
			Xls.WriteCellHour(CONSTANT.C_FORECAST_NOM_ITEM+j, 0);
		}
		
		// Write MO Euros
		Xls.CreateRow();
		j=0;
		Xls.WriteCellHeader(CONSTANT.C_FORECAST_NOM_ITEM+j++, Item.m_strNomItem);
		Xls.WriteCellHeader(CONSTANT.C_FORECAST_NOM_ITEM+j++, "MO (Euros)");
		for(i=0;i<size();i++,j++){
			Xls.WriteCellEuro(CONSTANT.C_FORECAST_NOM_ITEM+j, get(i)*Item.m_Cout_MO);
		}
		for(i=size();i<m_Globals.m_iNb_Mois;i++,j++){
			Xls.WriteCellEuro(CONSTANT.C_FORECAST_NOM_ITEM+j, 0);
		}
		
		// Write Achats
		Xls.CreateRow();
		j=0;
		Xls.WriteCellHeader(CONSTANT.C_FORECAST_NOM_ITEM+j++, Item.m_strNomItem);
		Xls.WriteCellHeader(CONSTANT.C_FORECAST_NOM_ITEM+j++, "Achats_e");
		for(i=0;i<size();i++,j++){
			Xls.WriteCellEuro(CONSTANT.C_FORECAST_NOM_ITEM+j, get(i)*Item.m_Cout_Achats);
		}
		for(i=size();i<m_Globals.m_iNb_Mois;i++,j++){
			Xls.WriteCellEuro(CONSTANT.C_FORECAST_NOM_ITEM+j, 0);
		}
		
		// Write FIM Euros
		Xls.CreateRow();
		j=0;
		Xls.WriteCellHeader(CONSTANT.C_FORECAST_NOM_ITEM+j++, Item.m_strNomItem);
		Xls.WriteCellHeader(CONSTANT.C_FORECAST_NOM_ITEM+j++, "FIM (Euros)");
		for(i=0;i<size();i++,j++){
			Xls.WriteCellEuro(CONSTANT.C_FORECAST_NOM_ITEM+j, get(i)*Item.m_Cout_FIM);
		}
		for(i=size();i<m_Globals.m_iNb_Mois;i++,j++){
			Xls.WriteCellEuro(CONSTANT.C_FORECAST_NOM_ITEM+j, 0);
		}

		// Write Marge Euros
		Xls.CreateRow();
		j=0;
		Xls.WriteCellHeader(CONSTANT.C_FORECAST_NOM_ITEM+j++, Item.m_strNomItem);
		Xls.WriteCellHeader(CONSTANT.C_FORECAST_NOM_ITEM+j++, "Marge (Euros)");
		for(i=0;i<size();i++,j++){
			Xls.WriteCellEuro(CONSTANT.C_FORECAST_NOM_ITEM+j, get(i)*Item.m_Cout_Marge_Estimation);
		}
		for(i=size();i<m_Globals.m_iNb_Mois;i++,j++){
			Xls.WriteCellEuro(CONSTANT.C_FORECAST_NOM_ITEM+j, 0);
		}

		// Write Frais Euros
		Xls.CreateRow();
		j=0;
		Xls.WriteCellHeader(CONSTANT.C_FORECAST_NOM_ITEM+j++, Item.m_strNomItem);
		Xls.WriteCellHeader(CONSTANT.C_FORECAST_NOM_ITEM+j++, "Frais (Euros)");
		for(i=0;i<size();i++,j++){
			Xls.WriteCellEuro(CONSTANT.C_FORECAST_NOM_ITEM+j, get(i)*Item.m_Cout_Frais);
		}
		for(i=size();i<m_Globals.m_iNb_Mois;i++,j++){
			Xls.WriteCellEuro(CONSTANT.C_FORECAST_NOM_ITEM+j, 0);
		}
		
		// Write Cost
		Xls.CreateRow();
		j=0;
		Xls.WriteCellHeader(CONSTANT.C_FORECAST_NOM_ITEM+j++, Item.m_strNomItem);
		Xls.WriteCellHeader(CONSTANT.C_FORECAST_NOM_ITEM+j++, "Cout_tot");
		for(i=0;i<size();i++,j++){
			Xls.WriteCellEuro(CONSTANT.C_FORECAST_NOM_ITEM+j, get(i)*Item.m_Cout_Total);
		}
		for(i=size();i<m_Globals.m_iNb_Mois;i++,j++){
			Xls.WriteCellEuro(CONSTANT.C_FORECAST_NOM_ITEM+j, 0);
		}

*/

		return true;
	}
	
	boolean SetGlobals(CGlobals Globals){
		if (Globals == null){
			m_Debug.PrintWarning("SetGobals at null");
			return false;
		}
		m_Globals = Globals;
		return true;
	}
	
	boolean ReadNode(Node Noeud){
		
		NodeList Noeuds = Noeud.getChildNodes();
		int nbNoeuds = Noeuds.getLength();
		
		for (int j = 0; j<nbNoeuds; j++) {	
		    if(Noeuds.item(j).getNodeType() == Node.ELEMENT_NODE) {
		        Node NoeudElement = Noeuds.item(j);
		        if (NoeudElement.getNodeName() == "Quantite")
		        {
		        	this.add((Double)Double.parseDouble(NoeudElement.getTextContent()));
		        }
		    }
		}

		return true;
	}
	
	boolean Print(){
		
		m_Debug.PrintInfo("------ Forecast ------");
		int i;
		String strPrintDate;
		String strPrintValue;
		strPrintDate = new String();
		strPrintValue= new String();
		int iYearCurrent=0;
		int iMonthCurrent=0;
		
		if (m_Globals == null){
			m_Debug.PrintWarning("CForecast::Print m_Globals == null");
			return false;
		}
		m_Globals.PrintGlobalsOf("CForecast");
		
		iYearCurrent = m_Globals.m_iStart_Annee;
		iMonthCurrent = m_Globals.m_iStart_Mois;
		
		
		for(i=0;i<size();i++){
			//strPrint += "> "+iYearCurrent+"-"+iMonthCurrent+":"+ get(i).intValue();
			strPrintDate += "> "+iYearCurrent+"-"+iMonthCurrent+"\t";
			strPrintValue += "> "+get(i).intValue()+"\t\t\t" ;
			
			iMonthCurrent++;
			if (iMonthCurrent > 12){
				iMonthCurrent = 0;
				iYearCurrent++;
			}
		}
		//m_Debug.PrintInfo(strPrint);
		m_Debug.PrintInfo(strPrintDate);
		m_Debug.PrintInfo(strPrintValue);
		
		return true;
	}
}
