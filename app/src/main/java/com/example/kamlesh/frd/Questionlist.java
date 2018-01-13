package com.example.kamlesh.frd;

/**
 * Created by linus on 06-01-2018.
 */


        
public class Questionlist {

    private String qID;
    private String question;
    private String a;
    private String b;
    private String c;
    private String d;
    private String answer;

        
    public Questionlist() {
    }

         
    public Questionlist(String qID, String question, String a, String b, String c, String d, String answer) {
        super();
        this.qID = qID;
        this.question = question;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.answer = answer;
    }

    public String getQID() {
        return qID;
    }

    public void setQID(String qID) {
        this.qID = qID;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

}
