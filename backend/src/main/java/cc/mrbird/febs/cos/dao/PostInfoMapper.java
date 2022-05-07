package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.PostInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Fank
 */
public interface PostInfoMapper extends BaseMapper<PostInfo> {

    // 分页查询帖子信息
    IPage<LinkedHashMap<String, Object>> getPostInfoByPage(Page page, @Param("postInfo") PostInfo postInfo);

    // 根据贴子ID获取回复信息
    List<LinkedHashMap<String, Object>> replyInfoByPostId(@Param("postId") Integer postId);

    // 获取贴子信息
    List<LinkedHashMap<String, Object>> getPostList();

    // 根据贴子编号获取详细信息
    LinkedHashMap<String, Object> getPostInfoById(@Param("postId") Integer postId);

    // 获取热门贴子信息
    List<LinkedHashMap<String, Object>> getPostListHot();
}
