package com.pgq.common.util;

import java.io.*;

public class FileUtil
{
    public static void writeBytesToFile(byte[] bs, String filePath) throws IOException
    {
        writeBytesToFile(bs, new File(filePath));
    }

    public static void writeBytesToFile(byte[] bs, File file) throws IOException
    {
        OutputStream out = new FileOutputStream(file);
        InputStream is = new ByteArrayInputStream(bs);
        byte[] buff = new byte[1024];
        int len = 0;
        while ((len = is.read(buff)) != -1)
        {
            out.write(buff, 0, len);
        }
        is.close();
        out.close();
    }

    public byte[] getBytes(String filePath) throws IOException
    {
        File file = new File(filePath);
        return getBytes(file);
    }

    public byte[] getBytes(File file) throws IOException
    {

        long fileSize = file.length();
        if (fileSize > Integer.MAX_VALUE)
        {
            System.out.println("file too big...");
            return null;
        }
        FileInputStream fi = new FileInputStream(file);
        byte[] buffer = new byte[(int) fileSize];
        int offset = 0;
        int numRead = 0;
        while (offset < buffer.length
                && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0)
        {
            offset += numRead;
        }
        // 确保所有数据均被读取
        if (offset != buffer.length)
        {
            throw new IOException("Could not completely read file "
                    + file.getName());
        }
        fi.close();
        return buffer;
    }
}
