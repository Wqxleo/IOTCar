package com.wqxiu.Service;

/**
 * Created by Administrator on 18-7-13.
 */

import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.TooManyListenersException;

/**
 * Created by Administrator on 18-7-10.
 */

import com.wqxiu.Utils.ByteUtils;
import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.TooManyListenersException;

/**
 * 串口参数的配置 串口一般有如下参数可以在该串口打开以前进行配置：
 * 包括波特率，输入/输出流控制，数据位数，停止位和奇偶校验。
 */
// 注：串口操作类一定要继承SerialPortEventListener
public class  SerialPortTest1 implements SerialPortEventListener {
    // 检测系统中可用的通讯端口类
    private CommPortIdentifier portId;
    // 枚举类型
    private Enumeration<CommPortIdentifier> portList;

    // RS232串口
    private static SerialPort serialPort;

    // 输入输出流
    private static InputStream inputStream;
    private static OutputStream outputStream;

    // 保存串口返回信息
    public static String test = "";

    // 单例创建
    private static SerialPortTest1 uniqueInstance = new SerialPortTest1();

    // 初始化串口
    @SuppressWarnings("unchecked")
    public void init() {
        System.out.println("初始化串口。。。。。。。。。。");
        // 获取系统中所有的通讯端口
        portList = CommPortIdentifier.getPortIdentifiers();
        // 循环通讯端口
        while (portList.hasMoreElements()) {
            portId = portList.nextElement();
            // 判断是否是串口
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                // 比较串口名称是否是"COM1"
                if ("COM4".equals(portId.getName())) {
                    System.out.println("找到串口COM1");
                    // 打开串口
                    try {
                        // open:（应用程序名【随意命名】，阻塞时等待的毫秒数）
                        serialPort = (SerialPort) portId.open(Object.class.getSimpleName(), 2000);
                        System.out.println("获取到串口对象,COM1");
                        // 设置串口监听
                        serialPort.addEventListener(this);
                        // 设置串口数据时间有效(可监听)
                        serialPort.notifyOnDataAvailable(true);
                        // 设置串口通讯参数
                        // 波特率，数据位，停止位和校验方式
                        // 波特率2400,偶校验
                        serialPort.setSerialPortParams(115200, SerialPort.DATABITS_8,//
                                SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                        test = "";
                        outputStream = serialPort.getOutputStream();

                    } catch (PortInUseException e) {
                        e.printStackTrace();
                    } catch (TooManyListenersException e) {
                        e.printStackTrace();
                    } catch (UnsupportedCommOperationException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    // 实现接口SerialPortEventListener中的方法 读取从串口中接收的数据
    @Override
    public void serialEvent(SerialPortEvent event) {
        switch (event.getEventType()) {
            case SerialPortEvent.BI:	//通讯中断
            case SerialPortEvent.OE:	//溢位错误
            case SerialPortEvent.FE:	//帧错误
            case SerialPortEvent.PE:	//奇偶校验错误
            case SerialPortEvent.CD:	//载波检测
            case SerialPortEvent.CTS:	//清除发送
            case SerialPortEvent.DSR:	//数据设备准备好
            case SerialPortEvent.RI:	//响铃侦测
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:	//输出缓冲区已清空
                break;
            case SerialPortEvent.DATA_AVAILABLE:	//有数据到达
                readComm();
                break;
            default:
                break;
        }
    }

    // 读取串口返回信息
    public static void readComm() {
        byte[] readBuffer = new byte[1024];
        try {
            inputStream = serialPort.getInputStream();
            // 从线路上读取数据流
            int len = 0;


            len = inputStream.available();
            while (len != 0) {
                // 初始化byte数组为buffer中数据的长度
                readBuffer = new byte[len];
                inputStream.read(readBuffer);
                len = inputStream.available();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        String data = ByteUtils.bytesToHexString(readBuffer);
        //System.out.println("实时消息：" + data + " 时间："+ new Date());
        BusService.preHandle(data);

    }




    /**
     * 向串口发送数据

     */
    public  static void sendToPort( byte[] order) {
        try {
            outputStream = serialPort.getOutputStream();
            outputStream.write(order);
            outputStream.flush();
        } catch (IOException e) {

        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                    outputStream = null;
                }
            } catch (IOException e) {

            }
        }
        System.out.println("发送消息：" + ByteUtils.bytesToHexString(order) + " 时间："+ new Date());

    }

    // 关闭串口
    public void closeSerialPort() {
        if (serialPort != null) {
            serialPort.notifyOnDataAvailable(false);
            serialPort.removeEventListener();
            if (inputStream != null) {
                try {
                    inputStream.close();
                    inputStream = null;
                }
                catch (IOException e) {}
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                    outputStream = null;
                }
                catch (IOException e) {}
            }
            serialPort.close();
            serialPort = null;
        }
    }




}