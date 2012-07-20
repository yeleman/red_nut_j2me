
package nut;

import javax.microedition.lcdui.*;
import nut.Configuration.*;
import java.util.TimeZone;
import java.util.Date;
import nut.Constants.*;
import nut.HelpForm.*;

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

    public NUTMIDlet midlet;

    private DateField date_disable;

    private Configuration config;
    private static final String[] reason = {"ABANDON", "TRANSFER",
                                            "GUERISON","NON-REPONDANT",
                                            "DECES"};
    private ChoiceGroup reasonField;
    private TextField id_patient;
    private String ErrorMessage = "";


public DisableForm(NUTMIDlet midlet) {
    super("Sortie");
    this.midlet = midlet;

    config = new Configuration();

    // creating al fields (blank)
    id_patient =  new TextField("ID:", null, 4, TextField.DECIMAL);
    reasonField =  new ChoiceGroup("Raison:", ChoiceGroup.POPUP, reason, null);
    date_disable =  new DateField("Date de sortie:", DateField.DATE, TimeZone.getTimeZone("GMT"));

    date_disable.setDate(new Date());

    // add fields to forms
    append(id_patient);
    append(reasonField);
    append(date_disable);

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
        if (id_patient.getString().length() == 0) {
            return false;
        }
        return true;
    }
    public boolean isValid() {

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
        if ((this.reasonField.getString(reasonField.getSelectedIndex())).equals("ABANDON")){
            rea = "a";
        } else if ((this.reasonField.getString(reasonField.getSelectedIndex())).equals("TRANSFER")){
            rea = "t";
        } else if ((this.reasonField.getString(reasonField.getSelectedIndex())).equals("GUERISON")){
            rea = "h";
        } else if ((this.reasonField.getString(reasonField.getSelectedIndex())).equals("NON-REPONDANT")){
            rea = "n";
        } else if ((this.reasonField.getString(reasonField.getSelectedIndex())).equals("DECES")){
            rea = "d";
        }

        int date_array[] = SharedChecks.formatDateString(date_disable.getDate());
        String disable_d = String.valueOf(date_array[2])
                         + SharedChecks.addzero(date_array[1])
                         + SharedChecks.addzero(date_array[0]);

        return "nut off" + sep + id_patient.getString()
                         + sep + rea
                         + sep + disable_d;
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