import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CComposant {

	// Membres input paramters
	//boolean m_bisFromLibrary; 	// True si les éléments de valorisation viennent de la librairie
	double m_dQuantite;			// Quantite d'elements
	String m_strReference; 		// Référence de l'objet
	String m_strDescriptif;		// Descriptif du composant
	double m_dMO_h_etude;		// Nombre d'heures d'etudes associees au composant
	double m_dMO_h_production;	// Nombre d'heures de production
	double m_dAchats;			// cout en Euro des achats de matériel
	double m_dMarge_Estimation;	// % d'incertitude sur l'estimation
	double m_dFrais;			// Frais associés
	String m_strCommentaire;	// Commentaires associés au composant
	String m_strPar;			// Unité pour laquelle ce quantitatif est donné
	CGlobals m_Globals;
	
	// Computed values
	double m_Cout_Unit_MO;
	double m_Cout_Unit_FIM;
	double m_Cout_Unit_Marge_Estimation;
	double m_Cout_Achats;
	double m_Cout_Frais;
	double m_Cout_MO;
	double m_Cout_FIM;
	double m_Cout_Marge_Estimation;
	double m_Cout_Total;
		
	CDebug m_Debug;
	


	
	public CComposant(CDebug Debug) {
		//m_bisFromLibrary 		= false;
		m_dQuantite 			= -999999;
		m_strReference 			= "";
		m_strDescriptif 		= new String();
		m_dMO_h_etude 			= -1;
		m_dMO_h_production 		= -1;
		m_dAchats 				= -1;
		m_dMarge_Estimation 	= -1;
		m_dFrais 				= -1;
		m_strCommentaire		= new String();
		m_strPar 				= new String();
		m_Globals = null;
		m_Debug = Debug;
		
		// Initialisation of output values
		m_Cout_Unit_MO = -1;
		m_Cout_Unit_FIM = -1;
		m_Cout_Unit_Marge_Estimation = -1;
		
		m_Cout_MO = -1;
		m_Cout_FIM = -1;
		m_Cout_Marge_Estimation = -1;
		m_Cout_Total = -1;
		
		m_Cout_Achats = -1;
		m_Cout_Frais = -1;

	}
	
	public boolean SetToZero(){
		m_dQuantite 			= 0;
		//m_strReference 			= "";
		//m_strDescriptif 		= new String();
		m_dMO_h_etude 			= 0;
		m_dMO_h_production 		= 0;
		m_dAchats 				= 0;
		m_dMarge_Estimation 	= 0;
		m_dFrais 				= 0;
		//m_strCommentaire		= new String();
		//m_strPar 				= new String();
		//m_Globals = null;
		//m_Debug = Debug;
		
		// Initialisation of output values
		m_Cout_Unit_MO = 0;
		m_Cout_Unit_FIM = 0;
		m_Cout_Unit_Marge_Estimation = 0;
		
		m_Cout_MO = 0;
		m_Cout_FIM = 0;
		m_Cout_Marge_Estimation = 0;
		m_Cout_Unit_Marge_Estimation = 0;
		m_Cout_Unit_MO = 0;
		m_Cout_Total = 0;
		
		m_Cout_Achats = 0;
		m_Cout_Frais = 0;

		return true;
	}
	
	public boolean Copy(CComposant Composant){

		if (Composant == null)
		{
			m_Debug.PrintWarning("Copy()=> m_Globals is null !");
			return false;
		}
		
		m_dQuantite 	= Composant.m_dQuantite;
		m_strReference 	= new String(Composant.m_strReference);
		m_strDescriptif = new String(Composant.m_strDescriptif);
		m_dMO_h_etude 	= Composant.m_dMO_h_etude;
		m_dMO_h_production = Composant.m_dMO_h_production;
		m_dAchats	 	= Composant.m_dAchats;
		m_dMarge_Estimation	 = Composant.m_dMarge_Estimation;
		m_dFrais	 	= Composant.m_dFrais;
		m_strCommentaire = new String(Composant.m_strCommentaire);
		m_strPar	 	= new String(Composant.m_strPar);
		m_Globals 		= Composant.m_Globals;
		//m_Debug = Composant.m_Debug;

		m_Cout_Unit_MO 	= Composant.m_Cout_Unit_MO;
		m_Cout_Unit_FIM = Composant.m_Cout_Unit_FIM;
		m_Cout_Unit_Marge_Estimation = Composant.m_Cout_Unit_Marge_Estimation;

		m_Cout_MO = Composant.m_Cout_MO;
		m_Cout_FIM = Composant.m_Cout_FIM;
		m_Cout_Marge_Estimation = Composant.m_Cout_Marge_Estimation;
		m_Cout_Total = Composant.m_Cout_Total;

		return true;
	}
	
	boolean ComputeCosts(){
		if (m_Globals == null){
			m_Debug.PrintWarning("ComputeCosts()=> m_Globals is null !");
			return false;
		}
		
		// Couts de main d'oeuvre
		m_Cout_Unit_MO = 
				(m_dMO_h_etude * m_Globals.m_dCout_Horaire_Etudes) +
				(m_dMO_h_production * m_Globals.m_dCout_Horaire_Production);
		
		// Couts de frais indirect matériel
		m_Cout_Unit_FIM = m_dAchats * ( m_Globals.m_dFIM_L + m_Globals.m_dFIM_N) / 100 ;
		
		m_Cout_Unit_Marge_Estimation =
				(m_Cout_Unit_MO + m_dAchats + m_Cout_Unit_FIM + m_dFrais)*m_dMarge_Estimation/100;
		
		if (m_dQuantite <= -999999){
			m_Debug.PrintWarning("CComposant:Compute cost : quantite <0");
			return false;
		}
		
		
		m_Cout_MO 				= m_dQuantite * m_Cout_Unit_MO;
		m_Cout_Achats 			= m_dQuantite * m_dAchats;
		m_Cout_FIM 				= m_dQuantite * m_Cout_Unit_FIM;
		m_Cout_Marge_Estimation = m_dQuantite * m_Cout_Unit_Marge_Estimation; 
		m_Cout_Frais 			= m_dQuantite * m_dFrais;

		
		m_Cout_Total = 
				m_Cout_MO +
				m_Cout_Achats +
				m_Cout_FIM +
				m_Cout_Marge_Estimation +
				m_Cout_Frais;
		
		// Mettre à jour le nombre d'occurences dans la librairie de couts
		UpdateLibraryCount();
				
		return true;
	}
	
	boolean UpdateLibraryCount(){
		
		return true;
	}
	
	boolean SetGlobals(CGlobals Globals){
		if (Globals == null){
			m_Debug.PrintWarning("Globals == null");
			return false;
		}
		m_Globals = Globals;
		return true;
	}
	
	boolean WriteXls(CXls Xls){
		
		if (Xls == null){
			return false;
		}
		
		int iOffset;
		iOffset=CONSTANT.C_COMPOSANT;
		int iCellIndex;
		iCellIndex = 0;
        Xls.CreateRow();
        /*
         if (m_bisFromLibrary == true){
        	Xls.WriteCellString(iOffset+iCellIndex++, "o");
        }else{
        	Xls.WriteCellString(iOffset+iCellIndex++, "n");
        }
        */
        Xls.WriteCellString(iOffset+iCellIndex++, m_strDescriptif);
        Xls.WriteCellHour(iOffset+iCellIndex++, m_dMO_h_etude*m_dQuantite);
        Xls.WriteCellHour(iOffset+iCellIndex++, m_dMO_h_production*m_dQuantite);
        Xls.WriteCellEuro(iOffset+iCellIndex++, m_Cout_MO);
        Xls.WriteCellEuro(iOffset+iCellIndex++, m_Cout_Achats);
        Xls.WriteCellEuro(iOffset+iCellIndex++, m_Cout_FIM);
        Xls.WriteCellValue(iOffset+iCellIndex++, 1);
        Xls.WriteCellPercent(iOffset+iCellIndex++, m_dMarge_Estimation/100);
        Xls.WriteCellEuro(iOffset+iCellIndex++, m_Cout_Marge_Estimation);
        Xls.WriteCellEuro(iOffset+iCellIndex++, m_Cout_Frais);
        Xls.WriteCellValue(iOffset+iCellIndex++, m_dQuantite);
        Xls.WriteCellString(iOffset+iCellIndex++, "Est.");
        Xls.WriteCellEuro(iOffset+iCellIndex++, m_Cout_Total);
        Xls.WriteCellString(iOffset+iCellIndex++, m_strCommentaire);
        

		
		return true;
	}
	
	public boolean Print()
	{
		//m_Debug.PrintInfo("============= PRINT COMPOSANT ===============");
		m_Debug.PrintInfo(
				//"isLib(b)="+ m_bisFromLibrary+
				": Q="+m_dQuantite+
				": Ref="+m_strReference+
				": MOe(h)="+m_dMO_h_etude+
				": MOp(H)="+m_dMO_h_production+
				": Achat(e)="+m_dAchats+ 
				": Marge(p)="+m_dMarge_Estimation+
				": Frais(e)="+m_dFrais+
				": Desc="+m_strDescriptif+
				": Com="+ m_strCommentaire+
				": Par="+m_strPar);
		
		//m_Globals.PrintGlobalsOf("CComposant");
		/*
		m_Debug.PrintInfo(" Par = " + m_strPar);
		m_Debug.PrintInfo(" Reference = " + m_strReference);
		m_Debug.PrintInfo(" Descriptif = " + m_strDescriptif);
		m_Debug.PrintInfo(" MO (h) etude = " + m_dMO_h_etude);
		m_Debug.PrintInfo(" MO (h) production = " + m_dMO_h_production);
		m_Debug.PrintInfo(" Achats (Euros) = " + m_dAchats);
		m_Debug.PrintInfo(" Marge Estimation (%) = " + m_dMarge_Estimation);
		m_Debug.PrintInfo(" Frais = " + m_dFrais);
		*/
		//m_Debug.PrintInfo("==============================================");
		
		return true;
	}
	
	boolean UpdateWithLibrary(){
		if (m_Globals.m_Catalogue == null){
			m_Debug.PrintWarning("CComposant : UpdateWithLibrary Catalogue = null");
			return false;
		}
		
		if (m_strReference.isEmpty()==true){
			// On ne recherche pas un objet bibliothèque
			return true;
		}
		
		// C'est un objet bibliotheque
		CComposant Composant;
		//m_Debug.PrintInfo("CCComposant Update Library FindComposant");
		Composant = m_Globals.m_Catalogue.FindComposant(m_strReference);
		
		if (Composant == null){
			m_Debug.PrintWarning("CComposant, UPdateWithLibrary. Could not find composant "+ m_strReference);
			m_strCommentaire += "\nComposant introuvé"; 
			return false;
		}
		else{
			m_strCommentaire += "\nComposant trouvé";
		}
		
		if (m_strDescriptif.isEmpty()){
			m_strDescriptif = Composant.m_strDescriptif;
		}
		else{
			m_strCommentaire += "\nSpecif : Descriptif";
		}
		
		if (m_dMO_h_etude == 0){
			m_dMO_h_etude = Composant.m_dMO_h_etude;
		}
		else{
			m_strCommentaire += "\nSpecif MO_h Etude";
		}
		
		if (m_dMO_h_production == 0){
			m_dMO_h_production = Composant.m_dMO_h_production;
		}
		else{
			m_strCommentaire += "\nSpecif MO_h Prod";
		}
		
		if (m_dAchats== 0){
			m_dAchats= Composant.m_dAchats;
		}
		else{
			m_strCommentaire += "\nSpecif : Achats";
		}
		
		if (m_dMarge_Estimation== 0){
			m_dMarge_Estimation= Composant.m_dMarge_Estimation;
		}
		else{
			m_strCommentaire += "\nSpecif : Marge Estim";
		}
		
		if (m_dFrais== 0){
			m_dFrais= Composant.m_dFrais;
		}
		else{
			m_strCommentaire += "\nSpecif : Frais";
		}
		
		return true;
	}
	public boolean ReadNode(Node thisNode){
		
		NodeList NoeudsItems = thisNode.getChildNodes();
		
		int nbNoeudsItems = NoeudsItems.getLength();
		for (int i = 0; i<nbNoeudsItems; i++) {	
		    if(NoeudsItems.item(i).getNodeType() == Node.ELEMENT_NODE) {
		        Node NoeudItem = NoeudsItems.item(i);
		        //m_Debug.PrintInfo(NoeudComposant.getTextContent());
		        // m_Debug.PrintInfo(NoeudItem.getNodeName() + " " + NoeudItem.getTextContent() );
		        /*
		        if (NoeudItem.getNodeName() == "isFromLibrary" ){
		        	m_bisFromLibrary = Boolean.parseBoolean(NoeudItem.getTextContent());
		        }
		        */
		        if (NoeudItem.getNodeName() == "Quantite" ){
		        	m_dQuantite = Double.parseDouble(NoeudItem.getTextContent());
		        }
		        
		        if (NoeudItem.getNodeName() == "Reference" ){
		        	m_strReference = NoeudItem.getTextContent();
		        }
		        if (NoeudItem.getNodeName() == "Descriptif" ){
		        	m_strDescriptif = NoeudItem.getTextContent();
		        }
		        if (NoeudItem.getNodeName() == "MO_h_etude" ){
		        	m_dMO_h_etude = Double.parseDouble(NoeudItem.getTextContent());
		        }
		        if (NoeudItem.getNodeName() == "MO_h_production" ){
		        	m_dMO_h_production = Double.parseDouble(NoeudItem.getTextContent());
		        }
		        if (NoeudItem.getNodeName() == "Achats" ){
		        	m_dAchats = Double.parseDouble(NoeudItem.getTextContent());
		        }
		        if (NoeudItem.getNodeName() == "Marge_Estimation" ){
		        	m_dMarge_Estimation = Double.parseDouble(NoeudItem.getTextContent());
		        }
		        if (NoeudItem.getNodeName() == "Frais" ){
		        	m_dFrais = Double.parseDouble(NoeudItem.getTextContent());
		        }
		        if (NoeudItem.getNodeName() == "Commentaire" ){
		        	m_strCommentaire = NoeudItem.getTextContent();
		        }
		        if (NoeudItem.getNodeName() == "Par" ){
		        	m_strPar = NoeudItem.getTextContent();
		        }
		    }
		}
		return true;
	}

}
