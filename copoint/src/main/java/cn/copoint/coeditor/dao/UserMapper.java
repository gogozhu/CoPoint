package cn.copoint.coeditor.dao;


import cn.copoint.coeditor.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper//@Mapper 表示这是一个Mybatis Mapper接口。
@Repository
public interface UserMapper {
    @Select("select * from t_user")
    List<UserEntity> findAll();
    //    https://blog.csdn.net/qq_35261296/article/details/73559247
    @Select("select * from t_user where name=#{name} and pwd=#{pwd}")
    List<UserEntity> findOne(String name,String pwd);
    @Select("select * from t_user where name=#{name} and pwd=#{pwd}")
    List<UserEntity> findOneUserEntity(UserEntity user);
}
