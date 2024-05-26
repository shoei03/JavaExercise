import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

//メッセージ受信のためのスレッド
public class MesgRecvThread extends GameLogic implements Runnable {
    
    //通信状況を監視し，受信データによって動作する
    public void run() {
        try{
            InputStreamReader sisr = new InputStreamReader(SetupNetWorkingPanel.socket.getInputStream());
            BufferedReader br = new BufferedReader(sisr);
            SetupNetWorkingPanel.out.println(TitlePanel.myName);//接続の最初に名前を送る
            

            while(true) {
                String inputLine = br.readLine();//データを一行分だけ読み込んでみる
                if (inputLine != null) {//読み込んだときにデータが読み込まれたかどうかをチェックする
                  
                    String[] inputTokens = inputLine.split(" ");	//入力データを解析するために、スペースで切り分ける
                    String cmd = inputTokens[0];//コマンドの取り出し．１つ目の要素を取り出す
                    if (cmd.equals("FRIP")) {
                        int index = Integer.parseInt(inputTokens[1]);
                        int place = Integer.parseInt(inputTokens[2]);
                        flipCard(upDateIndex(index), place);              
                    } else if (cmd.equals("SHUFFLE")) {
                        int seed = Integer.parseInt(inputTokens[1]);
                        shuffleCard(seed);  
                    } else if (cmd.equals("PASS")) {
                        doPass();
                    } else if (cmd.equals("TITLE")) {
                        returnTitle();
                    } else if (cmd.equals("TURN")) {
                        setTurn(inputTokens[1], inputTokens[2]);
                    } else if (cmd.equals("NUMBER")) {
                        setCard(inputTokens[1]);
                    } else if (cmd.equals("TIMER")) {
                        setTime(inputTokens[1]);
                    } else if (cmd.equals("GAME")) {
                        startGame(inputTokens[1], inputTokens[2]);
                    }
                } else {
                    break;
                }
            }
            SetupNetWorkingPanel.socket.close();
        } catch (IOException e) {
            System.err.println("エラーが発生しました: " + e);
        } catch(InterruptedException e){
            e.printStackTrace();
        } 
    }
}