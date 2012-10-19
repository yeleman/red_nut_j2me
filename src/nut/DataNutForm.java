
package nut;

import javax.microedition.lcdui.*;
import java.util.TimeZone;
import java.util.Date;
import nut.Configuration.*;
import nut.Constants.*;
import nut.HelpForm.*;
import nut.SharedChecks.*;

/**
 * J2ME Patient NutritionalData Form
 * Displays NutritionalData fields
 * Checks completeness
 * Sends as SMS
 * @author alou
 */

public class DataNutForm extends Form implements CommandListener {

    private static final Command CMD_EXIT = new Command ("Retour", Command.BACK, 1);
    private static final Command CMD_SAVE = new Command ("Envoi.", Command.OK, 1);
    private static final Command CMD_HELP = new Command ("Aide", Command.HELP, 2);
    private static final int MAX_SIZE = 5; // max no. of chars per field.

    public NUTMIDlet midlet;

    private Configuration config;

    private String ErrorMessage = "";
    private String health_center = "";
    private String hc_code = "";

    private static final String[] oedema = {"NON", "OUI", "Inconnue"};
    private static final String[] typeurenlist = {"URENAS", "URENI"};
    private static final String[] isureni = {"MAS", "MAS+"};

    private ChoiceGroup oedemaField;
    private ChoiceGroup isurenifield;

    private DateField create_datefield;
    private TextField id_patientfield;
    private ChoiceGroup type_urenfield;
    private TextField weightfield;
    private TextField heightfield;
    private TextField pbfield;
    private TextField nbr_plufield;

    public DataNutForm(NUTMIDlet midlet) {
        super("Suivi nutritionnel");
        this.midlet = midlet;

        config = new Configuration();
        health_center = config.get("health_center");
        hc_code = config.get("hc_code");

        // creating all fields (blank)
        create_datefield = new DateField("Date de visite:", DateField.DATE, TimeZone.getTimeZone("GMT"));
        id_patientfield = new TextField("ID:", null, 10, TextField.DECIMAL);
        type_urenfield = new ChoiceGroup("Type UREN:", ChoiceGroup.POPUP, typeurenlist, null);
        weightfield = new TextField("Poids (en kg):", null, MAX_SIZE, TextField.DECIMAL);
        heightfield = new TextField("Taille (en cm):", null, MAX_SIZE, TextField.DECIMAL);
        oedemaField = new ChoiceGroup("Oedème:", ChoiceGroup.POPUP, oedema, null);
        pbfield = new TextField("Périmètre brachial (en mm):", null, MAX_SIZE, TextField.DECIMAL);
        nbr_plufield = new TextField("Sachets plumpy nut donnés:", null, MAX_SIZE, TextField.NUMERIC);
        isurenifield = new ChoiceGroup("MAS/MAS+:", ChoiceGroup.POPUP, isureni, null);

        create_datefield.setDate(new Date());

        // add fields to form
        append(create_datefield);
        append(type_urenfield);
        append(id_patientfield);
        append(weightfield);
        append(heightfield);
        append(oedemaField);
        append(pbfield);
        if (!this.hc_code.equals("URENAS")) {
            append(isurenifield);
        }
        append(nbr_plufield);

        // add command to form
        addCommand(CMD_HELP);
        addCommand(CMD_SAVE);
        addCommand(CMD_EXIT);
        this.setCommandListener (this);
    }

    /*
     * Whether all required fields are filled
     * @return <code>true</code> is all fields are filled
     * <code>false</code> otherwise.
     */
    public boolean isComplete() {
        // all fields are required to be filled.
        //SharedChecks checks = new SharedChecks();
        if (id_patientfield.getString().length() == 0 || !SharedChecks.isComplete(weightfield, heightfield, pbfield)) {
            return false;
        }
        return true;
    }

    /*
     * Whether all filled data is correct
     * @return <code>true</code> if all fields are OK
     * <code>false</code> otherwise.
     */
     public boolean isValid() {
        ErrorMessage = SharedChecks.Message(weightfield, heightfield, pbfield);
        if (!ErrorMessage.equals("")){
           return false;
        }
        if (SharedChecks.isDateValide(create_datefield.getDate()) != true) {
            ErrorMessage = "La date indiquée est dans le futur.";
            return false;
        }
        return true;
    }

    /* Converts Form request to SMS message
     * @return <code>String</code> to be sent by SMS
     */
    public String toSMSFormat() {
        String sep = " ";
        String oed = " ";
        String nbrplu = " ";
        String uren = " ";

        if (oedemaField.getString(oedemaField.getSelectedIndex()).equals("OUI")){
            oed = "YES";
        } else if (oedemaField.getString(oedemaField.getSelectedIndex()).equals("NON")){
            oed = "NO";
        }else if (oedemaField.getString(oedemaField.getSelectedIndex()).equals("Inconnue")){
            oed = "Unknown";
        }
        if (type_urenfield.getString(type_urenfield.getSelectedIndex()).equals("URENAS")){
            uren = "sam";
        // } else if (type_urenfield.getString(type_urenfield.getSelectedIndex()).equals("URENAM")){
        //     uren = "mas";
        }else if (type_urenfield.getString(type_urenfield.getSelectedIndex()).equals("URENI")){
            uren = "samp";
        }
        if (nbr_plufield.getString().length() == 0) {
            nbrplu = "-";
        } else {
            nbrplu = nbr_plufield.getString();
        }

        int reporting_date_array[] = SharedChecks.formatDateString(create_datefield.getDate());
        String reporting_d = String.valueOf(reporting_date_array[2])
                              + SharedChecks.addzero(reporting_date_array[1])
                              + SharedChecks.addzero(reporting_date_array[0]);

        return "nut fol" + sep + health_center
                         + sep + reporting_d
                         + sep + uren
                         + sep + id_patientfield.getString()
                         + sep + weightfield.getString()
                         + sep + heightfield.getString()
                         + sep + oed
                         + sep + pbfield.getString()
                         + sep + nbrplu
                         + sep + isurenifield.getSelectedIndex();
    }

    public void commandAction(Command c, Displayable d) {
        // help command displays Help Form.
        if (c == CMD_HELP) {
            HelpForm h = new HelpForm(this.midlet, this, "datanut");
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
                alert = new Alert("Données manquantes", "Tous les champs " +
                        "requis doivent être remplis!", null, AlertType.ERROR);
                alert.setTimeout(Alert.FOREVER);
                this.midlet.display.setCurrent (alert, this);
                return;
            }

            // check for errors and display first error
            if (!this.isValid()) {
                alert = new Alert("Données incorrectes!", this.ErrorMessage,
                        null, AlertType.ERROR);
                alert.setTimeout(Alert.FOREVER);
                this.midlet.display.setCurrent (alert, this);
                return;
            }

            // sends the sms and reply feedback
            SMSSender sms = new SMSSender();
            String number = config.get("server_number");

            if (sms.send(number, this.toSMSFormat())) {
                alert = new Alert ("Demande envoyée !", "Vous allez " +
                        "recevoir une confirmation du serveur.", null,
                        AlertType.CONFIRMATION);
                this.midlet.display.setCurrent (alert, this.midlet.mainMenu);
            } else {
                alert = new Alert ("Échec d'envoi SMS", "Impossible " +
                        "d'envoyer la demande par SMS.", null,
                        AlertType.WARNING);
                this.midlet.display.setCurrent (alert, this);
            }
        }
    }
}
