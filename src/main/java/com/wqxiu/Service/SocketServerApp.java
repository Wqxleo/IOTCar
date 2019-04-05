package com.wqxiu.Service;

import com.wqxiu.Entity.BusEntity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 18-7-13.
 */
public class SocketServerApp implements Runnable{
    public int port;

    public SocketServerApp(int port) {
        this.port = port;
    }

    @Override
    public void run() {

        try {
            ServerSocket server = new ServerSocket(port);
            Socket socket = server.accept();


            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                public void run() {
                    //等待client的请求
                    System.out.println("waiting...");

                    // 接收客户端的数据
                    DataInputStream in = null;
                    try {
                        in = new DataInputStream(socket.getInputStream());
                        String string = in.readUTF();
                        System.out.println("client:" + string);
                        if(string == "bye"){
                            socket.close();
                        }
                        // 发送给客户端数据

                        String data = BusEntity.tem + " " + BusEntity.hum + " " + BusEntity.Light + " " + BusEntity.ultrasound1+ " " + BusEntity.ultrasound2 + " " + BusEntity.peoplenum + " " + BusEntity.shake + " " + BusEntity.smoke;
                        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                        out.writeUTF(data);
                        System.out.println("发送数据： "+data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }, 1000,2000);// 设定指定的时间time,此处为2000毫秒

            /*while (true) {
                //等待client的请求
                System.out.println("waiting...");

                // 接收客户端的数据
                DataInputStream in = new DataInputStream(socket.getInputStream());
                String string = in.readUTF();
                System.out.println("client:" + string);
                if(string == "bye"){
                    socket.close();
                }
                // 发送给客户端数据

                String data = BusEntity.tem + " " + BusEntity.hum + " " + BusEntity.Light + " " + BusEntity.ultrasound1+ " " + BusEntity.ultrasound2 + " " + BusEntity.peoplenum + " " + BusEntity.shake + " " + BusEntity.smoke;
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                out.writeUTF(data);
                System.out.println("发送数据： "+data);

            }*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
