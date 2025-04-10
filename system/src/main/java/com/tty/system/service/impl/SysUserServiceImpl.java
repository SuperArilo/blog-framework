package com.tty.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tty.common.utils.JsonResult;
import com.tty.common.utils.PageUtil;
import org.springframework.stereotype.Service;
import com.tty.system.entity.SysUserEntity;
import com.tty.system.mapper.SysUserMapper;
import com.tty.system.service.SysUserService;

import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author rgc
 * @since 2023-05-11
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUserEntity> implements SysUserService {
    @Override
    public SysUserEntity getUserByUsername(String username) {
        return this.getOne(new QueryWrapper<SysUserEntity>().lambda().eq(SysUserEntity::getUsername, username));
    }

    /**
     * 获取用户列表
     *
     * @param pageUtil 分页参数，包含当前页码和每页的大小
     * @return JsonResult 包含分页用户信息的结果对象
     */
    @Override
    public JsonResult getUserList(PageUtil pageUtil) {
        // 创建查询条件，排除超级管理员
        LambdaQueryWrapper<SysUserEntity> lambda = new QueryWrapper<SysUserEntity>().lambda();
        lambda.ne(SysUserEntity::getType, 1);

        // 初始化分页
        PageHelper.startPage(pageUtil.getCurrent(), pageUtil.getSize());

        // 执行查询
        List<SysUserEntity> list = this.list(lambda);

        // 移除用户密码信息，保护隐私
        list.forEach(item -> item.setPassword(null));

        // 构建分页信息
        PageInfo<SysUserEntity> pageInfo = new PageInfo<>(list);

        // 返回分页查询结果
        return JsonResult.OK(new PageUtil(pageInfo));
    }

}
