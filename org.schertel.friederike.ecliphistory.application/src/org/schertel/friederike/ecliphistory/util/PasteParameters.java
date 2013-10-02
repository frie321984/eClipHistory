package org.schertel.friederike.ecliphistory.util;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.IParameterValues;
import org.schertel.friederike.ecliphistory.handler.PasteHandler;

/**
 * This List describes which parameter values are valid for the PasteHandler.
 * 
 * @see PasteHandler
 */
public class PasteParameters  implements IParameterValues {
	
    public Map<String, String> getParameterValues() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("Position 1", "0");
        params.put("Position 2", "1");
        params.put("Position 3", "2");
        params.put("Position 4", "3");
        params.put("Position 5", "4");
        params.put("Position 6", "5");
        params.put("Position 7", "6");
        params.put("Position 8", "7");
        params.put("Position 9", "8");
        return params;
    }
}