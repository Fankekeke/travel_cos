package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.ScenicInfo;
import cc.mrbird.febs.cos.dao.ScenicInfoMapper;
import cc.mrbird.febs.cos.service.IScenicInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author Fank
 */
@Service
public class ScenicInfoServiceImpl extends ServiceImpl<ScenicInfoMapper, ScenicInfo> implements IScenicInfoService {

    @Override
    public IPage<LinkedHashMap<String, Object>> scenicInfoByPage(Page page, ScenicInfo scenicInfo) {
        return baseMapper.scenicInfoByPage(page, scenicInfo);
    }
}
