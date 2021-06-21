package com.crushtech.socketspractice;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public static void main(String[] args) {
        try {
            //open a socket
            Socket socket = new Socket("127.0.0.1", 2323);
            //gets  output stream from the socket
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            //get an input stream from the socket
            BufferedReader in = new BufferedReader(new InputStreamReader
                    (socket.getInputStream()));

            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
                System.out.println("echo: " + in.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}