/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  javax.microedition.rms.RecordStore
 *  javax.microedition.rms.RecordStoreException
 *  javax.microedition.rms.RecordStoreNotFoundException
 */
package CLib;

import CLib.LibSysTem;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotFoundException;
import model.CRes;

public class RMS {
    public static final String path = "rms/";

    private static String storeName(String string) {
        String string2 = string.replace('/', '_');
        if (string2.length() > 32) {
            string2 = string2.substring(0, 32);
        }
        return string2;
    }

    public static void saveRMSInt(String string, int n) {
        try {
            RMS.saveRMS(string, new byte[]{(byte)n});
        }
        catch (Exception exception) {
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void saveRMS(String string, byte[] byArray) throws Exception {
        String string2 = RMS.storeName(string);
        RecordStore recordStore = null;
        try {
            recordStore = RecordStore.openRecordStore((String)string2, (boolean)true);
            if (recordStore.getNumRecords() > 0) {
                recordStore.setRecord(1, byArray, 0, byArray.length);
            } else {
                recordStore.addRecord(byArray, 0, byArray.length);
            }
        }
        finally {
            if (recordStore != null) {
                recordStore.closeRecordStore();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static byte[] loadRMS(String string) {
        String string2 = RMS.storeName(string);
        RecordStore recordStore = null;
        try {
            recordStore = RecordStore.openRecordStore((String)string2, (boolean)false);
            if (recordStore.getNumRecords() > 0) {
                byte[] byArray = recordStore.getRecord(1);
                return byArray;
            }
        }
        catch (Exception exception) {
        }
        finally {
            if (recordStore != null) {
                try {
                    recordStore.closeRecordStore();
                }
                catch (Exception recordStoreException) {}
            }
        }
        return RMS.loadBundledDefault(string);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
        private static byte[] loadBundledDefault(String string) {
        InputStream inputStream = null;
        try {
            inputStream = LibSysTem.openResource("/rms/" + string);
            if (inputStream == null) {
                inputStream = LibSysTem.openResource("/" + string);
            }
            if (inputStream == null) {
                inputStream = RMS.class.getResourceAsStream("/rms/" + string);
            }
            if (inputStream == null) {
                inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("rms/" + string);
            }
            if (inputStream == null) {
                File f = new File("rms/" + string);
                if (f.exists()) inputStream = new FileInputStream(f);
            }
            if (inputStream == null) {
                File f = new File("bin/rms/" + string);
                if (f.exists()) inputStream = new FileInputStream(f);
            }
            if (inputStream == null) {
                File f = new File("src/rms/" + string);
                if (f.exists()) inputStream = new FileInputStream(f);
            }
            if (inputStream != null) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] byArray3 = new byte[1024];
                int n;
                while ((n = inputStream.read(byArray3)) != -1) {
                    byteArrayOutputStream.write(byArray3, 0, n);
                }
                return byteArrayOutputStream.toByteArray();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try { inputStream.close(); } catch (IOException iOException) {}
            }
        }
        return null;
    }

    public static int loadRMSInt(String string) {
        byte[] byArray = RMS.loadRMS(string);
        return byArray == null ? -1 : byArray[0];
    }

    public static void saveRMSString(String string, String string2) {
        try {
            RMS.saveRMS(string, string2.getBytes("UTF-8"));
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static String loadRMSString(String string) {
        byte[] byArray = RMS.loadRMS(string);
        if (byArray == null) {
            return null;
        }
        try {
            return new String(byArray, "UTF-8");
        }
        catch (Exception exception) {
            return new String(byArray);
        }
    }

    public static void clearRMS(String string) {
        try {
            RecordStore.deleteRecordStore((String)RMS.storeName(string));
        }
        catch (Exception exception) {
            CRes.out("RMS clear " + string + " error is " + exception.getMessage());
        }
    }

    public static String getPathRMS() {
        return path;
    }

    public static void clearAll() {
        try {
            String[] stringArray = RecordStore.listRecordStores();
            if (stringArray != null) {
                for (int i = 0; i < stringArray.length; ++i) {
                    try {
                        RecordStore.deleteRecordStore((String)stringArray[i]);
                        continue;
                    }
                    catch (Exception exception) {
                    }
                }
            }
        }
        catch (Exception exception) {
        }
    }
}

