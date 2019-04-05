package com.wqxiu.Dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 18-7-13.
 */
@Repository
public interface BusDao {

    //将车上的人数上传到服务器数据库
      int upLoadPeoplenum(@Param("busid") int busid, @Param("peoplenum") int peoplenum, @Param("nowdate") String nowdate);
}
