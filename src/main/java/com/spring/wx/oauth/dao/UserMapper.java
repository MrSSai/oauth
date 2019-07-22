package com.spring.wx.oauth.dao;

import com.spring.wx.oauth.entity.UserEntity;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select("select * from tb_system_user where openid = #{openid}")
    UserEntity getOpenid(@Param(value = "openid") String openid);

    @Insert("insert into tb_system_user(id, type, openid, nickname, phone, email, image) values(#{id}, #{type}, #{openid}, #{nickname}, #{phone}, #{email}, #{image})")
    @SelectKey(statement = "select concat('ac-', date_format(now(), '%Y%m%d'), '-', lower(uuid())) as id", keyProperty = "id", resultType = String.class, before = true)
    int insert(UserEntity userEntity);

    @Update("update tb_system_user set nickname = #{nickname}, image = #{image} where openid = #{openid}")
    int update(UserEntity userEntity);

    @Update("update tb_system_user set phone = #{phone}, email = #{email} where openid = #{openid}")
    int register(@Param(value = "openid") String openid, @Param(value = "phone") String phone, @Param(value = "email") String email);


}
