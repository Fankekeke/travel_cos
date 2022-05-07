package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author Fank
 */
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    // 分页查询用户信息
    IPage<LinkedHashMap<String, Object>> getUserInfoByPage(Page page, @Param("userInfo") UserInfo userInfo);
}
