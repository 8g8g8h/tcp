import jdk.nashorn.internal.runtime.ECMAException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class LabMartTCPClient {

    public static void main(String[] args) {
        BufferedReader reader = null;
        Socket socket = null;

        try {
            reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Your Server? >");
            String serverName = reader.readLine();
            socket = new Socket(serverName, 5002);
            System.out.println("クライアントからの接続成功");

            //クライアントが所持金を入力する
            System.out.println("あなたの所持金を入力してください");
            int money = 0;
            while (true) {
                try {
                    String line = reader.readLine();
                    money = Integer.parseInt(line);
                    if (money < 30) {
                        System.out.println("買える商品がありません");
                        System.out.println("もう一回お金を入力してください。");
                        continue;
                    } else {
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            LabMart wallet = new LabMart();
            wallet.setClientWallet(money);
            //サーバーに情報を送る。
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(wallet);
            oos.flush();

            //サーバから情報を得る
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            LabMart market = (LabMart) ois.readObject();
            HashMap<String, Integer> goodslist = new HashMap<>();
            goodslist = market.getGoodsList();
            System.out.println("あなたが買える商品は以下のものです。");
            for (String key : goodslist.keySet()) {
                System.out.println("商品名:" + key + " 値段:" + goodslist.get(key)+"円");
            }

            System.out.println("何を購入しますか?");
            System.out.println("商品名を入力してください");

            //サーバーに商品名を送る
            String goodName = reader.readLine();
            for (String key : goodslist.keySet()) {
                if (goodName.equals(key)) {
                    market.setGoodsName(goodName);
                }
            }
                oos.writeObject(market);
                oos.flush();

            //サーバーから情報を得る
            LabMart resultChange=(LabMart)ois.readObject();
            int change=resultChange.getChange();
            System.out.println("購入しました。おつりは"+change+"円です");
            System.out.println("ご利用ありがとうございました。またのご利用お待ちしております。");

            ois.close();
            oos.close();
            socket.close();
        } catch (java.net.UnknownHostException uhe) {
            uhe.printStackTrace();
            System.err.println("送信先のサーバ名が間違っているのでプログラムを終了します");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("エラーが発生したのでプログラムを終了します");
        }
    }
}