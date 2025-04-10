package com.tty.blog.controller;

import com.tty.blog.service.BlogFriendService;
import com.tty.common.utils.JsonResult;
import com.tty.common.utils.PageUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/friend")
public class BlogFriendController {

    @Resource
    BlogFriendService blogFriendService;

    @GetMapping("/list")
    public JsonResult friendsList(PageUtil pageUtil){
        return blogFriendService.friendsList(pageUtil);
    }
    @PostMapping("/apply")
    public JsonResult friendApply(@RequestParam(value = "blogName") String linkName,
                                  @RequestParam(value = "blogLocation") String linkLocation,
                                  @RequestParam(value = "blogIntroduction") String linkIntroduction,
                                  @RequestParam(value = "blogAvatar") String linkAvatar,
                                  HttpServletRequest request){
        return blogFriendService.friendApply(linkName, linkLocation, linkIntroduction, linkAvatar, request);
    }
}
