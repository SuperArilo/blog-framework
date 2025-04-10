package com.tty.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tty.common.utils.JsonResult;
import com.tty.system.entity.SysDictionaryEntity;


public interface SysDictionaryService extends IService<SysDictionaryEntity> {

    /**
     * 根据字典类型和父级id获取字典
     * @param type 字典类型
     * @param parentId 父级id
     * @return JsonResult
     */
    JsonResult getDictionaryByTypeParentId(String type, Long parentId);
}
