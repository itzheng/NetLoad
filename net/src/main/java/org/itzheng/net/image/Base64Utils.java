package org.itzheng.net.image;

import android.util.Base64;

/**
 * Title:<br>
 * Description: <br>
 *
 * @email ItZheng@ZoHo.com
 * Created by itzheng on 2018-5-14.
 */
public class Base64Utils {
    /**
     * 将base64字符还原成byte数组
     *
     * @param base64Str
     * @return
     */
    public static byte[] decode(String base64Str) {
        return Base64.decode(base64Str, Base64.CRLF | Base64.NO_WRAP);
    }

    /**
     * 将byte数组转成base64字符串
     *
     * @param bytes
     * @return
     */
    public static String encodeToString(byte[] bytes) {
        String base64Str = Base64.encodeToString(bytes, 0, bytes.length, Base64.CRLF | Base64.NO_WRAP);
        return base64Str;
    }
}
