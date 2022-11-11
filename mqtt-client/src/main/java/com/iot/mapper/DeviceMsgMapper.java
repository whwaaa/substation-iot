package com.iot.mapper;

import com.iot.pojo.DeviceMsg;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface DeviceMsgMapper extends Mapper<DeviceMsg> {
    public List<DeviceMsg> findLastBydId(@Param("dId")  Long dId );
}
