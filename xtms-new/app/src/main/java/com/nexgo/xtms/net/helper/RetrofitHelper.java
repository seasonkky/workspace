package com.nexgo.xtms.net.helper;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.services.DownloadMgrInitialParams;
import com.nexgo.xtms.R;
import com.nexgo.xtms.constant.ApiConstant;
import com.nexgo.xtms.constant.SPConstant;
import com.nexgo.xtms.data.source.repository.ServerRepository;
import com.nexgo.xtms.mvp.base.App;
import com.nexgo.xtms.net.server.ServerListService;
import com.nexgo.xtms.net.update.TasksService;
import com.nexgo.xtms.util.CommonUtil;
import com.nexgo.xtms.util.LogUtil;
import com.nexgo.xtms.util.PreferenceUtil;
import com.nexgo.xtms.util.ToastUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocketFactory;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.nexgo.xtms.constant.ApiConstant.DEFAULT_URL;

/**
 * Retrofit帮助类
 */
public class RetrofitHelper {

    private static OkHttpClient mOkHttpClient;

    static {
        initOkHttpClient();
        // 获取激活的URL地址
        baseUrl = ServerRepository.getInstance().getActiveServer().getUrlByModel();
    }

    private static String baseUrl;

    public static void setBaseUrl(String baseUrl) {
        RetrofitHelper.baseUrl = baseUrl;
    }

    /**
     * 创建【服务器列表】模块接口对象
     *
     * @return
     */

    public static ServerListService getServerListService() {

        return createApi(ServerListService.class, DEFAULT_URL);
    }

    /**
     * 创建【更新信息】模块接口对象
     *
     * @return
     */

    public static TasksService getTasksService() {

        return createApi(TasksService.class, baseUrl);
    }

    /**
     * 根据传入的baseUrl，和api创建retrofit
     *
     * @param clazz
     * @param baseUrl
     * @param <T>
     * @return
     */
    private static <T> T createApi(Class<T> clazz, String baseUrl) {

        if (baseUrl == null || "".equals(baseUrl)) {
            baseUrl = DEFAULT_URL;
        }
        Retrofit retrofit;
        try {
            // 捕获因为服务器地址设置非法导致的异常

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(mOkHttpClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        } catch (Exception e) {
            ToastUtil.ShortToast(R.string.url_illegal);

            // 还原设置
            PreferenceUtil.putLong(SPConstant.KEY_SERVER, 0L);
            RetrofitHelper.setBaseUrl(ApiConstant.DEFAULT_URL);

            retrofit = new Retrofit.Builder()
                    .baseUrl(DEFAULT_URL)
                    .client(mOkHttpClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit.create(clazz);
    }

    /**
     * 初始化OKHttpClient,设置缓存,设置超时时间,设置打印日志,设置UA拦截器
     */

    public static void initOkHttpClient() {
        LogUtil.test("initOkHttpClient");

        SSLSocketFactory socketFactory = SSLHelper.getSocketFactory(App.getInstance());
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        if (mOkHttpClient == null) {
            synchronized (RetrofitHelper.class) {
                if (mOkHttpClient == null) {
                    //设置Http缓存
                    Cache cache = new Cache(new File(App.getInstance()
                            .getCacheDir(), "HttpCache"), 1024 * 1024 * 10);

                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                    FileDownloader.init(App.getInstance(), new DownloadMgrInitialParams.InitCustomMaker()
                            .connectionCreator(new OkHttp3Connection.Creator(builder)));
                    mOkHttpClient = builder
                            .cache(cache)
                            .addInterceptor(interceptor)
                            .sslSocketFactory(socketFactory)
                            .addNetworkInterceptor(new CacheInterceptor())
//                            .addNetworkInterceptor(new StethoInterceptor())
                            //  .addInterceptor(new UserAgentInterceptor())
                            .retryOnConnectionFailure(true)
                            .connectTimeout(10, TimeUnit.SECONDS)
                            .writeTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(10, TimeUnit.SECONDS)
                            .hostnameVerifier((hostname, session) -> true)
                            .build();
                }
            }
        }
    }


//    /**
//     * UA拦截器
//     */
//    private static class UserAgentInterceptor implements Interceptor
//    {
//
//        @Override
//        public Response intercept(Chain chain) throws IOException
//        {
//
//            Request originalRequest = chain.request();
//            Request requestWithUserAgent = originalRequest.newBuilder()
//                    .removeHeader("User-Agent")
//                    .addHeader("User-Agent", ApiConstant.COMMON_UA_STR)
//                    .build();
//            return chain.proceed(requestWithUserAgent);
//        }
//    }


    /**
     * 为okhttp添加缓存，这里是考虑到服务器不支持缓存时，从而让okhttp支持缓存
     */
    private static class CacheInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {

            // 有网络时 设置缓存超时时间1个小时
            int maxAge = 60 * 60;
            // 无网络时，设置超时为1天
            int maxStale = 60 * 60 * 24;
            Request request = chain.request();

            if (CommonUtil.isNetworkAvailable(App.getInstance())) {
                //有网络时只从网络获取
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_NETWORK)
                        .build();
            } else {
                //无网络时只从缓存中读取
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            Response response = chain.proceed(request);
            if (CommonUtil.isNetworkAvailable(App.getInstance())) {
                response = response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            } else {

                response = response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
            return response;
        }
    }
}
