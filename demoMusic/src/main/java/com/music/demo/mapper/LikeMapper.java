package com.music.demo.mapper;

import com.music.demo.entities.Like;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LikeMapper {
    public void insertLike(Like like);

    public void deleteLike(Integer likeId);
    public void deleteByLike(Like like);

    public List<Like> findBySongId(Integer songId);
    public List<Like> findAllLikeByUsername(String username);

    public Like findByUsernameAndSongId(@Param("currentUsername") String currentUsername,
                                        @Param("songId") Integer songId);
}
