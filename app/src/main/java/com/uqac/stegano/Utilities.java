package com.uqac.stegano;

import android.util.Log;

public class Utilities {
    /**
     * This method converts the byte array to an integer array.
     *
     * @return : Integer Array
     * @parameter : b {the byte array}
     */

    public static int[] byteArrayToIntArray(byte[] b) {

        Log.v("Size byte array", b.length + "");

        int size = b.length / 3;

        Log.v("Size Int array", size + "");

        System.runFinalization();
        //Garbage collection
        System.gc();

        Log.v("FreeMemory", Runtime.getRuntime().freeMemory() + "");
        int[] result = new int[size];
        int offset = 0;
        int index = 0;

        while (offset < b.length) {
            result[index++] = byteArrayToInt(b, offset);
            offset = offset + 3;
        }

        return result;
    }

    /**
     * Convert the byte array to an int.
     *
     * @return : Integer
     * @parameter :  b {the byte array}
     */
    public static int byteArrayToInt(byte[] b) {

        return byteArrayToInt(b, 0);

    }


    /**
     * Convert the byte array to an int starting from the given offset.
     *
     * @return :  Integer
     * @parameter :  b {the byte array}, offset {integer}
     */
    private static int byteArrayToInt(byte[] b, int offset) {

        int value = 0x00000000;

        for (int i = 0; i < 3; i++) {
            int shift = (3 - 1 - i) * 8;
            value |= (b[i + offset] & 0x000000FF) << shift;
        }

        value = value & 0x00FFFFFF;

        return value;
    }

    /**
     * Convert integer array representing [argb] values to byte array
     * representing [rgb] values
     *
     * @return : byte Array representing [rgb] values.
     * @parameter : Integer array representing [argb] values.
     */
    public static byte[] convertArray(int[] array) {

        byte[] newarray = new byte[array.length * 3];

        for (int i = 0; i < array.length; i++) {

            newarray[i * 3] = (byte) ((array[i] >> 16) & 0xFF);
            newarray[i * 3 + 1] = (byte) ((array[i] >> 8) & 0xFF);
            newarray[i * 3 + 2] = (byte) ((array[i]) & 0xFF);

        }

        return newarray;
    }

    /**
     * This method is used to check whether the string is empty of not
     *
     * @return : true or false {boolean}
     * @parameter : String
     */
    public static boolean isStringEmpty(String str) {
        boolean result = true;

        if (str == null) ;
        else {
            str = str.trim();
            if (str.length() > 0 && !str.equals("undefined"))
                result = false;
        }

        return result;
    }


}
