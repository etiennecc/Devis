import java.io.IOException;
//import java.util.logging.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
//import java.util.logging.SimpleFormatter;
import java.io.*;

public class CDebug {

	File m_logFile;
	BufferedWriter m_writer = null;
	
	public CDebug(){
		try {
			
		m_logFile = new File("Debug.txt");
		m_logFile.delete();
		m_writer = new BufferedWriter(new FileWriter(m_logFile,true));
		
		} catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public void PrintInfo(String str){
		String timeLog = new SimpleDateFormat("yyyyMMdd_HH:mm:ss").format(Calendar.getInstance().getTime());
		try {
			m_writer.write(timeLog + " > " + str+"\n");
			m_writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void PrintWarning(String str){
		String timeLog = new SimpleDateFormat("yyyyMMdd_HH:mm:ss").format(Calendar.getInstance().getTime());
		try {
			m_writer.write(timeLog + " > - WARNING -" + str+"\n");
			m_writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
