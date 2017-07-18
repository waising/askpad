package com.asking.pad.app.presenter;

import com.asking.pad.app.api.Networks;
import com.asking.pad.app.entity.PayEntity;
import com.asking.pad.app.entity.ShopCartPayEntity;
import com.asking.pad.app.mvp.BaseModel;
import com.asking.pad.app.mvp.RxSchedulers;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Observable;

/**
 * Created by jswang on 2017/4/6.
 */

public class UserModel extends BaseModel {

    public Observable<ResponseBody> getYZM(String mobile) {
        return Networks.getInstance().getUserApi().getYZM(mobile)
                .compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> ssoLogin(String userName, String pwd) {
        return Networks.getInstance().getUserApi().ssoLogin(userName, pwd)
                .compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> register(String mobile, String passWord, String verifyCode) {
        return Networks.getInstance().getUserApi().register(mobile, passWord, verifyCode)
                .compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> getResetPassYZM(String mobile) {
        return Networks.getInstance().getUserApi().getResetPassYZM(mobile)
                .compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> resetPass(String mobile, String password, String verifyCode) {
        return Networks.getInstance().getUserApi().resetPass(mobile, password, verifyCode).compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> findTreeListWithAllCourse(String productId) {
        return Networks.getInstance().getUserApi().findTreeListWithAllCourse(productId).compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> checkUserInfo() {
        return Networks.getInstance().getUserApi().checkUserInfo().compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> synclesson(String commodityId) {
        return Networks.getInstance().getUserApi().synclesson(commodityId).compose(RxSchedulers.<ResponseBody>io_main());
    }


    public Observable<ResponseBody> buyStudyLevel(String versionLevelId) {
        return Networks.getInstance().getUserApi().buyStudyLevel(versionLevelId).compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> firstreviewzhangjd(String orgId) {
        return Networks.getInstance().getUserApi().firstreviewzhangjd(orgId).compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> firstreviewkesjd(String pid) {
        return Networks.getInstance().getUserApi().firstreviewkesjd(pid).compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> secondreviewtree(String orgId) {
        return Networks.getInstance().getUserApi().secondreviewtree(orgId).compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> synclesson(String gradeId, String knowledgeId, int type) {
        return Networks.getInstance().getUserApi().synclesson(gradeId, knowledgeId, type).compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> getVoicePath(String gradeId, String knowledgeId, int type, int position) {
        return Networks.getInstance().getUserApi().getVoicePath(gradeId, knowledgeId, type, position).compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> subject(String answerstr, String code) {
        return Networks.getInstance().getUserApi().subject(answerstr, code).compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> getSubjectTopic(String gradeId, String knowledgeId) {
        return Networks.getInstance().getUserApi().getSubjectTopic(gradeId, knowledgeId).compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> getAllSubjectClassic(String gradeId, String knowledgeId, String topic_id, int start, int limit) {
        return Networks.getInstance().getUserApi().getAllSubjectClassic(gradeId, knowledgeId, topic_id, start, limit).compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> subjectClassic(Map<String, RequestBody> params) {
        return Networks.getInstance().getUserApi().subjectClassic(params).compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> firstreviewkaoqfx(String pid) {
        return Networks.getInstance().getUserApi().firstreviewkaoqfx(pid).compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> firstreviewdiant(String pid) {
        return Networks.getInstance().getUserApi().firstreviewdiant(pid).compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> firstreviewbeigk(String pid, String index) {
        return Networks.getInstance().getUserApi().firstreviewbeigk(pid, index).compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> firstreviewshizyl(String pid) {
        return Networks.getInstance().getUserApi().firstreviewshizyl(pid).compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> secondreviewzhuant(String pid, String field) {
        return Networks.getInstance().getUserApi().secondreviewzhuant(pid, field).compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> qiniutoken() {
        return Networks.getInstance().getUserApi().qiniutoken().compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> studentinfo(String userName) {
        return Networks.getInstance().getUserApi()
                .studentinfo(userName).compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> communionapi(String grade,String subject,int start,int limit) {
        return Networks.getInstance().getUserApi()
                .communionapi(grade,subject,start,limit).compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> getQuestionList(String type,String query,String km,
                                                    String levelId,String state,int start,int limit) {
        return Networks.getInstance().getUserApi()
                .getQuestionList(type,query,km,levelId,state,start,limit).compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> topicmsg(RequestBody body) {
        return Networks.getInstance().getUserApi().topicmsg(body).compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> orderbuild(Map<String, RequestBody> params) {
        return Networks.getInstance().getUserApi()
                .orderbuild(params).compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> orderstate(String id) {
        return Networks.getInstance().getUserApi()
                .orderstate(id).compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> nimtoken(String accid) {
        return Networks.getInstance().getUserApi()
                .nimtoken(accid).compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> ordercancel(String id) {
        return Networks.getInstance().getUserApi()
                .ordercancel(id).compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> orderfirstorder(String id) {
        return Networks.getInstance().getUserApi()
                .orderfirstorder(id).compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> integralLog(int start, int limit) {
        return Networks.getInstance().getUserApi().integralLog(start,limit).compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> ordercheckbill(String id) {
        return Networks.getInstance().getUserApi()
                .ordercheckbill(id).compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> orderevaluate(String id, String reward, String star, String suggest) {
        return Networks.getInstance().getUserApi()
                .orderevaluate(id, reward, star, suggest).compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> studentfavor(String userName,String account) {
        return Networks.getInstance().getUserApi()
                .studentfavor(userName,account).compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> orderhistory(String start, String limit, String account
            , String role) {
        return Networks.getInstance().getUserApi()
                .orderhistory(start, limit, account, role).compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> userreact(String start, String limit) {
        return Networks.getInstance().getUserApi()
                .userreact(start, limit).compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> findByCommodityId(String commodityId) {
        return Networks.getInstance().getUserApi()
                .findByCommodityId(commodityId).compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> updateWithSchedule(String commodityId, String schedulePercent
            , String scheduleTitle, String scheduleId, String scheduleContent) {
        return Networks.getInstance().getUserApi()
                .updateWithSchedule(commodityId, schedulePercent
                        , scheduleTitle, scheduleId, scheduleContent).compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> findListByPage(String courseTypeId,int start, int limit) {
        return Networks.getInstance().getUserApi()
                .findListByPage(courseTypeId,start, limit).compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> paymentcharge(String orderType, String payType, String commodityId,String finalPrice) {
        return Networks.getInstance().getUserApi()
                .paymentcharge(orderType, payType,commodityId,finalPrice,"androidPad").compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> packagefind(String packageId) {
        return Networks.getInstance().getUserApi()
                .packagefind(packageId).compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> studentcomplain(String reason, String details, String id) {
        return Networks.getInstance().getUserApi()
                .studentcomplain(reason, details, id).compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> getRegionInfo(String regionCode) {
        return Networks.getInstance().getUserApi()
                .getRegionInfo(regionCode).compose(RxSchedulers.<ResponseBody>io_main());
    }

    /**
     * /获取学校
     *
     * @param regionCode
     * @return
     */
    public Observable<ResponseBody> modelGetSchoolInfo(String regionCode) {
        return Networks.getInstance().getUserApi().getSchoolInfo(regionCode).compose(RxSchedulers.<ResponseBody>io_main());
    }


    /**
     * 更新个人信息
     *
     * @param ticket
     * @param name
     * @param nickName
     * @param sex
     * @param birthday
     * @param regionName
     * @param regionCode
     * @param schoolName
     * @param remark
     * @param area
     * @param levelId
     * @param classId
     * @return
     */
    public Observable<ResponseBody> updateUser(String ticket, String name, String nickName, String sex,
                                               String birthday, String regionName,
                                               String regionCode, String schoolName,
                                               String remark, String area,
                                               String levelId, String classId,String avatar) {
        return Networks.getInstance().getUserApi().updateUser(ticket, name, nickName, sex, birthday, regionName, regionCode, schoolName,
                remark, area, levelId, classId,avatar).compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> productType(String packageTypeId) {
        return Networks.getInstance().getUserApi()
                .productType(packageTypeId).compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> getCommodityList(String packageTypeId, int timeLimit, int start, int limit) {
        return Networks.getInstance().getUserApi()
                .getCommodityList(packageTypeId, timeLimit, start,limit).compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> getRechargeList(int start, int limit) {
        return Networks.getInstance().getUserApi()
                .getRechargeList(start,limit).compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> getAppCharge(PayEntity payEntity) {
        return Networks.getInstance().getUserApi()
                .getAppCharge(payEntity.getCommodityId(), payEntity.getRechargeId(), payEntity.getNum(), payEntity.getVersionId(),
                        payEntity.getType(), payEntity.getPayType(), payEntity.getClientIP(), "androidPad").compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> getAppReCharge(PayEntity payEntity) {
        return Networks.getInstance().getUserApi()
                .getAppReCharge(payEntity.getOrderId(),
                        payEntity.getType(), payEntity.getPayType(), "androidPad").compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> logout() {
        return Networks.getInstance().getUserApi().loginOut().compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> logout2() {
        return Networks.getInstance().getUserApi().loginOut2().compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> checkTodaySign() {
        return Networks.getInstance().getUserApi().checkTodaySign().compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> dailySign() {
        return Networks.getInstance().getUserApi()
                .dailySign().compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> suitcy(String classId, String difficultyId, String choice_count, String filling_count,
                                              String solving_count) {
        return Networks.getInstance().getUserApi()
                .suitcy(classId,difficultyId,choice_count,filling_count,solving_count).compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> tuozyy(String classId, String difficultyId, String choice_count, String filling_count,
                                           String solving_count) {
        return Networks.getInstance().getUserApi()
                .tuozyy(classId,difficultyId,choice_count,filling_count,solving_count).compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> sign() {
        return Networks.getInstance().getUserApi()
                .sign().compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> modelMyWrongVersions(String subjectCatalog, String type) {
        return Networks.getInstance().getUserApi()
                .getMyWrongVersions(subjectCatalog, type).compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> convertTabLClassic(String subjectCatalog, String subjectId) {
        return Networks.getInstance().getUserApi()
                .convertTabLClassic(subjectCatalog, subjectId).compose(RxSchedulers.<ResponseBody>io_main());
    }

    public Observable<ResponseBody> modelMyWrongGrade(String versionId, String type) {
        return Networks.getInstance().getUserApi()
                .getMyWrongGrade(versionId, type).compose(RxSchedulers.<ResponseBody>io_main());
    }

    /**
     * 获取错题本
     *
     * @return
     */
    public Observable<ResponseBody> errorCollection(String subjectCatalog, Map<String, RequestBody> params) {
        return Networks.getInstance().getUserApi()
                .errorCollection(subjectCatalog, params).compose(RxSchedulers.<ResponseBody>io_main());
    }

    // 修改密码
    public Observable<ResponseBody> modelchangePassword(String oldPass, String pass, String pass1) {
        return Networks.getInstance().getUserApi()
                .chagePassword(oldPass, pass, pass1).compose(RxSchedulers.<ResponseBody>io_main());
    }

    /**
     * 加入购物车超级辅导课购买
     */
    public Observable<ResponseBody> modelAddShopCartSuper(String commodityId, int versionId) {
        return Networks.getInstance().getUserApi()
                .AddShopCartSuper(commodityId, versionId).compose(RxSchedulers.<ResponseBody>io_main());
    }

    /**
     * 加入购物车阿思币充值
     */
    public Observable<ResponseBody> modelAddShopCartAsAskCoin(String rechargeId) {
        return Networks.getInstance().getUserApi()
                .AddShopCartAskCoin(rechargeId).compose(RxSchedulers.<ResponseBody>io_main());
    }

    /**
     * 购物车支付生成订单
     *
     * @param ids
     * @return
     */
    public Observable<ResponseBody> getShopCartPayAdd(String[] ids) {
        return Networks.getInstance().getUserApi()
                .ShopCartPayAdd(ids).compose(RxSchedulers.<ResponseBody>io_main());
    }

    /**
     * 购物车结算
     *
     * @param
     * @return
     */
    public Observable<ResponseBody> getShopCartAppCharge(ShopCartPayEntity shopCartPayEntity) {
        return Networks.getInstance().getUserApi()
                .getShopCartAppCharge(shopCartPayEntity.getOrderId(),shopCartPayEntity.getType(), shopCartPayEntity.getPayType(), "androidPad").compose(RxSchedulers.<ResponseBody>io_main());
    }

    /**
     * 答疑记录删除
     * @return
     */
    public Observable<ResponseBody> orderhistoryDel(String id,String role) {
        return Networks.getInstance().getUserApi()
                .orderhistoryDel(id, role).compose(RxSchedulers.<ResponseBody>io_main());
    }


}
