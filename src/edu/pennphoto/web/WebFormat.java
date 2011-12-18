package edu.pennphoto.web;

import java.text.DecimalFormat;

public class WebFormat {

    private static DecimalFormat df = new DecimalFormat("#.#");
    
    public static String format(double d){
    	return df.format(d);
    }
    
    
}
