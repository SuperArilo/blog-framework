package com.tty.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tty.common.entity.BaseEntity;
import com.tty.common.utils.JsonResult;
import org.springframework.stereotype.Service;
import com.tty.system.entity.SysDictionaryEntity;
import com.tty.system.mapper.SysDictionaryMapper;
import com.tty.system.service.SysDictionaryService;

import java.util.List;


@Service
public class SysDictionaryServiceImpl extends ServiceImpl<SysDictionaryMapper, SysDictionaryEntity> implements SysDictionaryService {
    @Override
    public JsonResult getDictionaryByTypeParentId(String type, Long parentId) {
        LambdaQueryWrapper<SysDictionaryEntity> lambda = new QueryWrapper<SysDictionaryEntity>().lambda();
        lambda.eq(SysDictionaryEntity::getType, type).orderByAsc(SysDictionaryEntity::getSort).orderByDesc(BaseEntity::getId);
        if(parentId != null) {
            lambda.eq(SysDictionaryEntity::getParentId, parentId);
        }
        List<SysDictionaryEntity> list = this.list(lambda);
        return JsonResult.OK(list);
    }
}
