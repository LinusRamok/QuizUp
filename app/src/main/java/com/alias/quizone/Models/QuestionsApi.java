
package com.alias.quizone.Models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QuestionsApi {

    @SerializedName("topic")
    @Expose
    private String topic;
    @SerializedName("questionlist")
    @Expose
    private List<Questionlist> questionlist = null;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public List<Questionlist> getQuestionlist() {
        return questionlist;
    }

    public void setQuestionlist(List<Questionlist> questionlist) {
        this.questionlist = questionlist;
    }

}
