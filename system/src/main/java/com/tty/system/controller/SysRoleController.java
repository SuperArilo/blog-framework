package com.tty.system.controller;

import com.tty.common.utils.JsonResult;
import com.tty.common.utils.PageUtil;
import com.tty.system.dto.SysRoleDTO;
import com.tty.system.entity.SysRoleEntity;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import com.tty.system.service.SysRoleService;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author rgc
 * @since 2023-05-14
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController {

    @Resource
    private SysRoleService sysRoleService;

    /**
     * 获取角色
     * @param queryDto 参数
     * @param page 分页
     * @return JsonResult
     */
    @GetMapping("/list")
    public JsonResult getAllRoles(SysRoleDTO queryDto, PageUtil page) {
        return sysRoleService.getAllRoles(queryDto, page);
    }

    /**
     * 获取角色
     * @param id id
     * @return JsonResult
     */
    @GetMapping("/{id}")
    public JsonResult getRoleById(Long id) {
        return sysRoleService.getRoleById(id);
    }

    /**
     * 添加角色
     * @param entity entity
     * @return JsonResult
     */
    @PostMapping("/")
    public JsonResult addRole(@RequestBody SysRoleEntity entity) {
        return sysRoleService.addRole(entity);
    }

    /**
     * 修改角色
     * @param id id
     * @param entity entity
     * @return JsonResult
     */
    @PutMapping("/{id}")
    public JsonResult updateRole(@PathVariable("id") Long id, @RequestBody SysRoleEntity entity) {
        entity.setId(id);
        return sysRoleService.updateRole(entity);
    }

    /**
     * 删除角色
     * @param id id
     * @return JsonResult
     */
    @DeleteMapping("/{id}")
    public JsonResult deleteRole(@PathVariable("id") Long id) {
        return sysRoleService.deleteRoleBatch(Collections.singletonList(id));
    }

    /**
     * 批量删除
     * @param ids ids
     * @return JsonResult
     */
    @DeleteMapping("/batch")
    public JsonResult deleteRoleBatch(@RequestBody List<Long> ids) {
        return sysRoleService.deleteRoleBatch(ids);
    }
}
