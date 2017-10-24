package com.nexgo.xtms.util;

import android.content.Context;

import com.nexgo.xtms.data.Repository;
import com.nexgo.xtms.data.entity.CountryModel;
import com.nexgo.xtms.database.CountryModelDao;

import java.util.List;
import java.util.Locale;

/**
 * Created by zhouxie on 2017/6/26.
 */

public class CountryUtil {

    private static CountryModelDao countryModelDao = Repository.getInstance().getCountryModelDao();

    public static String getCountryNameFromCountryCode(Context context, String countryCode) {
        if (countryCode != null) {
            LogUtil.d("test", "countryCode = " + countryCode);
            List<CountryModel> modelList = countryModelDao.queryBuilder().where(CountryModelDao.Properties.CountryCode.eq(countryCode)).list();
            if (modelList != null && modelList.size() > 0)
                return isCNLanguage(context) ? modelList.get(0).getCn_name() : modelList.get(0).getEn_name();
        }
        return null;

    }

    public static boolean isCNLanguage(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("zh"))
            return true;
        else
            return false;
    }

}
