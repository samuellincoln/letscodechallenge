package imdbgame.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

public class MBMethods {
	public static String stringCommaRemoved (String str) {
		if (!str.contains(",")) {
			return str;
		}
		else {
			int commaindex = str.indexOf(",");
			String prefix = str.substring (0, commaindex);
			String sufix = str.substring (commaindex + 1);
			return stringCommaRemoved (prefix + sufix);
		}
	}
	public static String [] tokenizeLine (String line) {
		StringTokenizer st = new StringTokenizer (line);
		String [] vec = new String [st.countTokens()];
		int counter = 0;
		while (st.hasMoreTokens()) {
			vec [counter] = st.nextToken();
			counter++;
		}
		return vec;
	}
	public static int rankingposition (String user, HashMap <String, Integer> user2points) {
		Integer [] points = new Integer [user2points.keySet().size()];
		String [] users = new String [user2points.keySet().size()];
		points = (Integer []) user2points.values().toArray();
		Arrays.sort(points);
		Integer value = user2points.get(user);
		int currentvalue = points [0];
		int counter = 0;
		while (value > currentvalue) {
			counter++;
			currentvalue = points [counter];
		}
		return counter;
	}
	public static String mountIMDBURL (String moviename) {
		StringTokenizer st = new StringTokenizer (moviename);
		String movienamelink = "";
		int ctok = st.countTokens();
		int tokindex = 0;
		while (st.hasMoreTokens()) {
			movienamelink += st.nextToken();
			if (tokindex < ctok) {
				movienamelink += "+";
			}
			tokindex++;
		}
		return "http://www.omdbapi.com/?apikey=" + MBConstants.key + "&t=" + movienamelink + "&r=xml";
	}
}