package com.etsdk.app.huov7.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2018\3\13 0013.
 */

public class ReplyBean implements Serializable{
    private String id;
    private String argument_id;
    private String comments_id;
    private String time;
    private String content;
    private String status;
    private String up_time;
    private String from_uid;
    private String to_uid;
    private String from_uname;
    private String to_uname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArgument_id() {
        return argument_id;
    }

    public void setArgument_id(String argument_id) {
        this.argument_id = argument_id;
    }

    public String getComments_id() {
        return comments_id;
    }

    public void setComments_id(String comments_id) {
        this.comments_id = comments_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUp_time() {
        return up_time;
    }

    public void setUp_time(String up_time) {
        this.up_time = up_time;
    }

    public String getFrom_uid() {
        return from_uid;
    }

    public void setFrom_uid(String from_uid) {
        this.from_uid = from_uid;
    }

    public String getTo_uid() {
        return to_uid;
    }

    public void setTo_uid(String to_uid) {
        this.to_uid = to_uid;
    }

    public String getFrom_uname() {
        return from_uname;
    }

    public void setFrom_uname(String from_uname) {
        this.from_uname = from_uname;
    }

    public String getTo_uname() {
        return to_uname;
    }

    public void setTo_uname(String to_uname) {
        this.to_uname = to_uname;
    }
}
