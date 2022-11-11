package com.iot.service;

import com.iot.mapper.DeviceMapper;
import com.iot.pojo.Device;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;

@Service
public class DeviceService {

    @Resource
    private DeviceMapper deviceMapper;

    /**
     * 根据主键查询
     * @param id :需要查询数据的主键id
     * @return :
     */
    @Transactional(propagation = Propagation.REQUIRED,readOnly = true)
    public Device queryById(Long id ) {
        return deviceMapper.selectByPrimaryKey(id);
    }

    /**
     * 添加一条数据
     * @param device :添加数据信息
     * @return :
     */
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = {Exception.class})
    public Integer add(Device device) {
        device.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return deviceMapper.insert(device);
    }

    /**
     * 根据主键更新
     * @param id :需要更新数据的主键id
     * @param device :更新的封装对象
     * @return :
     */
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = {Exception.class})
    public Integer updateById(Long id, Device device) {
        device.setId(id);
        device.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        return deviceMapper.updateByPrimaryKeySelective(device);
    }

    /**
     * 根据主键删除逻辑一条数据
     * @param id :需要逻辑删除的数据主键id
     * @return :
     */
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = {Exception.class})
    public Integer deleteById(Long id) {
        Device device = new Device();
        device.setId(id);
        device.setIsDelete(1);//1:逻辑删除
        return deviceMapper.updateByPrimaryKeySelective(device);
    }
}







