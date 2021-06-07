
import java.io.Serializable;
import java.util.HashMap;

public class LabMart implements Serializable{
     public HashMap<String ,Integer> goodsList=new HashMap<String,Integer>();

     String goodsName=" ";
     int clientWallet=0;
     int change=0;


    public HashMap<String, Integer> getGoodsList(){
        return goodsList;
    }

    public void setGoodsList(HashMap<String,Integer>goodsList) {
        this.goodsList=goodsList;
    }

    public void setClientWallet(int money){
        this.clientWallet=money;
    }

    public int getClientWallet(){
        return clientWallet;
    }

    public void setGoodsName(String goodsName){
        this.goodsName=goodsName;
    }
    public String getGoodsName(){
        return goodsName;
    }
    public void setChange(int change){
        this.change=change;
    }
    public int getChange(){
        return change;
    }

}
