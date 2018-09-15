package com.music.demo.controller;

import com.music.demo.entities.Singer;
import com.music.demo.mapper.SingerMapper;
import com.music.demo.mapper.SongMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SingerController {

    @Autowired
    SongMapper songMapper;

    @Autowired
    SingerMapper singerMapper;

//    取得全部的歌手
    @GetMapping("/singer")
    public String singer(Model model){
        model.addAttribute("singersList", singerMapper.getAllSinger());
        return "singer";
    }

//    取得该歌手的歌曲
    @GetMapping("/singer/{id}")
    public String singerSong(@PathVariable("id") Integer sid, Model model){
        model.addAttribute("singerSong",songMapper.findSongBySingerId(sid)) ;
        Singer singer=singerMapper.findBySingerId(sid);
        System.out.println(singer.getName());
        model.addAttribute("singerName",singer);
        return "show";
    }

}
