package p.lodz.dsa;

import java.io.*;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Scanner;

public class Helper {


    static public byte[] readFile(String fileName) throws FileException {
        byte[] result;

        try(FileInputStream fis = new FileInputStream(fileName)) {
            result = fis.readAllBytes();
        } catch (IOException e) {

            throw new FileException(e);
        }
        return result;
    }

    static public void saveFile(byte[] dane, String fileName) throws FileException {

        try(FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(dane);
        } catch (IOException e) {
            throw new FileException(e);
        }
    }

    static public byte[] hexToBytes(String text) throws HexStringException {
        if (text.length() % 2 != 0){
            throw new HexStringException("Zła długość szyfrogramu");
        }
        int len = text.length()/2;
        byte[] bytes = new byte[len];
        for (int i = 0; i < text.length(); i++) {
            int first = Character.digit(text.charAt(i++), 16);
            int second = Character.digit(text.charAt(i), 16);
            bytes[i/2] = (byte) ((first << 4) + second);
        }
        return bytes;
    }


    static public String bytesToHex(byte[] bytes) {
        StringBuilder hexText = new StringBuilder();
        String initial = null;
        char[] hexDigits = new char[2];
        for (int i = 0; i < bytes.length; i++) {
            byte next = bytes[i];

            hexDigits[0] = Character.forDigit((next >> 4) & 0xF, 16);
            hexDigits[1] = Character.forDigit((next & 0xF), 16);
            hexText.append(hexDigits);
        }
        initial = hexText.toString();

        return initial;
    }


    public static void saveBigIntsWithNewlineToFile(BigInteger[] dane, String nazwa_pliku) throws FileException {
        try(FileWriter writer = new FileWriter(nazwa_pliku)) {
            for (BigInteger bigInteger : dane) {
                writer.write(bigInteger.toString() + "\n");
            }
        } catch (IOException e) {
            throw new FileException(e);
        }
    }

    public static BigInteger[] readBigIntArrayFromFile(String nazwa_pliku) throws FileException {
        BigInteger[] array = new BigInteger[1];
        try( Scanner sc = new Scanner(new File(nazwa_pliku))) {
            int i = 0;
            while(sc.hasNextBigInteger())
            { if(i > 0) {
                array = Arrays.copyOf(array, array.length+1);
            }
                array[i] = sc.nextBigInteger();
                i++;
            }
        } catch (FileNotFoundException e) {
            throw new FileException(e);
        }
        return array;
    }



}

