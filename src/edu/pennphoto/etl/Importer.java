package edu.pennphoto.etl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.zorbaxquery.api.DocumentManager;
import org.zorbaxquery.api.InMemoryStore;
import org.zorbaxquery.api.Item;
import org.zorbaxquery.api.Iterator;
import org.zorbaxquery.api.XQuery;
import org.zorbaxquery.api.XmlDataManager;
import org.zorbaxquery.api.Zorba;

/**Import data from groups 8, 11 and 12
 * @author Mark
 *
 */
public class Importer {
	
	static InMemoryStore store;
	static Zorba zorba;
	
	static{
		System.out.println(System.getProperty("java.library.path"));
		System.loadLibrary ( "zorba_api" );	
		store = InMemoryStore.getInstance();
		zorba = Zorba.getInstance(store);
	}
	


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String xml = (readFile("DataExchange/pennphoto-11.xml"));
		System.out.println(generateCsv(xml));
		
	}
	
	public static String readFile(String fileName){
	    StringBuilder sb = new StringBuilder();
		try{
			File file = new File(fileName);
			System.out.println(file.getAbsolutePath());
		    BufferedReader reader = new BufferedReader(new FileReader (file));
		    String line;
		    while((line = reader.readLine()) != null) {
		        sb.append(line + "\n");
		    }
		} catch (Exception e){
			
		}
		return sb.toString();
	}
	
	public static final String query = "for $x in doc('input.xml')/photodb/* return " +
			"<user>{$x/id}{$x/name}{$x/email}{$x/age}{$x/gender}</user>";
	
	public static String generateCsv(final String xml)
			throws IllegalArgumentException {
	    XmlDataManager dm = zorba.getXmlDataManager();

	    Iterator i = dm.parseXML(xml);
	    i.open();
	    Item doc = Item.createEmptyItem();
	    i.next(doc);
	    i.close();
	    i.delete();

	    DocumentManager docMgr = dm.getDocumentManager();
	    docMgr.put("input.xml", doc);

	    doc.delete();
	 

	    XQuery xquery = zorba.compileQuery(query);
	    String ret =  xquery.execute() ;
	    
	    System.out.println("getPersonAndNeighbors(): expandedQuery = **" + query + "** end expandedQuery");
	    
	    System.out.println("getPersonAndNeighbors(): ret = **" + ret + "** end ret");

	    docMgr.remove("input.xml");
	    
	    return ret;
	}

}
