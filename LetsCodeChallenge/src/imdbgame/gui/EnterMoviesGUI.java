package imdbgame.gui;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import imdbgame.data.MBUserData;
import imdbgame.utils.MBConstants;
import imdbgame.utils.MBMethods;
import imdbgame.utils.MBSugars;

//import sun.misc.BASE64Encoder;
//import com.sun.jersey.api.core.util.Base64;
//import sun.net.www.protocol.http.HttpURLConnection;
public class EnterMoviesGUI extends JFrame implements ActionListener {
	String movieBiggestScore = "";
	private String currentuser = "";
	private int updatedpoints = 0;
	private int updatederrors = 0;
	private int updatedlogins = 0;
	private int errorsonthisquiz;
	private MBUserData userdata;
	private HashSet <HashSet <String>> chosenpairs;
	public EnterMoviesGUI (String username, Integer points, Integer errors, MBUserData userdata) {
        super ("WELCOME, " + username + "\n! You have " + points + " points, " + errors + " errors and \nthis is your quiz " + userdata.getLogins(username) + " in MOVIES BATTLE!!");
        this.updatedpoints = points;
        this.updatederrors = errors;
        this.updatedlogins = userdata.getLogins(username) + 1;
        this.currentuser = username;
        this.userdata = userdata;
        this.setLayout(new FlowLayout ());
        this.add(new JLabel ("Type name of movie 1: "));
        this.add(new JTextField (MBConstants.textfieldsize));
        this.add(new JLabel ("Type name of movie 2: "));
        this.add(new JTextField (MBConstants.textfieldsize));
        JButton button = new JButton (MBConstants.CHOOSEMOVIES);
        button.addActionListener (this);
        this.add (button);
        JButton button2 = new JButton (MBConstants.EXIT);
        button2.addActionListener(this);
        this.add (button2);
        JButton button3 = new JButton (MBConstants.NEWROUND);
        button3.setEnabled(false);
        button3.addActionListener(this);
        this.add (button3);
        //this.add(new JLabel (""));
        //this.add(new JLabel (""));
        this.chosenpairs = new HashSet <HashSet <String>> ();
        this.errorsonthisquiz = 0;
        this.setSize(MBConstants.guisize1, MBConstants.guisize2);
        this.setVisible(true);
	}
	private ImageIcon getPosterFromURL (String urlposter) {
		try {
			URL u = new URL (urlposter);
			BufferedImage bi = ImageIO.read (u);
			return new ImageIcon (bi);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public Document getMovieContentFromIMDB (String moviename) {
		String url = MBMethods.mountIMDBURL (moviename);
		Document doc = null;
		try {
			URL u = new URL (url);
			InputStream inputstream = u.openStream();
			InputStreamReader streamreader = new InputStreamReader (inputstream);
			BufferedReader br = new BufferedReader (streamreader);
			String l = br.readLine();
			System.out.println (l);
			br.close();
			doc = Jsoup.parse (l);
			System.out.println ();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}
	public double getAbsoluteRate (Document doc) { //Calculated by multiplying the rate and the number of votes
		Elements els = doc.getElementsByAttribute("imdbRating");
		Element el = els.get(0);
		String rValue1 = el.attr("imdbRating");
		String value2 = el.attr("imdbVotes");
		String rValue2 = MBMethods.stringCommaRemoved(value2);
		return Double.parseDouble(rValue1) * Double.parseDouble(rValue2);
	}
	public ImageIcon getPoster (Document doc) {
		Elements els = doc.getElementsByAttribute("poster");
		Element el = els.get(0);
		String urlposter = el.attr("poster");
		return getPosterFromURL (urlposter);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String movie1 = "";
		String movie2 = "";
        Component [] cs = this.getContentPane().getComponents();
		if (e.getActionCommand().equals(MBConstants.CHOOSEMOVIES)) {
	        movie1 = ((JTextField)cs[1]).getText();
	        movie2 = ((JTextField)cs[3]).getText();
	        HashSet <String> pair = new HashSet <String> ();
	        pair.add(movie1);
	        pair.add(movie2);
			if (this.chosenpairs.contains(pair)) {
				MBSugars.outDialog("THIS PAIR OF MOVIES WAS ALREADY CHOSEN! PLEASE CHOOSE ANOTHER PAIR!");
			}
			else {
				this.chosenpairs.add(pair);
		        Document docmovie1 = getMovieContentFromIMDB (movie1);
		        Document docmovie2 = getMovieContentFromIMDB (movie2);
		        if (movie1.equals(movie2)) {
			        if (!movie1.equals("")) {
			        	MBSugars.outDialog("IDENTICAL movie names! type again!");
			        	return;
			        }
		        }
		        ((JButton)cs[MBConstants.choosemovies]).setEnabled(false);
	
		        ImageIcon ii1 = getPoster (docmovie1);
		        ImageIcon ii2 = getPoster (docmovie2);
	
		        this.add (new JLabel (ii1));
		        this.add (new JLabel (ii2));
				this.add (new JLabel ("Which one has the highest score?! "));
				this.add (new JComboBox <String> (new String [] {movie1, movie2}));
				JButton gamble = new JButton (MBConstants.GAMBLE);
				gamble.addActionListener (this);
				this.add(gamble);
		        this.setSize(MBConstants.guisize1, MBConstants.guisize2);
		        this.setVisible(true);
		        System.out.println ();
		        double abs1 = getAbsoluteRate (docmovie1);
		        double abs2 = getAbsoluteRate (docmovie2);

		        if (Double.compare(abs1, abs2) > 0) {
		        	movieBiggestScore = movie1;
		        }
		        else if (Double.compare(abs1, abs2) < 0) {
		        	movieBiggestScore = movie2;
		        }
		        else {
		        	movieBiggestScore = "tie";
		        }
			}
		}
		else if (e.getActionCommand().equals (MBConstants.NEWROUND)) {
			((JButton)cs[MBConstants.nextroundindex]).setEnabled (false);
	        ((JButton)cs[MBConstants.choosemovies]).setEnabled(true);
	        this.remove(cs[7]);
	        this.remove(cs[8]);
	        this.remove(cs[9]);
	        this.remove(cs[10]);
	        this.remove(cs[11]);
		}
		else if (e.getActionCommand().equals(MBConstants.GAMBLE)) {
	        String chosen = (String) ((JComboBox)cs[MBConstants.comboindex]).getSelectedItem();
	        if (chosen.equals(movieBiggestScore)) {
	        	MBSugars.outDialog("CONGRATULATIONS! You made it! " + chosen + " really is the movie with highest score!!");
	        	this.updatedpoints++;
	        }
	        else if (!chosen.equals(movieBiggestScore)) {
	        	MBSugars.outDialog("WRONG ANSWER! You now have now " + (this.errorsonthisquiz + 1) + " wrong answers on this quiz. Try again...");
	        	this.updatederrors++;
	        	this.errorsonthisquiz++;
	        	if (this.errorsonthisquiz >= 3) {
	        		MBSugars.outDialog("You have reached 3 ERRORS on this match! This match will end...");
	    			this.userdata.updateData (this.currentuser, this.updatedpoints, this.updatederrors, this.updatedlogins);
	        		System.exit(0);
	        	}
	        }
	        else {
	        	MBSugars.outDialog("SURREAL! both movies have EXACTLY the SAME score!!");
	        }
	        ((JButton)cs[MBConstants.comboindex + 1]).setEnabled(false);
	        ((JButton)cs[MBConstants.nextroundindex]).setEnabled (true);
		}
		else if (e.getActionCommand().equals(MBConstants.EXIT)) {
			this.userdata.updateData (this.currentuser, this.updatedpoints, this.updatederrors, this.updatedlogins);
			System.exit (0);
		}
	}
}
