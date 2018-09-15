package com.music.demo.controller;


import com.music.demo.entities.User;
import com.music.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.*;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    UserMapper userMapper;

    @PostMapping(value = "/userLogin")
    public String login(@RequestParam("username1") String username,
                        @RequestParam("password1") String password,
                        HttpSession session, Map<String, Object> map) {
        User userFind = userMapper.findUsername(username);
        if (userFind == null) {
            map.put("msg", "该用户不存在！");
            return "login";
        } else {
            if (password.equals(userFind.getPassword())) {
                session.setAttribute("loginUser", username);
                return "index";
            } else {
                map.put("msg", "用户名或密码错误！");
                return "login";
            }
        }
    }

    //   用户注册
    @PostMapping("/userRegister")
    public String register(User user, @RequestParam("picFile") MultipartFile file, Map<String, Object> map) {
        if (user.getUsername().length() < 6 || user.getUsername().length() > 12) {
            map.put("m", "该用户名不符合，请输入6~11个字母和数字或其他组合！");
        }else {
            User beUser = userMapper.findUsername(user.getUsername());
            if (beUser != null) {
                map.put("m", "该用户已经存在，请重新输入用户名！");
            } else {
                user.setRate(Integer.valueOf(0));
                String originName = file.getOriginalFilename();
                String suffixName = originName.substring(originName.lastIndexOf("."));//后缀名
                System.out.println(suffixName);

                user.setPicUrl("image/pic/" + user.getUsername() + suffixName);
                new UserController().uploadFile(file, user.getPicUrl());

                user.setMaxSize(Long.valueOf(50));
                user.setUsedSize(Long.valueOf(0));
                userMapper.insertUser(user);
                map.put("success", user.getUsername());
            }
        }
        return "login";
    }

    //    来到我的信息修改页面
    @GetMapping("/user/{username}")
    public String getUserInformation(@PathVariable("username") String username, Model model) {
        User user = userMapper.findUsername(username);
        model.addAttribute("nowUser", user);
        return "user/myInfoUpdate";
    }

    //    @Valid注解用于校验，发送修改请求
    @PostMapping("/user/update")
    public String updateUser(@Valid User u,Map<String,Object> map, @RequestParam("file") MultipartFile file, HttpSession session) {
        System.out.println("来到修改页面");
        if (!file.isEmpty()) {
            String originName = file.getOriginalFilename();
            if (!originName.endsWith(".NPG") || !originName.endsWith(".jpg")) {
                map.put("m", "头像的格式不对！请上传.NPG或.jpg格式的文件！");
                return "/user/"+u.getUsername();
            }

            String suffixName = originName.substring(originName.lastIndexOf("."));//后缀名
            u.setPicUrl("image/pic/" + u.getUsername() + suffixName);
            uploadFile(file, u.getPicUrl());
        }

        u.setUsername(String.valueOf(session.getAttribute("loginUser")));
        userMapper.updateUser(u);
        return "redirect:/user/songByu";
    }

    //    文件上传，数据流的方式
    public void uploadFile(MultipartFile file, String relativeUrl) {
        try {
            OutputStream outputStream = new FileOutputStream(new File("F:/myResource/" + relativeUrl));
            outputStream.write(file.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
