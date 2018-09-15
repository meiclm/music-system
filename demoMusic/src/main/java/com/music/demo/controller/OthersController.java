package com.music.demo.controller;

import com.music.demo.entities.Like;
import com.music.demo.entities.Song;
import com.music.demo.mapper.LikeMapper;
import com.music.demo.mapper.SongMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class OthersController {

    @Autowired
    SongMapper songMapper;

    @Autowired
    LikeMapper likeMapper;

    //    搜素框
    @PostMapping("/search")
    public String search(@RequestParam("str") String str, Model model) {
        List<Song> songsOrSingerSong = songMapper.findBySingerNameOrSongName(str);
        if (songsOrSingerSong.size() > 0) {
            model.addAttribute("singerSong", songsOrSingerSong);
        } else {
            model.addAttribute("msgValue", "没有匹配的记录");
        }
        model.addAttribute("str",str);
        return "show";
    }

    //    播放该歌曲
    @GetMapping("/display/{id}")
    public String display(@PathVariable("id") Integer songId, Model model, HttpSession session) {
        String currentUsername = (String) session.getAttribute("loginUser");
        if (currentUsername != null) {
            Like like = likeMapper.findByUsernameAndSongId(currentUsername, songId);
            model.addAttribute("like", like);
        }

        Song song = songMapper.findSongBySongId(songId);
//        播放量+1
        song.setClickCount(song.getClickCount() + 1);
        songMapper.updateSong(song);

        song.setFilePath("http://localhost:8080/upload/" + song.getFilePath());
        System.out.println(song.getTxtUrl());
        model.addAttribute("displaySong", song);
        return "display";
    }

    //    热搜榜
    @GetMapping("/hotTop")
    public String hotTop(Model model) {
        List<Song> songsTop_10 = songMapper.finByClickCountTop();
        model.addAttribute("clickTop", songsTop_10);

        List<Song> songsZan_10 = songMapper.findByZanTop();
        model.addAttribute("zanTop", songsZan_10);

        List<Song> alienSong = songMapper.findAlien();
        model.addAttribute("alienSong", alienSong);
        return "hotTop";
    }

    @GetMapping("/allSong")
    public String getAllSong(Model model){
        model.addAttribute("singerSong",songMapper.findAllSong());
        return "show";
    }

}
