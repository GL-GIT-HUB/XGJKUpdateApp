package com.xgjk.xgjkupdateapp.otherupdate;

/**
 * Created by XG on 2017/6/9.
 */

public class UpdateAppBean {
    private int lineVersion;//线上版本
    private String updateUrl;//更新的地址
    private String updateContent;//更新的内容
    private boolean isForceUpdate;//是否强制更新

    public int getLineVersion() {
        return lineVersion;
    }

    public void setLineVersion(int lineVersion) {
        this.lineVersion = lineVersion;
    }

    public String getUpdateUrl() {
        return updateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }

    public String getUpdateContent() {
        return updateContent;
    }

    public void setUpdateContent(String updateContent) {
        this.updateContent = updateContent;
    }

    public boolean isForceUpdate() {
        return isForceUpdate;
    }

    public void setForceUpdate(boolean forceUpdate) {
        isForceUpdate = forceUpdate;
    }
}
