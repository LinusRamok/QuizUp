
package com.alias.quizone.ScorePagePOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Message {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("topic")
    @Expose
    private String topic;
    @SerializedName("totalScore")
    @Expose
    private Integer totalScore;
    @SerializedName("accuracy")
    @Expose
    private String accuracy;
    @SerializedName("q_Total")
    @Expose
    private Integer qTotal;
    @SerializedName("q_Solved")
    @Expose
    private Integer qSolved;
    @SerializedName("correct")
    @Expose
    private Integer correct;
    @SerializedName("pid")
    @Expose
    private String pid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public Integer getQTotal() {
        return qTotal;
    }

    public void setQTotal(Integer qTotal) {
        this.qTotal = qTotal;
    }

    public Integer getQSolved() {
        return qSolved;
    }

    public void setQSolved(Integer qSolved) {
        this.qSolved = qSolved;
    }

    public Integer getCorrect() {
        return correct;
    }

    public void setCorrect(Integer correct) {
        this.correct = correct;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

}
