
package nut;

import javax.microedition.lcdui.*;
import nut.Configuration.*;
import java.util.TimeZone;
import java.util.Date;
import nut.Constants.*;
import nut.HelpForm.*;
import nut.SharedChecks.*;

/**
 * J2ME Patient DisableForm Form
 * Displays Disable fields
 * Checks completeness
 * Sends as SMS
 * @author Fadiga
 */

public class DisableForm extends Form implements CommandListener {

    private static final Command CMD_EXIT = new Command ("Retour", Command.BACK, 1);
    private static final Command CMD_SAVE = new Command ("Envoi.", Command.OK, 1);
    private static final Command CMD_HELP = new Command ("Aide", Command.HELP, 2);
    private static final int MAX_SIZE = 5; // max no. of chars per field.

    public NUTMIDlet midlet;

    private Configuration config;

    private String ErrorMessage = "";
    private String health_center = "";

    private static final String[] reason = {"ABANDON", "TRANSFERT", "REFERENCE",
                                            "GUERISON","NON-REPONDANT", "DECES"};
    private static final String[] typeurenlist = {"URENAS", "URENI"};

    private DateField date_disable;
    private TextField id_patientfield;
    private ChoiceGroup type_urenfield;
    private TextField weightfield;
    private TextField heightfield;
    private TextField pbfield;
    private ChoiceGroup reasonfield;


    public DisableForm(NUTMIDlet midlet) {
        super("Sortie");
        this.midlet = midlet;

        config = new Configuration();
        health_center = config.get("health_center");

        // creating al fields (blank)
        id_patientfield =  new TextField("ID:", null, 4, TextField.DECIMAL);
        type_urenfield = new ChoiceGroup("Type UREN:", ChoiceGroup.POPUP, typeurenlist, null);
        reasonfield =  new ChoiceGroup("Raison:", ChoiceGroup.POPUP, reason, null);
        date_disable =  new DateField("Date de sortie:", DateField.DATE, TimeZone.getTimeZone("GMT"));
        weightfield =  new TextField("Poids (en kg):", null, MAX_SIZE, TextField.DECIMAL);
        heightfield =  new TextField("Taille (en cm):", null, MAX_SIZE, TextField.DECIMAL);
        // oedemaField =  new ChoiceGroup("Oedème:", ChoiceGroup.POPUP, oedema, null);
        pbfield =  new TextField("Périmètre brachial (en mm):", null, MAX_SIZE, TextField.DECIMAL);

        date_disable.setDate(new Date());

        // add fields to forms
        append(date_disable);
        append(type_urenfield);
        append(id_patientfield);
        append(reasonfield);
        append(weightfield);
        append(heightfield);
        append(pbfield);

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
        // all fields are required to be filled.
        if (id_patientfield.getString().length() == 0) {
            return false;
        }
        if ((this.reasonfield.getString(reasonfield.getSelectedIndex())).equals("GUERISON")) {
            if (!SharedChecks.isComplete(weightfield, heightfield, pbfield)){
             return false;
             }
        }
        return true;
    }
    public boolean isValid() {
        if ((this.reasonfield.getString(reasonfield.getSelectedIndex())).equals("GUERISON")) {
            ErrorMessage = SharedChecks.Message(weightfield, heightfield, pbfield);
        }
        if (!ErrorMessage.equals("")){
           return false;
        }
        if (SharedChecks.isDateValide(date_disable.getDate()) != true) {
            ErrorMessage = "[Date de sortie] La date indiquée est dans le futur.";
            return false;
        }

        return true;
    }

    /* Converts Form request to SMS message
     * @return <code>String</code> to be sent by SMS
     */
    public String toSMSFormat() {
        String sep = " ";
        String rea = " ";
        String uren = " ";
        String weight = weightfield.getString();
        String height = heightfield.getString();
        String pb = pbfield.getString();

        if ((this.reasonfield.getString(reasonfield.getSelectedIndex())).equals("ABANDON")){
            rea = "a";
        } else if ((this.reasonfield.getString(reasonfield.getSelectedIndex())).equals("TRANSFERT")){
            rea = "t";
        } else if ((this.reasonfield.getString(reasonfield.getSelectedIndex())).equals("GUERISON")){
            rea = "h";
        } else if ((this.reasonfield.getString(reasonfield.getSelectedIndex())).equals("NON-REPONDANT")){
            rea = "n";
        } else if ((this.reasonfield.getString(reasonfield.getSelectedIndex())).equals("DECES")){
            rea = "d";
        } else if ((this.reasonfield.getString(reasonfield.getSelectedIndex())).equals("REFERENCE")){
            rea = "r";
        }

        if (type_urenfield.getString(type_urenfield.getSelectedIndex()).equals("URENAS")){
            uren = "sam";
        // } else if (type_urenfield.getString(type_urenfield.getSelectedIndex()).equals("URENAM")){
        //     uren = "mam";
        }else if (type_urenfield.getString(type_urenfield.getSelectedIndex()).equals("URENI")){
            uren = "samp";
        }

        if (!(this.reasonfield.getString(reasonfield.getSelectedIndex())).equals("GUERISON")) {
            weight = "-";
            height = "-";
            pb = "-";
        }
        int date_array[] = SharedChecks.formatDateString(date_disable.getDate());
        String disable_d =  SharedChecks.addzero(date_array[2])
                            + SharedChecks.addzero(date_array[1])
                            + SharedChecks.addzero(date_array[0]);

        return "nut off" + sep + health_center
                         + sep + disable_d
                         + sep + uren
                         + sep + id_patientfield.getString()
                         + sep + weight
                         + sep + height
                         + sep + pb
                         + sep + rea;
    }

    public void commandAction(Command c, Displayable d) {
        // help command displays Help Form.
        if (c == CMD_HELP) {
            HelpForm h = new HelpForm(this.midlet, this, "Disable");
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
                alert = new Alert("Données manquantes", "Le champ ID du patient doit être remplie!", null, AlertType.ERROR);
                alert.setTimeout(Alert.FOREVER);
                this.midlet.display.setCurrent (alert, this);
                return;
            }

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
                alert = new Alert ("Demande envoyée !", "Vous allez recevoir une confirmation du serveur.", null, AlertType.CONFIRMATION);
                this.midlet.display.setCurrent (alert, this.midlet.mainMenu);
            } else {
                alert = new Alert ("Échec d'envoi SMS", "Impossible d'envoyer la demande par SMS.", null, AlertType.WARNING);
                this.midlet.display.setCurrent (alert, this);
            }
        }
    }
}