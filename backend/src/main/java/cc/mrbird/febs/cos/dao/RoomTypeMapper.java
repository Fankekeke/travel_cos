package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.RoomType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Fank
 */
public interface RoomTypeMapper extends BaseMapper<RoomType> {

    // 分页查询房间类型
    IPage<LinkedHashMap<String, Object>> roomTypeByPage(Page page, @Param("roomType") RoomType roomType);

    // 查询当前房间余量
    Integer roomNum(@Param("roomTypeId") Integer roomTypeId);
}
