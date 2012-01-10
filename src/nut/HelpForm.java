
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
        } else if (section.equalsIgnoreCase("option")) {
            text = "Changez le numéro du serveur, le code du centre et type " +
                   "centre uniquement sur demande expresse de la " +
                   "Croix-Rouge.\n Un mauvais numéro vous empêchera de " +
                   "transmettre vos rapports.\n" ;
        } else if (section.equalsIgnoreCase("registration")) {
            text = "Enregistrement patient: \n" +
                   "Renseignez le prenom, \n" +
                   "le nom, l'age et le nom de la mere de l'enfant\n";
        } else if (section.equalsIgnoreCase("research")) {
            text = "Renseignez le nom, prénom ou le nom de la mère, vous " +
                   "recevrez un SMS contenant l'id du patient s'il existe.\n" +
                   "En cas de problème, contactez Croix-Rouge.";
        } else if (section.equalsIgnoreCase("Disable")) {
            text = "Renseignez l'id du patient pour signaler sa sortie du " +
                   "programme, vous recevrez un SMS " +
                   "confirmant ou non de deactivation.\n" +
                   "En cas de problème, contactez Croix-Rouge.";
        } else if (section.equalsIgnoreCase("version")) {
            text = "NUT - Version " + Constants.version + "\n\n" +
                   "En cas de problème, contactez la Croix-Rouge.";
        }  else if (section.equalsIgnoreCase("datanut")) {
            text = "Renseigner les champs: \n" +
                    "ID avec l'identifiant de l'enfant, \n" +
                    "le poids en kilogramme, \n" +
                    "la taille en centimètre, \n" +
                    "le érimètre brachial en millimètre \n" +
                    "et le signe de danger \n\n" +
                    "En cas de problème, contactez Croix-Rouge.";
        } else if (section.equalsIgnoreCase("stock")) {
            text = "Renseigner les champs: \n" +
                    "Stock initial, \n" +
                    "Stock reçu, \n" +
                    "Stock utilisé, \n" +
                    "Stock perdu \n" +
                    "pour chaque produit, \n" +
                    "puis on indique la periode." +
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

