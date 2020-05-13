package com.example.communityoutbreakmanagement;

public class EpidemicData {

    private int todayConfirm;
    private int todayHeal;
    private int todayDead;
    private int todayStoreConfirm;
    private int todayInput;
    private int extDataIncrNoSymptom;

    private int totalConfirm;
    private int totalHeal;
    private int totalDead;
    private int totalStoreConfirm;
    private int totalInput;
    private int extDataNoSymptom;

    public EpidemicData(int todayConfirm, int todayHeal, int todayDead,
                        int todayStoreConfirm, int todayInput, int extDataIncrNoSymptom,
                        int totalConfirm, int totalHeal, int totalDead, int totalStoreConfirm,
                        int totalInput, int extDataNoSymptom) {
        this.todayConfirm = todayConfirm;
        this.todayHeal = todayHeal;
        this.todayDead = todayDead;
        this.todayStoreConfirm = todayStoreConfirm;
        this.todayInput = todayInput;
        this.extDataIncrNoSymptom = extDataIncrNoSymptom;
        this.totalConfirm = totalConfirm;
        this.totalHeal = totalHeal;
        this.totalDead = totalDead;
        this.totalStoreConfirm = totalStoreConfirm;
        this.totalInput = totalInput;
        this.extDataNoSymptom = extDataNoSymptom;
    }

    @Override
    public String toString() {
        return "EpidemicData{" +
                "todayConfirm=" + todayConfirm +
                ", todayHeal=" + todayHeal +
                ", todayDead=" + todayDead +
                ", todayStoreConfirm=" + todayStoreConfirm +
                ", todayInput=" + todayInput +
                ", extDataIncrNoSymptom=" + extDataIncrNoSymptom +
                ", totalConfirm=" + totalConfirm +
                ", totalHeal=" + totalHeal +
                ", totalDead=" + totalDead +
                ", totalStoreConfirm=" + totalStoreConfirm +
                ", totalInput=" + totalInput +
                ", extDataNoSymptom=" + extDataNoSymptom +
                '}';
    }

    public int getTodayConfirm() {
        return todayConfirm;
    }

    public void setTodayConfirm(int todayConfirm) {
        this.todayConfirm = todayConfirm;
    }

    public int getTodayHeal() {
        return todayHeal;
    }

    public void setTodayHeal(int todayHeal) {
        this.todayHeal = todayHeal;
    }

    public int getTodayDead() {
        return todayDead;
    }

    public void setTodayDead(int todayDead) {
        this.todayDead = todayDead;
    }

    public int getTodayStoreConfirm() {
        return todayStoreConfirm;
    }

    public void setTodayStoreConfirm(int todayStoreConfirm) {
        this.todayStoreConfirm = todayStoreConfirm;
    }

    public int getTodayInput() {
        return todayInput;
    }

    public void setTodayInput(int todayInput) {
        this.todayInput = todayInput;
    }

    public int getExtDataIncrNoSymptom() {
        return extDataIncrNoSymptom;
    }

    public void setExtDataIncrNoSymptom(int extDataIncrNoSymptom) {
        this.extDataIncrNoSymptom = extDataIncrNoSymptom;
    }

    public int getTotalConfirm() {
        return totalConfirm;
    }

    public void setTotalConfirm(int totalConfirm) {
        this.totalConfirm = totalConfirm;
    }

    public int getTotalHeal() {
        return totalHeal;
    }

    public void setTotalHeal(int totalHeal) {
        this.totalHeal = totalHeal;
    }

    public int getTotalDead() {
        return totalDead;
    }

    public void setTotalDead(int totalDead) {
        this.totalDead = totalDead;
    }

    public int getTotalStoreConfirm() {
        return totalStoreConfirm;
    }

    public void setTotalStoreConfirm(int totalStoreConfirm) {
        this.totalStoreConfirm = totalStoreConfirm;
    }

    public int getTotalInput() {
        return totalInput;
    }

    public void setTotalInput(int totalInput) {
        this.totalInput = totalInput;
    }

    public int getExtDataNoSymptom() {
        return extDataNoSymptom;
    }

    public void setExtDataNoSymptom(int extDataNoSymptom) {
        this.extDataNoSymptom = extDataNoSymptom;
    }
}
