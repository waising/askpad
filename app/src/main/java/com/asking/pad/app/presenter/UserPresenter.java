package com.asking.pad.app.presenter;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asking.pad.app.AppContext;
import com.asking.pad.app.api.ApiRequestListener;
import com.asking.pad.app.commom.AESHelper;
import com.asking.pad.app.commom.CommonUtil;
import com.asking.pad.app.commom.Constants;
import com.asking.pad.app.commom.FileUtils;
import com.asking.pad.app.entity.PayEntity;
import com.asking.pad.app.entity.ShopCartPayEntity;
import com.asking.pad.app.mvp.BasePresenter;
import com.asking.pad.app.ui.camera.utils.BitmapUtil;
import com.asking.pad.app.ui.downbook.db.DbBookHelper;
import com.hanvon.HWCloudManager;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static com.asking.pad.app.commom.CommonUtil.getRequestBody;

/**
 * Created by jswang on 2017/4/6.
 */

public class UserPresenter extends BasePresenter<UserModel> {

    public void checkUserInfo() {
        mRxManager.add(mModel.checkUserInfo()
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ResponseBody>() {
                    @Override
                    public void call(ResponseBody obj) {
                        try {
                            String resStr = obj.string();
                            JSONObject jsonRes = JSON.parseObject(resStr);
                            String flag = jsonRes.getString("flag");
                            AppContext.getInstance().setIsUserDataPerfect(TextUtils.equals(flag, "0"));
                        } catch (Exception e) {
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                })

        );
    }

    public void ssoLogin(String userName, String pwd, ApiRequestListener mListener) {
        baseReq(mModel.ssoLogin(userName, pwd), "", mListener);
    }

    public void getYZM(String userName, ApiRequestListener mListener) {
        baseReq(mModel.getYZM(userName), "", mListener);
    }

    public void register(String mobile, String passWord, String verifyCode, ApiRequestListener mListener) {
        baseReq(mModel.register(mobile, passWord, verifyCode), "", mListener);
    }

    public void resetPass(String mobile, String passWord, String verifyCode, ApiRequestListener mListener) {
        baseReq(mModel.resetPass(mobile, passWord, verifyCode), "", mListener);
    }

    public void getResetPassYZM(String userName, ApiRequestListener mListener) {
        baseReq(mModel.getResetPassYZM(userName), "", mListener);
    }

    public void synclesson(boolean isReadDB, String commodityId, ApiRequestListener mListener) {
        String id = String.format("courseapi/synclesson/%s", commodityId);
        baseReqStrDB(mModel.synclesson(commodityId), isReadDB, commodityId, id, mListener);
    }

    public void findTreeListWithAllCourse(String productId, ApiRequestListener mListener) {
        String cacheKey = AppContext.getInstance().getUserId() + "_findTreeListWithAllCourse";
        baseReqFlag0Cache(mModel.findTreeListWithAllCourse(productId), "content", cacheKey, mListener);
    }

    public void firstreviewzhangjd(String orgId, ApiRequestListener mListener) {
        String filePath = Constants.getBookPath(String.format("firstreview/zhangjd/%s", orgId));
        baseReqFlag1File(mModel.firstreviewzhangjd(orgId), "firstReviewNodes", filePath, mListener);
    }

    public void firstreviewkesjd(String pid, ApiRequestListener mListener) {
        String filePath = Constants.getBookPath(String.format("firstreview/kesjd/%s", pid));
        baseReqFlag1File(mModel.firstreviewkesjd(pid), "firstReviewNodes", filePath, mListener);
    }

    public void secondreviewtree(String orgId, ApiRequestListener mListener) {
        String filePath = Constants.getBookPath(String.format("secondreview/tree？orgId=%s", orgId));
        baseReqFlag0File(mModel.secondreviewtree(orgId), "content", filePath, mListener);
    }

    public void synclesson(boolean isReadDB, String gradeId, String knowledgeId, int type, ApiRequestListener mListener) {
        String id = String.format("courseapi/synclesson/%s/%s/%s", gradeId, knowledgeId, type);
        baseReqStrDB(mModel.synclesson(gradeId, knowledgeId, type), isReadDB, gradeId, id, mListener);
    }

    public void getVoicePath(boolean isReadDB, final String gradeId, String knowledgeId, int type, int position, ApiRequestListener mListener) {
        final String id = String.format("courseapi/synclesson/voice/%s/%s/%s/%s", gradeId, knowledgeId, type, position);
        final String fileName = String.format("%s_%s_%s_%s", gradeId, knowledgeId, type, position);
        Observable mObservable = null;
        if (isReadDB) {
            try {
                mObservable = Observable.create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(final Subscriber<? super String> subscriber) {
                        try {
                            String musicUrl = FileUtils.getFileMusicPath(fileName);
                            if(!FileUtils.isFileExists(musicUrl)){
                                String value = DbBookHelper.getInstance().setDatabase(gradeId).getBookTableVoicePath(id);
                                byte[] res = AESHelper.decodeByte(value);
                                FileUtils.writeFile(res,fileName);
                            }
                            subscriber.onNext(musicUrl);
                        } catch (Exception e) {
                            subscriber.onError(e);
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            mObservable = mModel.getVoicePath(gradeId, knowledgeId, type, position);
        }
        setObservableByResponseBodyOrString(mObservable, 2,"", mListener);
    }

    public void subject(String answerstr, String code, ApiRequestListener mListener) {
        baseReqStr(mModel.subject(answerstr, code), mListener);
    }

    public void getSubjectTopic(boolean isReadDB, String gradeId, String knowledgeId, ApiRequestListener mListener) {
        String id = String.format("courseapi/sprint/%s/%s", gradeId, knowledgeId);
        baseReqStrDB(mModel.getSubjectTopic(gradeId, knowledgeId), isReadDB, gradeId, id, mListener);
    }

    public void getAllSubjectClassic(boolean isReadDB, String gradeId, String knowledgeId, String topic_id, int start, int limit, ApiRequestListener mListener) {
        String id = String.format("courseapi/sprint/%s/%s/%s", gradeId, knowledgeId, topic_id);
        baseReqStrDB(mModel.getAllSubjectClassic(gradeId, knowledgeId, topic_id, start, limit), isReadDB, gradeId, id, mListener);
    }

    public void checkTodaySign(ApiRequestListener mListener) {
        baseReqStr(mModel.checkTodaySign(), mListener);
    }

    public void dailySign(final ApiRequestListener mListener) {
        baseReq(mModel.dailySign(), "", mListener);
    }

    public void suitcy(String classId, String difficultyId, String choice_count, String filling_count,
                       String solving_count, final ApiRequestListener mListener) {
        baseReqStr(mModel.suitcy(classId, difficultyId, choice_count, filling_count, solving_count), mListener);
    }

    public void tuozyy(String classId, String difficultyId, String choice_count, String filling_count,
                       String solving_count, final ApiRequestListener mListener) {
        baseReq(mModel.tuozyy(classId, difficultyId, choice_count, filling_count, solving_count), "content", mListener);
    }

    public void sign(ApiRequestListener mListener) {
        baseReqStr(mModel.sign(), mListener);
    }

    /**
     * 答疑记录请求
     *
     * @param start
     * @param limit
     * @param mListener
     */
    public void findListByPage(String courseTypeId, int start, int limit, ApiRequestListener mListener) {
        baseReq(mModel.findListByPage(courseTypeId, start, limit), "content", mListener);
    }

    public void paymentcharge(String orderType, String payType, String commodityId, String finalPrice, ApiRequestListener mListener) {
        baseReq(mModel.paymentcharge(orderType, payType, commodityId, finalPrice), "content", mListener);
    }

    public void packagefind(String packageId, ApiRequestListener mListener) {
        baseReq(mModel.packagefind(packageId), "content", mListener);
    }

    public void orderhistory(String start, String limit, String account
            , String role, ApiRequestListener mListener) {
        baseReq(mModel.orderhistory(start, limit, account, role), "content", mListener);
    }

    public void userreact(String start, String limit, ApiRequestListener mListener) {
        baseReq(mModel.userreact(start, limit), "content", mListener);
    }

    public void findByCommodityId(String commodityId, ApiRequestListener mListener) {
        baseReq(mModel.findByCommodityId(commodityId), "content", mListener);
    }

    public void updateWithSchedule(String commodityId, String schedulePercent
            , String scheduleTitle, String scheduleId, String scheduleContent, ApiRequestListener mListener) {
        baseReq(mModel.updateWithSchedule(commodityId, schedulePercent
                , scheduleTitle, scheduleId, scheduleContent), "content", mListener);
    }

    public void studentinfo(ApiRequestListener mListener) {
        baseReq(mModel.studentinfo(AppContext.getInstance().getUserEntity().getUserName()), "content", mListener);
    }

    public void subjectClassic(String answerstr, String code, ApiRequestListener mListener) {
        try {
            Map<String, RequestBody> params = new HashMap<>();
            params.put("answerstr", getRequestBody(answerstr));
            params.put("code", getRequestBody(code.substring(0, 1)));
            baseReqStr(mModel.subjectClassic(params), mListener);
        } catch (Exception e) {
        }
    }

    public void firstreviewkaoqfx(String pid, ApiRequestListener mListener) {
        String filePath = Constants.getBookPath(String.format("firstreview/kaoqfx/%s"
                , pid));
        baseReqFlag1File(mModel.firstreviewkaoqfx(pid), "content", filePath, mListener);
    }

    public void firstreviewdiant(String pid, ApiRequestListener mListener) {
        String filePath = Constants.getBookPath(String.format("diant/%s"
                , pid));
        baseReqFlag1File(mModel.firstreviewdiant(pid), "", filePath, mListener);
    }

    public void firstreviewbeigk(String pid, String index, ApiRequestListener mListener) {
        String filePath = Constants.getBookPath(String.format("firstreview/beigk/%s/%s"
                , pid, index));
        baseReqFlag1File(mModel.firstreviewbeigk(pid, index), "nodes", filePath, mListener);
    }

    public void firstreviewshizyl(String pid, ApiRequestListener mListener) {
        String filePath = Constants.getBookPath(String.format("firstreview/shizyl/%s"
                , pid));
        baseReqFlag1File(mModel.firstreviewshizyl(pid), "subjects", filePath, mListener);
    }

    public void secondreviewzhuant(String pid, String field, ApiRequestListener mListener) {
        String filePath = Constants.getBookPath(String.format("secondreview/zhuant？pid=%s&field=%s"
                , pid, field));
        baseReqFlag0File(mModel.secondreviewzhuant(pid, field), "content", filePath, mListener);
    }

    public void orderfirstorder(String id, ApiRequestListener mListener) {
        baseReq(mModel.orderfirstorder(id), "content", mListener);
    }

    public void integralLog(int start, int limit, ApiRequestListener mListener) {
        baseReqStr(mModel.integralLog(start, limit), mListener);
    }

    public void ordercheckbill(String id, ApiRequestListener mListener) {
        baseReq(mModel.ordercheckbill(id), "content", mListener);
    }

    public void studentcomplain(String reason, String details, String id, ApiRequestListener mListener) {
        baseReq(mModel.studentcomplain(reason, details, id), "content", mListener);
    }

    public void getRegionInfo(String regionCode, ApiRequestListener mListener) {
        baseReqStr(mModel.getRegionInfo(regionCode), mListener);
    }

    public void productType(String packageTypeId, ApiRequestListener mListener) {
        baseReq(mModel.productType(packageTypeId), "content", mListener);
    }

    public void getCommodityList(String packageTypeId, int timeLimit, int start, int limit, ApiRequestListener mListener) {
        baseReq(mModel.getCommodityList(packageTypeId, timeLimit, start, limit), "content", mListener);
    }

    public void getAppCharge(PayEntity payEntity, ApiRequestListener mListener) {
        baseReq(mModel.getAppCharge(payEntity), "charge", mListener);
    }

    public void getAppReCharge(PayEntity payEntity, ApiRequestListener mListener) {
        baseReq(mModel.getAppReCharge(payEntity), "charge", mListener);
    }

    /**
     * 获取学校
     *
     * @param regionCode
     * @param mListener
     */
    public void presenterGetSchoolInfo(String regionCode, ApiRequestListener mListener) {
        baseReqStr(mModel.modelGetSchoolInfo(regionCode), mListener);
    }

    public void getRechargeList(int start, int limit, ApiRequestListener mListener) {
        baseReq(mModel.getRechargeList(start, limit), "content", mListener);
    }

    /**
     * 更新个人资料
     */
    public void updateUser(String name, String nickName, String sex,
                           String birthday, String regionName,
                           String regionCode, String schoolName,
                           String remark, String area,
                           String levelId, String classId, String avatar, ApiRequestListener mListener) {
        baseReqStr(mModel.updateUser(AppContext.getInstance().getToken(), name, nickName, sex, birthday, regionName,
                regionCode, schoolName, remark, area, levelId, classId, avatar), mListener);
    }

    public void orderbuild(String subject, String grade, String parse, String url, String wanted
            , String account, String name, String avatar, ApiRequestListener mListener) {
        if (TextUtils.isEmpty(parse)) {
            parse = "";
        }
        Map<String, RequestBody> params = new HashMap<>();
        params.put("subject", getRequestBody(subject));
        params.put("grade", getRequestBody(grade));
        params.put("parse", getRequestBody(parse));
        params.put("url", getRequestBody(url));
        params.put("wanted", getRequestBody(wanted));
        params.put("account", getRequestBody(account));
        params.put("name", getRequestBody(name));
        params.put("avatar", getRequestBody(avatar));

        baseReq(mModel.orderbuild(params), "content", mListener);
    }

    public void orderstate(String id, ApiRequestListener mListener) {
        baseReq(mModel.orderstate(id), "content", mListener);
    }

    public void nimtoken(String accid, ApiRequestListener mListener) {
        baseReqStr(mModel.nimtoken(accid), mListener);
    }

    public void modelMyWrongVersions(String subjectCatalog, String type, ApiRequestListener mListener) {
        baseReqStr(mModel.modelMyWrongVersions(subjectCatalog, type), mListener);
    }

    public void modelMyWrongGrade(String versionId, String type, ApiRequestListener mListener) {
        baseReqStr(mModel.modelMyWrongGrade(versionId, type), mListener);
    }

    public void errorCollection(String subjectCatalog, String postStr, ApiRequestListener mListener) {
        Map<String, RequestBody> params = new HashMap<>();
        params.put("post_str", getRequestBody(postStr));

        baseReqStr(mModel.errorCollection(subjectCatalog, params), mListener);
    }

    public void convertTabLClassic(String subjectCatalog, String subjectId, ApiRequestListener mListener) {
        baseReqStr(mModel.convertTabLClassic(subjectCatalog, subjectId), mListener);
    }

    public void ordercancel(String id, ApiRequestListener mListener) {
        baseReq(mModel.ordercancel(id), "content", mListener);
    }

    public void orderevaluate(String id, String reward, String star, String suggest, ApiRequestListener mListener) {
        baseReq(mModel.orderevaluate(id, reward, star, suggest), "content", mListener);
    }

    public void studentfavor(String userName, String account, ApiRequestListener mListener) {
        baseReq(mModel.studentfavor(userName, account), "content", mListener);
    }

    public void loadCameraPic(final String filePath, final ApiRequestListener mListener) {
        Observable<String> mObservable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                try {
                    BitmapUtil.createImageThumbnail(mContext, filePath, filePath, 500, 80);
                    subscriber.onNext(filePath);
                } catch (Exception e) {
                }
            }
        });
        baseReqStr1(mObservable, mListener);
    }

    public void getHwyQuestion(final String filePath, final HWCloudManager hwManager, final ApiRequestListener mListener) {
        Observable<String> mObservable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String greyPath = BitmapUtil.saveGreyBitmap(filePath);
                String result = "";
                try {
                    String re = hwManager.formulaOCRLanguage4Https(greyPath);
                    if (!TextUtils.isEmpty(re)) {
                        String reDecode = CommonUtil.decode1(re);
                        result = JSON.parseObject(reDecode).getString("result");
                    }
                } catch (Exception e) {
                }
                subscriber.onNext(result);
            }
        });
        baseReqStr1(mObservable, mListener);
    }

    public void logout(final ApiRequestListener mListener) {
        baseReqStr(mModel.logout(), new ApiRequestListener<String>() {
            @Override
            public void onResultSuccess(String res) {
                baseReqStr(mModel.logout2(), mListener);
            }

            @Override
            public void onResultFail() {
                baseReqStr(mModel.logout2(), mListener);
            }
        });
    }


    /**
     * 修改密码
     *
     * @param oldPass
     * @param pass
     * @param pass1
     * @param mListener
     */
    public void presenterChangePassword(String oldPass, String pass, String pass1, ApiRequestListener mListener) {
        baseReq(mModel.modelchangePassword(oldPass, pass, pass1), "", mListener);
    }

    /**
     * 超级辅导课购买加入购物车
     */
    public void presenterAddShopCartSuper(String commodityId, int versionId, ApiRequestListener mListener) {
        baseReq(mModel.modelAddShopCartSuper(commodityId, versionId), "", mListener);
    }


    /**
     * 阿思币充值加入购物车
     */
    public void presenterAddShopCartAskCoin(String rechargeId, ApiRequestListener mListener) {
        baseReq(mModel.modelAddShopCartAsAskCoin(rechargeId), "", mListener);
    }


    /**
     * @param ids
     * @param mListener
     */
    public void getShopCartPayAdd(String[] ids, ApiRequestListener mListener) {
        baseReqStr(mModel.getShopCartPayAdd(ids), mListener);
    }

    public void ShopCartPay(ShopCartPayEntity shopCartPayEntity, ApiRequestListener mListener) {
        baseReq(mModel.getShopCartAppCharge(shopCartPayEntity), "charge", mListener);
    }

    /**
     * 答疑记录删除接口
     *
     * @param mListener
     */
    public void orderhistoryDel(String id, String role, ApiRequestListener mListener) {
        baseReq(mModel.orderhistoryDel(id, role), "content", mListener);
    }

}
