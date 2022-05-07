package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.UserInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author Fank
 */
public interface IUserInfoService extends IService<UserInfo> {

    // 分页查询用户信息
    IPage<LinkedHashMap<String, Object>> getUserInfoByPage(Page page, UserInfo userInfo);
}
