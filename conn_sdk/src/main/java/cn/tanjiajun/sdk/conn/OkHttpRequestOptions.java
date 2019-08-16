package cn.tanjiajun.sdk.conn;

/**
 * 网络请求选项
 *
 * @author TanJiaJun
 * @version 0.0.1
 */
public final class OkHttpRequestOptions {

    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";

    public String charset;
    public String method = POST;
    public boolean async = true;

    private OkHttpRequestOptions(Builder builder) {
        charset = builder.charset;
        method = builder.method;
        async = builder.async;
    }

    public static class Builder {

        private static final String DEFAULT_CHARSET = "UTF-8";

        private String charset = DEFAULT_CHARSET;
        private String method = "POST";
        private boolean async = true;

        public Builder charset(String charset) {
            this.charset = charset;
            return this;
        }

        public Builder method(String method) {
            if (method.equals(GET) || method.equals(POST) || method.equals(PUT) || method.equals(DELETE)) {
                this.method = method;
            }
            return this;
        }

        public Builder async(boolean async) {
            this.async = async;
            return this;
        }

        public Builder cloneFrom(OkHttpRequestOptions options) {
            if (null != options) {
                this.charset = options.charset;
                this.method = options.method;
                this.async = options.async;
            }
            return this;
        }

        public OkHttpRequestOptions build() {
            return new OkHttpRequestOptions(this);
        }
    }

    public static OkHttpRequestOptions createSimple() {
        return new Builder().build();
    }

}
