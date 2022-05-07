package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.OrderInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Fank
 */
public interface IOrderInfoService extends IService<OrderInfo> {

    // 分页查询订单信息
    IPage<LinkedHashMap<String, Object>> orderInfoByPage(Page page, OrderInfo orderInfo);

    // 主页信息
    LinkedHashMap<String, Object> home(Integer type, Integer userId);

    // 根据用户获取订单
    List<LinkedHashMap<String, Object>> getOrderByUserId(Integer userId);
}
