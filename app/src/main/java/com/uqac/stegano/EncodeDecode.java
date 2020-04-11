package com.uqac.stegano;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Vector;

class EncodeDecode {

    private static final String TAG = EncodeDecode.class.getName();

    //start and end message constants
//    private static final String END_MESSAGE_CONSTANT = "#!@";
//    private static final String START_MESSAGE_CONSTANT = "@!#";
    //start and end message constants
    private static final String END_MESSAGE_CONSTANT = Character.toString((char)28);
    private static final String START_MESSAGE_CONSTANT = Character.toString((char)31);

    private static final int[] binary = {16, 8, 0};
    private static final byte[] andByte = {(byte) 0xC0, 0x30, 0x0C, 0x03};
    private static final int[] toShift = {6, 4, 2, 0};

    /**
     * This method represent the core of 2 bit Encoding
     *
     * @return : byte encoded pixel array
     * @parameter :  integer_pixel_array {The integer RGB array}
     * @parameter : image_columns {Image width}
     * @parameter : image_rows {Image height}
     */

    private static byte[] encodeMessage(int[] integer_pixel_array, int image_columns, int image_rows, byte[] messageByteArray) {
        boolean messageEncoded = false;
        int messageIndex = 0;

        //denotes RGB channels
        int channels = 3;

        int shiftIndex = 4;

        //creating result byte_array
        byte[] result = new byte[image_rows * image_columns * channels];

        int resultIndex = 0;

        for (int row = 0; row < image_rows; row++) {

            for (int col = 0; col < image_columns; col++) {

                //2D matrix in 1D
                int element = row * image_columns + col;

                byte tmp;

                for (int channelIndex = 0; channelIndex < channels; channelIndex++) {

                    if (!messageEncoded) {

                        // Shifting integer value by 2 in left and replacing the two least significant digits with the message_byte_array values..
                        tmp = (byte) ((((integer_pixel_array[element] >> binary[channelIndex]) & 0xFF) & 0xFC) | ((messageByteArray[messageIndex] >> toShift[(shiftIndex++)
                                % toShift.length]) & 0x3));// 6

                        if (shiftIndex % toShift.length == 0) {
                            messageIndex++;
                        }

                        if (messageIndex == messageByteArray.length) {
                            messageEncoded = true;
                        }

                    } else {
                        //Simply copy the integer to result array
                        tmp = (byte) ((((integer_pixel_array[element] >> binary[channelIndex]) & 0xFF)));
                    }

                    result[resultIndex++] = tmp;

                }

            }

        }


        return result;

    }

    /**
     * This method implements the above method on the list of chunk image list.
     *
     * @return : Encoded list of chunk images
     * @parameter : splitted_images {list of chunk images}
     * @parameter : encrypted_message {string}
     */
    public static Bitmap encodeMessage(Bitmap bitmap, String message) {

        //Adding start and end message constants to the encrypted message
        message = message + END_MESSAGE_CONSTANT;
        message = START_MESSAGE_CONSTANT + message;


        //getting byte array from string
        byte[] byte_encrypted_message = message.getBytes(Charset.forName("ISO-8859-1"));

        //Just a log to get the byte message length
        Log.i(TAG, "Message length " + byte_encrypted_message.length);

        //getting bitmap height and width
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        //Making 1D integer pixel array
        int[] oneD = new int[width * height];
        bitmap.getPixels(oneD, 0, width, 0, 0, width, height);

        //getting bitmap density
        int density = bitmap.getDensity();

        //encoding image
        byte[] encodedImage = encodeMessage(oneD, width, height, byte_encrypted_message);

        //converting byte_image_array to integer_array
        int[] oneDMod = Utilities.byteArrayToIntArray(encodedImage);

        //creating bitmap from encrypted_image_array
        Bitmap encodedBitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        encodedBitmap.setDensity(density);

        int masterIndex = 0;

        //setting pixel values of above bitmap
        for (int j = 0; j < height; j++)
            for (int i = 0; i < width; i++) {

                encodedBitmap.setPixel(i, j, Color.argb(0xFF,
                        oneDMod[masterIndex] >> 16 & 0xFF,
                        oneDMod[masterIndex] >> 8 & 0xFF,
                        oneDMod[masterIndex++] & 0xFF));

            }

        return encodedBitmap;
    }

    /**
     * This is the decoding method of 2 bit encoding.
     *
     * @return : Void
     * @parameter : byte_pixel_array {The byte array image}
     * @parameter : image_columns {Image width}
     * @parameter : image_rows {Image height}
     */
    private static String decodeMessage(byte[] byte_pixel_array, int image_columns, int image_rows) {
        String message = "";

        //encrypted message
        Vector<Byte> byte_encrypted_message = new Vector<>();

        int shiftIndex = 4;

        byte tmp = 0x00;


        for (byte aByte_pixel_array : byte_pixel_array) {


            //get last two bits from byte_pixel_array
            tmp = (byte) (tmp | ((aByte_pixel_array << toShift[shiftIndex
                    % toShift.length]) & andByte[shiftIndex++ % toShift.length]));

            if (shiftIndex % toShift.length == 0) {
                //adding temp byte value
                byte_encrypted_message.addElement(tmp);


                //converting byte value to string
                byte[] nonso = {byte_encrypted_message.elementAt(byte_encrypted_message.size() - 1)};
                String str = new String(nonso, StandardCharsets.ISO_8859_1);

                if (message.endsWith(END_MESSAGE_CONSTANT)) {

                    Log.i("TEST", "Decoding ended");

                    //fixing ISO-8859-1 decoding
                    byte[] temp = new byte[byte_encrypted_message.size()];

                    for (int index = 0; index < temp.length; index++)
                        temp[index] = byte_encrypted_message.get(index);


                    String stra = new String(temp, StandardCharsets.ISO_8859_1);


                    message = stra.substring(0, stra.length() - 1);
                    //end fixing
                    //TODO ENDED

                    break;
                } else {
                    //just add the decoded message to the original message
                    message += str;

                    //If there was no message there and only start and end message constant was there
                    if (message.length() == START_MESSAGE_CONSTANT.length()
                            && !START_MESSAGE_CONSTANT.equals(message) ){

                        message = "";
                        //TODO ENDED

                        break;
                    }
                }

                tmp = 0x00;
            }

        }

        if (!Utilities.isStringEmpty(message))
            //removing start and end constants form message

            try {
                message = message.substring(START_MESSAGE_CONSTANT.length(), message.length() - END_MESSAGE_CONSTANT.length());
            } catch (Exception e) {
                e.printStackTrace();
            }

        return message;
    }

    /**
     * This method takes the list of encoded chunk images and decodes it.
     *
     * @return : encrypted message {String}
     * @parameter : encodedImages {list of encode chunk images}
     */

    public static String decodeMessage(Bitmap bit) {


        int[] pixels = new int[bit.getWidth() * bit.getHeight()];

        bit.getPixels(pixels, 0, bit.getWidth(), 0, 0, bit.getWidth(),
                bit.getHeight());

        byte[] b;

        b = Utilities.convertArray(pixels);

        return decodeMessage(b, bit.getWidth(), bit.getHeight());
    }

    /**
     * Calculate the numbers of pixels needed
     *
     * @return : The number of pixel {integer}
     * @parameter : message {Message to encode}
     */
    public static int numberOfPixelsForMessage(String message) {
        int result = -1;
        if (message != null) {
            message += END_MESSAGE_CONSTANT;
            message = START_MESSAGE_CONSTANT + message;
            result = message.getBytes(StandardCharsets.ISO_8859_1).length * 4 / 3;
        }

        return result;
    }

}