package com.asking.pad.app;

import android.content.Context;
import android.graphics.Color;
import android.os.Environment;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.asking.pad.app.api.CustomImageDownaloder;
import com.asking.pad.app.api.OkHttpNetworkFetcher;
import com.asking.pad.app.commom.Constants;
import com.asking.pad.app.commom.DeviceUtil;
import com.asking.pad.app.entity.superclass.StudyClassSubject;
import com.bugtags.library.Bugtags;
import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.github.piasy.biv.BigImageViewer;
import com.github.piasy.biv.loader.fresco.FrescoImageLoader;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.L;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jswang on 2017/4/6.
 */

public class AppContext extends BaseApplication {

    public static AppContext mInstance;

    public static AppContext getInstance() {
        if (mInstance == null) {
            mInstance = new AppContext();
        }
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.mInstance = this;

        /**
         * 设置 Fresco 图片缓存的路径
         */
        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(getApplicationContext())
                .setBaseDirectoryPath(getOwnCacheDirectory(this, Constants.APP_CACHE_PATH))
                .build();
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(getApplicationContext())
                .setNetworkFetcher(new OkHttpNetworkFetcher(this))
                .setMainDiskCacheConfig(diskCacheConfig)
                .setSmallImageDiskCacheConfig(diskCacheConfig)
                .build();

        //初始化 Fresco 图片缓存库ImagePipelineConfigUtils.getDefaultImagePipelineConfig(getApplicationContext())
        //Fresco.initialize(this, config);
        BigImageViewer.initialize(FrescoImageLoader.with(this, config));

        ImageLoaderConfiguration iconfig = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheExtraOptions(480, 800)
                .threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new WeakMemoryCache())
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .memoryCacheSize(2*1024*1024)
                .diskCacheSize(50 * 1024 * 1024) // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .imageDownloader(new CustomImageDownaloder(this))
                .build();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(iconfig);
        L.writeLogs(false);

        /**********网易白板************/
        // SDK初始化（启动后台服务，若已经存在用户登录信息， SDK 将完成自动登录）
        NIMClient.init(this, loginInfo(), getOptions());

        int bugtagsEvent = Bugtags.BTGInvocationEventNone;
        if(BuildConfig.DEBUG){
            bugtagsEvent = Bugtags.BTGInvocationEventBubble;
        }
        //在这里初始化
        Bugtags.start("946471e6b02485187261a14ba24c5a39", this, bugtagsEvent);
    }

    public static File getOwnCacheDirectory(Context context, String cachePath) {
        File appCacheDir = null;

        if ("mounted".equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
            appCacheDir = new File(Environment.getExternalStorageDirectory(), cachePath);
        }

        if (appCacheDir == null || !appCacheDir.exists() && !appCacheDir.mkdirs()) {
            appCacheDir = context.getCacheDir();
        }

        return appCacheDir;
    }

    public static boolean hasExternalStoragePermission(Context context) {
        int perm = context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE");
        return perm == 0;
    }

    // 如果已经存在用户登录信息，返回LoginInfo，否则返回null即可
    private LoginInfo loginInfo() {
        String token = getWYToken();
        if (TextUtils.isEmpty(token)) {
            return null;
        }
        String loginAcount = getUserName().toLowerCase();
        return new LoginInfo(loginAcount, token);
    }

    //    ****************网易白板******************
// 如果返回值为 null，则全部使用默认参数。
    private SDKOptions getOptions() {
        SDKOptions options = new SDKOptions();

        // 如果将新消息通知提醒托管给 SDK 完成，需要添加以下配置。否则无需设置。
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
//        config.notificationEntrance = WelcomeActivity.class; // 点击通知栏跳转到该Activity
        config.notificationSmallIconId = R.mipmap.ic_logo;
        // 呼吸灯配置
        config.ledARGB = Color.GREEN;
        config.ledOnMs = 1000;
        config.ledOffMs = 1500;
        // 通知铃声的uri字符串
        config.notificationSound = "android.resource://com.netease.nim.demo/raw/msg";
        options.statusBarNotificationConfig = config;

        // 配置保存图片，文件，log 等数据的目录
        // 如果 options 中没有设置这个值，SDK 会使用下面代码示例中的位置作为 SDK 的数据目录。
        // 该目录目前包含 log, file, image, audio, video, thumb 这6个目录。
        // 如果第三方 APP 需要缓存清理功能， 清理这个目录下面个子目录的内容即可。
        String sdkPath = Environment.getExternalStorageDirectory() + "/" + Constants.APP_BASE_PATH + "/nim";
        options.sdkStorageRootPath = sdkPath;

        // 配置是否需要预下载附件缩略图，默认为 true
        options.preloadAttach = true;

        // 配置附件缩略图的尺寸大小。表示向服务器请求缩略图文件的大小
        // 该值一般应根据屏幕尺寸来确定， 默认值为 Screen.width / 2
        options.thumbnailSize = DeviceUtil.getImageMaxEdge(this);
        return options;
    }

    private Map<String,StudyClassSubject> studyClassMap = new HashMap<>();
    public StudyClassSubject getStudyClassic(String classType) {
        try{
            String cacheKey = AppContext.getInstance().getUserId() + "_getSuperSelect";
            String res = AppContext.getInstance().getPreferencesStr(cacheKey);
            List<StudyClassSubject> list = JSON.parseArray(res,StudyClassSubject.class);

            studyClassMap.clear();
            for(StudyClassSubject e:list){
                studyClassMap.put(e.getSubjectCatalog(),e);
            }
        }catch (Exception e){}
        return studyClassMap.get(classType);
    }
}
