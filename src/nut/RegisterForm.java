
package nut;

import javax.microedition.lcdui.*;
import java.util.TimeZone;
import java.util.Date;
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

    private static final Command CMD_EXIT = new Command ("Retour",
                                                            Command.BACK, 1);
    private static final Command CMD_SAVE = new Command ("Envoi.",
                                                            Command.OK, 1);
    private static final Command CMD_HELP = new Command ("Aide",
                                                            Command.HELP, 2);
    private static final String month_list[] = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

    public NUTMIDlet midlet;

    private Configuration config;

    private String health_center = "";

    private static final String[] sexList= {"F", "M"};
    private String ErrorMessage = "";

    private TextField first_name;
    private TextField last_name;
    private TextField mother_name;
    private ChoiceGroup sex;
    private DateField dob;


public RegisterForm(NUTMIDlet midlet) {
    super("Enregistrement");
    this.midlet = midlet;

    config = new Configuration();

    health_center = config.get("health_center");

    // creating al fields (blank)
    first_name =  new TextField("Prénom:", null, 20, TextField.ANY);
    last_name =  new TextField("Nom:", null, 20, TextField.ANY);
    mother_name =  new TextField("Nom de la mère:", null, 20, TextField.ANY);
    dob =  new DateField("Date de naissance:", DateField.DATE, TimeZone.getTimeZone("GMT"));
    sex = new ChoiceGroup("Sexe:", ChoiceGroup.POPUP, sexList, null);
    
    dob.setDate(new Date());
    // add fields to forms
    append(first_name);
    append(last_name);
    append(mother_name);
    append(dob);
    append(sex);
    addCommand(CMD_EXIT);
    addCommand(CMD_SAVE);
    addCommand(CMD_HELP);
    this.setCommandListener (this);

}

    private int[] previous_month( int day, int month, int year) {
        int new_month[] = {day, 1, 2012};
        if (month > 1){
            new_month[1] = month -1;
            new_month[2] = year;
        }
        else{
            new_month[1] = 12;
            new_month[2] = year - 1;
        }
        return new_month;
    }

    private boolean is_before_month(int day1, int month1, int year1, int day2, int month2, int year2) {
        if (year1 < year2) {
            return true;
        }
        else if (year1 == year2) {
            if (month1 < month2){
                return true;
            }
            else if (month1 == month2){
                if (day1 < day2){
                    return true;
                }
                else {
                    return false;
                }
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }
    /*
     * Whether all required fields are filled
     * @return <code>true</code> is all fields are filled
     * <code>false</code> otherwise.
     */
    public boolean isComplete() {
        // all fields are required to be filled.
        Date now = new Date();
        System.out.println(now);
        long epoch = 0;
        Date empty = new Date(epoch);
        System.out.println(empty);

        try {
            System.out.println(dob.getDate().toString());
        } catch (NullPointerException e) {
            System.out.println("EMPTY");
            return false;
        }
        System.out.println("NOT EMPTY");
        System.out.println(dob.getDate().toString());

        if (first_name.getString().length() == 0 ||
            last_name.getString().length() == 0 ||
            mother_name.getString().length() == 0) {
            return false;
        }
        return true;
    }

    private int[] formatDateString(Date date_obj) {
        String date = date_obj.toString();
        int day = Integer.valueOf(date.substring(8, 10)).intValue();
        int month = monthFromString(date.substring(4,7));
        int year = Integer.valueOf(date.substring(30, 34)).intValue();
        int list_date[] = {day, month, year};
        return list_date;
    }

    /*
     * Whether all filled data is correct
     * @return <code>true</code> if all fields are OK
     * <code>false</code> otherwise.
     */
    public boolean isValid() {
        int dob_array[] = formatDateString(dob.getDate());
        int day = dob_array[0];
        int month = dob_array[1];
        int year = dob_array[2];

        Date now = new Date();
        int now_array[] = formatDateString(now);
        int now_day = now_array[0];
        int now_month = now_array[1];
        int now_year = now_array[2];

        if ((now_year - 5 > year)) {
            ErrorMessage = "DATE TROP VIEILLE";
            return false;
        }
        // calcule date 6 mois
        int d[] = {now_day, now_month, now_year};

        int i;
        for(i=0; i<=6; i++){
            d = previous_month(d[0], d[1], d[2]);
        }

        int six_month_old[] = d;

        for(i=0; i<=59; i++){
            d = previous_month(d[0], d[1], d[2]);
        }

        int fifty_month_old[] = d;

        if (is_before_month(six_month_old[0], six_month_old[1], six_month_old[2],
                           day, month, year)){
            // date a moins de 6 mois.
            ErrorMessage = "Date a moins de 6 mois";
            return false;
        }
        if (is_before_month(day, month, year,
                           fifty_month_old[0], fifty_month_old[1], fifty_month_old[2])){
            // date a moins de 6 mois.
            ErrorMessage = "Plus de 59 mois";
            return false;
        }
        return true;
    }

   /* Converts Form request to SMS message
     * @return <code>String</code> to be sent by SMS
     */

    private int monthFromString(String month_str) {
        int i;
        for(i=0; i<=month_list.length; i++){
            if(month_list[i].equals(month_str)){
                return i + 1;
            }
        }
        return 1;
    }

    public String toSMSFormat() {
        String sep = " ";
        int dob_array[] = formatDateString(dob.getDate());
        int day = dob_array[0];
        int month = dob_array[1];
        int year = dob_array[2];
        return "nut register" + sep
                              + health_center + sep
                              + first_name.getString() + sep
                              + last_name.getString() + sep
                              + mother_name.getString() + sep
                              + sex.getString(sex.getSelectedIndex()) + sep
                              + year + "-" + month + "-" + day;
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
                         "doivent être remplis!", null, AlertType.ERROR);
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
