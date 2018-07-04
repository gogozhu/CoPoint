package cn.copoint.coeditor.controller;

//import com.simditortest.dao.UserMapper;
import cn.copoint.coeditor.dao.UserMapper;
import cn.copoint.coeditor.entity.Login;
//import com.simditortest.entity.UserEntity;
import cn.copoint.coeditor.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.parser.Entity;
import java.util.List;

// https://blog.csdn.net/boonya/article/details/60601935
@Controller
public class LoginController {
    @Autowired
    UserMapper userMapper;
    public UserEntity validateUser(Login login) {
        UserEntity user = new UserEntity();
        user.setName(login.getUsername());
        user.setPwd(login.getPassword());
        List<UserEntity> users = userMapper.findOneUserEntity(user);
//        List<UserEntity> users = userMapper.findOne(login.getUsername(),login.getPassword());
        return users.size() > 0 ? users.get(0) : null;
    }
    /**
     * 初始化登录页面
     * @return
     */
    @GetMapping(value = "/login")
    public ModelAndView showLogin(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("login");
        mav.addObject("login", new Login());
        return mav;
    }
    @PostMapping(value = "/login")
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response,
                              @ModelAttribute("login") Login login) {
        //登录成功
        ModelAndView mav = null;
        boolean flag = true;

        if (validateUser(login)==null)
        {
            flag=false;
        }
        //用户不存在
//        if(!login.getUsername().equals("root") ){
//            flag = false;
//        }
//        //密码错误
//        else if(!login.getPassword().equals("123456")){
//            flag = false;
//        }

        //登录成功
        if(flag){
            //将用户写入session
            System.out.println("Sucess");
            request.getSession().setAttribute("loginInfo",login);
            mav = new ModelAndView("redirect:/etherpad");
            mav.addObject("username", login.getUsername());
        }else
        {
            System.out.println("Failed");
            mav = new ModelAndView("login");
            mav.addObject("message", "Username or Password is wrong!!");
        }
        return mav;
    }
}