package com.music.demo.controller;

import com.music.demo.entities.*;
import com.music.demo.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class SongsController {
    @Autowired
    SongMapper songMapper;
    @Autowired
    SingerMapper singerMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    LikeMapper likeMapper;
    //    获取我的歌曲数据，并返回页面
    @GetMapping("/user/songByu")
    public String findSongByUsername(HttpSession session, Model model) {
        String username = (String) session.getAttribute("loginUser");
        if (username == null) {
            return "redirect:/login";
        }
        List<Song> songList = songMapper.findSongByUsername(username);

        List<Like> likes = likeMapper.findAllLikeByUsername(username);

        User user = userMapper.findUsername(username);
        user.setPicUrl("http://localhost:8080/upload/" + user.getPicUrl());
        session.setAttribute("currentUser", user);
        model.addAttribute("myList", songList);
        model.addAttribute("likeList", likes);
        return "user/mySongList";
    }

    //    删除歌曲
    @GetMapping("/song/{id}")
    public String deleteSong(@PathVariable("id") Integer songId, HttpSession session) {
        String username = (String) session.getAttribute("loginUser");

//        删除所有我喜欢列表的该首歌
        List<Like> likes = likeMapper.findBySongId(songId);
        if (likes.size() > 0) {
            for (Like like : likes) {
                likeMapper.deleteLike(like.getLikeId());
            }
        }

//        删除这首歌的占用大小
        Song song=songMapper.findSongBySongId(songId);
        User u=userMapper.findUsername(username);
        u.setUsedSize(u.getUsedSize()-song.getFileSize());
        userMapper.updateUser(u);
        songMapper.deleteSongById(songId);

        int song_count=songMapper.findUsernameWithSongCount(username);
//        删了一首歌后，计算该用户的还剩多少歌曲,每5首一个等级
        if (song_count > 0&&(song_count+1)%5==0) {
            User user = userMapper.findUsername(username);
            user.setRate(user.getRate() - 1);
            userMapper.updateUser(user);
        }
        return "redirect:/user/songByu";
    }

    //    歌曲上传操作
    @PostMapping(value = "/song/upload")
    public String fileOprt(Song song, @RequestParam("file") MultipartFile file, @RequestParam("txtFile") MultipartFile txtFile, Map<String, Object> map) {
        String fileName = "";
        System.out.println("上传的信息：" + song.toString());
        if (file.isEmpty()) {
            map.put("songError", "没有上传音频'mp3'文件");
            return "/user/uploadPage";
        } else {

            List<Song> oldSong = songMapper.findSongBySongName("%" + song.getSname() + "%");//查找该歌名的歌，模糊查询
            //歌名和歌手相同
            if (oldSong.size() > 0) {
                for (Song s : oldSong) {
                    if (s.getSinger().getName().equals(song.getSinger().getName())) {
                        map.put("songError", "该文件已经存在了");
                        return "/user/uploadPage";
                    }
                }
            }

//             没有这首歌的情况
//            判断可用空间
            System.out.println("文件的大小"+file.getSize());
            User user = userMapper.findUsername(song.getUsername());
            if (user.getUsedSize() == (user.getMaxSize() * 1024 * 1024)
                    || file.getSize() > (user.getMaxSize() * 1024 * 1024 - user.getUsedSize())) {
                map.put("songError", "该用户的歌曲的存储空间不足！");
                return "/user/uploadPage";
            }

            song.setSname(song.getSinger().getName() + " - " + song.getSname());//歌手 - 歌名.mp3
            String originName = file.getOriginalFilename();
            String suffixName = originName.substring(originName.lastIndexOf("."));//后缀名
            fileName = "audio/" + song.getSname() + suffixName;//数据库保存的路径

            song.setFilePath(fileName);
            UserController userController = new UserController();
            userController.uploadFile(file, fileName);

            if (!txtFile.isEmpty()) {
                String txtOrigin=txtFile.getOriginalFilename();
                String suffixName_txt = txtOrigin.substring(txtOrigin.lastIndexOf("."));
                String txtName = "txt/" + song.getSinger().getName() + "-" + song.getSname() + suffixName_txt;
                song.setTxtUrl(txtName);
                userController.uploadFile(txtFile, txtName);

            } else {
                song.setTxtUrl("null");
            }

            //         获取当前的时间
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            song.setSdate(df.format(new Date()));

//            歌手信息插入或获取
            Singer singer = singerMapper.findBySingerName(song.getSinger().getName());
            if (singer != null) {
                song.getSinger().setId(singer.getId());
            } else {
                singerMapper.insertSinger(song.getSinger().getName());
                String singerName = song.getSinger().getName();
                Singer singerId = singerMapper.findBySingerName(singerName);
                song.getSinger().setId(singerId.getId());
            }

            song.setFileSize(file.getSize());
            song.setZanCount(0);
            song.setClickCount(0);
            song.setThumbsDown(0);
            songMapper.insertSong(song);

//             获取用户的歌曲数量，每5首，增加一个等级
            User u = userMapper.findUsername(song.getUsername());
            System.out.println("更新前使用过的大小："+u.getUsedSize());
            u.setUsedSize(u.getUsedSize()+file.getSize());
            System.out.println("更新后："+u.getUsedSize());

            int songCount=songMapper.findUsernameWithSongCount(u.getUsername());

            if (songCount%5==0&&songCount>5){
                u.setRate(u.getRate()+1);
                u.setMaxSize(u.getMaxSize()*2);
                System.out.println("等级升一级后："+u.getMaxSize());
            }

            userMapper.updateUser(u);
            return "redirect:/user/songByu";
        }
    }

    //    来到修改页面
    @GetMapping("/song/upPage/{id}")
    public String toUpdate(Model model, @PathVariable("id") Integer id) {
        model.addAttribute("songById", songMapper.findSongBySongId(id));
        return "user/uploadPage";
    }

    //    歌曲信息修改
    @PutMapping("/song/upload")
    public String fileUpdate(Song song, @RequestParam("file") MultipartFile file,HttpSession session) {
        //         获取当前的时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式;
        song.setSdate(df.format(new Date()));
//        改变歌手
        Singer singer = singerMapper.findBySingerName(song.getSinger().getName());
        if (singer == null) {
            singerMapper.insertSinger(song.getSinger().getName());
            singer = singerMapper.findBySingerName(song.getSinger().getName());
        }
        song.getSinger().setId(singer.getId());
        song.getSinger().setName(singer.getName());

        String filePath = "audio/" + singer.getName() + "-" + song.getSname() + song.getFilePath().lastIndexOf(".");
//      重新上传文件
        if (!file.isEmpty()) {
            filePath = "audio/" + singer.getName() + "-" + song.getSname() +
                    file.getOriginalFilename().lastIndexOf(".");
            UserController userController = new UserController();
            userController.uploadFile(file, filePath);
            String username=(String) session.getAttribute("loginUser");
            userMapper.findUsername(username);
        }
        song.setFilePath(filePath);
        songMapper.updateSong(song);
        return "redirect:/user/songByu";
    }

}
