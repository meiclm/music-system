package com.music.demo.mapper;

import com.music.demo.entities.Singer;
import com.music.demo.entities.Song;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SingerMapper {

    public List<Singer> getAllSinger();

    public Singer findBySingerId(Integer sid);

    public Singer findBySingerName(String singerName);

    public void insertSinger(String name);

    public List<Singer> findByNationality(String nationality);

    public List<Singer> findByNationAndSex(@Param("nationality")String nationality,@Param("sex") Integer sex);

}
