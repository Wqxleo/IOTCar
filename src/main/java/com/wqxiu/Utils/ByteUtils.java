package com.wqxiu.Utils;

/**
 * Created by Administrator on 18-7-13.
 */
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

/*
 * 用于各种类型，进制与byte之间的转换
 *
 * */
public class ByteUtils {
    private static String hexString="0123456789ABCDEF";


    /*将字符串转化为16进制，存入字符串数组中*/
    public static String string2Hex(String content) {
        byte[] bytes = content.getBytes();
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for(int i = 0; i < bytes.length; i++) {
            sb.append(hexString.charAt((bytes[i]&0xf0)>>4));
            sb.append(hexString.charAt((bytes[i]&0x0f)>>0));
        }
        return sb.toString();
    }

    /**将16进制字符串转化为10进制字符串**/
    public static String hexStr2Str(String hexStr)
    {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;

        for (int i = 0; i < bytes.length; i++)
        {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }

    /*将16进制字符串转化为10进制字符串*/
    public static String toStringHex(String s)
    {
        byte[] baKeyword = new byte[s.length()/2];
        for(int i = 0; i < baKeyword.length; i++)
        {
            try
            {
                baKeyword[i] = (byte)(0xff & Integer.parseInt(s.substring(i*2, i*2+2),16));
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        try
        {
            s = new String(baKeyword, "utf-8");//UTF-16 le:Not
        }
        catch (Exception e1)
        {
            e1.printStackTrace();
        }
        return s;
    }

    /*将16进制字符串转化为bytes数组*/
    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte( achar[pos + 1] ));
        }
        return result;
    }

    /*将bytes数组转化为16进制字符串*/
    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString().toUpperCase();
    }

    /*将十六进制字符串解码成字符串*/
    public static String decode(String bytes)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream( bytes.length() / 2 );
        //将每2位16进制整数组装成一个字节
        for(int i = 0; i < bytes.length() ; i+=2)
            baos.write((hexString.indexOf(bytes.charAt(i))<<4 | hexString.indexOf(bytes.charAt(i+1))));
        return new String(baos.toByteArray());
    }



    /*将Short Int类型转化为16进制再转化为byte数组*/
    private static byte toByte(char c) {
        byte b = (byte) hexString.indexOf(c);
        return b;
    }


    /*将INT类型转化为10进制byte数组（占4字节）*/
    public static byte[] int2Bytes(int num) {
        byte[] byteNum = new byte[4];
        for (int ix = 0; ix < 4; ++ix) {
            int offset = 32 - (ix + 1) * 8;
            byteNum[ix] = (byte) ((num >> offset) & 0xff);
        }
        return byteNum;
    }


    /*将int类型转化为16进制数，转化为byte数组类型*/
    public static byte[] intToHexToBytes(short num) {
        byte[] byteNum = new byte[]{(byte)((num>>8)&0xFF), (byte)(num&0xFF)};
        return byteNum;
    }

    /*将int类型转化为16进制数，转化为byte类型*/
    public static byte intToHexToByte(int integer) {
        String hexStr = Integer.toHexString(integer);
        //Log.i("data", hexStr);
        return Byte.valueOf(hexStr,16);
    }


    /*将int类型转化为byte类型*/
    public static byte int2OneByte(int num) {
        return (byte) (num & 0x000000ff);
    }


    /*将2个字节的byte数组转化为int类型*/
    public static int twoBytes2Int(byte[] buffer) {
        return buffer[0] | buffer[1] << 8;
    }

    /*将byte类型数转化为int类型*/
    public static int oneByte2Int(byte byteNum) {
        return byteNum & 0xFF;
    }

    /*将16进制的byte类型转化为10进制的int类型*/
    public static int byteToInt16(byte b) {
        String result = Integer.toHexString(b & 0xFF);
        return Integer.valueOf(result, 16);
    }

    /*将byte类型数组（4字节）转化为int类型*/
    public static int bytes2Int(byte[] byteNum) {
        int num = 0;
        for (int ix = 0; ix < 4; ++ix) {
            num <<= 8;
            num |= (byteNum[ix] & 0xff);
        }
        return num;
    }



    /*将长整形转化为byte数组*/
    public static byte[] long2Bytes(long num) {
        byte[] byteNum = new byte[8];
        for (int ix = 0; ix < 8; ++ix) {
            int offset = 64 - (ix + 1) * 8;
            byteNum[ix] = (byte) ((num >> offset) & 0xff);
        }
        return byteNum;
    }

    /*将byte数组（长度为8）转化为长整形*/
    public static long bytes2Long(byte[] byteNum) {
        long num = 0;
        for (int ix = 0; ix < 8; ++ix) {
            num <<= 8;
            num |= (byteNum[ix] & 0xff);
        }
        return num;
    }




    /**将16进制的byte数组转化为float类型**/
    public static float byte162float(byte[] num){
        String hexString=bytesToHexString(num);
        Integer temp=Integer.valueOf(hexString.trim(), 16);
        float value=Float.intBitsToFloat(temp.intValue());
        System.out.println(value);
        return value;
    }


    /**将float转化为byte数组，占用4个字节**/
    public static byte [] float2ByteArray (float value)
    {
        return ByteBuffer.allocate(4).putFloat(value).array();
    }

    /**
     * 将10进制byte数组转化为Float
     *
     * @param b 字节（至少4个字节）
     * @param index 开始位置
     * @return
     */
    public static float bytes2float(byte[] b, int index) {
        int l;
        l = b[index + 0];
        l &= 0xff;
        l |= ((long) b[index + 1] << 8);
        l &= 0xffff;
        l |= ((long) b[index + 2] << 16);
        l &= 0xffffff;
        l |= ((long) b[index + 3] << 24);
        return Float.intBitsToFloat(l);
    }


    /*两个byte数组是否值相等的比较*/
    public static boolean byteCompare(byte[] data1, byte[] data2, int len) {
        if (data1 == null && data2 == null) {
            return true;
        }
        if (data1 == null || data2 == null) {
            return false;
        }
        if (data1 == data2) {
            return true;
        }
        boolean bEquals = true;
        int i;
        for (i = 0; i < data1.length && i < data2.length && i < len; i++) {
            if (data1[i] != data2[i]) {
                bEquals = false;
                break;
            }
        }
        return bEquals;
    }

    /*将byte（字节）类型转化为位*/
    public static String byteToBit(byte b) {
        return "" +(byte)((b >> 7) & 0x1) +
                (byte)((b >> 6) & 0x1) +
                (byte)((b >> 5) & 0x1) +
                (byte)((b >> 4) & 0x1) +
                (byte)((b >> 3) & 0x1) +
                (byte)((b >> 2) & 0x1) +
                (byte)((b >> 1) & 0x1) +
                (byte)((b >> 0) & 0x1);
    }



    /*将指定byte数组以16进制的形式打印到控制台 */
    public static void printHexString( byte[] b) {
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            System.out.print(hex.toUpperCase() );
        }
    }

    /*判断字符串中是否出现非数字字符，如果出现非数字字符，返回true，否则返回false*/
    public static  boolean ExistOtherChar(String str){
        String numstr = "0123456789";
        int i = 0;
        for(i = 0 ; i < str.length() ; i++ )
        {
            if(numstr.indexOf(str.charAt(i)) == -1 )
            {
                return true;
            }
        }
        return false;
    }



    /*判断字符串中是否只包含字母，只包含字母，返回true，否则返回false*/
    public  static boolean ExistChar(String str){
        String regex = "[a-zA-Z]+$";
        return str.matches(regex);
    }

    /*判断版本号是否符合格式*/
    public static boolean LegalVersion(String str){
        String regex = "[0-9]+[.][0-9]+[.][0-9]+";
        return str.matches(regex);
    }
}