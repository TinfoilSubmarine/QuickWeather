# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# For testing only
#-keepnames class **

-keepattributes Exceptions, InnerClasses

-keep class org.nanohttpd.protocols.http.* { *; }
-keep class com.woxthebox.draglistview.* { *; }
-keep class com.mapbox.mapboxsdk.maps.AttributionDialogManager { *; }
-keep interface androidx.* { *; }
-keep class androidx.* { *; }

# Keep Annotations
-keep class com.ominous.tylerutils.annotation.*

# Inner classes get built via reflection, need to keep them
-keep class com.ominous.quickweather.data.WeatherResponseOneCall* { *; }
-keep class com.ominous.quickweather.data.WeatherResponseForecast* { *; }
-keep class com.ominous.tylerutils.plugins.GithubUtils* { *; }