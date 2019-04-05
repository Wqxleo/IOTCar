package com.wqxiu.Service;

import com.wqxiu.Dao.BusDao;
import com.wqxiu.Entity.BusEntity;
import com.wqxiu.Utils.ByteUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static com.wqxiu.Entity.BusEntity.busid;

/**
 * Created by Administrator on 18-7-13.
 */
@Service
@Component
public class BusService {
    @Autowired
    BusDao busDao;

    private BusService busService;

    //处理串口返回的消息
    public static void preHandle(String data){
        String head = data.substring(0,10);
        switch(head){
            //获取温湿度光照
            case "400C010201":
                getTemHumLight(data);
                break;
            //获取震动
            case "4007010301":
                getShake(data);
                break;
            //获取烟雾
            case "4007010401":
                getSmoke(data);
                break;
            //红外上车
            case "4007010501":
                getPIRUp(data);
                break;
            //红外上车
            case "4007020501":
                getPIRDown(data);
                break;
            //超声波1
            case "400A010801":
                getUltraSound1(data);
                break;
            //超声波2
            case "400A020801":
                getUltraSound2(data);
                break;
            default:
                break;
        }
    }

    //电机正转
    public static void motorRevolve(){
        String data = "400601060A57";
        SerialPortTest1.sendToPort(ByteUtils.hexStringToByte(data));
    }

    //电机停止
    public static void motorStop(){
        String data = "400601060C59";
        SerialPortTest1.sendToPort(ByteUtils.hexStringToByte(data));
    }

    //电机反转
    public static void motorRevolveBack(){
        String data = "400601060B58";
        SerialPortTest1.sendToPort(ByteUtils.hexStringToByte(data));
    }

    //获取温湿度光照
    public static void getTemHumLight(String data){

        //System.out.println("获取温度！！！！" + data);

        int XH = Integer.parseInt(data.substring(10,12),16);
        int XL = Integer.parseInt(data.substring(12,14),16);
        int YH = Integer.parseInt(data.substring(14,16),16);
        int YL = Integer.parseInt(data.substring(16,18),16);
        int GH = Integer.parseInt(data.substring(18,20),16);
        int GL = Integer.parseInt(data.substring(20,22),16);
        int tem = XH*256 + XL;
        int hum = YH*256+YL;
        int Light = (int) ((GH*256+GL)*3012.9/(32768*4));

        System.out.println("温度：" + tem + "°C" + "  湿度："+ hum + "%  光照：" +Light+"LX");

        BusEntity.tem = tem;
        BusEntity.hum = hum;
        BusEntity.Light = Light;
        if(BusEntity.Light >= 200){
            motorRevolve();
        }

    }

    //获取震动
    public static void getShake(String data){
        int S = Integer.parseInt(data.substring(10,12),16);
        if(S == 0){
            System.out.println("无震动");
            BusEntity.shake = 0;
        }
        if(S == 1){
            System.out.println("有震动");
            BusEntity.shake = 1;
        }
    }

    //获取烟雾消息
    public static void getSmoke(String data){
        int S = Integer.parseInt(data.substring(10,12),16);
        if(S == 0){
            System.out.println("无烟雾");
            BusEntity.smoke = 0;

        }
        if(S == 1){
            System.out.println("有烟雾");
            BusEntity.smoke = 1;
        }
    }

    //获取红外上车
    public static void getPIRUp(String data){
        int S = Integer.parseInt(data.substring(10,12),16);
        if(S == 0){
            System.out.println("没有人上车");
        }
        if(S == 1){
            System.out.println("有人上车！");
            BusEntity.peoplenum++;
        }
        if (BusEntity.peoplenum >= 20){

        }
    }
    //获取红外下车
    public static void getPIRDown(String data){
        int S = Integer.parseInt(data.substring(10,12),16);
        if(S == 0){
            System.out.println("没有人下车5");
        }
        if(S == 1){
            System.out.println("有人下车！");
            if(BusEntity.peoplenum > 0){
                BusEntity.peoplenum--;
            }
        }
    }


    //获取超声波测距1
    public static void getUltraSound1(String data){
        int DH = Integer.parseInt(data.substring(10,12),16);
        int DL = Integer.parseInt(data.substring(12,14),16);
        double dis = (DH*256 + DL)/1000.00;
        if(dis >= 5){           //有改动
            System.out.println("车前距是：" + dis + "m");
        }
        else {
            System.out.println("警告：车前距是：" + dis + "m ！！！");
        }



        BusEntity.ultrasound1 = dis;
    }
    //获取超声波测距2
    public static void getUltraSound2(String data){
        int DH = Integer.parseInt(data.substring(10,12),16);
        int DL = Integer.parseInt(data.substring(12,14),16);
        double dis = (DH*256 + DL)/1000.00;
        if(dis >= 5){             //有改动
            System.out.println("车后距是：" + dis + "m");
        }
        else {
            System.out.println("警告：车后距是：" + dis + "m ！！！");
        }
        BusEntity.ultrasound2 = dis;
    }

//    @PostConstruct
//    public void init(){
//        busService = this;
//        busService.busDao = this.busDao;
//    }
    public int upLoadPeopleNum(int peoplenum){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowdate = formatter.format(date);
//        System.out.println(peoplenum+"      " +nowdate+"  进入service");
//        init();
        busid = BusEntity.busid;
        return busDao.upLoadPeoplenum(busid,peoplenum,nowdate);
    }

}




