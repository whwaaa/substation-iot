package com.iot.service;

import com.iot.mapper.OperatMapper;
import com.iot.pojo.Operat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;

@Service
public class OperatService {

    @Resource
    private OperatMapper operatMapper;

    /**
     * 根据主键查询
     * @param id :需要查询数据的主键id
     * @return :
     */
    @Transactional(propagation = Propagation.REQUIRED,readOnly = true)
    public Operat queryById(Long id ) {
        return operatMapper.selectByPrimaryKey(id);
    }

    /**
     * 添加一条数据
     * @param operat :添加数据信息
     * @return :
     */
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = {Exception.class})
    public Integer add(Operat operat) {
        operat.setTime(new Timestamp(System.currentTimeMillis()));
        return operatMapper.insert(operat);
    }

    /**
     * 根据主键更新
     * @param id :需要更新数据的主键id
     * @param operat :更新的封装对象
     * @return :
     */
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = {Exception.class})
    public Integer updateById(Long id, Operat operat) {
        operat.setId(id);
        operat.setTime(new Timestamp(System.currentTimeMillis()));
        return operatMapper.updateByPrimaryKeySelective(operat);
    }

    /**
     * 根据主键删除一条数据
     * @param id :需要删除的数据主键id
     * @return :
     */
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = {Exception.class})
    public Integer deleteById(Long id) {
        return operatMapper.deleteByPrimaryKey(id);
    }
}
