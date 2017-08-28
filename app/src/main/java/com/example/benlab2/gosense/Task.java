package com.example.benlab2.gosense;

/**
 * Created by ben Lab 2 on 2017-05-08.
 */

public class Task {

    String current_time;
    String expire_Time;
    String client_id;
    String task;
    String video;

    public String getCurrent_time() {
        return current_time;
    }

    public void setCurrent_time(String current_time) {
        this.current_time = current_time;
    }

    public String getExpire_Time() {
        return expire_Time;
    }

    public void setExpire_Time(String expire_Time) {
        this.expire_Time = expire_Time;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    @Override
    public String toString() {
        return "Task [current_time=" + current_time + ", expire_Time=" + expire_Time + ", client_id=" + client_id + ", task=" + task + ", video=" + video + "]";
    }

}