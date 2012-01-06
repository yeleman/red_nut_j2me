
package nut;

import javax.microedition.lcdui.*;
import nut.Configuration.*;
import nut.Constants.*;
import nut.HelpForm.*;

/**
 * J2ME Patient ResearchForm Form
 * Displays Research fields
 * Checks completeness
 * Sends as SMS
 * @author Fadiga
 */
public class ResearchForm extends Form implements CommandListener {

    private static final Command CMD_EXIT = new Command ("Retour", Command.BACK, 1);
    private static final Command CMD_SAVE = new Command ("Envoi.", Command.OK, 1);
    private static final Command CMD_HELP = new Command ("Aide", Command.HELP, 2);

    public NUTMIDlet midlet;

    private Configuration config;

    private TextField first_name;
    private TextField last_name;
    private TextField surname_mother;

public ResearchForm(NUTMIDlet midlet) {
    super("Recherche d'id");
    this.midlet = midlet;

    config = new Configuration();

    // creating al fields (blank)
    first_name =  new TextField("Prenom", null, 20, TextField.ANY);
    last_name =  new TextField("Nom", null, 20, TextField.ANY);
    surname_mother =  new TextField("Nom de la mère", null, 20, TextField.ANY);


    // add fields to forms
    append(first_name);
    append(last_name);
    append(surname_mother);

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
        if (first_name.getString().length() == 0 &&
            last_name.getString().length() == 0 &&
            surname_mother.getString().length() == 0) {
            return false;
        }
        return true;
    }


    /* Converts Form request to SMS message
     * @return <code>String</code> to be sent by SMS
     */
    private String firstname;
    private String lastname;
    private String surnamemother;

    public String toSMSFormat() {
        String sep = " ";
        String None = "n";

        if (first_name.getString().length() == 0){
            firstname = None;
        } else{
            firstname = first_name.getString();
          }
        if (last_name.getString().length() == 0){
            lastname = None;
        } else{
            lastname = last_name.getString();
          }
        if (surname_mother.getString().length() == 0){
            surnamemother = None;
        } else{
            surnamemother = surname_mother.getString();
          }
        return "nut research" + sep + config.get("health_center")
                    + sep + firstname + sep + lastname + sep + surnamemother;
    }

    public void commandAction(Command c, Displayable d) {
        // help command displays Help Form.
        if (c == CMD_HELP) {
            HelpForm h = new HelpForm(this.midlet, this, "research");
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
