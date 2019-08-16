# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\AndroidStudioSDK/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
# 指定代码的压缩级别
-optimizationpasses 5
# 包明不混合大小写
-dontusemixedcaseclassnames
# 不去忽略非公共的库类
-dontskipnonpubliclibraryclasses
# 优化  不优化输入的类文件
#-dontoptimize
 #预校验
-dontpreverify
# 混淆时是否记录日志
-verbose
# 忽略警告
-ignorewarning
# 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
# 保护注解
-keepattributes *Annotation*
# 保持哪些类不被混淆
-useuniqueclassmembernames

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
    public void get*(...);
}

# 保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

# 保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}


# 保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# 保持自定义控件类不被混淆
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

# 不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}

-keepclassmembers enum * {
  public static **[] values();
  public static ** valueOf(java.lang.String);
}

-keepclasseswithmembers class *{
    public *;
}

-keep class * implements android.os.Parcelable {
public static final android.os.Parcelable$Creator *;
}

-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
    public <fields>;
    public <methods>;
}

-keepattributes Exceptions,InnerClasses,...
#-keep class [packagename].A{
#    *;
#}
#-keep class [packagename].A$* {
#    *;
#}

#-dontwarn android.support.v4.**
#-keep class android.support.v4.** { *; }
#-keep interface android.support.v4.app.** { *; }
#-keep public class * extends android.support.v4.**
#-keep public class * extends android.app.Fragment
#
#-dontwarn android.support.v7.**
#-keep class android.support.v7.** { *; }
#-keep interface android.support.v7.app.** { *; }
#-keep public class * extends android.support.v7.**

-keep class com.nostra13.universalimageloader.** { *; }
-dontwarn com.nostra13.universalimageloader.**

-keep class com.squareup.okhttp.**{ *; }
-dontwarn com.squareup.okhttp.**

-keep class okio.**{ *; }
-dontwarn okio.**

-keep class com.alibaba.fastjson.**{ *; }
-dontwarn com.alibaba.fastjson.**

-keep class android.** {*;}

-keep class android.support.**{ *; }

# 如果引用了v4或者v7包
-dontwarn android.support.**

-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

-keepattributes Signature

-keepattributes SourceFile,LineNumberTable

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# 环信
-keep class com.hyphenate.** {*;}
-dontwarn  com.hyphenate.**
-keep class com.superrtc.** {*;}

# 友盟
-keep class com.umeng.** {*;}
-dontwarn com.google.android.maps.**
-dontwarn android.webkit.WebView
-dontwarn com.umeng.**
-dontwarn com.tencent.weibo.sdk.**
-dontwarn com.tencent.**
-dontwarn com.facebook.**

-keep enum com.facebook.**
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

-keep public interface com.facebook.**
-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**

-keep public class com.umeng.socialize.* {*;}
-keep public class javax.**
-keep public class android.webkit.**

-keep class com.facebook.**
-keep class com.umeng.scrshot.**
-keep public class com.tencent.** {*;}
-keep class com.umeng.socialize.sensor.**

-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}

-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}

-keep class im.yixin.sdk.api.YXMessage {*;}
-keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep public class com.allintask.lingdao.R$*{
public static final int *;
}

# 高德地图定位
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}

# 支付宝
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}
-keep class com.alipay.sdk.app.H5PayCallback {
    <fields>;
    <methods>;
}
-keep class com.alipay.android.phone.mrpc.core.** { *; }
-keep class com.alipay.apmobilesecuritysdk.** { *; }
-keep class com.alipay.mobile.framework.service.annotation.** { *; }
-keep class com.alipay.mobilesecuritysdk.face.** { *; }
-keep class com.alipay.tscenter.biz.rpc.** { *; }
-keep class org.json.alipay.** { *; }
-keep class com.alipay.tscenter.** { *; }
-keep class com.ta.utdid2.** { *;}
-keep class com.ut.device.** { *;}

#==================protobuf======================
-dontwarn com.google.**
-keep class com.google.protobuf.** {*;}
-keep class com.huawei.** { *; }
-dontwarn com.huawei.**
-keep class com.parse.** { *; }
-dontwarn com.parse.**