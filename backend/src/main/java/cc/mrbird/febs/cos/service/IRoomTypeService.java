package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.RoomType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author Fank
 */
public interface IRoomTypeService extends IService<RoomType> {

    // 分页查询房间类型
    IPage<LinkedHashMap<String, Object>> roomTypeByPage(Page page, RoomType roomType);

    // 查询当前房间余量
    Integer roomNum(Integer roomTypeId);
}
