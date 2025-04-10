package com.tty.system.controller;

import com.tty.common.utils.PageUtil;
import com.tty.system.dto.SysResourcesDTO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tty.common.utils.JsonResult;
import com.tty.system.service.SysResourcesService;

/**
 * <p>
 * 资源表 前端控制器
 * </p>
 *
 * @author rgc
 * @since 2023-05-14
 */
@RestController
@RequestMapping("/sys/resource")
public class SysResourcesController {

    @Resource
    private SysResourcesService sysResourcesService;

    /**
     * 获取资源列表
     * @param queryDto 查询条件
     * @param page 分页
     * @return JsonResult
     */
    @GetMapping("/list")
    public JsonResult getResourcesList(SysResourcesDTO queryDto, PageUtil page) {
        return sysResourcesService.getResourcesList(queryDto, page);
    }

}
