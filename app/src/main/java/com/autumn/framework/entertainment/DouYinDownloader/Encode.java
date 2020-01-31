package com.autumn.framework.entertainment.DouYinDownloader;

import java.io.UnsupportedEncodingException;

//
//Decompiled by CFR - 1113ms
//

/*
* Exception performing whole class analysis ignored.
*/
public class Encode {
static final char[] model = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

static {
}

public Encode() {
}

private static String operate(byte[] arrby, int n, int n2) {
   if (arrby != null) {
      if (n >= 0 && n + n2 <= arrby.length) {
         int n3 = n2 * 2;
         char[] arrc = new char[n3];
         int n4 = 0;
         for (int i = 0; i < n2; ++i) {
            int n5 = (arrby[i + n] & 255) + i & 255;
            int n6 = n4 + 1;
            char[] arrc2 = model;
            arrc[n4] = arrc2[n5 >> 4];
            n4 = n6 + 1;
            arrc[n6] = arrc2[n5 & 15];
         }
         return new String(arrc, 0, n3);
      }
      throw new IndexOutOfBoundsException();
   }
   throw new NullPointerException("bytes is null");
}

/*
 * Enabled force condition propagation
 * Lifted jumps to return sites
 */
private static String encryptwithxor(String object) throws UnsupportedEncodingException {
   if (object == null) return null;
   if (object.equals("")) return null;
   byte[] object2 = object.getBytes("UTF-8");
   //int i = object.getBytes("UTF-8").length;
   int n = 0;
   do {
      if (n >= object2.length) break;
      object2[n] = (byte)(object2[n] ^ 5);
      n = (byte)(n + 1);
   } while (true);
   try {
      return Encode.operate((byte[])object2, (int)0, (int)object2.length);
   }
   catch (Exception exception) {
      return object;
   }
}

public static String work(String string) throws Exception {
   return Encode.encryptwithxor((String)string);
}
}

