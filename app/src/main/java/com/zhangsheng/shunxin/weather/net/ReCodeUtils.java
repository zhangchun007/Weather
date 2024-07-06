package com.zhangsheng.shunxin.weather.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ReCodeUtils {

    private static List<char[]> alList;
    private static List<byte[]> codeList;

    /**
     * alStrArr中前20个重新打乱顺序；第21个为原始顺序。为了兼容老版本
     */
    private static String[] alStrArr = {"rMf5HE08tGnPYbydcZUiAgTlkepJqCNsO2xS7R6FLvQ9Va3WoDm4Ku1hwBzIjX+/=",
            "2WjYJh60fmDru1nXMRSGQsU5OE7avAIL3PiyxolpC9gNdV8zwBcqek4TKHbFtZ+/=",
            "xtH0aiTugLPBz9NyWb76crAXkwqs1v3C4FpJfoDeM2SUnm8QOdRjKYVhZ5GEIl+/=",
            "McNirjO3X7pKIBwCFSu2HEZU15ae8xsPVvzJqQdgWA9h0DtlmGoT6yn4YLkbRf+/=",
            "RO3rB2wpXmcixTd8qCnhQ9FHzofbG4YkyIZEWjDNtLPa5KuA1gsMl0eJV7vUS6+/=",
            "FufxbzJCOp4kn3yUasH6itX0ejqMow5vE1dKSTZ9lgYWrBAIhN7mQDG8V2RcLP+/=",
            "30Owz1lWvSRN2e94a5xiTI6Q8LbgytGFuVqjEfhsDkcAPKUHJp7rmYZCoMnBXd+/=",
            "hgYcPI58nidvW14yVGtlSx9eTKZ3LU0bRB6JmOFqHjX7zfas2ECkoQprDAuNMw+/=",
            "fCgxyqDnpBdAVuiPOsQ0TK9N8LhkFJe4vlIEwz5H62XGSmRUcW3Z7jYab1oMtr+/=",
            "UoKWvCdiHtwS5B27PnLaATFy4DYfrIlukNX9bQ3EpZeRqJGx0cj81zhM6smVOg+/=",
            "qjod0M1Sn9rsUAwtmLkBPx53JOHuGD2hEz6paFN8iIeZbTlg4YXQfKyv7VWRcC+/=",
            "OdUbEeSBp7yHLoRW82wFDsAZzgXfnQIJCkGt9ach0V4N31rvKqYx6mjTi5luPM+/=",
            "6tXKe5bwvORkfJmh1VEBFyP30HSlj4u2DqGcdMnxpoWzYIAgUr97QCiNaLTZs8+/=",
            "MOtoudUjsv1KlEpfCTaRnZrwbceDH5q4JLyB6PXhW9FQxAk7NImSVgY832G0iz+/=",
            "VRWucGgEofizMdq6PxlS0JX95bmT4pvUetZwjaOK1Ik3rYChDQ8ynNLHsA2B7F+/=",
            "nOm1vuWUN5KMHRxpFVykI9zALCT8BX3brZj0fedlS6DPoGcws2aJq4EhYQgt7i+/=",
            "x1sdAYPzyWipREB6GwlojVCXeM9rkUFOSa2NhHq0gfmLZT8KJtnIb4357QcvuD+/=",
            "KGBAijw9PJHLcD1SNnaEqerCmbzUpl0RhFg7kyoVduT6vtYQ43fW28xOs5IXZM+/=",
            "Cjli5W2FY8wBHg4OJkvATdhq1Xo0bIpxnuLPZsmafUN9y37tcEG6zMRQKDVeSr+/=",
            "Y28iFvuXOCoE7fDq3MBkAJrwygbdGeVhHWZNmcln4aT9sxLIS5Q6t0pKPRjz1U+/=",
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="};

    static {
        alList = new ArrayList<>();
        codeList = new ArrayList<>();
        for (String alStr : alStrArr) {
            char[] alphabet = alStr.toCharArray();
            alList.add(alphabet);
            byte[] codes = new byte[256];
            for (int i = 0; i < 256; i++)
                codes[i] = -1;
            int ct = 0;
            for (char ch : alphabet) {
                if ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9')) {
                    codes[ch] = (byte) ct;
                    ct++;
                }
            }
            codes['+'] = 62;
            codes['/'] = 63;
            codeList.add(codes);
        }

    }

    /**
     * 加密入口
     *
     * @param data
     * @param ver:-1为原始base64加密
     * @return
     * @author qianyayun
     */
    public static String encode(String data, int ver) {
        if (ver == -1)
            return new String(encode(data.getBytes(), 20));
        else {
            ver = (int) (System.currentTimeMillis() % 20);
            return String.format("@%03d%s", ver, new String(encode(data.getBytes(), ver)));
        }
    }

    /**
     * 解密入口
     *
     * @param data
     * @return
     * @throws IOException
     * @author qianyayun
     */
    public static String decode(String data) throws IOException {
        int ver = 20;// 默认为原始base64加密
        if (data.startsWith("@")) {// 以@开头，则是重构过的的base64加密
            ver = Integer.parseInt(data.substring(1, 4));
            data = data.substring(4);
        }
        return new String(decode(data.toCharArray(), ver), "UTF-8");
    }

    private static char[] encode(byte[] data, int ver) {
        char[] out = new char[((data.length + 2) / 3) * 4];
        char[] alphabet = alList.get(ver);
        for (int i = 0, index = 0; i < data.length; i += 3, index += 4) {
            boolean quad = false;
            boolean trip = false;

            int val = (0xFF & (int) data[i]);
            val <<= 8;
            if ((i + 1) < data.length) {
                val |= (0xFF & (int) data[i + 1]);
                trip = true;
            }
            val <<= 8;
            if ((i + 2) < data.length) {
                val |= (0xFF & (int) data[i + 2]);
                quad = true;
            }
            out[index + 3] = alphabet[(quad ? (val & 0x3F) : 64)];
            val >>= 6;
            out[index + 2] = alphabet[(trip ? (val & 0x3F) : 64)];
            val >>= 6;
            out[index + 1] = alphabet[val & 0x3F];
            val >>= 6;
            out[index + 0] = alphabet[val & 0x3F];
        }
        return out;
    }

    private static byte[] decode(char[] data, int ver) {
        int tempLen = data.length;
        byte[] codes = codeList.get(ver);
        for (int ix = 0; ix < data.length; ix++) {
            if ((data[ix] > 255) || codes[data[ix]] < 0) {
                --tempLen; // ignore non-valid chars and padding
            }
        }
        int len = (tempLen / 4) * 3;
        if ((tempLen % 4) == 3) {
            len += 2;
        }
        if ((tempLen % 4) == 2) {
            len += 1;
        }
        byte[] out = new byte[len];
        int shift = 0;
        int accum = 0;
        int index = 0;
        for (int ix = 0; ix < data.length; ix++) {
            int value = (data[ix] > 255) ? -1 : codes[data[ix]];
            if (value >= 0) {
                accum <<= 6;
                shift += 6;
                accum |= value;
                if (shift >= 8) {
                    shift -= 8;
                    out[index++] = (byte) ((accum >> shift) & 0xff);
                }
            }
        }

        if (index != out.length) {
            throw new Error("Miscalculated data length (wrote " + index + " instead of " + out.length + ")");
        }

        return out;
    }


    private static final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-=+";

    private  static final String key="40WanAkIDNV4qkLw";


    public static String httpParmsEncode(String txt) throws UnsupportedEncodingException {
        txt = URLEncoder.encode(txt, "UTF-8");
        txt += key;
        Random random = new Random();
        int nh = random.nextInt(64);
        char[] byteChars = chars.toCharArray();
        String mdkey = encodeToMD5(key + byteChars[nh]);
        mdkey = mdkey.substring(nh % 8, (nh % 8) * 2 + 7);
        txt = encode(txt, -1);
        byte[] byteBase64Text = txt.getBytes();
        byte[] byteMdkey = mdkey.getBytes();
        StringBuilder tmp = new StringBuilder();
        int j = 0, k = 0;
        for (int i = 0; i < txt.length(); i++) {
            if (k == mdkey.length()) {
                k = 0;
            }
            j = nh + chars.indexOf(byteBase64Text[i]) + (int) byteMdkey[k];
            j = j % 64;
            k++;
            tmp.append(byteChars[j]);
        }
        return encode(byteChars[nh] + tmp.toString(), -1);
    }

public static String httpResponseDecode(String txt) {
    String decode = "";
    try {
        txt = decode(txt);
        String ch = txt.substring(0, 1);
        int nh = chars.indexOf(ch);
        String mdKey = encodeToMD5(key + ch);
        mdKey = mdKey.substring(nh % 8, (nh % 8) * 2 + 7);
        txt = txt.substring(1);
        StringBuilder tmp=new StringBuilder();
        int j = 0, k = 0;
        byte[] byteMdkey = mdKey.getBytes();
        for (int i = 0; i < txt.length(); i++) {
            if (k == mdKey.length()) {
                k = 0;
            }
            j = chars.indexOf(txt.substring(i, i + 1)) - nh - (int) byteMdkey[k++];
            while (j < 0) {
                j += 64;
            }
            tmp.append(chars.charAt(j));
        }
        decode = URLDecoder.decode(decode(tmp.toString()),"UTF-8");
        decode = decode.replace(key, "");
    } catch (IOException e) {
        e.printStackTrace();
    }
    return decode;
}


    public static String encodeToMD5(String string) {
        byte[] hash = new byte[0];
        try {
            hash = MessageDigest.getInstance("MD5").digest(
                    string.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }
}