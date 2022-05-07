package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.ScenicInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author Fank
 */
public interface ScenicInfoMapper extends BaseMapper<ScenicInfo> {

    // 分页查询景点信息
    IPage<LinkedHashMap<String, Object>> scenicInfoByPage(Page page, @Param("scenicInfo") ScenicInfo scenicInfo);
}
