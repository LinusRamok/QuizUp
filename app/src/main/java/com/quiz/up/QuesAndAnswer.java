package com.quiz.up;

/**
 * Created by linus on 05-01-2018.
 */

public class QuesAndAnswer {

    private String question;
    private String option_one;
    private String option_two;
    private String option_three;
    private String option_four;
    public QuesAndAnswer(String option_one, String question, String option_two, String option_three,String option_four) {
        this.option_one =option_one;
        this.question=question;
        this.option_two=option_two;
        this.option_three=option_three;
        this.option_four=option_four;
    }
    public QuesAndAnswer(){

    }
    public String getoption_four(){
        return option_four;
    }

    public String getoption_three() {
        return option_three;
    }

    public String getoption_two() {
        return option_two;
    }

    public String getoption_one() {
        return option_one;
    }

    public String getquestion() {
        return question;
    }
    public void setoption_four(String option_four) {
        this.option_three = option_four;
    }

    public void setoption_three(String option_three) {
        this.option_three = option_three;
    }

    public void setoption_two(String option_two) {
        this.option_two = option_two;
    }

    public void setoption_one(String option_one) {
        this.option_one = option_one;
    }

    public void setquestion(String question) {
        this.question = question;
    }
}
