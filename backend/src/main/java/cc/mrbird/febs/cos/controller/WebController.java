package cc.mrbird.febs.cos.controller;

import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.*;
import cc.mrbird.febs.cos.service.*;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WebController {

    private final IUserInfoService userInfoService;

    private final IReplyInfoService replyInfoService;

    private final IPostInfoService postInfoService;

    private final IBulletinInfoService bulletinInfoService;

    private final IHotelInfoService hotelInfoService;

    private final IScenicInfoService scenicInfoService;

    private final IRoomTypeService roomTypeService;

    private final IEvaluationService evaluationService;

    private final IOrderInfoService orderInfoService;

    @PostMapping("/userAdd")
    public R userAdd(@RequestBody UserInfo user) throws Exception {
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        url += "?appid=wx76a6577665633a86";//自己的appid
        url += "&secret=78070ccedf3f17b272b84bdeeca28a2e";//自己的appSecret
        url += "&js_code=" + user.getOpenId();
        url += "&grant_type=authorization_code";
        url += "&connect_redirect=1";
        String res = null;
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);    //GET方式
        CloseableHttpResponse response = null;
        // 配置信息
        RequestConfig requestConfig = RequestConfig.custom()          // 设置连接超时时间(单位毫秒)
                .setConnectTimeout(5000)                    // 设置请求超时时间(单位毫秒)
                .setConnectionRequestTimeout(5000)             // socket读写超时时间(单位毫秒)
                .setSocketTimeout(5000)                    // 设置是否允许重定向(默认为true)
                .setRedirectsEnabled(false).build();           // 将上面的配置信息 运用到这个Get请求里
        httpget.setConfig(requestConfig);                         // 由客户端执行(发送)Get请求
        response = httpClient.execute(httpget);                   // 从响应模型中获取响应实体
        HttpEntity responseEntity = response.getEntity();
        System.out.println("响应状态为:" + response.getStatusLine());
        if (responseEntity != null) {
            res = EntityUtils.toString(responseEntity);
            System.out.println("响应内容长度为:" + responseEntity.getContentLength());
            System.out.println("响应内容为:" + res);
        }
        // 释放资源
        if (httpClient != null) {
            httpClient.close();
        }
        if (response != null) {
            response.close();
        }
        String openid = JSON.parseObject(res).get("openid").toString();
        System.out.println("openid" + openid);
        Integer count = userInfoService.count(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getOpenId, openid));
        if (count > 0) {
            return R.ok(userInfoService.getOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getOpenId, openid)));
        } else {
            user.setOpenId(openid);
            user.setCreateDate(DateUtil.formatDateTime(new Date()));
            user.setCode("U-"+new Date().getTime());
            user.setType(1);
            userInfoService.save(user);
            return R.ok(user);
        }
    }

    @RequestMapping("/openid")
    public R getUserInfo(@RequestParam(name = "code") String code) throws Exception {
        System.out.println("code" + code);
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        url += "?appid=wx9fffb151ded22005";//自己的appid
        url += "&secret=9666e94c91361e7de4d3a6d09a23402f";//自己的appSecret
        url += "&js_code=" + code;
        url += "&grant_type=authorization_code";
        url += "&connect_redirect=1";
        String res = null;
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);    //GET方式
        CloseableHttpResponse response = null;
        // 配置信息
        RequestConfig requestConfig = RequestConfig.custom()          // 设置连接超时时间(单位毫秒)
                .setConnectTimeout(5000)                    // 设置请求超时时间(单位毫秒)
                .setConnectionRequestTimeout(5000)             // socket读写超时时间(单位毫秒)
                .setSocketTimeout(5000)                    // 设置是否允许重定向(默认为true)
                .setRedirectsEnabled(false).build();           // 将上面的配置信息 运用到这个Get请求里
        httpget.setConfig(requestConfig);                         // 由客户端执行(发送)Get请求
        response = httpClient.execute(httpget);                   // 从响应模型中获取响应实体
        HttpEntity responseEntity = response.getEntity();
        System.out.println("响应状态为:" + response.getStatusLine());
        if (responseEntity != null) {
            res = EntityUtils.toString(responseEntity);
            System.out.println("响应内容长度为:" + responseEntity.getContentLength());
            System.out.println("响应内容为:" + res);
        }
        // 释放资源
        if (httpClient != null) {
            httpClient.close();
        }
        if (response != null) {
            response.close();
        }
        String openid = JSON.parseObject(res).get("openid").toString();
        System.out.println("openid" + openid);
        return R.ok(openid);
    }

    @GetMapping("/subscription")
    public R subscription(@RequestParam("taskCode") String taskCode) throws Exception {
        LinkedHashMap<String, Object> tokenParam = new LinkedHashMap<String, Object>() {
            {
                put("grant_type", "client_credential");
                put("appid", "wx76a6577665633a86");
                put("secret", "78070ccedf3f17b272b84bdeeca28a2e");
            }
        };
        String tokenResult = HttpUtil.get("https://api.weixin.qq.com/cgi-bin/token", tokenParam);
        String token =  JSON.parseObject(tokenResult).get("access_token").toString();
        LinkedHashMap<String, Object> data = new LinkedHashMap<String, Object>(){
            {
                put("thing1", new HashMap<String, Object>(){
                    {
                        put("value", "张三");
                    }
                });
                put("character_string3", new HashMap<String, Object>(){
                    {
                        put("value", taskCode);
                    }
                });
                put("time4", new HashMap<String, Object>(){
                    {
                        put("value", DateUtil.formatDateTime(new Date()));
                    }
                });
                put("thing5", new HashMap<String, Object>(){
                    {
                        put("value", "若查看详细内容，请登录小程序");
                    }
                });
            }
        };
        String url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + token;
        LinkedHashMap<String, Object> subscription = new LinkedHashMap<String, Object>() {
            {
                put("touser", "oeDfR5zqxQD3EEA3uPT836qnauSc");
                put("template_id", "Z27pBK1n9WnKNtQ_fo7TC-nUJUlOQ9KVJk6LIgp0nH8");
                put("data", data);
            }
        };
        String result = HttpUtil.post(url, JSONUtil.toJsonStr(subscription));
        return R.ok(result);
    }

    /**
     * 获取贴子信息
     * @return
     */
    @GetMapping("/getPostList")
    public R getPostList() {
        return R.ok(postInfoService.getPostList());
    }

    /**
     * 根据贴子编号获取详细信息
     * @param postId
     * @return
     */
    @GetMapping("/getPostInfoById")
    public R getPostInfoById(@RequestParam Integer postId) {
        return R.ok(postInfoService.getPostInfoById(postId));
    }

    /**
     * 贴子回复
     * @return
     */
    @PostMapping("/replyPost")
    public R replyPost(@RequestBody ReplyInfo replyInfo) {
        replyInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(replyInfoService.save(replyInfo));
    }

    /**
     * 添加贴子
     * @param postInfo
     * @return
     */
    @PostMapping("/postAdd")
    public R postAdd(@RequestBody PostInfo postInfo) {
        postInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(postInfoService.save(postInfo));
    }

    /**
     * 获取公告信息
     * @return
     */
    @GetMapping("/getBulletinList")
    public R getBulletinList() {
        return R.ok(bulletinInfoService.list());
    }

    /**
     * 进入主页
     * @return
     */
    @GetMapping("/home")
    public R home() {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        result.put("hotelInfo", hotelInfoService.list(Wrappers.<HotelInfo>lambdaQuery().last("limit 3")));
        result.put("scenicInfo", scenicInfoService.list(Wrappers.<ScenicInfo>lambdaQuery().last("limit 4")));
        return R.ok(result);
    }

    /**
     * 景区详情
     * @param scenicId
     * @return
     */
    @GetMapping("/scenicDetail")
    public R scenicDetail(Integer scenicId) {
        return R.ok(scenicInfoService.getOne(Wrappers.<ScenicInfo>lambdaQuery().eq(ScenicInfo::getId, scenicId)));
    }

    /**
     * 酒店详情信息
     * @param hotelId
     * @return
     */
    @GetMapping("/hotelDetail")
    public R hotelDetail(Integer hotelId) {
        HotelInfo hotelInfo = hotelInfoService.getOne(Wrappers.<HotelInfo>lambdaQuery().eq(HotelInfo::getId, hotelId));
        hotelInfo.setRoomTypes(roomTypeService.list(Wrappers.<RoomType>lambdaQuery().eq(RoomType::getHotelId, hotelId)));
        hotelInfo.getRoomTypes().forEach(item -> {
            item.setNum(roomTypeService.roomNum(item.getId()));
        });
        return R.ok(hotelInfo);
    }

    /**
     * 酒店列表
     * @return
     */
    @GetMapping("/hotelList")
    public R hotelList() {
        List<HotelInfo> hotelInfoList = hotelInfoService.list();
        hotelInfoList.forEach(item -> {
            item.setRoomTypes(roomTypeService.list(Wrappers.<RoomType>lambdaQuery().eq(RoomType::getHotelId, item.getId())));
        });
        return R.ok(hotelInfoList);
    }

    /**
     * 房间详情
     * @param roomTypeId
     * @return
     */
    @GetMapping("/getRoomTypeDetail")
    public R getRoomTypeDetail(Integer roomTypeId) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        RoomType roomType = roomTypeService.getById(roomTypeId);
        roomType.setNum(roomTypeService.roomNum(roomTypeId));
        result.put("roomType", roomType);
        result.put("hotel", hotelInfoService.getById(roomType.getHotelId()));
        result.put("evaluation", evaluationService.getEvaluationByRoomType(roomTypeId));
        return R.ok(result);
    }

    /**
     * 获取房间评价
     * @param roomType
     * @return
     */
    @GetMapping("/getEvaluationByRoomType")
    public R getEvaluationByRoomType(Integer roomType) {
        return R.ok(evaluationService.getEvaluationByRoomType(roomType));
    }

    /**
     * 添加订单
     * @param orderInfo
     * @return
     */
    @PostMapping("/orderAdd")
    public R orderAdd(@RequestBody OrderInfo orderInfo) {
        System.out.println(orderInfo.toString());
        orderInfo.setCode("ORD"+new Date().getTime());
        orderInfo.setOrderStatus(0);
        orderInfo.setStartDate(DateUtil.formatDate(new Date()));
        orderInfo.setEndDate(DateUtil.formatDate(DateUtil.offsetDay(new Date(), orderInfo.getDayNum())));
        orderInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(orderInfoService.save(orderInfo));
    }

    /**
     * 景点列表
     * @return
     */
    @GetMapping("/scenicList")
    public R scenicList() {
        return R.ok(scenicInfoService.list());
    }

    /**
     * 根据用户获取订单
     * @param userId
     * @return
     */
    @GetMapping("/getOrderByUserId")
    public R getOrderByUserId(Integer userId) {
        return R.ok(orderInfoService.getOrderByUserId(userId));
    }

    /**
     * 添加评价信息
     * @param evaluation
     * @return
     */
    @PostMapping("/evaluationAdd")
    public R evaluationAdd(@RequestBody Evaluation evaluation) {
        evaluation.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(evaluationService.save(evaluation));
    }

    @GetMapping("/getHotelByScenic")
    public R getHotelByScenic(Integer scenicId) {
        ScenicInfo scenicInfo = scenicInfoService.getById(scenicId);
        if (!StrUtil.isBlank(scenicInfo.getHotelIds())) {
            return R.ok(hotelInfoService.list(Wrappers.<HotelInfo>lambdaQuery().in(HotelInfo::getId, scenicInfo.getHotelIds().split(","))));
        } else {
            return R.ok();
        }
    }
}
