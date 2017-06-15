package com.asking.pad.app.commom;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.asking.pad.app.AppContext;
import com.asking.pad.app.BuildConfig;
import com.asking.pad.app.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jswang on 2017/4/6.
 */

public class Constants {
    /**
     * 获取用户头像
     */
    public final static String USER_AVATAR = BuildConfig.API_URL + "/user/avatar/";

    public final static String APP_BASE_PATH = "AskingPad";

    public final static String APP_CACHE_PATH = APP_BASE_PATH + "/cache/";

    public final static String APP_BOOK_PATH = APP_BASE_PATH + "/BOOK/";

    public final static String APP_RECORDER_VIDEO_PATH = APP_BASE_PATH + "/RecorderVideo/";

    public final static String APP_CLASSMEDIA_PATH = APP_BASE_PATH + "/classvideo";

    public final static String APP_CLASSMEDIA_PDF_PATH = APP_CLASSMEDIA_PATH + "/pdf/";

    public final static String APP_CLASSMEDIA_VIDEO_PATH = APP_CLASSMEDIA_PATH + "/video/";

    public final static String APP_CAMERA_PATH_KEY = "APP_CAMERA_PATH_KEY";

    /**
     * 缓存文件，这下面包括：安装包、拍照缓存、灰度照片保存，缓存清理位置
     */
    public final static String APP_CACHE_CACHE_PATH = APP_CACHE_PATH + "cache/";

    /**
     * 拍照保存位置
     */
    public final static String APP_CACHE_IMG_PATH = APP_CACHE_CACHE_PATH + "img/";
    /**
     * 缓存灰度照片位置
     */
    public final static String APP_CACHE_IMG_GRAY_PATH = APP_CACHE_CACHE_PATH + "img/gray/";

    public static final String GifHeard = "asset://com.asking91.app/";

    public static final String TAG = "UserConstant";
    public static final String TOKEN = "ticket";
    public static final String QINIUTOKEN = "QINIUTOKEN";

    public static final String IS_USER_DATA_PERFECT = "is_user_data_perfect";

    public static final String USER_DATA = "user_data";

    public static final String ASK_HOST = "phphub.org";

    public static final String DEEP_LINK_PREFIX = "phphub://";

    public final static String PLATFORM = "Android";

    public final static String WEB_URL = "web_url";
    public final static String WEB_TITLE = "web_title";
    public final static String WEB_IMAGE_URL = "web_image_url";

    public final static String ASKPAD_ACCOUNT = "ASKPAD_ACCOUNT";
    public final static String ASKPAD_PASSWORD = "ASKPAD_PASSWORD";

    public final static String BOOK_PATH_KEY = "BOOK_PATH_KEY";

    /**
     * 用户注册协议
     */
    public static final String PROTOCOL = "http://7xj9ur.com1.z0.glb.clouddn.com/app/agreement.html";

    public static final String SuperTutorialHtmlCss = "<html><head><style type=\"text/css\">body{color:#888888;vertical-align:middle;}</style></head><body>";

    public static final String SuperTutorialHtmlCss2 = "<html><head><style type=\"text/css\">body{color:%s;vertical-align:middle;}</style></head><body>%s</body></html>";

    public static final String MD_HTML_SUFFIX = "</body></html>";

    public static final String[] gradeVersionValues = new String[]{"一年级", "二年级", "三年级", "四年级", "五年级", "六年级", "七年级", "八年级", "九年级", "高一", "高二", "高三"};

    /**
     * M2-初中数学（8）  P2-初中物理（6）  M3-高中数学（9）  P3-高中物理（7）
     */
    public static final String[] subjectKeys = {"M2", "P2", "M3", "P3"};

    public static final String[] subjectNames = new String[]{"初中数学", "初中物理", "高中数学", "高中物理"};
    /**
     * 学科对应的id
     */
    public static final String[] subjectValues = {"M", "P"};
    /**
     * 年级对应的id
     */
    public static final String[] versionTvValues = {"07", "08", "09", "10", "11", "12"};
    public static final String[] versionTv = new String[]{"七年级", "八年级", "九年级", "高一", "高二", "高三"};
    /**
     * 荣联通讯录
     */
    public static final String RLTXL_KEY = "aaf98f89524954cc0152580140ca1bc3";
    public static final String RLTXL_TOKEN = "cae43d78c06055ded68b493705896702";
    /**
     * 汉王云
     */
    public static final String HWY_KEY = "a2f10384-59ca-472f-ad67-2471d7694c53";
    /**
     * 七牛上传存放文件的投递至
     */
    public static final String QiNiuHead = "http://7xj9ur.com1.z0.glb.clouddn.com/";

    /**
     * 网易Token
     */
    public static final String WYBAIBAN_TOKEN = "wybaiban_token";

    public static String getBookPath(String path) {
        String filePath = String.format("%s/%s%s/offline/%s",Environment.getExternalStorageDirectory(),Constants.APP_BOOK_PATH,getBookdir(),path);
        return filePath;
    }

    public static String getQiNiuFile(String path) {
        String filePath = String.format("%s%s",QiNiuHead ,path);
        return filePath;
    }

    /**
     * 书本解压文件夹
     * @param classType
     * @param versionId
     * @param gradeId
     */
    public static void setBookdir(String classType,String versionId,String gradeId) {
        String fileDir = String.format("%s_%s_%s_dir",classType,versionId,gradeId);
        AppContext.getInstance().setPreferencesValue(BOOK_PATH_KEY,fileDir);
    }

    public static String getBookdir() {
        return  AppContext.getInstance().getPreferencesStr(BOOK_PATH_KEY);
    }

    public static final String[] versionImages = {"北京课改版:M2_1.png", "北师大版:M2_2.png", "沪科版:M2_3.png", "华师大版:M2_4.png"
            , "青岛版:M2_5.png", "人教版:M2_6.png", "苏科版:M2_7.png", "湘教版:M2_8.png", "冀教版:M2_9.png", "浙教版:M2_10.png",
            "沪科版:P2_1.png", "人教版:P2_2.png", "北师大版:P2_3.png",
            "北师大版:M3_1.png", "沪教版:M3_2.png", "人教A版:M3_3.png", "人教B版:M3_4.png", "苏教版:M3_5.png", "湘教版:M3_6.png",
            "沪科版:P3_1.png", "教科版:P3_2.png", "鲁科版:P3_3.png", "人教版:P3_4.png", "粤教版:P3_5.png"};

    public static String getVersionImages(String km) {
        try {
            for (String str : versionImages) {
                if (str.contains(km)) {
                    return str.split(":")[1];
                }
            }
        } catch (Exception e) {
        }
        return "";
    }

    public static String getClassName(Context mContext, String classType) {
        String className = "";
        if (TextUtils.equals("M2", classType)) {
            className = mContext.getString(R.string.ask_czsx);
        } else if (TextUtils.equals("P2", classType)) {
            className = mContext.getString(R.string.ask_czwl);
        } else if (TextUtils.equals("M3", classType)) {
            className = mContext.getString(R.string.ask_gzsx);
        } else if (TextUtils.equals("P3", classType)) {
            className = mContext.getString(R.string.ask_gzwl);
        }
        return className;
    }

    /**
     * M2-初中数学（8）  P2-初中物理（6）  M3-高中数学（9）  P3-高中物理（7）
     */
    public static String getActionType(String classType) {
        String actionType = "";
        if (TextUtils.equals("M2", classType)) {
            actionType = "8";
        } else if (TextUtils.equals("P2", classType)) {
            actionType = "6";
        } else if (TextUtils.equals("M3", classType)) {
            actionType = "9";
        } else if (TextUtils.equals("P3", classType)) {
            actionType = "7";
        }
        return actionType;
    }

    public static int getClassIcon(String classType) {
        int resId = 0;
        if (TextUtils.equals("M2", classType)) {
            resId = R.mipmap.ic_superclass_m2;
        } else if (TextUtils.equals("P2", classType)) {
            resId = R.mipmap.ic_superclass_p2;
        } else if (TextUtils.equals("M3", classType)) {
            resId = R.mipmap.ic_superclass_m3;
        } else if (TextUtils.equals("P3", classType)) {
            resId = R.mipmap.ic_superclass_p3;
        }
        return resId;
    }

    //收藏类型
    public interface Collect{
        int knowledge = 1;//知识点
        int title = 2;//题目
        int paper = 3;//试卷
        int refer = 4;//教育资讯
        int other= 5;//其他
    }


    /**更新包位置*/
    public final static String APP_CACHE_APK_PATH = APP_CACHE_PATH+"update/";

    public static String getUserGradeName() {
        try {
            String levelId = AppContext.getInstance().getUserEntity().getLevelId(); // 年级
            if (!TextUtils.isEmpty(levelId)) {
                int integerId = Integer.valueOf(levelId); // 包装类 Integer 不能直接运算(下面的减1)，会报错，得转成基本数据类型 int
                if (integerId > 0) { //要再判断下
                    String gradeVersionValue = gradeVersionValues[integerId - 1];
                    if (!TextUtils.isEmpty(gradeVersionValue)) {
                        return gradeVersionValue;
                    }
                }
            }
        }catch (Exception e){}
        return "";
    }

    public static String getPHtmlCss(String c){
        return String.format("<p>%s</p>",c);
    }

    public static String getRecorderVideoPath() {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String fileName = timeStamp + ".mp4";

        File file = new File(Environment.getExternalStorageDirectory() + "/" + Constants.APP_RECORDER_VIDEO_PATH);
        if (file.exists() && !file.isDirectory()) {
            file.delete();
            file.mkdirs();
        }

        if (!file.exists()) {
            file.mkdirs();
        }

        return file.getAbsolutePath() + "/" + fileName;
    }

    public static String getPdfDir() {
        File file = new File(Environment.getExternalStorageDirectory() + "/" + APP_CLASSMEDIA_PDF_PATH);
        if (file.exists() && !file.isDirectory()) {
            file.delete();
            file.mkdirs();
        }

        if (!file.exists()) {
            file.mkdirs();
        }

        return file.getAbsolutePath();
    }

    public static String getVideoPath(String url) {
        String fileName = FileUtils.getFileName(url);
        File file = new File(Environment.getExternalStorageDirectory() + "/" + APP_CLASSMEDIA_VIDEO_PATH);
        if (file.exists() && !file.isDirectory()) {
            file.delete();
            file.mkdirs();
        }

        if (!file.exists()) {
            file.mkdirs();
        }

        return file.getAbsolutePath()+ "/" + fileName;
    }

    public static String getPdfPath(String url) {
        String fileName = FileUtils.getFileName(url);
        return getPdfDir() + "/" + fileName;
    }

}
