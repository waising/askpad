package com.asking.pad.app.api;

import com.asking.pad.app.BuildConfig;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 请求
 */

public interface UserApi {

    /**
     * 登录
     *
     * @param userName
     * @param pwd
     * @return
     */
    @POST(BuildConfig.API_SSO_URL + "sso/applogin")
    Observable<ResponseBody> ssoLogin(@Query("username") String userName, @Query("password") String pwd);

    /**
     * 获取注册验证码
     *
     * @param mobile
     * @return
     */
    @POST(BuildConfig.API_SSO_URL + "pass/register/requestMessage/mobile")
    Observable<ResponseBody> getYZM(@Query("mobile") String mobile);

    /**
     * 注册
     *
     * @return
     */
    @POST(BuildConfig.API_SSO_URL + "pass/register/appSave2")
    Observable<ResponseBody> register(@Query("mobile") String mobile, @Query("passWord") String passWord, @Query("verifyCode") String verifyCode);

    /**
     * 忘记密码
     *
     * @return
     */
    @POST(BuildConfig.API_SSO_URL + "pass/resetpw/appsave")
    Observable<ResponseBody> resetPass(@Query("mobile") String mobile, @Query("password") String password, @Query("verifyCode") String verifyCode);

    /**
     * 获取忘记密码的验证码
     *
     * @param mobile
     * @return
     */
    @POST(BuildConfig.API_SSO_URL +"modify/requestMessage/mobile")
    Observable<ResponseBody> getResetPassYZM(@Query("mobile") String mobile);

    /**
     *
     * @return
     */
    @POST("productapi/product/courseType/findTreeListWithAllCourse")
    Observable<ResponseBody> findTreeListWithAllCourse(@Query("productId") String productId);

    /**
     * 检查用户学校信息是否完善
     *
     * @return
     */
    @POST("user/checkUserInfo")
    Observable<ResponseBody> checkUserInfo();

    /**获取章节目录*/
    @GET("courseapi/synclesson/{commodityId}")
    Observable<ResponseBody> synclesson(@Path("commodityId") String commodityId);

    /**
     * 获取章节目录
     */
    @POST("coach/version/level/{versionLevelId}/knowledgeClassic")
    Observable<ResponseBody> buyStudyLevel(@Path("versionLevelId") String versionLevelId);

    /**
     * 获取章节点
     */
    @GET("firstreview/zhangjd/{orgId}")
    Observable<ResponseBody> firstreviewzhangjd(@Path("orgId") String orgId);

    /**
     * 获取课时节点
     */
    @GET("firstreview/kesjd/{pid}")
    Observable<ResponseBody> firstreviewkesjd(@Path("pid") String pid);

    /**
     * 获取树节点
     */
    @GET("secondreview/tree")
    Observable<ResponseBody> secondreviewtree(@Query("orgId") String orgId);

    /**
     * 获取超级辅导课阿思可博士有话说列表
     */
    @GET("courseapi/synclesson/{gradeId}/{knowledgeId}/{type}")
    Observable<ResponseBody> synclesson(@Path("gradeId") String gradeId, @Path("knowledgeId") String knowledgeId, @Path("type") int type);

    /**
     * 音频播放
     * prefix--1--有话说==2--问答时间==3来讲题（课堂总结没有音频）
     * suffix--获取的音频的内容在列表的顺序+1（第0个及最开头的一个播放音频的suffix=1）
     */
    @GET("courseapi/synclesson/voice/{gradeId}/{knowledgeId}/{type}/{position}")
    Observable<ResponseBody> getVoicePath(@Path("gradeId") String gradeId, @Path("knowledgeId") String knowledgeId,
                                          @Path("type") int type, @Path("position") int position);

    @POST("learn/subject/freeStudyClassic/like/test")
    Observable<ResponseBody> subject(@Query("answerstr") String answerstr, @Query("code") String code);

    /**
     * 获取变式题
     */
    @POST("freeStudyClassic/subject/subjectMul")
    Observable<ResponseBody> getSubjectMul(@Query("kindId") String kindId, @Query("catalogCode") String catalogCode, @Query("start") int start, @Query("limit") int limit);

    /**
     * 演练大冲关获取题类
     * subjectCatalog--M3
     */
    @GET("courseapi/sprint/{gradeId}/{knowledgeId}")
    Observable<ResponseBody> getSubjectTopic(@Path("gradeId") String gradeId, @Path("knowledgeId") String knowledgeId);

    /**
     * 演练大冲关获取题目
     **/
    @GET("courseapi/sprint/{gradeId}/{knowledgeId}/{topic_id}")
    Observable<ResponseBody> getAllSubjectClassic(@Path("gradeId") String gradeId, @Path("knowledgeId") String knowledgeId
            , @Path("topic_id") String topic_id, @Query("start") int start, @Query("limit") int limit);


    /**
     * 演练大冲关提交题目
     * answerstr--{"subList":[{"id":"0FCFA645-7491-49BC-BF0C-02F04E367379","subject_type":{"type_id":"1"},"user_answer":"B"}]}
     * code--M
     */
    @Multipart
    @POST("coach/subjectClassic/answer")
    Observable<ResponseBody> subjectClassic(@PartMap Map<String, RequestBody> params);

    /**
     * 阿思可博士考情分析接口
     */
    @GET("firstreview/kaoqfx/{pid}")
    Observable<ResponseBody> firstreviewkaoqfx(@Path("pid") String pid);

    /**
     * 典题
     */
    @GET("firstreview/diant/{pid}")
    Observable<ResponseBody> firstreviewdiant(@Path("pid") String pid);

    /**
     * 备高考三要求
     */
    @GET("firstreview/beigk/{pid}/{index}")
    Observable<ResponseBody> firstreviewbeigk(@Path("pid") String pid, @Path("index") String index);

    /**
     * 实战演练接口
     */
    @GET("firstreview/shizyl/{pid}")
    Observable<ResponseBody> firstreviewshizyl(@Path("pid") String pid);

    /**
     * 典题
     */
    @GET("secondreview/zhuant")
    Observable<ResponseBody> secondreviewzhuant(@Query("pid") String pid, @Query("field") String field);

    @GET("cloudapi/qiniu/token")
    Observable<ResponseBody> qiniutoken();

    /**
     * 1.学生获取信息接口
     *
     * @param userName
     * @return
     */
    @GET(BuildConfig.API_OTO_RE_URL + "student/info")
    Observable<ResponseBody> studentinfo(@Query("userName") String userName);

    @Multipart
    @POST(BuildConfig.API_OTO_RE_URL + "order/build")
    Observable<ResponseBody> orderbuild(@PartMap Map<String, RequestBody> params);

    /**
     * 学生轮询题目状态接口
     *
     * @param id
     * @return
     */
    @GET(BuildConfig.API_OTO_RE_URL + "order/state")
    Observable<ResponseBody> orderstate(@Query("id") String id);

    /**
     * .获取网易云Token
     *
     * @param accid
     * @return
     */
    @GET("cloudapi/nim/token")
    Observable<ResponseBody> nimtoken(@Query("accid") String accid);

    /**
     * 3.学生取消题目
     *
     * @param id
     * @return
     */
    @POST(BuildConfig.API_OTO_RE_URL + "order/cancel")
    Observable<ResponseBody> ordercancel(@Query("id") String id);

    /**
     * 4.学生首单退款接口
     *
     * @param id
     * @return
     */
    @POST(BuildConfig.API_OTO_RE_URL + "order/firstorder")
    Observable<ResponseBody> orderfirstorder(@Query("id") String id);


    @GET("user/integralLog")
    Observable<ResponseBody> integralLog(@Query("start") int start,@Query("limit") int limit);

    /**
     * 5.学生正常结算接口
     *
     * @param id
     * @return
     */
    @POST(BuildConfig.API_OTO_RE_URL + "order/checkbill")
    Observable<ResponseBody> ordercheckbill(@Query("id") String id);

    /**
     * 6.学生评价老师接口
     *
     * @param id
     * @param reward
     * @param star
     * @param suggest
     * @return
     */
    @POST(BuildConfig.API_OTO_RE_URL + "order/evaluate")
    Observable<ResponseBody> orderevaluate(@Query("id") String id, @Query("reward") String reward, @Query("star") String star
            , @Query("suggest") String suggest);

    /**
     * 学生收藏教师接口
     *
     * @param userName
     * @param account
     * @return
     */
    @POST(BuildConfig.API_OTO_RE_URL + "student/favor")
    Observable<ResponseBody> studentfavor(@Query("userName") String userName, @Query("account") String account);


    /**
     * 学生往期解答列表接口
     *
     * @param start
     * @param limit
     * @param account
     * @param role
     * @return
     */
    @GET(BuildConfig.API_OTO_RE_URL + "order/history")
    Observable<ResponseBody> orderhistory(@Query("start") String start, @Query("limit") String limit, @Query("account") String account
            , @Query("role") String role);

    @POST(BuildConfig.API_OTO_RE_URL + "student/complain")
    Observable<ResponseBody> studentcomplain(@Query("reason") String reason, @Query("details") String details, @Query("id") String id);

    /**
     * 我的课程（分页查询）
     * @param start
     * @param limit
     * @return
     */
    @GET("userreact/userCourse/findByPage")
    Observable<ResponseBody> userreact(@Query("start") String start, @Query("limit") String limit);

    @POST("userreact/userCourse/updateWithSchedule")
    Observable<ResponseBody> updateWithSchedule(@Query("commodityId") String commodityId, @Query("schedulePercent") String schedulePercent
            , @Query("scheduleTitle") String scheduleTitle, @Query("scheduleId") String scheduleId, @Query("scheduleContent") String scheduleContent);


    /**
     * 获取省份数据
     * regionCode:0 获取省份数据，传入省份ID获取城市数据，
     * 传入城市ID获得城市对应区县数据
     */
    @POST(BuildConfig.API_SSO_URL + "getRegionInfo")
    Observable<ResponseBody> getRegionInfo(@Query("regionCode") String regionCode);

    /**
     * 获取学校数据
     *
     * @param regionCode
     * @return
     */
    @POST(BuildConfig.API_SSO_URL + "getSchoolInfo")
    Observable<ResponseBody> getSchoolInfo(@Query("regionCode") String regionCode);

    /**
     * 修改用户信息
     * {
     * "ticket":"TK-0773bee7-fea3-43bc-b4e1-1d22b818a8cd3790312168",
     * …（需要修改的信息key:value）
     * region_code--只要县的region_code
     * region_name--学校所在地
     * levelId 年级
     * area 居住地
     * classId班级
     * nickName昵称
     * }
     */
    @POST(BuildConfig.API_SSO_URL + "user/updateUser")
    Observable<ResponseBody> updateUser(@Query("ticket") String ticket, @Query("name") String name, @Query("nickName") String nickName, @Query("sex") String sex,
                                        @Query("birthdayStr") String birthday, @Query("region_name") String regionName,
                                        @Query("region_code") String regionCode, @Query("school_name") String schoolName,
                                        @Query("remark") String remark, @Query("area") String area,
                                        @Query("levelId") String levelId, @Query("classId") String classId,@Query("avatar") String avatar);

    /**
     * 套餐类型（根据id查询类型列表）
     * @param packageTypeId
     * @return
     */
    @POST("productapi/product/productType/findList")
    Observable<ResponseBody> productType(@Query("packageTypeId") String packageTypeId);

    /**
     * 获取辅导课和知识包信息
     */
    @POST("productapi/product/package/findListByPage")
    Observable<ResponseBody> getCommodityList(@Query("packageTypeId") String packageTypeId,
                                              @Query("timeLimit") int timeLimit,
                                              @Query("start") int start,
                                              @Query("limit") int limit);

    /**
     * 获取阿思币充值套餐
     *
     * @param
     * @return
     */
    @POST("productapi/product/askCurrency/findByPage")
    Observable<ResponseBody> getRechargeList( @Query("start") int start,
                                              @Query("limit") int limit);

    /**
     * 获取充值提交数据
     *
     * @param
     * @return
     */
    @POST("commodity/space/appCharge")
    Observable<ResponseBody> getAppCharge(@Query("commodityId") String commodityId,
                                          @Query("rechargeId") String rechargeId,
                                          @Query("num") int num,
                                          @Query("versionId") int versionId,
                                          @Query("type") String type,
                                          @Query("payType") String payType,
                                          @Query("clientIP") String clientIP,
                                          @Query("deviceType") String deviceType);

    /**
     * 继续支付
     *
     * @param
     * @return
     */
    @POST("commodity/space/appRecharge")
    Observable<ResponseBody> getAppReCharge(@Query("orderId") String orderId,
                                            @Query("type") String type,
                                            @Query("payType") String payType,
                                            @Query("deviceType") String deviceType);

    /* 获取我的笔记 */
    @POST("space/noteBook/list")
    Observable<ResponseBody> getMyNote(@Query("ticket") String ticket,
                                       @Query("start") int start,
                                       @Query("limit") int limit);

    /* 获取我的收藏*/
    @POST("space/collection/list/")
    Observable<ResponseBody> getMyFavor(@Query("ticket") String ticket,
                                        @Query("type") int type,
                                        @Query("start") int start,
                                        @Query("limit") int limit);

    /**
     * 退出登录
     *
     * @return
     */
    @POST(BuildConfig.API_SSO_URL + "service/user/loginOut")
    Observable<ResponseBody> loginOut();


    @POST(BuildConfig.API_URL + "service/user/loginOut")
    Observable<ResponseBody> loginOut2();

    /**
     * 判断今日是否已经签到
     *
     * @return
     */
    @POST("user/api/v1/checkTodaySign")
    Observable<ResponseBody> checkTodaySign();

    /**
     * 获取签到的天数和最大连续签到次数
     *
     * @return
     */
    @POST("user/api/v1/dailySign")
    Observable<ResponseBody> dailySign();

    /**
     * 随堂测验
     *
     * @return
     */
    @GET("firstreview/suitcy/{pid}")
    Observable<ResponseBody> suitcy(@Path("pid") String pid,@Query("difficulty") String difficulty,@Query("xuanzt") String xuanzt
            ,@Query("tiankt") String tiankt,@Query("jiedt") String jiedt);

    /**
     * 拓展应用
     *
     * @return
     */
    @GET("secondreview/tuozyy")
    Observable<ResponseBody> tuozyy(@Query("pid") String pid,@Query("difficulty") String difficulty,@Query("xuanzt") String xuanzt
            ,@Query("tiankt") String tiankt,@Query("jiedt") String jiedt);

    /**
     * 签到
     *
     * @return
     */
    @POST("user/api/v1/sign")
    Observable<ResponseBody> sign();

    /* 请求我的收藏里的题目详情和解析 */
    @POST("space/collection/subject/{km}/{objId}")
    Observable<ResponseBody> getMyFavSubjectDetail(@Path("km") String km,
                                                   @Path("objId") String objId);

    /* 删除我的收藏 */
    @POST("space/collection/remove/")
    Observable<ResponseBody> delMyFavor(@Query("ticket") String ticket,
                                        @Query("id") String id);

    /* 获取我的题目*/
    @POST("space/collection/subjectList/")
    Observable<ResponseBody> getMySubject(
            @Query("start") int start,
            @Query("limit") int limit);


    /**
     * 获取错题版本
     *
     * @param subjectCatalog
     * @param type
     * @return
     */
    @POST("coach/{subject_catalog}/version")
    Observable<ResponseBody> getMyWrongVersions(@Path("subject_catalog") String subjectCatalog,
                                                @Query("type") String type
    );

    @POST("search/{subjectCatalog}/{subId}/convertTabLClassic")
    Observable<ResponseBody> convertTabLClassic(@Path("subjectCatalog") String subjectCatalog,
                                                @Path("subId") String subId
    );


    /**
     * 获取错题年级
     *
     * @param versionId
     * @param type
     * @return
     */
    @POST("coach/version/{version_id}/level")
    Observable<ResponseBody> getMyWrongGrade(@Path("version_id") String versionId,
                                             @Query("type") String type
    );

    /**
     * 获取我的错题本
     *
     * @param subjectCatalog
     * @return
     */
    @Multipart
    @POST("space/errorCollection/{subjectCatalog}/subject/list")
    Observable<ResponseBody> errorCollection(@Path("subjectCatalog") String subjectCatalog,
                                             @PartMap Map<String, RequestBody> params
    );


    /* 修改密码请求 */
    @POST("user/changePass")
    Observable<ResponseBody> chagePassword(@Query("oldPass") String oldPass,
                                           @Query("pass") String pass,
                                           @Query("pass1") String pass1
    );

    /* 获取我的购买记录 */
    @POST("commodity/wap/userOrder")
    Observable<ResponseBody> getMyBuyRecords(@Query("ticket") String ticket,
                                             @Query("start") int start,
                                             @Query("limit") int limit);


    /**
     * 保存笔记请求
     */
    @POST("space/noteBook/save")
    Observable<ResponseBody> saveNode(@Query("title") String title,@Query("content") String content,@Query("imgurl") String imgurl);

    /* 修改笔记 */
    @POST("space/noteBook/save/")
    Observable<ResponseBody> alterMyNote(@Query("content") String content,
                                         @Query("id") String id);

    /* 删除用户笔记 */
    @POST("space/noteBook/delete")
    Observable<ResponseBody> delMyNote(@Query("ticket") String ticket,
                                       @Query("id") String id);


    @POST("productapi/product/courseManage/finListdWithDownloadUrl")
    Observable<ResponseBody> finListdWithDownloadUrl(@Query("courseTypeId") String courseTypeId,
                                             @Query("start") int start,@Query("limit") int limit);


    /* 删除我的购买记录 */
    @POST("commodity/space/order/delete")
    Observable<ResponseBody> delMyBuyRecords(@Query("orderId") String orderId);

    //    /**更新接口
    @GET("/commonapi/datametas/search/findByKey?key=AndroidPadUpdate")
    Observable<ResponseBody> updateAPKUrl();


    /* 加入购物车超级辅导课 */
    @POST("commodity/space/shoppingCart/add")
    Observable<ResponseBody> AddShopCartSuper(@Query("commodityId") String commodityId,
                                              @Query("versionId") int versionId);

    /* 加入购物车阿思币充值 */
    @POST("commodity/space/shoppingCart/add")
    Observable<ResponseBody> AddShopCartAskCoin(@Query("rechargeId") String rechargeId);

    /* 获取购物车list */
    @POST("commodity/space/shoppingCart/list")
    Observable<ResponseBody> ShopCartList();


    /* 删除购物车 */
    @POST("commodity/space/shoppingCart/delete")
    Observable<ResponseBody> DelShopCart(@Query("id") String id);

    /* 购物车支付成功后生成订单 */
    @POST("commodity/space/userOrder/add")
    Observable<ResponseBody> ShopCartPayAdd(@Query("ids") String[] ids);

    /**
     * 购物车结算
     *
     * @param
     * @return
     */
    @POST("commodity/space/webCharge")
    Observable<ResponseBody> getShopCartAppCharge(@Query("orderId") String orderId,
                                                  @Query("type") String type,
                                                  @Query("payType") String payType,
                                                  @Query("deviceType") String deviceType);


    /**
     * 答疑记录删除接口
     *
     * @param role
     * @return
     */
    @DELETE(BuildConfig.API_OTO_RE_URL + "order/history/{id}")
    Observable<ResponseBody> orderhistoryDel(@Path("id") String id, @Query("role") String role);


    @POST("productapi/product/courseManage/V1/findListByPage")
    Observable<ResponseBody> findListByPage(@Query("courseTypeId") String courseTypeId,@Query("start") int start, @Query("limit") int limit);

    @POST("paymentNewapi/payment/charge/get")
    Observable<ResponseBody> paymentcharge(@Query("orderType") String orderType, @Query("payType") String payType
            , @Query("commodityId") String commodityId,@Query("finalPrice") String finalPrice,@Query("deviceType") String deviceType);

    @POST("productapi/product/package/V1/find")
    Observable<ResponseBody> packagefind(@Query("packageId") String packageId);

}
