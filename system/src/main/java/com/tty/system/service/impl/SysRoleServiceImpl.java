package com.tty.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tty.common.entity.BaseEntity;
import com.tty.common.utils.JsonResult;
import com.tty.common.utils.PageUtil;
import com.tty.system.dto.SysRoleDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.tty.system.entity.SysRoleEntity;
import com.tty.system.mapper.SysRoleMapper;
import com.tty.system.service.SysRoleService;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;


/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author rgc
 * @since 2023-05-14
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRoleEntity> implements SysRoleService {
    @Override
    public JsonResult getAllRoles(SysRoleDTO queryDto, PageUtil page) {
        // todo 添加条件
        LambdaQueryWrapper<SysRoleEntity> lambda = new QueryWrapper<SysRoleEntity>().lambda();

        PageHelper.startPage(page.getCurrent(), page.getSize());
        List<SysRoleEntity> list = this.list(lambda);
        PageInfo<SysRoleEntity> pageInfo = new PageInfo<>(list);
        return JsonResult.OK(new PageUtil(pageInfo));
    }

    @Override
    public JsonResult getRoleById(Long id) {
        return JsonResult.OK(this.getById(id));
    }

    @Override
    public JsonResult addRole(SysRoleEntity entity) {
        if(!StringUtils.hasLength(entity.getRoleName())) {
            return JsonResult.ERROR(HttpStatus.BAD_REQUEST.value(), "角色名称不能为空");
        }
        return this.save(entity) ? JsonResult.OK() : JsonResult.ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "添加失败");
    }

    @Override
    public JsonResult updateRole(SysRoleEntity entity) {
        if(!StringUtils.hasLength(entity.getRoleName())) {
            return JsonResult.ERROR(HttpStatus.BAD_REQUEST.value(), "角色名称不能为空");
        }
        return this.updateById(entity) ? JsonResult.OK() : JsonResult.ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "修改失败");
    }

    @Override
    public JsonResult deleteRoleBatch(List<Long> ids) {
        LambdaUpdateWrapper<SysRoleEntity> lambda = new UpdateWrapper<SysRoleEntity>().lambda().set(BaseEntity::getDeleted, Boolean.TRUE);
        JsonResult result = JsonResult.ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "删除失败");
        return Optional.ofNullable(ids)
                .map(list -> this.update(lambda.in(BaseEntity::getId, list)))
                .map(success -> success ? JsonResult.OK() : result)
                .orElse(result);
    }
}
