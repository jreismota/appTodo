/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.unipam.apptodo.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author josereis
 */
public class Criptografia {

    public static String md5(String input) {

        String md5 = null;

        if (null == input) {
            return null;
        }
        final String salt = "Random$SaltValue#WithSpecialCharacters12@$@4&#%^$*";

        input += salt;

                System.out.println(input);
        try {

            //Create MessageDigest object for MD5
            MessageDigest digest = MessageDigest.getInstance("MD5");

            //Update input string in message digest
            digest.update(input.getBytes(), 0, input.length());

            //Converts message digest value in base 16 (hex) 
            md5 = new BigInteger(1, digest.digest()).toString(16);

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
        }
                    System.out.println(md5);
        return md5;
    }
}
/***
 * Referências:
 * https://www.devmedia.com.br/criptografia-md5/2944
 * https://www.viralpatel.net/java-md5-hashing-salting-password/
 * 
 */
