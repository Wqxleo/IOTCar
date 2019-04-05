package com.wqxiu;

import com.wqxiu.Dao.BusDao;
import com.wqxiu.Entity.BusEntity;
import com.wqxiu.Service.BusService;
import com.wqxiu.Service.SerialPortTest1;
import com.wqxiu.Service.SocketServerApp;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@SpringBootApplication
@EnableSwagger2
@EnableAspectJAutoProxy
@MapperScan("com.wqxiu.Dao")//指明映射文件的位置
@Component
public class IotcarApplication {

	private static BusService busService;

	@Autowired
	public IotcarApplication(BusService busService){
		IotcarApplication.busService = busService;
	}
	public static void main(String[] args) {

		SpringApplication.run(IotcarApplication.class, args);

		//新建串口工具类
		SerialPortTest1 sp = new SerialPortTest1();
		//sp.closeSerialPort();
		//串口初始化
		sp.init();

		//新建Socket通信，端口号为9050
		SocketServerApp serverApp = new SocketServerApp(9050);

		//定时器，每五分钟向数据库上传数据
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				try {
					//BusService busService = new BusService();
					System.out.println("尝试上传到云服务器。。。。。");
					int peoplenum = BusEntity.peoplenum;
					busService.upLoadPeopleNum(peoplenum);
					System.out.println("上传到云服务器。。。。。此时公交车上的人数是"+peoplenum);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}, 1000,100*60*1);// 设定指定的时间time,此处为5分钟

		serverApp.run();

		//sp.closeSerialPort();
	}
}
