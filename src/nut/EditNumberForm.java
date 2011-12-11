
package nut;

import javax.microedition.lcdui.*;
import nut.Configuration.*;
import nut.Constants.*;
import nut.HelpForm.*;

/**
 * J2ME Form allowing Server number editing.
 * Saves the new number into <code>Configuration</code>
 * @author rgaudin
 */
public class EditNumberForm extends Form implements CommandListener {

    private static final Command CMD_EXIT = new Command ("Retour", Command.BACK, 1);
    private static final Command CMD_SAVE = new Command ("Enreg.", Command.OK, 1);
    private static final Command CMD_HELP = new Command ("Aide", Command.HELP, 2);
    
    private Configuration config;

    private TextField numberField;
    NUTMIDlet midlet;

public EditNumberForm(NUTMIDlet midlet) {
    super("Paramètres transmission");
    this.midlet = midlet;
    
    config = new Configuration();

    // retrieve phone number from config
    // if not present, use constant
    String phone_number = "";
    phone_number = config.get("server_number");
    if (phone_number.equals("")) {
        phone_number = Constants.server_number;
    }

    numberField = new TextField ("Numéro du serveur", phone_number, 8, TextField.PHONENUMBER);
    append(numberField);
    addCommand(CMD_EXIT);
    addCommand(CMD_SAVE);
    addCommand(CMD_HELP);
    this.setCommandListener (this);
  }

    public void commandAction(Command c, Displayable d) {
        // Help command displays Help Form
         if (c == CMD_HELP) {
            HelpForm h = new HelpForm(this.midlet, this, "edit_number");
            this.midlet.display.setCurrent(h);
        }

        // exit command goes back to Main Menu
        if (c == CMD_EXIT) {
            this.midlet.display.setCurrent(this.midlet.mainMenu);
        }

        // save command stores new number in config or display errors.
        if (c == CMD_SAVE) {
            Alert alert;
            if (config.set("server_number", numberField.getString())) {
                String saved = config.get("server_number");
                alert = new Alert ("Succès !", "Le nouveau numéro a été enregistré: " + saved, null, AlertType.CONFIRMATION);
            } else {
                alert = new Alert ("FAIL!", "fail to save", null, AlertType.WARNING);
                alert.setTimeout(Alert.FOREVER);
            }
            this.midlet.display.setCurrent (alert, this);
        }
    }
}
