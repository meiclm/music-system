package com.music.demo.mapper;

import com.music.demo.entities.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {

//主键查询,关系到根据用户名查询歌曲
    public User findUsername(String username);

    public void insertUser(User user);

    public void updateUser(User user);

}
