package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.OrderInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.aspectj.weaver.ast.Or;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Fank
 */
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    // 分页查询订单信息
    IPage<LinkedHashMap<String, Object>> orderInfoByPage(Page page, @Param("orderInfo") OrderInfo orderInfo);

    // 根据酒店ID获取统计信息
    LinkedHashMap<String, Object> orderInfoByHotelId(@Param("hotelId") Integer hotelId);

    // 订单量统计
    List<LinkedHashMap<String, Object>> orderNumByHotelId(@Param("hotelId") Integer hotelId);

    // 交易类型统计
   List<LinkedHashMap<String, Object>> orderTypeByHotelId(@Param("hotelId") Integer hotelId);

   // 根据用户获取订单
    List<LinkedHashMap<String, Object>> getOrderByUserId(@Param("userId") Integer userId);

}
