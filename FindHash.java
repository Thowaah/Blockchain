/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


import java.io.*;
import java.security.*;
import java.util.Scanner;
import java.nio.charset.StandardCharsets;
import java.lang.Math;

class FindHash {

  public static boolean searchHash(int d) {

    //Scanner reader = new Scanner(System.in);  // Reading from System.in
    byte[] encodedhash = hexStringToByteArray("bite");
    long startTime = System.currentTimeMillis();


    do {
      //System.out.println("Enter a number: ");
      //String s = reader.nextLine(); // Scans the next token of the input as an int.
      //once finished

      try{
        byte[] bytes = new byte[20];
        SecureRandom.getInstanceStrong().nextBytes(bytes);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        encodedhash = digest.digest(bytes);
        System.out.println("hash: "+bytesToHex(encodedhash));
      } catch(Exception e){
        System.err.println("Caught exception " + e.toString());
      }
      System.out.println("Premier caractère: " + encodedhash[0]);
    } while(!diff(encodedhash, d));

    System.out.println("Trouvé !");
    long stopTime = System.currentTimeMillis();
    long elapsedTime = stopTime - startTime;
    System.out.println("Temps écoulé: " + elapsedTime/1000+"s");

    return true;

    //reader.close();
    /* Generate a DSA signature */

  }


  private static boolean diff(byte[] array, int diff){
    for(int i=0; i<diff; i++){
      if(array[i] != (byte)0){return false;}
    }
    return true;
  }

  private static String bytesToHex(byte[] hash) {
    StringBuffer hexString = new StringBuffer();
    for (int i = 0; i < hash.length; i++) {
    String hex = Integer.toHexString(0xff & hash[i]);
    if(hex.length() == 1) hexString.append('0');
        hexString.append(hex);
    }
    return hexString.toString();
  }

  public static byte[] hexStringToByteArray(String s) {
    int len = s.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                             + Character.digit(s.charAt(i+1), 16));
    }
    return data;
  }
}
