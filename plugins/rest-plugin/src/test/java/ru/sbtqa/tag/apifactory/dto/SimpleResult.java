package ru.sbtqa.tag.apifactory.dto;

public class SimpleResult {

    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "SimpleResult [result=" + result + "]";
    }
}
