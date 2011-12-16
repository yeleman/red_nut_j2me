
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
public class RegisterForm extends Form implements CommandListener {

    private static final Command CMD_EXIT = new Command ("Retour", Command.BACK, 1);
    private static final Command CMD_SAVE = new Command ("Envoi.", Command.OK, 1);
    private static final Command CMD_HELP = new Command ("Aide", Command.HELP, 2);
    private static final int MAX_SIZE = 3; // max no. of chars per field.

    public NUTMIDlet midlet;

    private Configuration config;

    private String health_center = "";

    private static final String[] ageList= {" --- ", "jours", "mois", "ans"};
    private String ErrorMessage = "";

    private TextField first_name;
    private TextField last_name;
    private ChoiceGroup type_age;
    private TextField age;
    private TextField mother_name;

public RegisterForm(NUTMIDlet midlet) {
    super("Enregistrement");
    this.midlet = midlet;

    config = new Configuration();

    health_center = config.get("health_center");
    
    // creating al fields (blank)
    first_name =  new TextField("Prenom:", null, 20, TextField.ANY);
    last_name =  new TextField("Nom:", null, 20, TextField.ANY);
    mother_name =  new TextField("Nom de la mere:", null, 20, TextField.ANY);
    type_age = new ChoiceGroup("Age en:", ChoiceGroup.POPUP, ageList, null);
    age =  new TextField("Age:", null, MAX_SIZE, TextField.NUMERIC);

    // add fields to forms
    append(first_name);
    append(last_name);
    append(mother_name);
    append(type_age);
    append(age);

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
        if (first_name.getString().length() == 0 ||
            last_name.getString().length() == 0 ||
            mother_name.getString().length() == 0 ||
            age.getString().length() == 0) {
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
        if (first_name.getString().equals("reg")) {
            ErrorMessage = "Ala kele ye!";
            return false;
        }
        ErrorMessage = "";
        return true;
    }

    /* Converts Form request to SMS message
     * @return <code>String</code> to be sent by SMS
     */
    public String toSMSFormat() {
        String sep = " ";
        return "nut register" + sep
                + health_center + sep
                + first_name.getString() + sep
                + last_name.getString() + sep
                + mother_name.getString() + sep
                + age.getString() + type_age.getString(type_age.getSelectedIndex());
    }

    public void commandAction(Command c, Displayable d) {
        // help command displays Help Form.
        if (c == CMD_HELP) {
            HelpForm h = new HelpForm(this.midlet, this, "registration");
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
