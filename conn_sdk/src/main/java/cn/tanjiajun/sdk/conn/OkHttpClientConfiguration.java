package cn.tanjiajun.sdk.conn;

import android.content.Context;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;

import cn.tanjiajun.sdk.common.utils.FileUtils;

/**
 * 网络客户端配置
 *
 * @author TanJiaJun
 * @version 0.0.1
 */
public final class OkHttpClientConfiguration {

    final Context context;
    final int readTimeoutSeconds;
    final int writeTimeoutSeconds;
    final int connectTimeoutSeconds;
    final boolean isLocalCacheSupported;
    final boolean isLocalDatabankSupported;
    final boolean isPersistentCookieSupported;
    final OkHttpRequestOptions okHttpRequestOptions;
    final Cache localCache;
    final Interceptor interceptor;

    private OkHttpClientConfiguration(final Builder builder) {
        context = builder.context;
        readTimeoutSeconds = builder.readTimeoutSeconds;
        writeTimeoutSeconds = builder.writeTimeoutSeconds;
        connectTimeoutSeconds = builder.connectTimeoutSeconds;
        isLocalCacheSupported = builder.isLocalCacheSupported;
        isLocalDatabankSupported = builder.isLocalDatabankSupported;
        isPersistentCookieSupported = builder.isPersistentCookieSupported;
        okHttpRequestOptions = builder.okHttpRequestOptions;
        this.localCache = builder.localCache;
        this.interceptor = builder.interceptor;
    }

    public static OkHttpClientConfiguration createDefault(Context context) {
        return new Builder(context).build();
    }

    public static class Builder {

        private static final int DEFAULT_CONNECT_TIMEOUT = 10;
        private static final int DEFAULT_WRITE_TIMEOUT = 10;
        private static final int DEFAULT_READ_TIMEOUT = 30;

        private static final int MAX_LOCAL_CACHE_SIZE = 10 * 1024 * 1024;
        private static final String LOCAL_CACHE_DIR_NAME = "Http";
        private static final String LOCAL_CACHE_NAME = "HttpCache";

        private Context context;
        private int readTimeoutSeconds = DEFAULT_READ_TIMEOUT;
        private int writeTimeoutSeconds = DEFAULT_WRITE_TIMEOUT;
        private int connectTimeoutSeconds = DEFAULT_CONNECT_TIMEOUT;
        private boolean isLocalCacheSupported = false;
        private boolean isLocalDatabankSupported = true;
        private boolean isPersistentCookieSupported = true;
        private OkHttpRequestOptions okHttpRequestOptions;
        private Cache localCache;
        private Interceptor interceptor;

        public Builder(Context context) {
            this.context = context.getApplicationContext();
        }

        public Builder readTimeoutSeconds(int readTimeoutSeconds) {
            if (readTimeoutSeconds > 0) {
                this.readTimeoutSeconds = readTimeoutSeconds;
            }
            return this;
        }

        public Builder writeTimeoutSeconds(int writeTimeoutSeconds) {
            if (writeTimeoutSeconds > 0) {
                this.writeTimeoutSeconds = writeTimeoutSeconds;
            }
            return this;
        }

        public Builder connectTimeoutSeconds(int connectTimeoutSeconds) {
            if (connectTimeoutSeconds > 0) {
                this.connectTimeoutSeconds = connectTimeoutSeconds;
            }
            return this;
        }

        public Builder isLocalCacheSupported(boolean isLocalCacheSupported) {
            this.isLocalCacheSupported = isLocalCacheSupported;

            if (null == this.localCache) {
                File databaseDir = FileUtils.getCacheDir(context, LOCAL_CACHE_DIR_NAME);
                File databaseFile = new File(databaseDir, LOCAL_CACHE_NAME);
                this.localCache = new Cache(databaseFile, MAX_LOCAL_CACHE_SIZE);
            }

            if (null == this.interceptor) {
                this.interceptor = new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        //                        Response originalResponse = chain.proceed(chain.request());

                        //                        if (NetworkUtils.isNetworkAvaliable(context)) {
                        //                            int maxAge = 60;    // read from cache for 1 minute
                        //                            return originalResponse.newBuilder().header("Cache-Control", "public, max-age=" + maxAge).build();
                        //                        } else {
                        //                            int maxStale = 60 * 60 * 24 * 28;   // tolerate 4-weeks stale
                        //                            return originalResponse.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale).build();
                        //                        }

                        Response originalResponse = chain.proceed(chain.request());
                        return originalResponse.newBuilder().removeHeader("Pragma").header("Cache-Control", String.format("max-age=%d", 60)).build();
                    }
                };
            }

            return this;
        }

        public Builder isLocalDatabackSupported(boolean isLocalDatabankSupported) {
            this.isLocalDatabankSupported = isLocalDatabankSupported;
            return this;
        }

        public Builder isPersistentCookieSupported(boolean isPersistentCookieSupported) {
            this.isPersistentCookieSupported = isPersistentCookieSupported;
            return this;
        }

        public Builder defaultOkHttpRequestOptions(OkHttpRequestOptions okHttpRequestOptions) {
            this.okHttpRequestOptions = okHttpRequestOptions;
            return this;
        }

        public OkHttpClientConfiguration build() {
            initEmptyFiledWithDefaultValues();
            return new OkHttpClientConfiguration(this);
        }

        // TODO 补充
        private void initEmptyFiledWithDefaultValues() {
            if (null == this.okHttpRequestOptions) {
                this.okHttpRequestOptions = OkHttpRequestOptions.createSimple();
            }

            if (this.isLocalCacheSupported) {
                isLocalCacheSupported(true);
            }
        }
    }

}
