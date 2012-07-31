
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
    private static final String[] yearList = {" --- ", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020"};

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

    Hashtable mam_inputs = new Hashtable();
    mam_inputs.put("nie", "Niebe");
    mam_inputs.put("csb", "CSB");
    mam_inputs.put("uni", "Unimix");
    mam_inputs.put("hui", "Huile");
    mam_inputs.put("suc", "Sucre");
    mam_inputs.put("mil", "Mil");
    mam_inputs.put("auf", "Autre Farine");

    Hashtable sam_comp_inputs = new Hashtable();
    sam_comp_inputs.put("l75", "Lait F75");
    sam_comp_inputs.put("l100", "Lait F100");
    sam_comp_inputs.put("pln", "Plumpy Nut");
    sam_comp_inputs.put("auf", "Autre Farine");

    Hashtable sam_inputs = new Hashtable();
    sam_inputs.put("pln", "Plumpy Nut");

    inputs = new Hashtable();
    if (this.hc_code.equals("URENAM") || this.hc_code.equals("URENAM+URENAS")) {
        inputs.put("mam", mam_inputs);
    }
    if (this.hc_code.equals("URENAS") || this.hc_code.equals("URENAM+URENAS")) {
        inputs.put("sam", sam_inputs);
    }
    if (this.hc_code.equals("URENI") || this.hc_code.equals("URENI²+URENAS")) {
        inputs.put("sam_comp", sam_comp_inputs);
    }

    // store reference to fields tables
    inputs_fields = new Hashtable();

    for(Enumeration cap = inputs.keys(); cap.hasMoreElements();) {
        String mcap = (String)cap.nextElement();

        Hashtable cap_fields = new Hashtable();

        Hashtable inputs_table = (Hashtable)inputs.get(mcap);
        for(Enumeration input = inputs_table.keys(); input.hasMoreElements();) {

            Hashtable indiv_fields = new Hashtable();

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

            indiv_fields.put("code", input_code);
            indiv_fields.put("name", input_name);
            indiv_fields.put("initial", initial);
            indiv_fields.put("received", received);
            indiv_fields.put("used", used);
            indiv_fields.put("lost", lost);

            cap_fields.put(input_code, indiv_fields);
        }

        inputs_fields.put(mcap, cap_fields);
    }

    addCommand(CMD_EXIT);
    addCommand(CMD_SAVE);
    addCommand(CMD_HELP);
    this.setCommandListener (this);
}

    /*
     * Whether all required fields are filled
     * @return <code>true</code> is all fields are filled
     * <code>false</code> otherwise.
     */
    public boolean isComplete() {

        if (monthField.getSelectedIndex() == 0   ||
                    yearField.getSelectedIndex() == 0) {
            return false;
        }

        for(Enumeration cap = inputs_fields.keys(); cap.hasMoreElements();) {
            String mcap = (String)cap.nextElement();

            // table containing fields for that MAM/SAM
            Hashtable fields_table = (Hashtable)inputs_fields.get(mcap);
            // table containing inputs/name for that MAM/SAM

            for(Enumeration input = fields_table.keys(); input.hasMoreElements();) {
                String input_code = (String)input.nextElement();

                Hashtable indiv_fields = (Hashtable)fields_table.get(input_code);

                TextField initial = (TextField)indiv_fields.get("initial");
                TextField received = (TextField)indiv_fields.get("received");
                TextField used = (TextField)indiv_fields.get("used");
                TextField lost = (TextField)indiv_fields.get("lost");

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

            for(Enumeration input = fields_table.keys(); input.hasMoreElements();) {
                String input_code = (String)input.nextElement();

                Hashtable indiv_fields = (Hashtable)fields_table.get(input_code);

                String input_name = (String)indiv_fields.get("name");

                TextField initial = (TextField)indiv_fields.get("initial");
                TextField received = (TextField)indiv_fields.get("received");
                TextField used = (TextField)indiv_fields.get("used");
                TextField lost = (TextField)indiv_fields.get("lost");

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
        System.out.println("toSMSFormat");
        String sep = " ";
        String msg = "nut stock" + sep + this.hc_code + sep + this.health_center + sep
                     + monthField.getSelectedIndex() + sep
                     + Integer.parseInt(yearField.getString(yearField.getSelectedIndex())) + sep;

        for(Enumeration cap = inputs_fields.keys(); cap.hasMoreElements();) {
            String mcap = (String)cap.nextElement();

            // table containing fields for that MAM/SAM
            Hashtable fields_table = (Hashtable)inputs_fields.get(mcap);

            for(Enumeration input = fields_table.keys(); input.hasMoreElements();) {
                String input_code = (String)input.nextElement();

                Hashtable indiv_fields = (Hashtable)fields_table.get(input_code);

                TextField initial = (TextField)indiv_fields.get("initial");
                TextField received = (TextField)indiv_fields.get("received");
                TextField used = (TextField)indiv_fields.get("used");
                TextField lost = (TextField)indiv_fields.get("lost");
                msg += "#" + input_code + sep + initial.getString() + sep + received.getString() + sep
                           + used.getString() + sep + lost.getString() + sep;
            }
        }
        return msg;

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
