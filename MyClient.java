import javax.swing.JFrame;
import javax.swing.JPanel;

public class MyClient {

    public static JFrame frame; // JFrameをstaticで宣言
    public static JPanel panelContainer; // 基になるパネルをstaticで宣言

    public static void main(String[] args) {
        
        // フレームを生成
        frame = new JFrame(); // JFrameを初期化
        frame.setSize(1300, 900); // frameの大きさを設定
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ×ボタンで閉じるように設定        
        panelContainer = new JPanel(); // 基になるパネルを初期化
        panelContainer.setBackground(new java.awt.Color(0, 0, 0)); // 背景色を黒に設定

        // ネットワークを確立
        new TitlePanel(); // タイトルパネルの生成とネットワークも確立
        new OptionPanel(); // オプションパネルの生成

        // サーバからのメッセージを監視するスレッドを作成
		MesgRecvThread mrt = new MesgRecvThread();
		Thread t = new Thread(mrt);
		t.start(); // スレッドを開始させる

        // パネルを張り付ける
        panelContainer.add(TitlePanel.titlePanel); // 基になるパネルにタイトル画面を張り付ける
        panelContainer.add(OptionPanel.optionPanel); // 基になるパネルにオプション画面を張り付ける
        frame.add(panelContainer); // JFrameに基になるパネルを張り付ける

        // 画面に表示する
        frame.setVisible(true); // frameを表示する
        OptionPanel.optionPanel.setVisible(false);
    }
}