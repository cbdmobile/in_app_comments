package hr.prijavituriste.in_app_comments;

import androidx.annotation.NonNull;

import android.app.Activity;
import android.content.Intent;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;

/** InAppCommentsPlugin */
public class InAppCommentsPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware, PluginRegistry.ActivityResultListener {
  /// The MethodChannel that will the communication between Flutter and native
  /// Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine
  /// and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private Result result;
  Activity activity;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "in_app_comments");
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    this.result = result;
    if (call.method.equals("requestComments")) {
      Intent intent = new Intent("com.huawei.appmarket.intent.action.guidecomment");
      intent.setPackage("com.huawei.appmarket");
      activity.startActivityForResult(intent, 1001);
      //result.success(0);
    } else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }

  @Override
public void onAttachedToActivity(ActivityPluginBinding activityPluginBinding) {
    activity = activityPluginBinding.getActivity();
    activityPluginBinding.addActivityResultListener(this);
}

@Override
public void onDetachedFromActivityForConfigChanges() {
  activity = null;
}

@Override
public void onReattachedToActivityForConfigChanges(ActivityPluginBinding activityPluginBinding) {
  activity = activityPluginBinding.getActivity();
  activityPluginBinding.addActivityResultListener(this);
}

@Override
public void onDetachedFromActivity() {
  activity = null;
}

@Override
public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == 1001) {
        if (resultCode == 102 || resultCode == 103) {
          result.success(resultCode);
        } else if (resultCode == 0) {
          result.error("0", "error", "Unknown error");
        } else if (resultCode == 101) {
          result.error("101", "error", "The app has not been released on AppGallery");
        } else if (resultCode == 104) {
          result.error("104", "error", "The HUAWEI ID sign-in status is invalid");
        } else if (resultCode == 105) {
          result.error("105", "error", "The user does not meet the conditions for displaying the comment pop-up");
        } else if (resultCode == 106) {
          result.error("106", "error", "The commenting function is disabled");
        } else if (resultCode == 107) {
          result.error("107", "error", "The in-app commenting service is not supported");
        } else if (resultCode == 108) {
          result.error("108", "error", "The user canceled the comment");
        }
        return true;
  }
  return false;
}
}
