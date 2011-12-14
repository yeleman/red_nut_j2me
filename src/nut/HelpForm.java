
package nut;

import javax.microedition.lcdui.*;
import nut.Configuration.*;
import nut.Constants.*;

/**
 * J2ME Form displaying a long help text
 * Instanciated with a section paramater
 * which triggers appropriate text.
 * @author rgaudin
 */
public class HelpForm extends Form implements CommandListener {

    private static final Command CMD_EXIT = new Command ("Retour", Command.BACK, 1);

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

/*
 *
 */
private void getContentFromSection(String section) {
    String text;

    if (section.equalsIgnoreCase("mainmenu")) {
        text = "Renseignez votre identifiant et votre ancien mot de passe dans les champs adéquat.\n" +
               "Ensuite, indiquez le nouveau mot de passe désiré. Celui-ci doit faire au moins 3 caractères.\n" +
               "Vous recevrez un SMS du serveur confirmant ou non le changement de mot de passe.\n";
    } else if (section.equalsIgnoreCase("edit_number")) {
        text = "Changez le numéro du serveur uniquement sur demande expresse " +
               "de la Croix-Rouge.\n" +
               "Un mauvais numéro vous empêchera de transmettre vos rapports.\n";
    } else if (section.equalsIgnoreCase("registration")) {
        text = "Enregistrement patient: Renseignez le prenom, \n" +
               "le nom, l'age, le nom de la mere de l'enfant\n" +
               "et indiquez le code du CSCOM .\n ";
    }  else if (section.equalsIgnoreCase("alou")) {
        text = "Enregistrement je suis le best blabla.\n ";
    } else if (section.equalsIgnoreCase("version")) {
        text = "NUT - Version " + Constants.version + "\n\n" +
               "En cas de problème, contactez la Croix-Rouge.";
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

