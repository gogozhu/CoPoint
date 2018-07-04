package cn.copoint.coeditor.utils;

import java.util.ArrayList;
import java.util.List;

public class Base64 {
    private static String _keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
    public static int[] decode(String input) {
        StringBuilder output = new StringBuilder("");
        int chr1, chr2, chr3;
        int enc1, enc2, enc3, enc4;
        int i = 0;
        input = input.replace("[^A-Za-z0-9+/=]", "");
        while (i < input.length()) {
            enc1 = (i < input.length()) ? _keyStr.indexOf(input.charAt(i++)) : 0;
            enc2 = (i < input.length()) ? _keyStr.indexOf(input.charAt(i++)) : 0;
            enc3 = (i < input.length()) ? _keyStr.indexOf(input.charAt(i++)) : 0;
            enc4 = (i < input.length()) ? _keyStr.indexOf(input.charAt(i++)) : 0;
            chr1 = (enc1 << 2) | (enc2 >> 4);
            chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
            chr3 = ((enc3 & 3) << 6) | enc4;
            output.append(String.valueOf((char)chr1));
            if (enc3 != 64) {
                output.append(String.valueOf((char)chr2));
            }
            if (enc4 != 64) {
                output.append(String.valueOf((char)chr3));
            }
        }
        List<Integer> array = new ArrayList<>();
        i = 0;
        int c, c2 = 0, c3 = 0;
        while ( i < output.length() ) {
            c = (int)output.charAt(i);
            if (c < 128) {
                array.add(c);
                i++;
            } else if((c > 191) && (c < 224)) {
                c2 = output.charAt(i+1);
                array.add(((c & 31) << 6) | (c2 & 63));
                i += 2;
            } else {
                c2 = (int)output.charAt(i+1);
                c3 = (int)output.charAt(i+2);
                array.add(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
                i += 3;
            }
        }
        int[] result = new int[array.size()];
        for (i = 0; i < array.size(); i++) {
            result[i] = array.get(i);
        }
        return result;
    }

    public static String encode(int[] input) {
        StringBuilder output = new StringBuilder("");
        int chr1, chr2, chr3, enc1, enc2, enc3, enc4;
        int i = 0;
        StringBuilder strInput = new StringBuilder("");
        for (i = 0; i < input.length; i++) {
            strInput = strInput.append(String.valueOf((char)input[i]));
        }
        StringBuilder utftext = new StringBuilder("");
        for (int n = 0; n < strInput.length(); n++) {
            int c = (int)strInput.charAt(n);
            if (c < 128) {
                utftext.append(String.valueOf((char)c));
            } else if(c < 2048) {
                utftext.append(String.valueOf((char)((c >> 6) | 192)));
                utftext.append(String.valueOf((char)((c & 63) | 128)));
            } else {
                utftext.append(String.valueOf((char)((c >> 12) | 224)));
                utftext.append(String.valueOf((char)(((c >> 6) & 63) | 128)));
                utftext.append(String.valueOf((char)((c & 63) | 128)));
            }

        }

        i = 0;
        while (i < utftext.length()) {
            chr1 = (int)utftext.charAt(i++);
            chr2 = (i < utftext.length()) ? (int)utftext.charAt(i++) : -1;
            chr3 = (i < utftext.length()) ? (int)utftext.charAt(i++) : -1;
            enc1 = chr1 >> 2;
            enc2 = (chr2 > -1) ? ((chr1 & 3) << 4) | (chr2 >> 4) : ((chr1 & 3) << 4);
            enc3 = (chr2 > -1) ? ((chr3 > -1) ? ((chr2 & 15) << 2) | (chr3 >> 6) : ((chr2 & 15) << 2)) : 64;
            enc4 = (chr2 > -1) ? ((chr3 > -1) ? chr3 & 63 : 64) : 64;
            output.append(_keyStr.charAt(enc1)).append(_keyStr.charAt(enc2)).append(_keyStr.charAt(enc3)).append(_keyStr.charAt(enc4));
        }
        return output.toString();
    }
}
