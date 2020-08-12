package CBIR3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapSort {
	
	public static Map<String, Double> sortMapByValue(Map<String, Double> oriMap) {  
	    Map<String, Double> sortedMap = new LinkedHashMap<String, Double>();  
	    if (oriMap != null && !oriMap.isEmpty()) {  
	        List<Map.Entry<String, Double>> entryList = new ArrayList<Map.Entry<String, Double>>(oriMap.entrySet());  
	        Collections.sort(entryList,  
	                new Comparator<Map.Entry<String, Double>>(){  
	                    public int compare(Entry<String, Double> entry1,  
	                            Entry<String, Double> entry2) {  
	                    			double value1 = 0, value2 = 0;  
	                    			value1 = entry1.getValue();  
	                    			value2 =entry2.getValue();  
	                    			return  value1   <   value2   ?   1   :   (value1   ==   value2   ?   0   :   -1);   
	                    }  
	                });  
	        Iterator<Map.Entry<String, Double>> iter = entryList.iterator();  
	        Map.Entry<String, Double> tmpEntry = null;  
	        while (iter.hasNext()) {  
	            tmpEntry = iter.next();  
	            sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());  
	        }  
	    }  
	    return sortedMap;  
	}  
}
