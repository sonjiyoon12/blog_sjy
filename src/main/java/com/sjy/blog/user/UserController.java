package com.sjy.blog.user;

import com.sjy.blog.utils.Define;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;

    // 회원 가입 화면
    @GetMapping("/join-form")
    public String joinForm() {
        return "user/join-form";
    }

    // 회원 가입
    @PostMapping("/join")
    public String join(UserRequest.JoinDTO joinDTO) {
        joinDTO.validate();
        userService.join(joinDTO);
        return "redirect:/login-form";
    }

    // 로그인 화면
    @GetMapping("/login-form")
    public String loginForm() {
        return "user/login-form";
    }

    // 로그인
    @PostMapping("/login")
    public String login(UserRequest.LoginDTO loginDTO, HttpSession session) {
        loginDTO.validate();
        User user = userService.login(loginDTO);
        session.setAttribute(Define.SESSION_USER, user);
        return "redirect:/";
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // 회원 수정 화면
    @GetMapping("/user/update-form")
    public String updateForm(Model model, HttpSession session) {
        User sessionUser = (User) session.getAttribute(Define.SESSION_USER);
        User user = userService.findById(sessionUser.getId());
        model.addAttribute("user", user);
        return "user/update-form";
    }

    // 회원 수정
    @PostMapping("/user/update")
    public String update(UserRequest.UpdateDTO updateDTO,HttpSession session) {
        updateDTO.validate();
        User user = (User) session.getAttribute(Define.SESSION_USER);
        User updateUser = userService.updateById(user.getId(), updateDTO);
        session.setAttribute(Define.SESSION_USER, updateUser);
        return "redirect:/user/update-form";
    }
}
