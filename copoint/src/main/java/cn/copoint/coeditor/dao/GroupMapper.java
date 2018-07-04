package cn.copoint.coeditor.dao;

import cn.copoint.coeditor.entity.GroupEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper//@Mapper 表示这是一个Mybatis Mapper接口。
@Repository
public interface GroupMapper {
    //    使用@Select注解表示调用findAll方法会去执行对应的sql语句。
    @Select("select * from t_group")
    List<GroupEntity> findAll();
    @Select("select * from t_group where id= #{id} ")
    public GroupEntity get(int id);
    @Select("select * from t_group where authorName= #{authorName} ")
    public List<GroupEntity> getByAuthorName(String authorName);
    @Select("select * from t_group where memberName= #{memberName} ")
    public List<GroupEntity> getByMemberName(String memberName);
    @Select("select * from t_group where groupName= #{groupName} ")
    public List<GroupEntity> getByGroupName(String groupName);
    @Insert(" insert into t_group ( authorName,groupName,memberName ) values (#{authorName},#{groupName},#{memberName}) ")
    public int save(GroupEntity group);

//    @Delete(" delete from t_group where id= #{id} ")
//    public void delete(int id);
//
//    @Select("select * from t_group where id= #{id} ")
//    public GroupEntity get(int id);

//    @Update("update t_group set name=#{name} where id=#{id} ")
//    public int update(GroupEntity group);
}