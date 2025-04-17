package com.tty.blog.mapper;

import com.tty.blog.vo.map.MapRawConfigData;
import org.apache.ibatis.annotations.*;

@Mapper
public interface MapMapper {
    @Select("SELECT d.data, c.key " +
            "FROM bluemap_item_storage_data d " +
            "INNER JOIN bluemap_map m ON d.map = m.id " +
            "INNER JOIN bluemap_item_storage s ON d.storage = s.id " +
            "INNER JOIN bluemap_compression c ON d.compression = c.id " +
            "WHERE m.map_id = #{mapId, jdbcType=VARCHAR} " +
            "AND s.key = CONCAT('bluemap:', #{key, jdbcType=VARCHAR})")
    @Results({
            @Result(column = "data", property = "data", javaType = byte[].class),
            @Result(column = "key", property = "key")
    })
    MapRawConfigData getConfigRawData(@Param("mapId") String mapId, @Param("key") String key);

    @Select("select d.data, c.key " +
            "from bluemap_grid_storage_data d " +
            "INNER JOIN bluemap_map m ON d.map = m.id " +
            "INNER JOIN bluemap_grid_storage s ON d.storage = s.id " +
            "INNER JOIN bluemap_compression c ON d.compression = c.id " +
            "where m.map_id = #{mapId, jdbcType=VARCHAR} " +
            "and s.key = CONCAT('bluemap:', #{key, jdbcType=VARCHAR}) " +
            "and d.x = #{x} " +
            "and d.z = #{z}")
    MapRawConfigData getTilesRawData(@Param("mapId") String mapId, @Param("key") String key, @Param("x") int x, @Param("z") int z);
}
