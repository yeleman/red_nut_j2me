package nut;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import nut.RegisterForm.*;
import nut.ResearchForm.*;
import nut.DisableForm.*;
import nut.OptionForm.*;
import nut.DataNutForm.*;

/*
 * J2ME Midlet allowing user to fill and submit Nutrition SMS
 * @author rgaudin
 */
public class NUTMIDlet extends MIDlet implements CommandListener {

    private static final Command CMD_EXIT = new Command ("Quitter", Command.EXIT, 1);
    private static final Command CMD_VERSION = new Command ("Version", Command.SCREEN, 2);
    private static final Command CMD_SRVNUM = new Command ("Configuration", Command.SCREEN, 4);
    private static final Command CMD_HELP = new Command ("Aide", Command.HELP, 5);

    public Display display;
    public List mainMenu;
    private Configuration config;

    public NUTMIDlet() {
        display = Display.getDisplay(this);
    }

    public void startApp() {

        config = new Configuration();

        String[] mainMenu_items = {"Enregistrer enfant", "Suivi enfant", "Recherche ID", "Abandon", "Conso Intrants"};
        mainMenu = new List("Gestion Nutrition", Choice.IMPLICIT, mainMenu_items, null);

        // setup menu
        mainMenu.setCommandListener (this);
        mainMenu.addCommand (CMD_EXIT);
        mainMenu.addCommand (CMD_HELP);
        mainMenu.addCommand (CMD_VERSION);
        mainMenu.addCommand (CMD_SRVNUM);

       display.setCurrent(mainMenu);

    }


    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    public void commandAction(Command c, Displayable s) {

        // if it originates from the MainMenu list
        if (s.equals (mainMenu)) {
            // and is a select command
            if (c == List.SELECT_COMMAND) {

                    switch (((List) s).getSelectedIndex ()) {

                    // registration
                    case 0:
                        RegisterForm reg_form = new RegisterForm(this);
                        display.setCurrent (reg_form);
                        break;
                    // research
                    case 2:
                        ResearchForm re_form = new ResearchForm(this);
                        display.setCurrent (re_form);
                        break;
                    // follow-up
                    case 1:
                        DataNutForm data_form = new DataNutForm(this);
                        display.setCurrent (data_form);
                        break;
                    // DisableForm
                    case 3:
                        DisableForm off_form = new DisableForm(this);
                        display.setCurrent (off_form);
                        break;
					
					// StockForm
                    case 4:
                        StockForm stock_form = new StockForm(this);
                        display.setCurrent (stock_form);
                        break;
                    }
            }
        }

        // help command displays Help Form.
        if (c == CMD_HELP) {
            HelpForm h = new HelpForm(this, this.mainMenu, "mainmenu");
            display.setCurrent(h);
        }

        // version command displays Help Form for "version"
        if (c == CMD_VERSION) {
            HelpForm v = new HelpForm(this, this.mainMenu, "version");
            display.setCurrent(v);
        }

        // srvnum command displays Edit Number Form.
        if (c == CMD_SRVNUM) {
            OptionForm f = new OptionForm(this);
            display.setCurrent(f);
        }

        // exit commands exits application completely.
        if (c == CMD_EXIT) {
            destroyApp(false);
            notifyDestroyed();
        }
    }
}