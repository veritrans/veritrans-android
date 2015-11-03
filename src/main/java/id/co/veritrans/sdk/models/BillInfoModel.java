package id.co.veritrans.sdk.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by shivam on 11/3/15.
 */
public class BillInfoModel {


    @SerializedName("bill_info1")
    private String billInfo1;

    @SerializedName("bill_info2")
    private String billInfo2;

    @SerializedName("bill_info3")
    private String billInfo3;

    @SerializedName("bill_info4")
    private String billInfo4;

    @SerializedName("bill_info5")
    private String billInfo5;

    @SerializedName("bill_info6")
    private String billInfo6;

    @SerializedName("bill_info7")
    private String billInfo7;

    @SerializedName("bill_info8")
    private String billInfo8;


    /**
     * label 1
     *
     * @return
     */
    public String getBillInfo1() {
        return billInfo1;
    }

    /**
     * lable 1
     *
     * @param billInfo1
     */
    public void setBillInfo1(String billInfo1) {
        this.billInfo1 = billInfo1;
    }

    /**
     * value of lable 1
     *
     * @return billInfo2
     */
    public String getBillInfo2() {
        return billInfo2;
    }


    /**
     * value for lable 1
     *
     * @return billInfo2
     */
    public void setBillInfo2(String billInfo2) {
        this.billInfo2 = billInfo2;
    }


    /**
     * lable 2
     *
     * @return billInfo3
     */
    public String getBillInfo3() {
        return billInfo3;
    }

    /**
     * lable 2
     *
     * @param billInfo3
     */
    public void setBillInfo3(String billInfo3) {
        this.billInfo3 = billInfo3;
    }


    /**
     * value of lable 2
     *
     * @return billInfo4
     */
    public String getBillInfo4() {
        return billInfo4;
    }

    /**
     * value for lable 2
     *
     * @param billInfo4
     */
    public void setBillInfo4(String billInfo4) {
        this.billInfo4 = billInfo4;
    }


    /**
     * lable 3
     */
    public String getBillInfo5() {
        return billInfo5;
    }

    /**
     * lable 3
     *
     * @param billInfo5
     */
    public void setBillInfo5(String billInfo5) {
        this.billInfo5 = billInfo5;
    }

    /**
     * value of lable 3
     *
     * @return billInfo6
     */
    public String getBillInfo6() {
        return billInfo6;
    }


    /**
     * value for lable 3
     *
     * @param billInfo6
     */
    public void setBillInfo6(String billInfo6) {
        this.billInfo6 = billInfo6;
    }


    /**
     * lable 4
     *
     * @return billInfo7
     */
    public String getBillInfo7() {
        return billInfo7;
    }


    /**
     * lable 4
     *
     * @param billInfo7
     */
    public void setBillInfo7(String billInfo7) {
        this.billInfo7 = billInfo7;
    }


    /**
     * value of lable 4
     *
     * @return billInfo8
     */
    public String getBillInfo8() {
        return billInfo8;
    }

    /**
     * value for lable 4
     *
     * @param billInfo8
     */
    public void setBillInfo8(String billInfo8) {
        this.billInfo8 = billInfo8;
    }
}
