
package com.quiz.up.RankingPagePOJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Message {

    @SerializedName("top5")
    @Expose
    private List<Top5> top5 = null;

    public List<Top5> getTop5() {
        return top5;
    }

    public void setTop5(List<Top5> top5) {
        this.top5 = top5;
    }

}
