package com.fyf.parkinglot.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Base64;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.fyf.parkinglot.common.GlobalDefine;
import com.qiniu.util.Auth;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fengyifei on 15/11/23.
 */
public class Utils {

    private static String dateStringFormat = "yyyy-MM-dd HH:mm:ss";

    // 手机号验证
    public static boolean isMobileNum(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9])|177)\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    // base64解码
    public static String base64Decode(String str) {
        return new String((Base64.decode(str, Base64.NO_WRAP)));
    }


    // 获取上传token
    public static String getUploadToken() {
        Auth auth = Auth.create(GlobalDefine.QINIU_AK, GlobalDefine.QINIU_SK);
        String uploadToken = auth.uploadToken("fyfparkinglot");
        return uploadToken;
    }

    public static String getCurrentTime(String format) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        String currentTime = sdf.format(date);
        return currentTime;
    }

    public static String getCurrentTime() {
        return getCurrentTime(dateStringFormat);
    }

    // 与当前时间比较,现在时间小于比较时间,返回true
    public static boolean comparePointTime(String time) {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateStringFormat, Locale.getDefault());

        DateFormat df = new SimpleDateFormat(dateStringFormat, Locale.getDefault());
        try {
            long timeNow = df.parse(dateFormat.format(now)).getTime();
            long timeT = df.parse(time).getTime();
            if (timeNow < timeT)
                return true;
            else
                return false;
        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    // 指定时间是否在当前时间2小时以内,是则返回true
    public static boolean comparePointTimeInPointHour(String time, int hourDelay) {
        // 当前时间转换为calendar
        Calendar calendar;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateStringFormat);
            Date date = sdf.parse(getCurrentTime());
            calendar = Calendar.getInstance();
            calendar.setTime(date);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if (calendar == null) {
            return false;
        }
        //当前时间加2小时
        calendar.add(Calendar.HOUR_OF_DAY, hourDelay);

        SimpleDateFormat sdf = new SimpleDateFormat(dateStringFormat);

        DateFormat df = new SimpleDateFormat(dateStringFormat, Locale.getDefault());
        try {
            long timePoint = df.parse(time).getTime();
            long timeDelay = df.parse(sdf.format(calendar.getTime())).getTime();
            if (timePoint > timeDelay)
                return true;
            else
                return false;
        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    // 判断白天晚上,true为晚上,false为白天
    public static boolean isNight() {
        Calendar cal = Calendar.getInstance();
        if (cal.get(Calendar.HOUR_OF_DAY) > 6 && cal.get(Calendar.HOUR_OF_DAY) < 18) {
            return false;
        } else {
            return true;
        }
    }

    public static void loadImageUtils(SimpleDraweeView iv,String url,Context context){
        Uri uri = Uri.parse(url);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .build();
        iv.setController(controller);




//        ProgressiveJpegConfig pjpegConfig = new ProgressiveJpegConfig() {
//            @Override
//            public int getNextScanNumberToDecode(int scanNumber) {
//                return scanNumber + 2;
//            }
//
//            public QualityInfo getQualityInfo(int scanNumber) {
//                boolean isGoodEnough = (scanNumber >= 5);
//                return ImmutableQualityInfo.of(scanNumber, isGoodEnough, false);
//            }
//        };
//
//        ImageRequest request = ImageRequestBuilder
//                .newBuilderWithSource(uri)
//                .setProgressiveRenderingEnabled(true)
//                .build();
//
//        ImagePipelineConfig imagePipelineConfig = ImagePipelineConfig.newBuilder(context)
//                .setProgressiveJpegConfig(pjpegConfig)
//                .build();
//
//        iv.setController(controller);

    }
}
