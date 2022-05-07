package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.Evaluation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Fank
 */
public interface EvaluationMapper extends BaseMapper<Evaluation> {

    // 分页查询评价信息
    IPage<LinkedHashMap<String, Object>> evaluationByPage(Page page, @Param("evaluation") Evaluation evaluation);

    // 获取房间评价
    List<LinkedHashMap<String, Object>> getEvaluationByRoomType(@Param("roomTypeId") Integer roomTypeId);
}
