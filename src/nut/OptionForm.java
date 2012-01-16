
package nut;

import javax.microedition.lcdui.*;
import nut.Configuration.*;
import nut.Constants.*;
import nut.HelpForm.*;

/**
 * J2ME Form allowing Server number, health center and hc_code editing.
 * Saves the new number into <code>Configuration</code>
 * Saves the new health center into <code>Configuration</code>
 * Saves the new hc_code into <code>Configuration</code>
 * @author fadiga
 */
public class OptionForm extends Form implements CommandListener {

    private static final Command CMD_EXIT = new Command ("Retour", Command.BACK, 1);
    private static final Command CMD_SAVE = new Command ("Enreg.", Command.OK, 1);
    private static final Command CMD_HELP = new Command ("Aide", Command.HELP, 2);
    
    private Configuration config;
    private static final String[] type_center = {"URENI", "URENAM", "URENAS", "URENAM + URENAS"};
    private TextField numberField;
    private TextField health_centerField;
    private ChoiceGroup hc_codeField;
    NUTMIDlet midlet;

public OptionForm(NUTMIDlet midlet) {
    super("Paramètres de transmission");
    this.midlet = midlet;
    
    config = new Configuration();

    // retrieve phone number from config
    // if not present, use constant
    String phone_number = "";
    phone_number = config.get("server_number");
    if (phone_number.equals("")) {
        phone_number = Constants.server_number;
    }

    numberField = new TextField ("Numéro du serveur:", phone_number, 8, TextField.PHONENUMBER);
    health_centerField = new TextField ("Code du centre:", config.get("health_center"), 10, TextField.ANY);
    hc_codeField =  new ChoiceGroup("Type CSCOM:", ChoiceGroup.POPUP, type_center, null);
    int sel = 0;
    for (int i = 0; i<type_center.length ; i++) {
        if (type_center[i].equals(config.get("hc_code"))) {
            sel = i;
            break;
        }
    }
    hc_codeField.setSelectedIndex(sel, true);
    append(numberField);
    append(health_centerField);
    append(hc_codeField);
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
        if (numberField.getString().length() == 0 ||
            health_centerField.getString().length() == 0) {
            return false;
        }
        return true;
    }

    public void commandAction(Command c, Displayable d) {
        // Help command displays Help Form
         if (c == CMD_HELP) {
            HelpForm h = new HelpForm(this.midlet, this, "option");
            this.midlet.display.setCurrent(h);
        }

        // exit command goes back to Main Menu
        if (c == CMD_EXIT) {
            this.midlet.display.setCurrent(this.midlet.mainMenu);
        }

        // save command stores new number in config or display errors.
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
            if (config.set("server_number", numberField.getString()) &&
            config.set("health_center", health_centerField.getString()) &&
            config.set("hc_code", hc_codeField.getString(hc_codeField.getSelectedIndex()))) {
                alert = new Alert ("Confirmation!", "Votre modification a été bien enregistré.", null, AlertType.CONFIRMATION);
                this.midlet.display.setCurrent (alert, this.midlet.mainMenu);
            } else {
                alert = new Alert ("Échec", "Impossible d'enregistrer cette modification.", null, AlertType.WARNING);
                this.midlet.display.setCurrent (alert, this);
            }
        }
    }
}
