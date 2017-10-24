package com.nexgo.xtms.net.update;

/**
 * Created by fangqiang_001 on 2017/2/11.
 */

public class RequestUpdateAppResultData {
    private String KSN;
    private String SN;
    private String updatePlanId;
    private String status;
    private String desc;

    public String getKSN() {
        return KSN;
    }

    public void setKSN(String KSN) {
        this.KSN = KSN;
    }

    public String getSN() {
        return SN;
    }

    public void setSN(String SN) {
        this.SN = SN;
    }

    public String getUpdatePlanId() {
        return updatePlanId;
    }

    public void setUpdatePlanId(String updatePlanId) {
        this.updatePlanId = updatePlanId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
