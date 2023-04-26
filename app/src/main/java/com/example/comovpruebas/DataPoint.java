

package com.example.comovpruebas;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataPoint {

    @SerializedName("idPoint")
    @Expose
    private int idPoint;
    @SerializedName("signal")
    @Expose
    private int signal;
    @SerializedName("stage")
    @Expose
    private int stage;
    @SerializedName("mnc")
    @Expose
    private int mnc;
    @SerializedName("mcc")
    @Expose
    private int mcc;
    @SerializedName("lac")
    @Expose
    private int lac;
    @SerializedName("cellid")
    @Expose
    private int cellid;

    private static Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
    /**
     * No args constructor for use in serialization
     *
     */
    public DataPoint() {
    }

    /**
     *
     * @param mnc
     * @param stage
     * @param idPoint
     * @param mcc
     * @param cellid
     * @param signal
     * @param lac
     */
    public DataPoint(int idPoint, int signal, int stage, int mnc, int mcc, int lac, int cellid) {
        super();
        this.idPoint = idPoint;
        this.signal = signal;
        this.stage = stage;
        this.mnc = mnc;
        this.mcc = mcc;
        this.lac = lac;
        this.cellid = cellid;
    }

    public int getIdPoint() {
        return idPoint;
    }

    public void setIdPoint(int idPoint) {
        this.idPoint = idPoint;
    }

    public int getSignal() {
        return signal;
    }

    public void setSignal(int signal) {
        this.signal = signal;
    }

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public int getMnc() {
        return mnc;
    }

    public void setMnc(int mnc) {
        this.mnc = mnc;
    }

    public int getMcc() {
        return mcc;
    }

    public void setMcc(int mcc) {
        this.mcc = mcc;
    }

    public int getLac() {
        return lac;
    }

    public void setLac(int lac) {
        this.lac = lac;
    }

    public int getCellid() {
        return cellid;
    }

    public void setCellid(int cellid) {
        this.cellid = cellid;
    }

    public JsonElement toJson(){
        return gson.toJsonTree(this);
    }
}