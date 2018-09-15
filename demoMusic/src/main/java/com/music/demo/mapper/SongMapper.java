package com.music.demo.mapper;

import com.music.demo.entities.Singer;
import com.music.demo.entities.Song;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SongMapper {
    public List<Song> findAllSong();

    public List<Song> findSongByUsername(String username);

    public int findUsernameWithSongCount(String username);

    public List<Song> findSongBySongName(@Param("sname") String sname);//通过歌名查歌
    public List<Song> findSongBySingerId(Integer singerId);

    public Song findSongBySongId(@Param("songId") Integer songId);
    public List<Song> findBySingerNameOrSongName(@Param("str")String str);

    public List<Song> findByTimeAndNationality(@Param("sdate") String sdate,
                                               @Param("nationality") String nationality);

    public List<Song> finByClickCountTop();
    public List<Song> findByZanTop();
    public List<Song> findAlien();

    public List<Song> findByStyle(@Param("style") String style);

    public void updateSong(Song song);

    public void insertSong(Song song);

    public void deleteSongById(Integer id);



}
