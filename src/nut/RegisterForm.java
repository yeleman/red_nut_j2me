
package nut;

import javax.microedition.lcdui.*;
import java.util.TimeZone;
import java.util.Date;
import nut.Configuration.*;
import nut.Constants.*;
import nut.HelpForm.*;
import nut.SharedChecks.*;

/**
 * J2ME Patient Registration Form
 * Displays registration fields
 * Checks completeness
 * Sends as SMS
 * @author alou & Fad
 */
public class RegisterForm extends Form implements CommandListener {

    private static final Command CMD_EXIT = new Command ("Retour",
                                                            Command.BACK, 1);
    private static final Command CMD_SAVE = new Command ("Envoi.",
                                                            Command.OK, 1);
    private static final Command CMD_HELP = new Command ("Aide",
                                                            Command.HELP, 2);
    private static final int MAX_SIZE = 5; // max no. of chars per field.

    public NUTMIDlet midlet;

    private Configuration config;

    private String health_center = "";

    private static final String[] sexList= {"F", "M"};
    private static final String[] oedema = {"NON", "OUI", "Inconnue"};
    // private static final String[] typeurenlist = {"URENAS", "URENI"};
    private static final String[] isureni = {"MAS", "MAS+"};

    private String ErrorMessage = "";

    private String hc_code = "";
    //register
    private DateField create_date;
    private TextField id;
    private TextField first_name;
    private TextField last_name;
    private TextField mother_name;
    private ChoiceGroup sex;
    // private ChoiceGroup type_uren;
    private TextField dob;
    private TextField contacts;

    //datanut
    private StringItem intro;
    private TextField weight;
    private TextField height;
    private ChoiceGroup oedemaField;
    private TextField pb;
    private TextField nbr_plu;
    private ChoiceGroup isurenifield;


public RegisterForm(NUTMIDlet midlet) {
    super("Enregistrement");
    this.midlet = midlet;

    System.out.println();
    config = new Configuration();

    health_center = config.get("health_center");
    hc_code = config.get("hc_code");

    // creating all fields (blank)
    create_date =  new DateField("Date d'enregistrement:", DateField.DATE, TimeZone.getTimeZone("GMT"));
    id =  new TextField("ID:", null, 10, TextField.DECIMAL);
    first_name =  new TextField("Prénom:", null, 20, TextField.ANY);
    last_name =  new TextField("Nom:", null, 20, TextField.ANY);
    mother_name =  new TextField("Nom de la mère:", null, 20, TextField.ANY);
    dob =  new  TextField("Age (en mois):", null, 2, TextField.NUMERIC);
    sex = new ChoiceGroup("Sexe:", ChoiceGroup.POPUP, sexList, null);
    // type_uren = new ChoiceGroup("Type UREN:", ChoiceGroup.POPUP, typeurenlist, null);
    contacts =  new TextField("Contact:", null, 20, TextField.NUMERIC);

    intro = new StringItem(null, "Suivi nutritionnel");
    weight =  new TextField("Poids (en kg):", null, MAX_SIZE, TextField.DECIMAL);
    height =  new TextField("Taille (en cm):", null, MAX_SIZE, TextField.DECIMAL);
    oedemaField =  new ChoiceGroup("Oedème:", ChoiceGroup.POPUP, oedema, null);
    pb =  new TextField("Périmètre brachial (en mm):", null, MAX_SIZE, TextField.DECIMAL);
    nbr_plu =  new TextField("Sachets plumpy nut donnés:", null, MAX_SIZE, TextField.NUMERIC);
    isurenifield = new ChoiceGroup("MAS/MAS+", ChoiceGroup.POPUP, isureni, null);

    create_date.setDate(new Date());

    // add fields to forms
    append(create_date);
    // append(type_uren);
    append(id);
    append(first_name);
    append(last_name);
    append(mother_name);
    append(dob);
    append(sex);
    append(contacts);
    append(intro);
    append(weight);
    append(height);
    append(oedemaField);
    append(pb);
    if (!this.hc_code.equals("URENAS")) {
    append(isurenifield);
    }
    append(nbr_plu);
    
    // add command to form
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
        if (first_name.getString().length() == 0 ||
            last_name.getString().length() == 0 ||
            mother_name.getString().length() == 0 ||
            id.getString().length() == 0 ||
            !SharedChecks.isComplete(weight, height, pb)) {
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

        if (SharedChecks.isDateValide(create_date.getDate()) != true) {
            ErrorMessage = "[Date de visite] est dans le futur.";
            return false;
        }
        // Date plus de 59 mois
        if ( Integer.parseInt(dob.getString()) > Constants.MAX_AGE) {
            ErrorMessage = "La date est trop vieille.";
            return false;
        }

        if (Integer.parseInt(dob.getString()) < Constants.MIN_AGE){
            // date a moins de 6 mois.
            ErrorMessage = "L'enfant doit être agé de plus de 6 mois.";
            return false;
        }

        ErrorMessage = SharedChecks.Message(weight, height, pb);
           if (!ErrorMessage.equals("")){
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
        String contact = " ";
        String uren = " ";

        if (oedemaField.getString(oedemaField.getSelectedIndex()).equals("OUI")){
            oed = "YES";
        } else if (oedemaField.getString(oedemaField.getSelectedIndex()).equals("NON")){
            oed = "NO";
        }else if (oedemaField.getString(oedemaField.getSelectedIndex()).equals("Inconnue")){
            oed = "Unknown";
        }

        // if (type_uren.getString(type_uren.getSelectedIndex()).equals("URENAS")){
        //     uren = "sam";
        // } else if (type_uren.getString(type_uren.getSelectedIndex()).equals("URENAM")){
        //     uren = "mas";
        // }else if (type_uren.getString(type_uren.getSelectedIndex()).equals("URENI")){
        //     uren = "samp";
        // }

        if (nbr_plu.getString().length() == 0) {
            nbrplu = "-";
        } else {
            nbrplu = nbr_plu.getString();
        }
        if (contacts.getString().length() == 0) {
            contact = "-";
        } else {
            contact = contacts.getString();
        }
        int reporting_date_array[] = SharedChecks.formatDateString(create_date.getDate());
        String reporting_d = String.valueOf(reporting_date_array[2])
                              + SharedChecks.addzero(reporting_date_array[1])
                              + SharedChecks.addzero(reporting_date_array[0]);

        return "nut register" + sep + health_center
                              + sep + reporting_d
                              // + sep + uren
                              + sep + id.getString()
                              + sep + first_name.getString().replace(' ', '_')
                              + sep + last_name.getString().replace(' ', '_')
                              + sep + mother_name.getString().replace(' ', '_')
                              + sep + sex.getString(sex.getSelectedIndex())
                              + sep + dob.getString()
                              + sep + contact
                              + "#" + weight.getString()
                              + sep + height.getString()
                              + sep + oed
                              + sep + pb.getString()
                              + sep + nbrplu
                              + sep + isurenifield.getSelectedIndex();
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
                alert = new Alert ("Demande envoyée !", "Vous allez recevoir" +
                                   " une confirmation du serveur.",
                                   null, AlertType.CONFIRMATION);
                this.midlet.display.setCurrent (alert, this.midlet.mainMenu);
            } else {
                alert = new Alert ("Échec d'envoi SMS", "Impossible d'envoyer" +
                            " la demande par SMS.", null, AlertType.WARNING);
                this.midlet.display.setCurrent (alert, this);
            }
        }
    }
}
