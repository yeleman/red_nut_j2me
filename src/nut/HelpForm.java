
package nut;

import javax.microedition.lcdui.*;
import nut.Configuration.*;
import nut.Constants.*;

/**
 * J2ME Form displaying a long help text
 * Instanciated with a section paramater
 * which triggers appropriate text.
 * @author alou/fadiga
 */

public class HelpForm extends Form implements CommandListener {

    private static final Command CMD_EXIT = new Command ("Retour",
                                                        Command.BACK, 1);

    private StringItem helpText;
    NUTMIDlet midlet;
    Displayable returnTo;


    public HelpForm(NUTMIDlet midlet, Displayable d, String section) {
        super("Aide");
        this.midlet = midlet;
        this.returnTo = d;

        this.getContentFromSection(section);

        append(helpText);
        addCommand(CMD_EXIT);
        this.setCommandListener (this);
      }


    private void getContentFromSection(String section) {
        String text;

        if (section.equalsIgnoreCase("mainmenu")) {
            text = "Chaque élément de la liste correspond à une formulaire.\n" +
                   "Entrez dans ce qui correspond à ton opération et " +
                   "renseignez les champs et envoyer.\n" +
                   "En cas de problème, contactez Croix-Rouge.";
        } else if (section.equalsIgnoreCase("version")) {
            text = "NUT - Version " + Constants.version + "\n\n" +
                   "En cas de problème, contactez la Croix-Rouge.";
        } else if (section.equalsIgnoreCase("option")) {
            text = "Changez le numéro du serveur, le code du centre et type " +
                   "centre uniquement sur demande expresse de la " +
                   "Croix-Rouge.\n Un mauvais numéro vous empêchera de " +
                   "transmettre vos rapports.\n" ;
        } else if (section.equalsIgnoreCase("registration")) {
            text = "Renseignez le prenom, le nom, l'age, le nom de " +
                   "la mere puis les données nutritionelles de l'enfant.\n\n" +
                    "En cas de problème, contactez Croix-Rouge.";
        } else if (section.equalsIgnoreCase("datanut")) {
            text = "Renseigner les champs: \n" +
                    "ID avec l'identifiant de l'enfant, \n" +
                    "le poids en kilogramme, \n" +
                    "la taille en centimètre, \n" +
                    "le périmètre brachial en millimètre \n" +
                    "le nombre de sachets de plumpy nut donné \n\n" +
                    "En cas de problème, contactez Croix-Rouge.";
        } else if (section.equalsIgnoreCase("research")) {
            text = "Renseignez soit le nom, soit le prénom ou le nom de la mère, vous " +
                   "recevrez un SMS contenant l'id des patients si il existe.\n\n" +
                   "En cas de problème, contactez Croix-Rouge.";
        } else if (section.equalsIgnoreCase("Disable")) {
            text = "Renseignez l'id puis indiquez la raison de la sortie " +
                   " du patient du programme. \n" +
                   "Vous recevrez un SMS confirmant ou non la deactivation.\n\n" +
                   "En cas de problème, contactez Croix-Rouge.";
        } else if (section.equalsIgnoreCase("stock")) {
            text = "Renseigner les champs: \n" +
                    "Stock initial, " +
                    "Stock reçu, " +
                    "Stock utilisé, " +
                    "Stock perdu " +
                    "pour chaque produit, " +
                    "puis indiquez la periode.\n\n" +
                    "En cas de problème, contactez Croix-Rouge.";
        } else {
            text = "Aucune aide disponible pour cet élément.";
        }
        helpText = new StringItem(null, text);
    }

    public void commandAction(Command c, Displayable d) {
        if (c == CMD_EXIT) {
            this.midlet.display.setCurrent(this.returnTo);
        }
    }

}
