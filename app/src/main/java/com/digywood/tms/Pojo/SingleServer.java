package com.digywood.tms.Pojo;

public class SingleServer {

    private String branchId,serverId,serverName;

    public SingleServer(){

    }

    public SingleServer(String branchid,String serverid,String servername){

        this.branchId=branchid;
        this.serverId=serverid;
        this.serverName=servername;

    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
}
