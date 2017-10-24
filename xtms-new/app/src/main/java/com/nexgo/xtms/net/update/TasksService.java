package com.nexgo.xtms.net.update;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by zhouxie on 2017/6/30.
 */

public interface TasksService {
    @POST("spos/update")
    Observable<UpdateResponseData> getUpdateInfo(@Body UpdateRequestData requestData);

    @POST("spos/updateconfirm")
    Observable<ResponseUpdateAppResultData> getUpdateAppResult(@Body RequestUpdateAppResultData requestData);

}
