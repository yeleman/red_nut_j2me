
package nut;

import javax.microedition.lcdui.*;
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

    private static final String[] oedema = {"OUI", "NON", "Inconnue"};

    private ChoiceGroup oedemaField;

    private TextField id;
    private TextField weight;
    private TextField height;
    private TextField pb;
    private TextField nbr_plu;


    public DataNutForm(NUTMIDlet midlet) {
        super("Suivie nutritionnelle");
        this.midlet = midlet;

        config = new Configuration();

        // creating all fields (blank)
        id =  new TextField("ID:", null, 10, TextField.DECIMAL);
        weight =  new TextField("Poids (en kg):", null,
                MAX_SIZE, TextField.DECIMAL);
        height =  new TextField("Taille (en cm):", null,
                MAX_SIZE, TextField.DECIMAL);
        oedemaField =  new ChoiceGroup("Oedème:", ChoiceGroup.POPUP,
                oedema, null);
        pb =  new TextField("Périmètre brachial (en mm):", null,
                MAX_SIZE, TextField.DECIMAL);
        nbr_plu =  new TextField("Sachets plumpy nut donnés:", null,
                MAX_SIZE, TextField.NUMERIC);


        // add fields to form
        append(id);
        append(weight);
        append(height);
        append(oedemaField);
        append(pb);
        append(nbr_plu);

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
        if (id.getString().length() == 0 || !SharedChecks.isComplete(weight, height, pb)) {
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
           ErrorMessage = SharedChecks.Message(weight, height, pb);
           if (ErrorMessage != ""){
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
        String nbr = " ";
        if (oedemaField.getString(oedemaField.getSelectedIndex()).equals("OUI")){
            oed = "YES";
        } else if (oedemaField.getString(oedemaField.getSelectedIndex()).equals("NON")){
            oed = "NO";
        }else if (oedemaField.getString(oedemaField.getSelectedIndex()).equals("Inconnue")){
            oed = "Unknown";
        }
        if (nbr_plu.getString().length() == 0) {
            nbr = "-";
        } else {
            nbr = nbr_plu.getString();
        }

        return "nut fol" + sep + id.getString() + sep
                + weight.getString() + sep
                + height.getString() + sep
                + oed + sep
                + pb.getString() + sep
                + nbr;
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
