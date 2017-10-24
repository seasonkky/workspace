package com.nexgo.xtms.net.server;

import io.reactivex.Observable;
import retrofit2.http.POST;

/**
 * Created by zhouxie on 2017/6/29.
 */

public interface ServerListService {
    @POST("spos/server")
    Observable<ServerBean[]> getServerList();
}
