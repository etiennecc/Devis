import java.io.IOException;

// import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
// import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.DefaultParser;
//import java.util.logging.*;

public class CMain {

	public static void main(String[] args) throws IOException, ParseException {
		
		String strCatalogueFile;
		String strCostDriverFile;
		String strOutputFile;
		
		strCatalogueFile = "Catalogue.xml";
		strCostDriverFile = "CostDrivers.xml";
		strOutputFile = "Output.xlsx";
		
		Options options = new Options();
		
		options.addOption("cat", true, "Catalogue");
		options.addOption("i", true, "Input cost driver file");
		options.addOption("o", true, "Output file (with xlsx extension)");
		
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse( options, args);
		
		if (cmd.hasOption("cat")){
			strCatalogueFile = cmd.getOptionValue("cat");
		}
		
		if (cmd.hasOption("i")){
			strCostDriverFile = cmd.getOptionValue("i");
		}
		
		if (cmd.hasOption("o")){
			strOutputFile = cmd.getOptionValue("o");
		}
		
		// Debug Interface
		CDebug Debug;
		Debug = new CDebug();
		
		// Object for catalogue loading
		CCatalogue Catalogue;
		Catalogue = new CCatalogue(Debug);
		
		//Catalogue.ReadFile("Catalogue.xml");
		Catalogue.ReadFile(strCatalogueFile);
		//Catalogue.Print();
		
		// Xls
		CXls Xls;
		
		Xls = new CXls(Debug);
		
		CCostDrivers CostDrivers;
		
		CostDrivers = new CCostDrivers(Debug);
		CostDrivers.SetCatalogue(Catalogue);
		//CostDrivers.ReadFile("CostDrivers.xml");
		CostDrivers.ReadFile(strCostDriverFile);
		CostDrivers.Print();
		CostDrivers.WriteXls(Xls, strOutputFile);

	}

}
