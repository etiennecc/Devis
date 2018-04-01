import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.ArrayList;

public class CGroupe {
	CDebug m_Debug;
	String m_strNom_Groupe;
	double m_dQuantite;
	CGlobals m_Globals;
	CGlobals m_Locals;
	ArrayList<CComposant> m_ListComposant;
	
	//Couts du groupe
	double m_Cout_Achats;
	double m_Cout_Frais;
	double m_Cout_MO;
	double m_Cout_FIM;
	double m_Cout_Marge_Estimation;
	double m_Cout_Total;
	
	double m_MO_h_e_Tot;
	double m_MO_h_p_Tot;
	
	CGroupe(CDebug Debug){
		m_Debug = Debug;
		m_strNom_Groupe = new String();
		m_dQuantite = -1;
		m_Globals = null;
		m_Locals = new CGlobals(m_Debug);
		m_ListComposant = new ArrayList<CComposant>(); 

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
	boolean ComputeCosts(){
		boolean bRet;
		CComposant lComposant;
		CComposant lComposantCount;
		CComposant lComposantCountAutre;
		
		double ldMO_h_e_Tot ;
		double ldMO_h_p_Tot; 
		double ldCout_Achats;
		double ldCout_Frais;
		double ldCout_MO;
		double ldCout_FIM;
		double ldCout_Marge_Estimation;
		double ldCout_Total;
		
		ResetCout();
		
		lComposantCountAutre = m_Locals.m_CatalogueCount.FindComposant(CONSTANT.C_STR_REF_HORS_CATALOGUE);
		if (lComposantCountAutre == null){
			m_Debug.PrintWarning("CGroupe::ComputeCost => ComposantCountAutre n'a pu etre trouve !! Taille catalogue = " + m_Locals.m_CatalogueCount.m_MapComposant.size()+ "Composant = " + CONSTANT.C_STR_REF_HORS_CATALOGUE);
			return false;
		}
				
		for(int i = 0 ; i < m_ListComposant.size() ;i++){
			lComposant = m_ListComposant.get(i);

			bRet = lComposant.ComputeCosts();
			if (bRet == false){
				m_Debug.PrintWarning("CGroupe: ComputeCost failed");
				return false;
			}
			
			ldMO_h_e_Tot 			= m_dQuantite * 
					( lComposant.m_dMO_h_etude * lComposant.m_dQuantite);
			ldMO_h_p_Tot 			= m_dQuantite * 
					( lComposant.m_dMO_h_production * lComposant.m_dQuantite);

			ldCout_Achats 			= m_dQuantite * lComposant.m_Cout_Achats; 
			ldCout_Frais 			= m_dQuantite * lComposant.m_Cout_Frais;
			ldCout_MO 				= m_dQuantite * lComposant.m_Cout_MO;
			ldCout_FIM 				= m_dQuantite * lComposant.m_Cout_FIM;
			ldCout_Marge_Estimation = m_dQuantite * lComposant.m_Cout_Marge_Estimation;
			ldCout_Total 			= m_dQuantite * lComposant.m_Cout_Total;

			m_MO_h_e_Tot  			+= ldMO_h_e_Tot ;
			m_MO_h_p_Tot  			+= ldMO_h_p_Tot; 
			m_Cout_Achats 			+= ldCout_Achats;
			m_Cout_Frais 			+= ldCout_Frais;
			m_Cout_MO 				+= ldCout_MO;
			m_Cout_FIM 				+= ldCout_FIM;
			m_Cout_Marge_Estimation += ldCout_Marge_Estimation;
			m_Cout_Total 			+= ldCout_Total;


			/*
			m_MO_h_e_Tot 			+= m_dQuantite * 
					( lComposant.m_dMO_h_etude * lComposant.m_dQuantite);
			m_MO_h_p_Tot 			+= m_dQuantite * 
					( lComposant.m_dMO_h_production * lComposant.m_dQuantite);

			m_Cout_Achats 			+= m_dQuantite * lComposant.m_Cout_Achats; 
			m_Cout_Frais 			+= m_dQuantite * lComposant.m_Cout_Frais;
			m_Cout_MO 				+= m_dQuantite * lComposant.m_Cout_MO;
			m_Cout_FIM 				+= m_dQuantite * lComposant.m_Cout_FIM;
			m_Cout_Marge_Estimation += m_dQuantite * lComposant.m_Cout_Marge_Estimation;
			m_Cout_Total 			+= m_dQuantite * lComposant.m_Cout_Total;
			*/

			if ( lComposant.m_strReference.isEmpty() == false) {
				
				// Compter le nombre d'occurences d'item dans la librairie
				//m_Debug.PrintInfo("CGropue Compute Cost FindComposant: Composant recherche = "+ lComposant.m_strReference);
				lComposantCount = m_Locals.m_CatalogueCount.FindComposant(lComposant.m_strReference); 
				if (  lComposantCount != null){
					// Le composant a été trouvé
					lComposantCount.m_dQuantite += m_dQuantite * lComposant.m_dQuantite;

					lComposantCount.m_dMO_h_etude 			+= ldMO_h_e_Tot ;
					lComposantCount.m_dMO_h_production		+= ldMO_h_p_Tot; 
					lComposantCount.m_Cout_Achats 			+= ldCout_Achats;
					lComposantCount.m_Cout_Frais 			+= ldCout_Frais;
					lComposantCount.m_Cout_MO 				+= ldCout_MO;
					lComposantCount.m_Cout_FIM 				+= ldCout_FIM;
					lComposantCount.m_Cout_Marge_Estimation += ldCout_Marge_Estimation;
					lComposantCount.m_Cout_Total 			+= ldCout_Total;
					
				} else {
					// Erreur.. .le composant n'a pas été trouvé.
					m_Debug.PrintWarning("ERREUR CGroupe::ComputeCost Composant recherche = "+ lComposant.m_strReference + " n'a pu etre trouvé");
					return false;
				}
			}
			else
			{
				// Il ne s'agit pas d'un composant de la librairie.
				lComposantCountAutre.m_dQuantite += m_dQuantite * lComposant.m_dQuantite;
				
				lComposantCountAutre.m_dMO_h_etude 			+= ldMO_h_e_Tot ;
				lComposantCountAutre.m_dMO_h_production		+= ldMO_h_p_Tot; 
				lComposantCountAutre.m_Cout_Achats 			+= ldCout_Achats;
				lComposantCountAutre.m_Cout_Frais 			+= ldCout_Frais;
				lComposantCountAutre.m_Cout_MO 				+= ldCout_MO;
				lComposantCountAutre.m_Cout_FIM 			+= ldCout_FIM;
				lComposantCountAutre.m_Cout_Marge_Estimation += ldCout_Marge_Estimation;
				lComposantCountAutre.m_Cout_Total 			+= ldCout_Total;

			}
			
			/*
			bRet = m_ListComposant.get(i).ComputeCosts();
			if (bRet == false){
				m_Debug.PrintWarning("CGroupe: ComputeCost failed");
				return false;
			}
			
			m_MO_h_e_Tot 			+= m_dQuantite * 
					( m_ListComposant.get(i).m_dMO_h_etude * m_ListComposant.get(i).m_dQuantite);
			m_MO_h_p_Tot 			+= m_dQuantite * 
					( m_ListComposant.get(i).m_dMO_h_production * m_ListComposant.get(i).m_dQuantite);

			m_Cout_Achats 			+= m_dQuantite * m_ListComposant.get(i).m_Cout_Achats; 
			m_Cout_Frais 			+= m_dQuantite * m_ListComposant.get(i).m_Cout_Frais;
			m_Cout_MO 				+= m_dQuantite * m_ListComposant.get(i).m_Cout_MO;
			m_Cout_FIM 				+= m_dQuantite * m_ListComposant.get(i).m_Cout_FIM;
			m_Cout_Marge_Estimation += m_dQuantite * m_ListComposant.get(i).m_Cout_Marge_Estimation;
			m_Cout_Total 			+= m_dQuantite * m_ListComposant.get(i).m_Cout_Total;
			*/

			
			
		}
		return true;
	}
	
	boolean SetGlobals(CGlobals Globals){
		if (Globals == null){
			m_Debug.PrintInfo("SetGlobals with null value !");
			return false;
		}
		m_Globals = Globals;
		m_Locals.SmartCopy(m_Globals);
		
		for(int i = 0 ; i < m_ListComposant.size() ;i++){
			if ( m_ListComposant.get(i).SetGlobals(m_Locals) == false){
				m_Debug.PrintInfo("Failed to set Globals at index "+i);
				return false;
			}
		}

		return true;
	}
	
	boolean UpdateWithLibrary(){
		
		for(int i=0;i<m_ListComposant.size();i++){
			m_ListComposant.get(i).UpdateWithLibrary();
		}
		return true;
	}
	boolean WriteXls(CXls Xls){
		if (Xls == null){
			m_Debug.PrintWarning("CGroupe: WriteXLS => Xls = null !");
			return false;
		}

        Xls.m_CurrentSheet.setColumnWidth(2, 20*256);
        
        Xls.CreateRow();
        Xls.CreateRow();
        //Xls.WriteCellString(2, "Nom Groupe :");
        //Xls.WriteCellString(CONSTANT.C_GROUPE, "Nom Groupe :");
        //Xls.WriteCellString(CONSTANT.C_GROUPE+1, m_strNom_Groupe);
        Xls.WriteCellHeaderYellow(CONSTANT.C_GROUPE, "Nom Groupe :");
        Xls.WriteCellHeaderYellow(CONSTANT.C_GROUPE+1, m_strNom_Groupe);
        Xls.CreateRow();
        Xls.WriteCellString(CONSTANT.C_GROUPE, "Quantite :");
        Xls.WriteCellValue(CONSTANT.C_GROUPE+1, m_dQuantite);
        Xls.CreateRow();

        Xls.WriteCellString(CONSTANT.C_GROUPE, "Cout Horaire Etude	 :");
        Xls.WriteCellRate(CONSTANT.C_GROUPE+1, m_Locals.m_dCout_Horaire_Etudes);
        Xls.CreateRow();
        Xls.WriteCellString(CONSTANT.C_GROUPE, "Cout Horaire Production	 :");
        Xls.WriteCellRate(CONSTANT.C_GROUPE+1, m_Locals.m_dCout_Horaire_Production);

        
        Xls.CreateRow();
        int k,k_init;
        k_init=k=CONSTANT.C_COMPOSANT;
        //Xls.WriteCellHeader(k++,"Biblio\n(o/n)");
        Xls.m_CurrentSheet.setColumnWidth(k, 40*256);
        Xls.WriteCellHeader(k++,"Descriptif");
        Xls.WriteCellHeader(k++,"MO etud\n(h)");
        Xls.WriteCellHeader(k++,"MO pro\n(h)");
        Xls.WriteCellHeader(k++,"MO\n(€)");
        Xls.WriteCellHeader(k++,"Achats\n(€)");
        Xls.WriteCellHeader(k++,"FIM L+N\n(€)");
        Xls.WriteCellHeader(k++,"");
        Xls.WriteCellHeader(k++,"Marge est.\n(%)");
        Xls.m_CurrentSheet.setColumnWidth(k, 10*256);
        Xls.WriteCellHeader(k++,"Marge\n(€)");
        Xls.m_CurrentSheet.setColumnWidth(k, 10*256);
        Xls.WriteCellHeader(k++,"Frais\n(€)");
        Xls.WriteCellHeader(k++,"Quantité");
        Xls.WriteCellHeader(k++,"");
        Xls.m_CurrentSheet.setColumnWidth(k, 10*256);
        Xls.WriteCellHeader(k++,"SOUS-TOTAL\n(€)");
        Xls.m_CurrentSheet.setColumnWidth(k, 50*256);
        Xls.WriteCellHeader(k++,"Commentaire");
        
		for(int i=0;i<m_ListComposant.size();i++){
			m_ListComposant.get(i).WriteXls(Xls);
		}
		
		Xls.CreateRow();
        k=k_init;
        //Xls.WriteCellHeader(k++,"");
        
        Xls.WriteCellHeader(k++,"SOUS-TOTAL");
        Xls.WriteCellHeaderHeure(k++,m_MO_h_e_Tot);
        Xls.WriteCellHeaderHeure(k++,m_MO_h_p_Tot);
        Xls.WriteCellHeaderEuro(k++,m_Cout_MO);
        Xls.WriteCellHeaderEuro(k++,m_Cout_Achats);
        Xls.WriteCellHeaderEuro(k++,m_Cout_FIM);
        Xls.WriteCellHeader(k++,"");
        Xls.WriteCellHeader(k++,"NA");
        Xls.WriteCellHeaderEuro	(k++,m_Cout_Marge_Estimation);
        Xls.WriteCellHeaderEuro(k++,m_Cout_Frais);
        Xls.WriteCellHeader(k++,"NA");
        Xls.WriteCellHeader(k++,"");
        Xls.WriteCellHeaderEuro(k++,m_Cout_Total);

		
		return true;
	}
	
	boolean ReadNode(Node Noeud){
		
		NodeList Noeuds = Noeud.getChildNodes();
		
		int nbNoeuds = Noeuds.getLength();
		
		for (int j = 0; j<nbNoeuds; j++) {	
		    if(Noeuds.item(j).getNodeType() == Node.ELEMENT_NODE) {
		        Node NoeudElement = Noeuds.item(j);
		
		        if (NoeudElement.getNodeName() == "Nom_Groupe")
		        {
		        	m_strNom_Groupe = NoeudElement.getTextContent();
		        }

		        if (NoeudElement.getNodeName() == "Quantite")
		        {
		        	m_dQuantite = Double.parseDouble(NoeudElement.getTextContent());
		        }

		        if (NoeudElement.getNodeName() == "Composant")
		        {
		        	CComposant Composant;
		        	Composant = new CComposant(m_Debug);
		        	Composant.ReadNode(NoeudElement);
		        	m_ListComposant.add(Composant);
		        }
		        
		        if (NoeudElement.getNodeName() == "Cout_Horaire_Production")
		        {
		        	m_Locals.m_dCout_Horaire_Production = Double.parseDouble(NoeudElement.getTextContent());
		        }
		        
		        if (NoeudElement.getNodeName() == "Cout_Horaire_Etudes")
		        {
		        	//double d_Test = Double.parseDouble(NoeudElement.getTextContent());
		        	m_Locals.m_dCout_Horaire_Etudes = Double.parseDouble(NoeudElement.getTextContent());
		        }


		    }
		}	
		return true;
	}
	
	boolean Print(){
		m_Debug.PrintInfo("------ CGroupe ------");
		m_Debug.PrintInfo("Groupe="+m_strNom_Groupe);
		m_Debug.PrintInfo("Q="+m_dQuantite);
		if (m_Globals == null){
			m_Debug.PrintInfo("m_Debug null is Print of CGroupe");
			return false;
		}
		m_Globals.PrintGlobalsOf("CGroupe");
		int i;
		for(i=0;i<m_ListComposant.size();i++){
			m_ListComposant.get(i).Print();
		}
		
		return true;
	}

}
