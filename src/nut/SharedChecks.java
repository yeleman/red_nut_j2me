/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nut;
import nut.Configuration.*;
import java.util.Date;
import javax.microedition.lcdui.*;

/**
 *
 * @author Fad
 */
public class SharedChecks {

    private static final String month_list[] = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

    public static int[] formatDateString(Date date_obj) {
        String date = date_obj.toString();
        int day = Integer.valueOf(date.substring(8, 10)).intValue();
        int month = monthFromString(date.substring(4,7));
        int start = 24;
        int end = date.length();
        if (end == 34){
            start = 30;
        }
        int year = Integer.valueOf(date.substring(start, end)).intValue();
        int list_date[] = {day, month, year};
        return list_date;
    }

    public static int monthFromString(String month_str) {
        int i;
        for(i=0; i<=month_list.length; i++){
            if(month_list[i].equals(month_str)){
                return i + 1;
            }
        }
        return 1;
    }

    public static boolean isDateValide(Date date_obj) {
        // check sur les dates. En fin d'eliminer ceux qui sont au future
        int array[] = formatDateString(date_obj);
        int day = array[0];
        int month = array[1];
        int year = array[2];

        Date now = new Date();
        int now_array[] = formatDateString(now);
        int now_day = now_array[0];
        int now_month = now_array[1];
        int now_year = now_array[2];
        if (now_year < year){
            return false;
        }
        else {
            if (now_month < month){
                return false;
            }
            else {
                if (now_day < day){
                    return false;
                }
            }
        }
        return true;
    }

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
        if (Float.parseFloat(weight.getString()) < Constants.MIN_WEIGHT || Float.parseFloat(weight.getString()) > Constants.MAX_WEIGHT) {
           return "Le poids doit être compris entre " + Constants.MIN_WEIGHT + " et " + Constants.MAX_WEIGHT + " kg";
        }
        else if (Integer.parseInt(height.getString()) < Constants.MIN_HEIGHT || Integer.parseInt(height.getString()) > Constants.MAX_HEIGHT) {
            return "La taille de l'enfant doit être compris entre " + Constants.MIN_HEIGHT + " et " + Constants.MAX_HEIGHT + " cm";
        }
        else if (Integer.parseInt(pb.getString()) < Constants.MIN_PB || Integer.parseInt(pb.getString()) > Constants.MAX_PB) {
           return "Le périmètre brachial doit être compris entre " + Constants.MIN_PB + " et " + Constants.MAX_PB + " mm";
        }
        return "";
     }

    public static String addzero(int num){
        String snum = "";
        if (num < 10)
            snum = "0" + num;
        else
            snum = snum + num;
        return snum;
    }
}
