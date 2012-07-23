
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

    public NUTMIDlet midlet;

    private String ErrorMessage = "";
    private String health_center = "";

    private Configuration config;
    private static final String[] reason = {"ABANDON", "TRANSFER",
                                            "GUERISON","NON-REPONDANT",
                                            "DECES"};
    private static final String[] typeurenlist = {"URENAS", "URENI", "URENAM"};
    private DateField date_disable;
    private TextField id_patient;
    private ChoiceGroup type_uren;
    private ChoiceGroup reasonField;


public DisableForm(NUTMIDlet midlet) {
    super("Sortie");
    this.midlet = midlet;

    config = new Configuration();
    health_center = config.get("health_center");

    // creating al fields (blank)
    id_patient =  new TextField("ID:", null, 4, TextField.DECIMAL);
    type_uren = new ChoiceGroup("Type UREN:", ChoiceGroup.POPUP, typeurenlist, null);
    reasonField =  new ChoiceGroup("Raison:", ChoiceGroup.POPUP, reason, null);
    date_disable =  new DateField("Date de sortie:", DateField.DATE, TimeZone.getTimeZone("GMT"));

    date_disable.setDate(new Date());

    // add fields to forms
    append(date_disable);
    append(type_uren);
    append(id_patient);
    append(reasonField);

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
        String uren = " ";

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

        if (type_uren.getString(type_uren.getSelectedIndex()).equals("URENAS")){
            uren = "sam";
        } else if (type_uren.getString(type_uren.getSelectedIndex()).equals("URENAM")){
            uren = "mas";
        }else if (type_uren.getString(type_uren.getSelectedIndex()).equals("URENI")){
            uren = "samp";
        }

        int date_array[] = SharedChecks.formatDateString(date_disable.getDate());
        String disable_d =  SharedChecks.addzero(date_array[2])
                            + SharedChecks.addzero(date_array[1])
                            + SharedChecks.addzero(date_array[0]);

        return "nut off" + sep + health_center
                         + sep + disable_d
                         + sep + type_uren.getSelectedIndex() // return O = URENAS, 1 = URENI, URENAM = 2
                         + sep + id_patient.getString()
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