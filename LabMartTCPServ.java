import java.io.IOException;
import java.io.ObjectInputStream; //　入出力関連パッケージを利用する
import java.io.ObjectOutputStream;
import java.net.ServerSocket; //ネットワーク関連のパッケージを利用する
import java.net.Socket;
import java.rmi.server.ExportException;
import java.util.HashMap;


public class LabMartTCPServ {
    public static void main(String[] args) {
        ServerSocket server = null;
        ObjectInputStream ois1 = null;
        ObjectOutputStream oos1 = null;

        HashMap<String, Integer> goodsList = new HashMap<>();
        goodsList.put("鉛筆", 40);
        goodsList.put("消しゴム", 30);
        goodsList.put("シャープペンシル", 100);
        goodsList.put("ボールペン", 80);
        goodsList.put("色鉛筆", 60);

        try {
            //通信準備を行う
            System.out.println("ServerSocketを準備する");
            server = new ServerSocket(5002);
            while (true) {
                try {
                    //クライアントからの接続要求を待つ
                    Socket socket = server.accept();
                    System.out.println("接続完了");

                    //クライアントの所持金を受信する
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                    //所持金からクライアントが購入できる商品を提示する
                    LabMart labmart = (LabMart) ois.readObject();
                    int money = labmart.getClientWallet();
                    System.out.println("お客様の所持金は" + money + "円です。");
                    HashMap<String, Integer> goods = new HashMap<>();

                    for (String key : goodsList.keySet()) {
                        int value = goodsList.get(key);
                        if (value < money) {
                            goods.put(key, value);
                        }
                    }
                    labmart.setGoodsList(goods);
                    System.out.println(goods);

                    //結果をクライアントに送信する
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

                    oos.writeObject(labmart);
                    oos.flush();

                    //クライアントからの情報を受けとる（ここでは商品の名前）
                    LabMart paycatalog = (LabMart) ois.readObject();

                    //ここから情報を入れてクライアントに返す
                    String paygoodsName = paycatalog.getGoodsName();
                    int goodbvalue = paycatalog.getGoodsList().get(paygoodsName);
                    int change = paycatalog.getClientWallet() - goodbvalue;

                    //
                    paycatalog.setChange(change);
                    oos.writeObject(paycatalog);
                    oos.flush();
                    oos.close();
                    ois.close();
                    socket.close();
                    continue;
                } catch (Exception e) {
                    server.close();
                    System.out.println("socket切断によりサーバを停止");

                    break;
                }

            }

        } catch (Exception e) {
            System.out.println("エラーが発生したため終了します");
            e.printStackTrace();
        }
    }
}

