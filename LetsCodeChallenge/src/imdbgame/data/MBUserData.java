package imdbgame.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import imdbgame.utils.MBConstants;
import imdbgame.utils.MBMethods;
import imdbgame.utils.MBSugars;

public class MBUserData {
	private HashMap <String, String> user2password;
	private HashMap <String, Integer> user2points;
	private HashMap <String, Integer> user2errors;
	private HashMap <String, Integer> user2logins;
	private HashMap <String, Double> user2majorscore;
	public MBUserData () {
		this.user2password = new HashMap <String, String> ();
		this.user2points = new HashMap <String, Integer> ();
		this.user2errors = new HashMap <String, Integer> ();
		this.user2logins = new HashMap <String, Integer> ();
		this.user2majorscore = new HashMap <String, Double> ();
		try {
			BufferedReader brusers = MBSugars.readerFile(MBConstants.userdatapath);
			while (brusers.ready()) {
				String line = brusers.readLine();
				String [] tokens = MBMethods.tokenizeLine(line);
				if (tokens.length == MBConstants.tokenslength) { //the line can be empty
					this.user2password.put (tokens [0], tokens [1]);
					
					Integer points = Integer.parseInt(tokens [2]);
					this.user2points.put (tokens [0], points);
					
					Integer errors = Integer.parseInt(tokens [3]);
					this.user2errors.put (tokens [0], errors);
					
					Integer logins = Integer.parseInt(tokens [4]);
					this.user2logins.put (tokens [0], logins);
					this.user2majorscore.put (tokens [0], ((double)logins) * (((double)points) / ((double)errors)));
				}
			}
			brusers.close();
			MBSugars.println("");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public boolean containsKey (String key) {
		return this.user2password.containsKey(key);
	}
	public String getPassword (String key) {
		return this.user2password.get(key);
	}
	public Integer getPoints (String key) {
		return this.user2points.get(key);
	}
	public Integer getErrors (String key) {
		return this.user2errors.get(key);
	}
	public Integer getLogins (String key) {
		return this.user2logins.get(key);
	}
	public void insertNewUser (String login, String password) {
		this.user2password.put(login, password);
		updateData (login, 0, 0, 0);
	}
	public void updateData (String login, int newpoints, int newerrors, int newlogins) {
		this.user2points.put(login, newpoints);
		this.user2errors.put(login, newerrors);
		this.user2logins.put(login, newlogins);
		try {
			BufferedWriter bw = MBSugars.writerFile(MBConstants.userdatapath);
			Iterator <String> keys = this.user2password.keySet().iterator();
			String updatedtext = "";
			while (keys.hasNext()) {
				String next = keys.next();
				updatedtext += 
						next 
						+ " " + this.user2password.get(next) 
						+ " " + this.user2points.get (next) 
						+ " " + this.user2errors.get (next)
						+ " " + this.user2logins.get (next)
						+ "\n"
				;
			}
			bw.write(updatedtext);
			bw.close();
			MBSugars.println("");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public String generateRanking () {
		String str = "";
		Object [] values = this.user2majorscore.values().toArray();
		Arrays.sort(values);
		Object [] keys = this.user2majorscore.keySet().toArray();
		String [] finalranking = new String [keys.length];
		for (int i = 0; i < keys.length; i++) {
			for (int j = values.length - 1; j >= 0; j--) {
				Double currentvalue = this.user2majorscore.get((String)keys [i]);
				if (Double.compare (currentvalue, (Double)values [j]) == 0) {
					finalranking [values.length - j - 1] = "º: " + keys [i] + "(" + currentvalue + ")";
				}
			}
		}
		for (int i = 0; i < finalranking.length; i++) {
			str += (i + 1) + finalranking [i] + "\n";
		}
		return str;
	}
}