package com.tty.system.controller;

import com.tty.common.utils.JsonResult;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import com.tty.system.service.SysDictionaryService;


@RestController
@RequestMapping("/sys/dictionary")
public class SysDictionaryController {


    @Resource
    private SysDictionaryService dictionaryService;

    /**
     * 根据字典类型和父级id获取字典
     * @param type 字典类型
     * @param parentId 父级id
     * @return JsonResult
     */
    @GetMapping("/{type}")
    public JsonResult getDictionaryByTypeParentId(@PathVariable("type") String type, @RequestParam(required = false, defaultValue = "0") Long parentId) {
        return this.dictionaryService.getDictionaryByTypeParentId(type, parentId);
    }


}
