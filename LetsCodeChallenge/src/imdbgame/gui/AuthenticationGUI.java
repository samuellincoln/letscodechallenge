package imdbgame.gui;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import imdbgame.data.MBUserData;
import imdbgame.utils.MBConstants;
import imdbgame.utils.MBSugars;

import javax.crypto.*;

public class AuthenticationGUI extends JFrame implements ActionListener {
    private MBUserData userdata;
    private EnterMoviesGUI emgui;
	public void actionPerformed(ActionEvent e) {
		Component [] comps = this.getContentPane().getComponents();
		String claimedlogin = ((JTextField)comps [1]).getText().toString();
		String claimedpassword = ((JPasswordField)comps [3]).getText().toString();
		if (e.getActionCommand() == MBConstants.clogin) {
			if (!userdata.containsKey(claimedlogin)) {
				MBSugars.outDialog("No user login! You must REGISTER first!!");
			}
			else {
				if (!userdata.getPassword(claimedlogin).equals(claimedpassword)) {
					MBSugars.outDialog ("WRONG password! TRY typing again!");
				}
				else {
					this.emgui = new EnterMoviesGUI (claimedlogin, userdata.getPoints(claimedlogin), userdata.getErrors(claimedlogin), userdata);
				}
				MBSugars.println("");
			}
		}
		else if (e.getActionCommand() == MBConstants.cregister) {
			if (userdata.containsKey(claimedlogin)) {
				MBSugars.outDialog("User ALREADY REGISTERED!");
			}
			else {
				this.userdata.insertNewUser (claimedlogin, claimedpassword);
			}
		}
		else if (e.getActionCommand() == MBConstants.cranking) {
			MBSugars.outDialog(userdata.generateRanking());
		}
	}
	public AuthenticationGUI () {
		super ("Welcome to Movies Battle!");
		this.userdata = new MBUserData ();
        this.setLayout(new FlowLayout ());
        this.add (new JLabel ("User name: "));
        this.add (new JTextField (MBConstants.textfieldsize));
        this.add (new JLabel ("Password: "));
        this.add (new JPasswordField (MBConstants.textfieldsize));
        JButton blogin = new JButton (MBConstants.clogin);
        blogin.addActionListener(this);
        this.add (blogin);
        JButton branking = new JButton (MBConstants.cranking);
        branking.addActionListener(this);
        this.add (branking);
        JButton bregister = new JButton (MBConstants.cregister);
        bregister.addActionListener(this);
        this.add (bregister);
        this.setSize(350, 300);
        this.setVisible(true);
	}
}
