package com.quiz.up;

/**
 * Created by linus on 06-01-2018.
 */

import java.util.List;

public class QuestionsApi {

    private String topic;
    private List<Questionlist> questionlist = null;
    /**
     * No args constructor for use in serialization
     *
     */
    public QuestionsApi() {
    }

    /**
     *
     * @param topic
     * @param questionlist
     */
    public QuestionsApi(String topic, List<Questionlist> questionlist) {
        super();
        this.topic = topic;
        this.questionlist = questionlist;
    }

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
