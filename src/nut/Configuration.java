
package nut;

import javax.microedition.rms.*;
import java.io.*;
import nut.Constants.*;

/**
 * Store defined values into RMS
 * @author rgaudin
 */
public class Configuration {

    private String server_number;
    private String health_center;
    private String is_mam;
    private String is_sam;
    private String is_samp;

    // index 0 is invalid
    private int server_number_index = 1;
    private int health_center_index = 2;
    private int is_mam_index = 3;
    private int is_sam_index = 4;
    private int is_samp_index = 5;

    private static final String database = "configuration";
    private RecordStore recordstore = null;

    public Configuration() {
        RecordEnumeration recordEnumeration = null;
        try {
            recordstore = RecordStore.openRecordStore(Configuration.database, true);
            recordEnumeration = recordstore.enumerateRecords(null, null, false);
            recordstore.closeRecordStore();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (recordEnumeration.numRecords() < 4) {
            // the following has to be in order of indexes.
            this.set("server_number", "12345678", true);
            this.set("health_center", "false", true);
            this.set("is_mam", "false", true);
            this.set("is_sam", "false", true);
            this.set("is_samp", "false", true);
            this.set("server_number", Constants.server_number, false);
        }
    }

    /*
     * Fetch value from DB
     * @param variable the slug of the data to retrieve
     * @return a <code>String</code> of the stored data
     */
    public String get(String variable) {

        String value = "";

        int index = this.index_for(variable);
        if (index < 0) {
            return value;
        }

        try
        {
        // open record store
        recordstore = RecordStore.openRecordStore(Configuration.database, true );

        // record is internally a byte array
        byte[] byteInputData = new byte[1024];

        // we'll retrieve data in a stream
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteInputData);
        DataInputStream inputDataStream = new DataInputStream(inputStream);

        // actually retrieve data
        recordstore.getRecord(index, byteInputData, 0);

        value = inputDataStream.readUTF();

        // close streams
        inputStream.reset();
        inputStream.close();
        inputDataStream.close();

        // close connection
        recordstore.closeRecordStore();
      }
      catch (Exception error)
      {
          return value;
      }
      return value;
    }

    /*
     * the index of a variable in the RMS record
     * @param variable the slug of the data to query
     * @return the index
     */
    private int index_for(String variable) {
        int index;
        if (variable.equals("health_center")) {
            index = health_center_index;
        } else if (variable.equals("server_number")) {
            index = server_number_index;
        } else if (variable.equals("is_mam")) {
            index = is_mam_index;
        } else if (variable.equals("is_sam")) {
            index = is_sam_index;
        } else if (variable.equals("is_samp")) {
            index = is_samp_index;
        } else {
            index = -1;
        }
        return index;
    }

    /*
     * Stores a value associated with a variable in the DB
     * @param variable the slug of the data
     * @param value the actual <code>String</code> value
     * @param add whether to add (<code>true</code>) or to update DB row
     */
    public boolean set(String variable, String value, boolean add) {

        int index = this.index_for(variable);
        if (index < 0) {
            return false;
        }

        try
        {
        // open record store
        recordstore = RecordStore.openRecordStore(Configuration.database, true );

        // record is internaly a byte array
        byte[] outputRecord;

        // store all data in a stream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream outputDataStream = new DataOutputStream(outputStream);

        // add all fields to the stream
        outputDataStream.writeUTF(value);

        // finish preparing stream
        outputDataStream.flush();
        outputRecord = outputStream.toByteArray();

        // actual record storage
        if (add) {
            recordstore.addRecord(outputRecord, 0, outputRecord.length);
        } else {
            recordstore.setRecord(index, outputRecord, 0, outputRecord.length);
        }

        // close stream
        outputStream.reset();
        outputStream.close();
        outputDataStream.close();

        // close connection
        recordstore.closeRecordStore();
      }
      catch ( Exception error)
      {
          return false;
      }
      return true;
    }

    /*
     * Overrides <code>set()</code> to save the <code>add</code> param.
     * @see <code>set()</code>
     */
    public boolean set(String variable, String value) {
        return set(variable, value, false);
    }
}
