package edu.pennphoto.test;

import static org.junit.Assert.*;

import java.util.Date;

import edu.pennphoto.model.*;
import edu.pennphoto.model.User.Gender;
import edu.pennphoto.etl.*;

import org.junit.Before;
import org.junit.Test;

public class ExporterTest {
	
	User user1;
	StringBuilder sb;
	public static final String TAB = Exporter.TAB;

	@Before
	public void setup(){
		user1 = new Professor(1, "user1@penn.edu", "First 1", "Last 1", new Date(), 
				Gender.MALE, "Primero Street", "Isa City", "NY", "10001");
		sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		sb.append("<tns:photodb xmlns:tns=\"pennphoto17ns\"\n");
		sb.append(TAB + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
		sb.append(TAB + "xsi:schemaLocation=\"pennphoto17ns pennphoto17.xsd\">\n");
		sb.append(TAB + "<tns:professor>\n");
		sb.append(TAB + TAB + "<tns:id>1</tns:id>\n");
		sb.append(TAB + TAB + "<tns:email>user1@upenn.edu</tns:email>\n");
		sb.append(TAB + TAB + "<tns:first_name>First 1</tns:first_name>\n");
		sb.append(TAB + TAB + "<tns:last_name>Last 1</tns:last_name>\n");
		sb.append(TAB + TAB + "<tns:dob>1986-07-19</tns:dob>\n");
		sb.append(TAB + TAB + "<tns:street_address>Primero Street</tns:street_address>\n");
		sb.append(TAB + TAB + "<tns:city>Isa City</tns:city>\n");
		sb.append(TAB + TAB + "<tns:state>NY</tns:state>\n");
		sb.append(TAB + TAB + "<tns:zip_code>10001</tns:zipcode>\n");
		sb.append(TAB + TAB + "<tns:is_professor>true</tns:is_professor>\n");
		sb.append(TAB + TAB + "<tns:gender>m</tns:gender>\n");
	}

	@Test
	public void testSingleUserExport() {
		String actual = Exporter.export(user1);
		sb.append(TAB + "</tns:professor>\n");
		sb.append("</tns:photodb>\n");
		String expected = sb.toString();
		assertEquals(expected, actual);
	}

}
