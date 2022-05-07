package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.ScenicInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author Fank
 */
public interface IScenicInfoService extends IService<ScenicInfo> {

    // 分页查询景点信息
    IPage<LinkedHashMap<String, Object>> scenicInfoByPage(Page page, ScenicInfo scenicInfo);
}
