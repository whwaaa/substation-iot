package com.iot.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.iot.mapper.DeviceMsgMapper;
import com.iot.pojo.DeviceMsg;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;

@Service
public class DeviceMsgService {

    @Resource
    private DeviceMsgMapper deviceMsgMapper;

    /**
     * 根据主键查询
     * @param id :需要查询数据的主键id
     * @return :
     */
    @Transactional(propagation = Propagation.REQUIRED,readOnly = true)
    public DeviceMsg queryById( Long id ) {
        return deviceMsgMapper.selectByPrimaryKey(id);
    }


    /**
     * 根据设备id查询最后的状态
     * @param dId: 设备id
     * @return :
     */
    @Transactional(propagation = Propagation.REQUIRED,readOnly = true)
    public DeviceMsg queryLatestBydId( Long dId ) {
        List<DeviceMsg> lastBydId = deviceMsgMapper.findLastBydId(dId);
        if ( lastBydId.size() != 0 )
            return lastBydId.get(0);
        return null;
    }

    /**
     * 添加一条数据
     * @param deviceMsg :添加数据信息
     * @return :
     */
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = {Exception.class})
    public Integer add(DeviceMsg deviceMsg) {
        deviceMsg.setTime(new Timestamp(System.currentTimeMillis()));
        return deviceMsgMapper.insert(deviceMsg);
    }

    /**
     * 根据主键更新
     * @param id :需要更新数据的主键id
     * @param deviceMsg :更新的封装对象
     * @return :
     */
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = {Exception.class})
    public Integer updateById(Long id, DeviceMsg deviceMsg) {
        deviceMsg.setId(id);
        deviceMsg.setTime(new Timestamp(System.currentTimeMillis()));
        return deviceMsgMapper.updateByPrimaryKeySelective(deviceMsg);
    }

    /**
     * 根据主键删除一条数据
     * @param id :需要删除的数据主键id
     * @return :
     */
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = {Exception.class})
    public Integer deleteById(Long id) {
        return deviceMsgMapper.deleteByPrimaryKey(id);
    }

}
