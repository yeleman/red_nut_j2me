
package nut;

import javax.microedition.lcdui.*;
import java.util.Enumeration;
import java.util.Hashtable;
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

    private Hashtable inputs;  // list of list of code/name for inputs per CAP
    private Hashtable inputs_fields; // list of list of field per CAP

public StockForm(NUTMIDlet midlet) {
    super("Consommation Intrants");
    this.midlet = midlet;

    config = new Configuration();

    monthField = new ChoiceGroup("Mois:", ChoiceGroup.POPUP, monthList, null);
    yearField = new ChoiceGroup("Année:", ChoiceGroup.POPUP, yearList, null);
    hc_code = config.get("hc_code");
    health_center = config.get("health_center");
    period_intro = new StringItem(null, "Indiquez la periode");

    // add period
    append(period_intro);
    append(monthField);
    append(yearField);

     // creating all fields (blank)
    //private String[] inputs = {"Niebe", "Mil", "Sucre", "Huile", "Unimux", "CSB", "Lait F100", "Lait F75", "Plumpy Nut"};
    // inputs = {'MAM': [('nieb', "Niebe"), ('mil', "Mil")]}

    Hashtable mam_inputs = new Hashtable();
    mam_inputs.put("niebe", "Niebe");
    mam_inputs.put("mil", "Mil");
    mam_inputs.put("sugar", "Sucre");
    mam_inputs.put("oil", "Huile");

    Hashtable sam_inputs = new Hashtable();
    sam_inputs.put("plumpy", "Plumpy Nut");

    inputs = new Hashtable();
    if (this.hc_code.equals("URENAM") || this.hc_code.equals("URENAM+URENAS")) {
        inputs.put("mam", mam_inputs);    
    }
    if (this.hc_code.equals("URENAS") || this.hc_code.equals("URENAM+URENAS")) {
        inputs.put("sam", sam_inputs);
    }

    // store reference to fields tables
    inputs_fields = new Hashtable();

    for(Enumeration cap = inputs.keys(); cap.hasMoreElements();) {
        String mcap = (String)cap.nextElement();

        Hashtable cap_fields = new Hashtable();

        Hashtable inputs_table = (Hashtable)inputs.get(mcap);
        for(Enumeration input = inputs_table.keys(); input.hasMoreElements();) {
            String input_code = (String)input.nextElement();
            String input_name = (String)inputs_table.get(input_code);

            StringItem intro = new StringItem(null, input_name);
            TextField initial = new TextField("Stock de debut:", null, MAX_SIZE, TextField.DECIMAL);
            TextField received = new TextField("Stock reçu:", null, MAX_SIZE, TextField.DECIMAL);
            TextField used = new TextField("Stock utilisé:", null, MAX_SIZE, TextField.DECIMAL);
            TextField lost = new TextField("Stock perdu:", null, MAX_SIZE, TextField.DECIMAL);

            append(intro);
            append(initial);
            append(received);
            append(used);
            append(lost);
            
            cap_fields.put("code", input_code);
            cap_fields.put("name", input_name);
            cap_fields.put("initial", initial);
            cap_fields.put("received", received);
            cap_fields.put("used", used);
            cap_fields.put("lost", lost);
        }

        inputs_fields.put(mcap, cap_fields);
        
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
        System.out.println(monthField.getSelectedIndex());

        if (monthField.getSelectedIndex() == 0   || 
                    yearField.getSelectedIndex() == 0) {
            return false;
        }

        for(Enumeration cap = inputs_fields.keys(); cap.hasMoreElements();) {
            String mcap = (String)cap.nextElement();

            // table containing fields for that MAM/SAM
            Hashtable fields_table = (Hashtable)inputs_fields.get(mcap);
            // table containing inputs/name for that MAM/SAM
            Hashtable inputs_table = (Hashtable)inputs.get(mcap);

            for(Enumeration input = fields_table.keys(); input.hasMoreElements();) {
                String input_code = (String)input.nextElement();
                String input_name = (String)inputs_table.get(input_code);
                System.out.println(input_name);

                TextField initial = (TextField)fields_table.get("initial");
                TextField received = (TextField)fields_table.get("received");
                TextField used = (TextField)fields_table.get("used");
                TextField lost = (TextField)fields_table.get("lost");

                if (initial.getString().length() == 0 ||
                    received.getString().length() == 0 ||
                    used.getString().length() == 0 ||
                    lost.getString().length() == 0) {
                         return false;
                    }
                    
            } 
        }

        return true;
    }

    /*
     * Whether all filled data is correct
     * @return <code>true</code> if all fields are OK
     * <code>false</code> otherwise.
     */
    public boolean isValid() {

        for(Enumeration cap = inputs_fields.keys(); cap.hasMoreElements();) {
            String mcap = (String)cap.nextElement();

            // table containing fields for that MAM/SAM
            Hashtable fields_table = (Hashtable)inputs_fields.get(mcap);
            // table containing inputs/name for that MAM/SAM
            Hashtable inputs_table = (Hashtable)inputs.get(mcap);

            for(Enumeration input = fields_table.keys(); input.hasMoreElements();) {
                String input_code = (String)fields_table.get("code");
                String input_name = (String)fields_table.get("name");
                System.out.println(input_name);

                TextField initial = (TextField)fields_table.get("initial");
                TextField received = (TextField)fields_table.get("received");
                TextField used = (TextField)fields_table.get("used");
                TextField lost = (TextField)fields_table.get("lost");

                if (Integer.parseInt(initial.getString())
                + Integer.parseInt(received.getString())
                < Integer.parseInt(used.getString())
                + Integer.parseInt(lost.getString())) {
                    ErrorMessage = input_name + ": stock initial + stock reçu ne peut pas etre inferieur stock utilisé + stock perdu";
                    return false;
                }
            }
        }

        ErrorMessage = "";
        return true;
    }

    /* Converts Form request to SMS message
     * @return <code>String</code> to be sent by SMS
     */
    public String toSMSFormat() {
        String sep = " ";
        /*if ((this.hc_code).equals("URENAM")){
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
        } */return "nut stock";
        
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
