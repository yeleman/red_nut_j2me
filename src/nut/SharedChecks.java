/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nut;

import javax.microedition.lcdui.*;

/**
 *
 * @author ALou
 */
public class SharedChecks {


    public static boolean isComplete(TextField weight, TextField height, TextField pb) {
       // all fields are required to be filled.
        if (weight.getString().length() == 0 ||
            height.getString().length() == 0 ||
            pb.getString().length() == 0) {
            return false;
        }
        return true;
    }

     public static String Message(TextField weight, TextField height, TextField pb) {
        if (Float.parseFloat(weight.getString()) <= Constants.MIN_WEIGHT || Float.parseFloat(weight.getString()) > Constants.MAX_WEIGHT) {
           return "le poids doit etre compris entre " + Constants.MIN_WEIGHT + " et " + Constants.MAX_WEIGHT + " kg";
        }
        else if (Integer.parseInt(height.getString()) < Constants.MIN_HEIGHT || Integer.parseInt(height.getString()) >= Constants.MAX_HEIGHT) {
            return "La taille de l'enfant doit etre compris entre " + Constants.MIN_HEIGHT + " et " + Constants.MAX_HEIGHT + " cm";
        }
        else if (Integer.parseInt(pb.getString()) < Constants.MIN_PB || Integer.parseInt(pb.getString()) > Constants.MAX_PB) {
           return "Le périmètre brachial doit etre compris entre " + Constants.MIN_PB + " et " + Constants.MAX_PB + " mm";
        }
        return "";
     }
}
