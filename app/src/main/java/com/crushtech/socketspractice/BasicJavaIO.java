package com.crushtech.socketspractice;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class BasicJavaIO {
    //all bytes stream classes are descendants of inputStream and outputStream
    static class CopyBytes {
        public static void main(String[] args) throws IOException {
            try (FileInputStream in = new FileInputStream("/Users/dremo/Desktop/xanadu.txt");
                 FileOutputStream out = new FileOutputStream("/Users/dremo/Desktop/out.txt")) {
                int c;
                while ((c = in.read()) != -1) {
                    out.write(c);
                }
            }
        }
    }
}
