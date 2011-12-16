
package nut;

import javax.microedition.lcdui.*;
import nut.Configuration.*;
import nut.Constants.*;
import nut.HelpForm.*;

/**
 * J2ME Patient Registration Form
 * Displays registration fields
 * Checks completeness
 * Sends as SMS
 * @author alou
 */
public class StockForm extends Form implements CommandListener {

    private static final Command CMD_EXIT = new Command ("Retour", Command.BACK, 1);
    private static final Command CMD_SAVE = new Command ("Envoi.", Command.OK, 1);
    private static final Command CMD_HELP = new Command ("Aide", Command.HELP, 2);
    private static final int MAX_SIZE = 5; // max no. of chars per field.

    private static final String[] monthList= {" --- ", "Janvier (01)", "Février (02)", "Mars (03)", "Avril (04)", "Mai (05)", "Juin (06)", "Juillet (07)", "Aout (08)", "Septembre (09)", "Octobre (10)", "Novembre (11)", "Décembre (12)"};
    private static final String[] yearList = {" --- ", "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020"};

    public NUTMIDlet midlet;

    private Configuration config;
    private String hc_code = "";
    private String health_center = "";
    private StringItem period_intro;

    private ChoiceGroup monthField;
    private ChoiceGroup yearField;
    
    private String ErrorMessage = "";
    
    private StringItem nie_intro;
    private TextField nie_initial;
    private TextField nie_received;
    private TextField nie_used;
    private TextField nie_lost;
    
    private StringItem mil_intro;
    private TextField mil_initial;
    private TextField mil_received;
    private TextField mil_used;
    private TextField mil_lost;
    
    private StringItem suc_intro;
    private TextField suc_initial;
    private TextField suc_received;
    private TextField suc_used;
    private TextField suc_lost;
    
    private StringItem hui_intro;
    private TextField hui_initial;
    private TextField hui_received;
    private TextField hui_used;
    private TextField hui_lost;

    private StringItem uni_intro;
    private TextField uni_initial;
    private TextField uni_received;
    private TextField uni_used;
    private TextField uni_lost;

    private StringItem csb_intro;
    private TextField csb_initial;
    private TextField csb_received;
    private TextField csb_used;
    private TextField csb_lost;

    private StringItem l100_intro;
    private TextField l100_initial;
    private TextField l100_received;
    private TextField l100_used;
    private TextField l100_lost;

    private StringItem l75_intro;
    private TextField l75_initial;
    private TextField l75_received;
    private TextField l75_used;
    private TextField l75_lost;

    private StringItem pln_intro;
    private TextField pln_initial;
    private TextField pln_received;
    private TextField pln_used;
    private TextField pln_lost;

public StockForm(NUTMIDlet midlet) {
    super("Conso Intrant");
    this.midlet = midlet;

    config = new Configuration();

    monthField = new ChoiceGroup("Mois:", ChoiceGroup.POPUP, monthList, null);
    yearField = new ChoiceGroup("Année:", ChoiceGroup.POPUP, yearList, null);
    hc_code = config.get("hc_code");
    health_center = config.get("health_center");
    period_intro = new StringItem(null, "Indiquez la periode");

    // creating all fields (blank)
    
    //Niebe
    nie_intro = new StringItem(null, "Niebe");
    nie_initial =  new TextField("Stock de debut:", null, MAX_SIZE, TextField.DECIMAL);
    nie_received =  new TextField("Stock recu:", null, MAX_SIZE, TextField.DECIMAL);
    nie_used =  new TextField("Stock utilise:", null, MAX_SIZE, TextField.DECIMAL);
    nie_lost =  new TextField("Stock perdu:", null, MAX_SIZE, TextField.DECIMAL);
    
    //Mil
    mil_intro = new StringItem(null, "Mil");
    mil_initial =  new TextField("Stock de debut:", null, MAX_SIZE, TextField.DECIMAL);
    mil_received =  new TextField("Stock recu:", null, MAX_SIZE, TextField.DECIMAL);
    mil_used =  new TextField("Stock utilise:", null, MAX_SIZE, TextField.DECIMAL);
    mil_lost =  new TextField("Stock perdu:", null, MAX_SIZE, TextField.DECIMAL);

    //Sucre
    suc_intro = new StringItem(null, "Sucre");
    suc_initial =  new TextField("Stock de debut:", null, MAX_SIZE, TextField.DECIMAL);
    suc_received =  new TextField("Stock recu:", null, MAX_SIZE, TextField.DECIMAL);
    suc_used =  new TextField("Stock utilise:", null, MAX_SIZE, TextField.DECIMAL);
    suc_lost =  new TextField("Stock perdu:", null, MAX_SIZE, TextField.DECIMAL);

    //huile
    hui_intro = new StringItem(null, "Huile");
    hui_initial =  new TextField("Stock de debut:", null, MAX_SIZE, TextField.DECIMAL);
    hui_received =  new TextField("Stock recu:", null, MAX_SIZE, TextField.DECIMAL);
    hui_used =  new TextField("Stock utilise:", null, MAX_SIZE, TextField.DECIMAL);
    hui_lost =  new TextField("Stock perdu:", null, MAX_SIZE, TextField.DECIMAL);
    
    //Unimix
    uni_intro = new StringItem(null, "Unimix");
    uni_initial =  new TextField("Stock de debut:", null, MAX_SIZE, TextField.DECIMAL);
    uni_received =  new TextField("Stock recu:", null, MAX_SIZE, TextField.DECIMAL);
    uni_used =  new TextField("Stock utilise:", null, MAX_SIZE, TextField.DECIMAL);
    uni_lost =  new TextField("Stock perdu:", null, MAX_SIZE, TextField.DECIMAL);

    //CSB
    csb_intro = new StringItem(null, "CSB");
    csb_initial =  new TextField("Stock de debut:", null, MAX_SIZE, TextField.DECIMAL);
    csb_received =  new TextField("Stock recu:", null, MAX_SIZE, TextField.DECIMAL);
    csb_used =  new TextField("Stock utilise:", null, MAX_SIZE, TextField.DECIMAL);
    csb_lost =  new TextField("Stock perdu:", null, MAX_SIZE, TextField.DECIMAL);

    //l100
    l100_intro = new StringItem(null, "l100");
    l100_initial =  new TextField("Stock de debut:", null, MAX_SIZE, TextField.DECIMAL);
    l100_received =  new TextField("Stock recu:", null, MAX_SIZE, TextField.DECIMAL);
    l100_used =  new TextField("Stock utilise:", null, MAX_SIZE, TextField.DECIMAL);
    l100_lost =  new TextField("Stock perdu:", null, MAX_SIZE, TextField.DECIMAL);

    //l75
    l75_intro = new StringItem(null, "l75");
    l75_initial =  new TextField("Stock de debut:", null, MAX_SIZE, TextField.DECIMAL);
    l75_received =  new TextField("Stock recu:", null, MAX_SIZE, TextField.DECIMAL);
    l75_used =  new TextField("Stock utilise:", null, MAX_SIZE, TextField.DECIMAL);
    l75_lost =  new TextField("Stock perdu:", null, MAX_SIZE, TextField.DECIMAL);

    //plumpy
    pln_intro = new StringItem(null, "Plumpy nut");
    pln_initial =  new TextField("Stock de debut:", null, MAX_SIZE, TextField.DECIMAL);
    pln_received =  new TextField("Stock recu:", null, MAX_SIZE, TextField.DECIMAL);
    pln_used =  new TextField("Stock utilise:", null, MAX_SIZE, TextField.DECIMAL);
    pln_lost =  new TextField("Stock perdu:", null, MAX_SIZE, TextField.DECIMAL);
    
    if ((hc_code).equals("URENAM")){
    // add  niebe fields to forms
    append(nie_intro);
    append(nie_initial);
    append(nie_received);
    append(nie_used);
    append(nie_lost);

    // add  CSB fields to forms
    append(csb_intro);
    append(csb_initial);
    append(csb_received);
    append(csb_used);
    append(csb_lost);

    // add  unimix fields to forms
    append(uni_intro);
    append(uni_initial);
    append(uni_received);
    append(uni_used);
    append(uni_lost);

    // add  huile fields to forms
    append(hui_intro);
    append(hui_initial);
    append(hui_received);
    append(hui_used);
    append(hui_lost);

    // add  sucre fields to forms
    append(suc_intro);
    append(suc_initial);
    append(suc_received);
    append(suc_used);
    append(suc_lost);

    // add  mil fields to forms
    append(mil_intro);
    append(mil_initial);
    append(mil_received);
    append(mil_used);
    append(mil_lost);

    // add  period
    append(period_intro);
    append(monthField);
    append(yearField);

    } else if ((hc_code).equals("URENAS")){
    // add  plumpy nut fields to forms
    append(pln_intro);
    append(pln_initial);
    append(pln_received);
    append(pln_used);
    append(pln_lost);

    // add  period
    append(period_intro);
    append(monthField);
    append(yearField);

    } else if ((hc_code).equals("URENAM + URENAS")){
    // add  niebe fields to forms
    append(nie_intro);
    append(nie_initial);
    append(nie_received);
    append(nie_used);
    append(nie_lost);

    // add  CSB fields to forms
    append(csb_intro);
    append(csb_initial);
    append(csb_received);
    append(csb_used);
    append(csb_lost);

    // add  unimix fields to forms
    append(uni_intro);
    append(uni_initial);
    append(uni_received);
    append(uni_used);
    append(uni_lost);

    // add  huile fields to forms
    append(hui_intro);
    append(hui_initial);
    append(hui_received);
    append(hui_used);
    append(hui_lost);

    // add  sucre fields to forms
    append(suc_intro);
    append(suc_initial);
    append(suc_received);
    append(suc_used);
    append(suc_lost);

    // add  mil fields to forms
    append(mil_intro);
    append(mil_initial);
    append(mil_received);
    append(mil_used);
    append(mil_lost);

    // add  plumpy nut fields to forms
    append(pln_intro);
    append(pln_initial);
    append(pln_received);
    append(pln_used);
    append(pln_lost);

    // add  period
    append(period_intro);
    append(monthField);
    append(yearField);

    } else if ((hc_code).equals("URENI")){
    // add  l75 fields to forms
    append(l75_intro);
    append(l75_initial);
    append(l75_received);
    append(l75_used);
    append(l75_lost);

    // add  l100 fields to forms
    append(l100_intro);
    append(l100_initial);
    append(l100_received);
    append(l100_used);
    append(l100_lost);

    // add  plumpy nut fields to forms
    append(pln_intro);
    append(pln_initial);
    append(pln_received);
    append(pln_used);
    append(pln_lost);

    // add  period
    append(period_intro);
    append(monthField);
    append(yearField);

    }

    addCommand(CMD_EXIT);
    addCommand(CMD_SAVE);
    addCommand(CMD_HELP);
    this.setCommandListener (this);
}

    /*
     * converts internal <code>int</code> data to <code>String</code> for field
     * @param value the number to display on field
     * @return the <code>String</code> to attach to the field.
     */
    private String valueForField(int value) {
        if (value == -1) {
            return "";
        }
        return String.valueOf(value);
    }

    /*
     * Whether all required fields are filled
     * @return <code>true</code> is all fields are filled
     * <code>false</code> otherwise.
     */
    public boolean isComplete() {
        // all fields are required to be filled.
        if ((this.hc_code).equals("URENAM")){
            if (this.nie_initial.getString().length() == 0 ||
                this.nie_received.getString().length() == 0 ||
                this.nie_used.getString().length() == 0 ||
                this.nie_lost.getString().length() == 0 ||

                this.csb_initial.getString().length() == 0 ||
                this.csb_received.getString().length() == 0 ||
                this.csb_used.getString().length() == 0 ||
                this.csb_lost.getString().length() == 0 ||

                this.uni_initial.getString().length() == 0 ||
                this.uni_received.getString().length() == 0 ||
                this.uni_used.getString().length() == 0 ||
                this.uni_lost.getString().length() == 0 ||

                this.suc_initial.getString().length() == 0 ||
                this.suc_received.getString().length() == 0 ||
                this.suc_used.getString().length() == 0 ||
                this.suc_lost.getString().length() == 0 ||

                this.hui_initial.getString().length() == 0 ||
                this.hui_received.getString().length() == 0 ||
                this.hui_used.getString().length() == 0 ||
                this.hui_lost.getString().length() == 0 ||

                this.mil_initial.getString().length() == 0 ||
                this.mil_received.getString().length() == 0 ||
                this.mil_used.getString().length() == 0 ||
                this.mil_lost.getString().length() == 0 ){
                return false;
            } return true;
        } else if ((this.hc_code).equals("URENAS")){
            if (this.pln_initial.getString().length() == 0 ||
                this.pln_received.getString().length() == 0 ||
                this.pln_used.getString().length() == 0 ||
                this.pln_lost.getString().length() == 0){
                return false;
            } return true;

         } else if ((this.hc_code).equals("URENAM + URENAS")){
            if (this.nie_initial.getString().length() == 0 ||
                this.nie_received.getString().length() == 0 ||
                this.nie_used.getString().length() == 0 ||
                this.nie_lost.getString().length() == 0 ||

                this.csb_initial.getString().length() == 0 ||
                this.csb_received.getString().length() == 0 ||
                this.csb_used.getString().length() == 0 ||
                this.csb_lost.getString().length() == 0 ||

                this.uni_initial.getString().length() == 0 ||
                this.uni_received.getString().length() == 0 ||
                this.uni_used.getString().length() == 0 ||
                this.uni_lost.getString().length() == 0 ||

                this.suc_initial.getString().length() == 0 ||
                this.suc_received.getString().length() == 0 ||
                this.suc_used.getString().length() == 0 ||
                this.suc_lost.getString().length() == 0 ||

                this.hui_initial.getString().length() == 0 ||
                this.hui_received.getString().length() == 0 ||
                this.hui_used.getString().length() == 0 ||
                this.hui_lost.getString().length() == 0 ||

                this.mil_initial.getString().length() == 0 ||
                this.mil_received.getString().length() == 0 ||
                this.mil_used.getString().length() == 0 ||
                this.mil_lost.getString().length() == 0 ||

                this.pln_initial.getString().length() == 0 ||
                this.pln_received.getString().length() == 0 ||
                this.pln_used.getString().length() == 0 ||
                this.pln_lost.getString().length() == 0){
                return false;
            } return true;
         } else if ((this.hc_code).equals("URENI")){
             if (this.l75_initial.getString().length() == 0 ||
                this.l75_received.getString().length() == 0 ||
                this.l75_used.getString().length() == 0 ||
                this.l75_lost.getString().length() == 0 ||

                this.l100_initial.getString().length() == 0 ||
                this.l100_received.getString().length() == 0 ||
                this.l100_used.getString().length() == 0 ||
                this.l100_lost.getString().length() == 0 ||

                this.pln_initial.getString().length() == 0 ||
                this.pln_received.getString().length() == 0 ||
                this.pln_used.getString().length() == 0 ||
                this.pln_lost.getString().length() == 0){
                return false;
            } return true;
         }
         return true;
    }

    /*
     * Whether all filled data is correct
     * @return <code>true</code> if all fields are OK
     * <code>false</code> otherwise.
     */
    public boolean isValid() {
        if ((this.hc_code).equals("URENAM")){
            if (Integer.parseInt(this.nie_initial.getString())
                + Integer.parseInt(this.nie_received.getString())
                <= Integer.parseInt(this.nie_used.getString())
                + Integer.parseInt(this.nie_lost.getString())) {
                ErrorMessage = "stock initial + stock recu ne peut pas etre inferieur stock utilise + stock perdu";
                return false;
            }
            if (Integer.parseInt(this.csb_initial.getString())
                + Integer.parseInt(this.csb_received.getString())
                <= Integer.parseInt(this.csb_used.getString())
                + Integer.parseInt(this.csb_lost.getString())) {
                ErrorMessage = "stock initial + stock recu ne peut pas etre inferieur stock utilise + stock perdu";
                return false;
            }
            if (Integer.parseInt(this.uni_initial.getString())
                + Integer.parseInt(this.uni_received.getString())
                <= Integer.parseInt(this.uni_used.getString())
                + Integer.parseInt(this.uni_lost.getString())) {
                ErrorMessage = "stock initial + stock recu ne peut pas etre inferieur stock utilise + stock perdu";
                return false;
            }
            if (Integer.parseInt(this.suc_initial.getString())
                + Integer.parseInt(this.suc_received.getString())
                <= Integer.parseInt(this.suc_used.getString())
                + Integer.parseInt(this.suc_lost.getString())) {
                ErrorMessage = "stock initial + stock recu ne peut pas etre inferieur stock utilise + stock perdu";
                return false;
            }
            if (Integer.parseInt(this.hui_initial.getString())
                + Integer.parseInt(this.hui_received.getString())
                <= Integer.parseInt(this.hui_used.getString())
                + Integer.parseInt(this.hui_lost.getString())) {
                ErrorMessage = "stock initial + stock recu ne peut pas etre inferieur stock utilise + stock perdu";
                return false;
            }
            if (Integer.parseInt(this.mil_initial.getString())
                + Integer.parseInt(this.mil_received.getString())
                <= Integer.parseInt(this.mil_used.getString())
                + Integer.parseInt(this.mil_lost.getString())) {
                ErrorMessage = "stock initial + stock recu ne peut pas etre inferieur stock utilise + stock perdu";
                return false;
            }
            ErrorMessage = "";
            return true;
        } else if ((this.hc_code).equals("URENAM + URENAS")){
            if (Integer.parseInt(this.nie_initial.getString())
                + Integer.parseInt(this.nie_received.getString())
                <= Integer.parseInt(this.nie_used.getString())
                + Integer.parseInt(this.nie_lost.getString())) {
                ErrorMessage = nie_intro.getText() + ": stock initial + stock recu ne peut pas etre inferieur stock utilise + stock perdu";
                return false;
            }
            if (Integer.parseInt(this.csb_initial.getString())
                + Integer.parseInt(this.csb_received.getString())
                <= Integer.parseInt(this.csb_used.getString())
                + Integer.parseInt(this.csb_lost.getString())) {
                ErrorMessage = csb_intro.getText() + ": stock initial + stock recu ne peut pas etre inferieur stock utilise + stock perdu";
                return false;
            }
            if (Integer.parseInt(this.uni_initial.getString())
                + Integer.parseInt(this.uni_received.getString())
                <= Integer.parseInt(this.uni_used.getString())
                + Integer.parseInt(this.uni_lost.getString())) {
                ErrorMessage = uni_intro.getText() + ": stock initial + stock recu ne peut pas etre inferieur stock utilise + stock perdu";
                return false;
            }
            if (Integer.parseInt(this.suc_initial.getString())
                + Integer.parseInt(this.suc_received.getString())
                <= Integer.parseInt(this.suc_used.getString())
                + Integer.parseInt(this.suc_lost.getString())) {
                ErrorMessage = suc_intro.getText() + ": stock initial + stock recu ne peut pas etre inferieur stock utilise + stock perdu";
                return false;
            }
            if (Integer.parseInt(this.hui_initial.getString())
                + Integer.parseInt(this.hui_received.getString())
                <= Integer.parseInt(this.hui_used.getString())
                + Integer.parseInt(this.hui_lost.getString())) {
                ErrorMessage = hui_intro.getText() + ": stock initial + stock recu ne peut pas etre inferieur stock utilise + stock perdu";
                return false;
            }
            if (Integer.parseInt(this.mil_initial.getString())
                + Integer.parseInt(this.mil_received.getString())
                <= Integer.parseInt(this.mil_used.getString())
                + Integer.parseInt(this.mil_lost.getString())) {
                ErrorMessage = mil_intro.getText() + ": stock initial + stock recu ne peut pas etre inferieur stock utilise + stock perdu";
                return false;
            }
            if (Integer.parseInt(this.pln_initial.getString())
                + Integer.parseInt(this.pln_received.getString())
                <= Integer.parseInt(this.pln_used.getString())
                + Integer.parseInt(this.pln_lost.getString())) {
                ErrorMessage = pln_intro.getText() + ": stock initial + stock recu ne peut pas etre inferieur stock utilise + stock perdu";
                return false;
            }
            ErrorMessage = "";
            return true;
        } else if ((this.hc_code).equals("URENAS")) {
            if (Integer.parseInt(this.pln_initial.getString())
                + Integer.parseInt(this.pln_received.getString())
                <= Integer.parseInt(this.pln_used.getString())
                + Integer.parseInt(this.pln_lost.getString())) {
                ErrorMessage = pln_intro.getText() + ": stock initial + stock recu ne peut pas etre inferieur stock utilise + stock perdu";
                return false;
            }
            ErrorMessage = "";
            return true;
        } else if ((this.hc_code).equals("URENI")) {
            if (Integer.parseInt(this.l75_initial.getString())
                + Integer.parseInt(this.l75_received.getString())
                <= Integer.parseInt(this.l75_used.getString())
                + Integer.parseInt(this.l75_lost.getString())) {
                ErrorMessage = l75_intro.getText() + ": stock initial + stock recu ne peut pas etre inferieur stock utilise + stock perdu";
                return false;
            }
            if (Integer.parseInt(this.l100_initial.getString())
                + Integer.parseInt(this.l100_received.getString())
                <= Integer.parseInt(this.l100_used.getString())
                + Integer.parseInt(this.l100_lost.getString())) {
                ErrorMessage = l100_intro.getText() + ": stock initial + stock recu ne peut pas etre inferieur stock utilise + stock perdu";
                return false;
            }
            if (Integer.parseInt(this.pln_initial.getString())
                + Integer.parseInt(this.pln_received.getString())
                <= Integer.parseInt(this.pln_used.getString())
                + Integer.parseInt(this.pln_lost.getString())) {
                ErrorMessage = pln_intro.getText() + ": stock initial + stock recu ne peut pas etre inferieur stock utilise + stock perdu";
                return false;
            }
            ErrorMessage = "";
            return true;
        }
        ErrorMessage = "";
        return true;
    }

    /* Converts Form request to SMS message
     * @return <code>String</code> to be sent by SMS
     */
    public String toSMSFormat() {
        String sep = " ";
        if ((this.hc_code).equals("URENAM")){
            return "nut stock URENAM" + sep
                   + health_center + sep
                   + monthField.getSelectedIndex() + sep
                   + Integer.parseInt(yearField.getString(yearField.getSelectedIndex())) + sep +"#nie" + sep
                   + nie_initial.getString() + sep
                   + nie_received.getString() + sep
                   + nie_used.getString() + sep
                   + nie_lost.getString() + sep +"#csb" + sep

                   + csb_initial.getString() + sep
                   + csb_received.getString() + sep
                   + csb_used.getString() + sep
                   + csb_lost.getString() + sep +"#uni" + sep

                   + uni_initial.getString() + sep
                   + uni_received.getString() + sep
                   + uni_used.getString() + sep
                   + uni_lost.getString() + sep +"#hui" + sep

                   + hui_initial.getString() + sep
                   + hui_received.getString() + sep
                   + hui_used.getString() + sep
                   + hui_lost.getString() + sep +"#suc" + sep

                   + suc_initial.getString() + sep
                   + suc_received.getString() + sep
                   + suc_used.getString() + sep
                   + suc_lost.getString() + sep +"#mil" + sep

                   + mil_initial.getString() + sep
                   + mil_received.getString() + sep
                   + mil_used.getString() + sep
                   + mil_lost.getString();
        } else if ((this.hc_code).equals("URENAM + URENAS")){
            return "nut stock URENAMURENAS" + sep
                + health_center + sep
                + monthField.getSelectedIndex() + sep
                + Integer.parseInt(yearField.getString(yearField.getSelectedIndex())) + sep +"#nie" + sep
                + nie_initial.getString() + sep
                + nie_received.getString() + sep
                + nie_used.getString() + sep
                + nie_lost.getString() + sep + "#csb" + sep

                + csb_initial.getString() + sep
                + csb_received.getString() + sep
                + csb_used.getString() + sep
                + csb_lost.getString() + sep + "#uni" + sep

                + uni_initial.getString() + sep
                + uni_received.getString() + sep
                + uni_used.getString() + sep
                + uni_lost.getString() + sep + "#hui" + sep

                + hui_initial.getString() + sep
                + hui_received.getString() + sep
                + hui_used.getString() + sep
                + hui_lost.getString() + sep + "#suc" + sep

                + suc_initial.getString() + sep
                + suc_received.getString() + sep
                + suc_used.getString() + sep
                + suc_lost.getString() + sep + "#mil" + sep

                + mil_initial.getString() + sep
                + mil_received.getString() + sep
                + mil_used.getString() + sep
                + mil_lost.getString() + sep + "#pln" + sep

                + pln_initial.getString() + sep
                + pln_received.getString() + sep
                + pln_used.getString() + sep
                + pln_lost.getString();
        } else if ((this.hc_code).endsWith("URENAS")){
            return "nut stock URENAS" + sep
                + health_center + sep
                + monthField.getSelectedIndex() + sep
                + Integer.parseInt(yearField.getString(yearField.getSelectedIndex())) + sep + "#pln" + sep
                + pln_initial.getString() + sep
                + pln_received.getString() + sep
                + pln_used.getString() + sep
                + pln_lost.getString();
        } else if ((this.hc_code).equals("URENI")){
            return "nut stock URENI" + sep
                + health_center + sep
                + monthField.getSelectedIndex() + sep
                + Integer.parseInt(yearField.getString(yearField.getSelectedIndex())) + sep + "#l75" + sep
                + l75_initial.getString() + sep
                + l75_received.getString() + sep
                + l75_used.getString() + sep
                + l75_lost.getString() + sep + "#l100" + sep

                + l100_initial.getString() + sep
                + l100_received.getString() + sep
                + l100_used.getString() + sep
                + l100_lost.getString() + sep + "#pln" + sep

                + pln_initial.getString() + sep
                + pln_received.getString() + sep
                + pln_used.getString() + sep
                + pln_lost.getString();
        } return "nut stock";
        
    }

    public void commandAction(Command c, Displayable d) {
        // help command displays Help Form.
        if (c == CMD_HELP) {
            HelpForm h = new HelpForm(this.midlet, this, "stock");
            this.midlet.display.setCurrent(h);
        }

        // exit commands comes back to main menu.
        if (c == CMD_EXIT) {
            this.midlet.display.setCurrent(this.midlet.mainMenu);
        }

        // save command
        if (c == CMD_SAVE) {

            Alert alert;

            // check whether all fields have been completed
            // if not, we alert and don't do anything else.
            if (!this.isComplete()) {
                alert = new Alert("Données manquantes", "Tous les champs doivent être remplis!", null, AlertType.ERROR);
                alert.setTimeout(Alert.FOREVER);
                this.midlet.display.setCurrent (alert, this);
                return;
            }

            // check for errors and display first error
            if (!this.isValid()) {
                alert = new Alert("Données incorrectes!", this.ErrorMessage, null, AlertType.ERROR);
                alert.setTimeout(Alert.FOREVER);
                this.midlet.display.setCurrent (alert, this);
                return;
            }

            // sends the sms and reply feedback
            SMSSender sms = new SMSSender();
            String number = config.get("server_number");
            if (sms.send(number, this.toSMSFormat())) {
                alert = new Alert ("Demande envoyée !", "Vous allez recevoir une confirmation du serveur.", null, AlertType.CONFIRMATION);
                this.midlet.display.setCurrent (alert, this.midlet.mainMenu);
            } else {
                alert = new Alert ("Échec d'envoi SMS", "Impossible d'envoyer la demande par SMS.", null, AlertType.WARNING);
                this.midlet.display.setCurrent (alert, this);
            }
        }
    }
}
