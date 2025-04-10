package com.tty.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tty.common.utils.JsonResult;
import com.tty.common.utils.PageUtil;
import com.tty.system.dto.SysResourcesDTO;
import com.tty.system.entity.SysUserEntity;
import com.tty.system.utils.UserUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.tty.system.entity.SysResourcesEntity;
import com.tty.system.mapper.SysResourcesMapper;
import com.tty.system.service.SysResourcesService;


/**
 * <p>
 * 资源表 服务实现类
 * </p>
 *
 * @author rgc
 * @since 2023-05-14
 */
@Service
public class SysResourcesServiceImpl extends ServiceImpl<SysResourcesMapper, SysResourcesEntity> implements SysResourcesService {

    @Override
    public JsonResult getResourcesList(SysResourcesDTO queryDto, PageUtil page) {
        SysUserEntity user = UserUtil.getUserInfo();
        if(user == null) {
            return JsonResult.ERROR(HttpStatus.UNAUTHORIZED.value(), "token 无效");
        }

        return null;
    }
}
