package com.iot.service;

import com.iot.mapper.UserMapper;
import com.iot.pojo.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 根据主键查询
     * @param id :需要查询数据的主键id
     * @return :
     */
    @Transactional(propagation = Propagation.REQUIRED,readOnly = true)
    public User queryById(Long id ) {
        return userMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询账号和密码
     * @param account:
     * @param passwd:
     * @return :
     */
    @Transactional(propagation = Propagation.REQUIRED,readOnly = true)
    public List<User> queryUsernameAndPassword(String account, String passwd) {
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("account", account)
                .andEqualTo("passwd", passwd);
        return userMapper.selectByExample(example);
    }


    /**
     * 添加一条数据
     * @param user :添加数据信息
     * @return :
     */
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = {Exception.class})
    public Integer add(User user) {
        user.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return userMapper.insert(user);
    }

    /**
     * 根据主键更新
     * @param id :需要更新数据的主键id
     * @param user :更新的封装对象
     * @return :
     */
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = {Exception.class})
    public Integer updateById(Long id, User user) {
        user.setId(id);
        user.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        return userMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * 根据主键删除逻辑一条数据
     * @param id :需要逻辑删除的数据主键id
     * @return :
     */
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = {Exception.class})
    public Integer deleteById(Long id) {
        User user = new User();
        user.setId(id);
        user.setIsDelete(1);//1:逻辑删除
        return userMapper.updateByPrimaryKeySelective(user);
    }
}
