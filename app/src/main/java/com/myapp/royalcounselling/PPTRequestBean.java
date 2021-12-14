package com.myapp.royalcounselling;

public class PPTRequestBean {
    String comment;
    boolean queryOver;
    String requestQuery;
    String queryTime;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isQueryOver() {
        return queryOver;
    }

    public void setQueryOver(boolean queryOver) {
        this.queryOver = queryOver;
    }

    public String getRequestQuery() {
        return requestQuery;
    }

    public void setRequestQuery(String requestQuery) {
        this.requestQuery = requestQuery;
    }

    public String getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(String queryTime) {
        this.queryTime = queryTime;
    }
}
