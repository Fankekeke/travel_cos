package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.HotelInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author Fank
 */
public interface IHotelInfoService extends IService<HotelInfo> {

    // 分页查询民宿信息
    IPage<LinkedHashMap<String, Object>> hotelInfoByPage(Page page, HotelInfo hotelInfo);
}
