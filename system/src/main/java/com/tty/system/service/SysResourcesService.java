package com.tty.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tty.common.utils.JsonResult;
import com.tty.common.utils.PageUtil;
import com.tty.system.dto.SysResourcesDTO;
import com.tty.system.entity.SysResourcesEntity;

/**
 * <p>
 * 资源表 服务类
 * </p>
 *
 * @author rgc
 * @since 2023-05-14
 */
public interface SysResourcesService extends IService<SysResourcesEntity> {

    /**
     * 获取资源列表
     * @param queryDto 查询条件
     * @param page 分页
     * @return JsonResult
     */
    JsonResult getResourcesList(SysResourcesDTO queryDto, PageUtil page);
}
