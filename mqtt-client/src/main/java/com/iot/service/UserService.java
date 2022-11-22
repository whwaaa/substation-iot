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
     * 查询未授权的账号
     * @return :
     */
    @Transactional(propagation = Propagation.REQUIRED,readOnly = true)
    public List<User> queryIsDeleteList() {
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("isDelete", 1);
        return userMapper.selectByExample(example);
    }


    /**
     * 查询重置密码的账户
     * @return :
     */
    @Transactional(propagation = Propagation.REQUIRED,readOnly = true)
    public List<User> queryResetPasswd() {
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("isDelete", 2);
        return userMapper.selectByExample(example);
    }



    /**
     * 添加一条数据
     * @param user :添加数据信息
     * @return :
     */
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = {Exception.class})
    public Integer add(User user) {
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("account", user.getAccount());
        List<User> users = userMapper.selectByExample(example);
        if ( users!=null && users.size()!=0 && users.get(0)!=null )
            return null;
        user.setIsDelete(1);
        user.setPasswd("123456");
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
     * 授权账号
     * @param account :
     * @return :
     */
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = {Exception.class})
    public Integer authAccount(String account) {
        User user = new User();
        user.setIsDelete(0);
        user.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("account", account);
        return userMapper.updateByExampleSelective(user, example);
    }

    /**
     * 撤销密码重置
     * @param account :
     * @return :
     */
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = {Exception.class})
    public Integer requestResetAccount(String account) {
        User user = new User();
        user.setIsDelete(2);
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("account", account);
        return userMapper.updateByExampleSelective(user, example);
    }

    /**
     * 撤销密码重置
     * @param account :
     * @return :
     */
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = {Exception.class})
    public Integer unResetAccount(String account) {
        User user = new User();
        user.setIsDelete(0);
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("account", account);
        return userMapper.updateByExampleSelective(user, example);
    }
    /**
     * 重置密码
     * @param account :
     * @return :
     */
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = {Exception.class})
    public Integer resetAccount(String account) {
        User user = new User();
        user.setIsDelete(0);
        user.setPasswd("123456");
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("account", account);
        return userMapper.updateByExampleSelective(user, example);
    }



    /**
     * 根据主键删除一条数据
     * @return :
     */
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = {Exception.class})
    public Integer deleteById(Long id) {
        return userMapper.deleteByPrimaryKey(id);
    }
}
