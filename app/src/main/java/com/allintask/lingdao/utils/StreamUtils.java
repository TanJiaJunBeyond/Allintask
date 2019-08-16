package com.allintask.lingdao.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

/**
 * Created by TanJiaJun on 2018/1/17.
 */

public class StreamUtils {

    public static void save(File file, byte[] data) throws Exception {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(data);
        fileOutputStream.close();
    }

    public static String readLine(PushbackInputStream pushbackInputStream) throws IOException {
        char buf[] = new char[128];
        int room = buf.length;
        int offset = 0;
        int c;

        loop:
        while (true) {
            switch (c = pushbackInputStream.read()) {
                case -1:
                case '\n':
                    break loop;

                case '\r':
                    int c2 = pushbackInputStream.read();
                    if ((c2 != '\n') && (c2 != -1)) pushbackInputStream.unread(c2);
                    break loop;

                default:
                    if (--room < 0) {
                        char[] lineBuffer = buf;
                        buf = new char[offset + 128];
                        room = buf.length - offset - 1;
                        System.arraycopy(lineBuffer, 0, buf, 0, offset);
                    }

                    buf[offset++] = (char) c;
                    break;
            }
        }

        if ((c == -1) && (offset == 0)) {
            return null;
        }
        return String.copyValueOf(buf, 0, offset);
    }

    /**
     * 读取流
     *
     * @param inputStream
     * @return 字节数组
     * @throws Exception
     */
    public static byte[] readStream(InputStream inputStream) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;

        while ((len = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, len);
        }

        byteArrayOutputStream.close();
        inputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

}
