package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.ReplyInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author Fank
 */
public interface IReplyInfoService extends IService<ReplyInfo> {

    // 分页查询回复信息
    IPage<LinkedHashMap<String, Object>> replyInfoByPage(Page page, ReplyInfo replyInfo);
}
