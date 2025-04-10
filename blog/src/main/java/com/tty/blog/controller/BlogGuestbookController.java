package com.tty.blog.controller;

import com.tty.blog.service.BlogGuestbookService;
import com.tty.common.utils.JsonResult;
import com.tty.common.utils.PageUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/guestbook")
public class BlogGuestbookController {
    @Resource
    BlogGuestbookService blogGuestbookService;
    @GetMapping("/list")
    public JsonResult GuestbookListGet(PageUtil pageUtil) {
        return this.blogGuestbookService.guestbookList(pageUtil);
    }

    @PostMapping("/add")
    public JsonResult newGuestbook(@RequestParam(value = "content") String content,
                                   HttpServletRequest request){
        return this.blogGuestbookService.addGuestbook(content, request);
    }

    @DeleteMapping("/delete")
    public JsonResult deleteGuestbook(@RequestParam(value = "guestbookId") Long guestbookId,
                                      HttpServletRequest request) {
        return this.blogGuestbookService.deleteGuestbook(guestbookId, request);
    }
}
