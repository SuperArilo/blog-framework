package com.tty.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tty.common.utils.JsonResult;
import com.tty.common.utils.PageUtil;
import com.tty.system.dto.SysRoleDTO;
import com.tty.system.entity.SysRoleEntity;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author rgc
 * @since 2023-05-14
 */
public interface SysRoleService extends IService<SysRoleEntity> {

    /**
     * 获取角色
     * @param queryDto 参数
     * @param page 分页
     * @return JsonResult
     */
    JsonResult getAllRoles(SysRoleDTO queryDto, PageUtil page);

    /**
     * 获取角色
     * @param id id
     * @return JsonResult
     */
    JsonResult getRoleById(Long id);

    /**
     * 添加角色
     * @param entity entity
     * @return JsonResult
     */
    JsonResult addRole(SysRoleEntity entity);

    /**
     * 修改角色
     * @param entity entity
     * @return JsonResult
     */
    JsonResult updateRole(SysRoleEntity entity);

    /**
     * 删除角色
     * @param ids ids
     * @return JsonResult
     */
    JsonResult deleteRoleBatch(List<Long> ids);
}
