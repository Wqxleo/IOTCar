package com.wqxiu.Controller;

import com.wqxiu.Service.BusService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 18-7-13.
 */
@RestController
@RequestMapping("/businfo")
@ResponseBody
public class BusController {
    @Autowired
    BusService busService;
    @ApiOperation(value = "窗帘上升",httpMethod = "GET")
    @RequestMapping(value = "/curtainup")
    @ResponseBody
    public void curtainUp(){
        System.out.println("窗帘上升！");
        BusService.motorRevolve();
    }

    @ApiOperation(value = "窗帘停止",httpMethod = "GET")
    @RequestMapping(value = "/curtainstop")
    @ResponseBody
    public void curtainStop(){
        System.out.println("窗帘停止！");
        BusService.motorStop();
    }

    @ApiOperation(value = "窗帘下降",httpMethod = "GET")
    @RequestMapping(value = "/curtaindown")
    @ResponseBody
    public void curtainDown(){
        System.out.println("窗帘下降！");
        BusService.motorRevolveBack();
    }

    //向数据库传送数据
    @ApiOperation(value = "上传公交车上的人数到数据库",httpMethod = "GET")
    @RequestMapping(value = "/uploadpeoplenum/{peoplenum}")
    @ResponseBody
    public int uploadpeoplenum(@PathVariable("peoplenum") int peoplenum){
        System.out.println("controller--request:uploadpeoplenum>>"+peoplenum );
        return  busService.upLoadPeopleNum(peoplenum);
    }



}
