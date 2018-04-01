
public class CGlobals {
	CDebug m_Debug;
	double m_dCout_Horaire_Etudes;
	double m_dCout_Horaire_Production;
	double m_dFIM_L;
	double m_dFIM_N;
	double m_dFrais_Client;
	int m_iStart_Annee;
	int m_iStart_Mois;
	int m_iNb_Mois;
	CCatalogue m_Catalogue; // Catalogue copie conforme du fichier XML
	CCatalogue m_CatalogueCount; // Catalogue pour compte le nombre d'instance de chaque item

	CGlobals(CDebug Debug){
		m_Debug = Debug;
		m_dCout_Horaire_Etudes =  -1;
		m_dCout_Horaire_Production =  -1;
		m_dFIM_L =  -1;
		m_dFIM_N =  -1;
		m_dFrais_Client = -1;
		m_iStart_Annee =  0;
		m_iStart_Mois =  0;
		m_iNb_Mois = 0;
		m_Catalogue = null;
		m_CatalogueCount = null;
	}
	
	boolean PrintGlobalsOf(String strName){
		m_Debug.PrintInfo("Globals of "+strName);
		m_Debug.PrintInfo("Cout_Horaire_Etudes = " + m_dCout_Horaire_Etudes);
		m_Debug.PrintInfo("Cout_Horaire_Production = " + m_dCout_Horaire_Production);
		m_Debug.PrintInfo("FIM_L = " + m_dFIM_L);
		m_Debug.PrintInfo("FIM_N = " + m_dFIM_N);
		m_Debug.PrintInfo("Frais Client = " + m_dFrais_Client);
		m_Debug.PrintInfo("Annee = " + m_iStart_Annee);
		m_Debug.PrintInfo("Mois Start = " + m_iStart_Mois);
		m_Debug.PrintInfo("Nb Mois = " + m_iNb_Mois);
		return true;
	}
	
	boolean SmartCopy(CGlobals Global){
		if (m_dCout_Horaire_Etudes == -1){
			m_dCout_Horaire_Etudes = Global.m_dCout_Horaire_Etudes;
		}
		if ( m_dCout_Horaire_Production == -1){
			m_dCout_Horaire_Production = Global.m_dCout_Horaire_Production;
		}
		if (m_dFIM_L == -1){
			m_dFIM_L = Global.m_dFIM_L;
		}
		if (m_dFIM_N == -1){
			m_dFIM_N = Global.m_dFIM_N;
		}
		if (m_dFrais_Client == -1){
			m_dFrais_Client = Global.m_dFrais_Client;
		}
		if ( m_iStart_Annee == 0){
			m_iStart_Annee = Global.m_iStart_Annee;
		}
		if (m_iStart_Mois == 0){
			m_iStart_Mois = Global.m_iStart_Mois;
		}
		
		if (m_iNb_Mois == 0){
			m_iNb_Mois = Global.m_iNb_Mois;
		}
		
		m_Catalogue 		= Global.m_Catalogue;
		
		if (Global.m_CatalogueCount != null){
			m_CatalogueCount 	= Global.m_CatalogueCount;
		}
		
		/*if ( (m_Catalogue != null) && (m_CatalogueCount != null) ){
			//m_CatalogueCount = new CCatalogue(m_Debug);
			m_CatalogueCount.Copy(m_Catalogue);
		}
		*/

		return true;
	}
}
