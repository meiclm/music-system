package com.music.demo.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.music.demo.entities.*;
import com.music.demo.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class StaticDataController {
    @Autowired
    SongMapper songMapper;
    @Autowired
    LikeMapper likeMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    SingerMapper singerMapper;

    //    首页的轮播数据
    @GetMapping("/index/nationality")
    public PageInfo<Song> indexByNationality(@RequestParam("national") String national,@RequestParam("page")int page){
        PageHelper.startPage(page,10);
        SimpleDateFormat df = new SimpleDateFormat("MM-dd");//设置日期格式

        List<Song> songByNationality = songMapper.findByTimeAndNationality(df.format(new Date()),national);
        PageInfo<Song> pageInfo=new PageInfo<>(songByNationality);
        return pageInfo;
    }
    //    首页的分页
    @GetMapping("/index/{page}")
    public PageInfo<Song> index(@PathVariable("page") int page,@RequestParam("style") String style){
        PageHelper.startPage(page,5);
        List<Song> songsStyle=songMapper.findByStyle(style);
        PageInfo<Song> pageInfo = new PageInfo<>(songsStyle);
        return pageInfo;
    }

    //    点赞+1
    @RequestMapping("/zanCount/{id}")
    public Integer zanCountAdd(@PathVariable("id") Integer songId,HttpSession session) {
        String currentUser = (String) session.getAttribute("loginUser");
        if (currentUser==null){
            return -1;
        }
        Song song = songMapper.findSongBySongId(songId);
        song.setZanCount(song.getZanCount() + 1);
        songMapper.updateSong(song);
        return song.getZanCount();
    }

    //    赞-1
    @RequestMapping("/zanCount_reduce/{id}")
    public Integer zanCountReduce(@PathVariable("id") Integer songId,HttpSession session) {
        String currentUser = (String) session.getAttribute("loginUser");
        if (currentUser==null){
            return -1;
        }
        Song song = songMapper.findSongBySongId(songId);
        song.setZanCount(song.getZanCount() - 1);
        songMapper.updateSong(song);
        return song.getZanCount();
    }

//    踩操作+1
    @GetMapping("/thumbs_add/{id}")
    public Integer thumbsDown(@PathVariable("id") Integer songId,HttpSession session){
        String currentUser = (String) session.getAttribute("loginUser");
        if (currentUser==null){
            return -1;
        }else {
           Song song= songMapper.findSongBySongId(songId);
           song.setThumbsDown(song.getThumbsDown()+1);
           songMapper.updateSong(song);
           return song.getThumbsDown();
        }
    }

//    踩-1
    @GetMapping("/thumbs_redu/{id}")
    public Integer thumbsDownSUb(@PathVariable("id") Integer songId,HttpSession session){
        String currentUser = (String) session.getAttribute("loginUser");
        if (currentUser==null){
            return -1;
        }else {
            Song song= songMapper.findSongBySongId(songId);
            Integer count=song.getThumbsDown()-1;
            if (count<=0){
                song.setThumbsDown(Integer.valueOf(0));
            }else {
                song.setThumbsDown(count);
            }
            songMapper.updateSong(song);
            return song.getThumbsDown();
        }
    }

    //    喜欢的操作
    @RequestMapping("/like/{id}")
    public Integer addLike(@PathVariable("id") Integer songId, HttpSession session) {
        String currentUser = (String) session.getAttribute("loginUser");
        if (currentUser == null) {
            return -1;
        } else {
            Like like1 = likeMapper.findByUsernameAndSongId(currentUser, songId);
            if (like1 == null) {
                Like like = new Like();
                like.setSongId(songId);
                like.setUsernameId(currentUser);
                likeMapper.insertLike(like);
                return 0;
            } else {
                return 1;
            }
        }
    }

//在我喜欢的列表移出一个我喜欢的歌曲
    @RequestMapping("/removeLike/{id}")
    public Integer reduceLike(@PathVariable("id") Integer likeId) {
        likeMapper.deleteLike(likeId);
        return 0;
    }

//    在播放页面，根据用户名和歌曲id移除我喜欢的歌
    @RequestMapping("/removeLikeWithUsername/{id}")
    public Integer removeLike(@PathVariable("id") Integer songId,HttpSession session){
        String currentUsername=(String) session.getAttribute("loginUser");
        Like like=likeMapper.findByUsernameAndSongId(currentUsername,songId);
        likeMapper.deleteLike(like.getLikeId());
        return 0;
    }

//    下载操作
    @GetMapping("/download")
    public int download(HttpSession session){
        String username=(String) session.getAttribute("loginUser");
        if (username!=null){
            User user=userMapper.findUsername(username);
            if (user!=null){
                int rate=user.getRate();
                if (rate>=2){
                    return 0;
                }else {
                    return 1;
                }
            }
        }
            return -1;
    }


    //    根据国籍性别找歌手
    @RequestMapping("/singer/national")
    public List<Singer> singerByNationality(@RequestParam("national") String nationality,@RequestParam("sex") Integer sex){
        List<Singer> singers=singerMapper.findByNationAndSex(nationality,sex);
        return singers;
    }


}
