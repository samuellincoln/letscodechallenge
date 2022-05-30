package imdbgame.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.JOptionPane;

public class MBSugars {
	public static void println (String str) {
		System.out.println (str);
	}
	public static void outDialog (String str) {
		JOptionPane.showMessageDialog (null, str);
	}
	public static void inDialog (String str) {
		JOptionPane.showInputDialog (str);
	}
	public static BufferedReader readerFile (String path) throws FileNotFoundException {
		return new BufferedReader (new FileReader (new File (path)));
	}
	public static BufferedWriter writerFile (String path) throws IOException {
		return new BufferedWriter (new FileWriter (new File (path)));
	}
	public static BufferedReader readerURL (URL u) throws IOException {
		return new BufferedReader (new InputStreamReader (u.openStream()));
	}
}