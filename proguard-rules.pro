# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in F:\Android\sdk/tools/proguard/proguard-android.txt
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

# 主工程必须包含这个文件，避免proguard的问题！

## 这句设置混淆用到的字符串文本文件，这个文件就是一系列的字符串，用换行分割，每行一个
#-obfuscationdictionary mapping_name.properties
## 这句设置类名混淆用到的文本文件，格式同上
#-classobfuscationdictionary class_mapping_name.properties
## 这句设置类名混淆用到的文本文件，格式同上
#-packageobfuscationdictionary package_mapping_name.properties
# 这句设置去掉一些Log信息
#-assumenosideeffects class android.util.Log{ public static *** d(...); public static *** i(...); }
# 这里设置指定类的类名(或enum、interface)和所有public成员不混淆

-keep public enum com.windfindtech.icommon.jsondata.enumtype.** {
	*;
}
-keep public class com.windfindtech.icommon.jsondata.** {
	private *;
}
-keep public class com.windfindtech.icommon.jsondata.usageinfo.** {
	private *;
	protected *;
}
-keep public class com.windfindtech.icommon.jsondata.stats.** {
	private *;
	protected *;
}
-keep public class com.windfindtech.icommon.jsondata.webservice.** {
	private *;
	protected *;
}
-keep public class com.windfindtech.icommon.jsondata.user.** {
	private *;
	protected *;
}
-keep public class com.windfindtech.icommon.jsondata.event.** {
	private *;
	protected *;
}
-keep public class com.windfindtech.icommon.jsondata.life.** {
	private *;
	protected *;
}
-keep public class com.windfindtech.icommon.jsondata.radio.** {
	private *;
	protected *;
}
-keep public class com.windfindtech.icommon.jsondata.shanghai.** {
	private *;
	protected *;
}
-keep public class com.windfindtech.icommon.jsondata.site.** {
	private *;
	protected *;
}
-keep public class com.windfindtech.icommon.jsondata.advertisement.** {
	private *;
	protected *;
}
-keep public class com.windfindtech.icommon.jsondata.nearsite.** {
	private *;
	protected *;
}
-keep public class com.windfindtech.icommon.xml.** {
	private *;
	protected *;
}
-keep public class com.windfindtech.icommon.volley.data.** {
	private *;
    protected *;
}

# retrofit & okhttp3
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-keepattributes Annotation
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-keep class okio.** { *; }
-keep interface okio.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**

# simple xml
-dontwarn javax.xml.stream.**
-keep class org.simpleframework.xml.** { *; }

# rxJava
-dontwarn sun.misc.Unsafe
-keep class rx.** { *; }

# 为防万一，指定派生自support库的类不混淆
-keep public class * extends android.support.v4.**
-dontwarn android.support.v7.**
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }
# 这句主要用于Activity，Fragment，View等，指定它们的派生类（类名）不混淆
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Application
-keep public class * extends android.app.Activity{
	public void *(android.view.View);
}
-dontnote android.support.v4.**

# keep enums from obfuscated to avoid error when parsing
-keepclassmembers enum * {
	public static **[] values();
	public static ** valueOf(java.lang.String);
}

## keep loopj http lib
#-keep class com.loopj.android.http.** {
#	*;
#}

# keep gson lib
-keep class com.google.gson.** {
	*;
}
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
#-keep class com.google.gson.examples.android.model.** { *; }


# log4j related
#-dontwarn org.apache.log4j.**
#-keep class org.apache.log4j.**
#-dontwarn java.awt.**, javax.security.**, java.beans.**, javax.swing.**, javax.management.**, javax.jms.**, javax.mail.**

# jsoup
-keep class org.jsoup.**

# tinylog
-dontwarn org.pmw.tinylog.**

# java lib
-dontnote java.util.**


# Retain generated class which implement Unbinder.
-keep public class * implements butterknife.Unbinder { public <init>(...); }

# Prevent obfuscation of types which use ButterKnife annotations since the simple name
# is used to reflectively look up the generated ViewBinding.
-keep class butterknife.*
-keepclasseswithmembernames class * { @butterknife.* <methods>; }
-keepclasseswithmembernames class * { @butterknife.* <fields>; }