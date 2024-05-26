import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;

/* 
 * Game Startボタンが押されると、初期設定の情報がサーバに送られる
 */
public class OptionPanel extends GameLogic{

    public static JPanel optionPanel; // optionPanelをstaticで宣言
    private String nameString = TitlePanel.myName; // 名前を格納した変数の宣言と初期化
    private String turnString = "first"; // ターンを格納した変数の宣言と初期化
    private int cardInt; // カードの枚数を格納した変数の宣言
    private int limitTimeInt; // 制限時間を格納した変数の宣言

    public OptionPanel() {

        // パネルを作成
        optionPanel = new JPanel(new GridLayout(7, 3, 10, 10)); // GridLayoutを設定（7行3列のグリッド、水平垂直の間隔は10）
        optionPanel.setBackground(new Color(60, 179, 113)); // パネルの背景色薄い緑に設定

        // 1行目のラベル
        JLabel turnSelectLabel = new JLabel("ターンを選択", SwingConstants.CENTER);
        turnSelectLabel.setOpaque(true);
        turnSelectLabel.setBackground(new java.awt.Color(0, 128, 0)); // コンポーネントの背景色を設定
        turnSelectLabel.setFont(new Font("MS UI Gothic", Font.BOLD, 50)); // フォントを設定
        turnSelectLabel.setForeground(new java.awt.Color(254, 254, 254)); // テキストの色を変更
        
        // 2行目のラジオボタン
        ButtonGroup group = new ButtonGroup();
        JRadioButton radioButton_1 = new JRadioButton("先攻");
        JRadioButton radioButton_2 = new JRadioButton("後攻");
        radioButton_1.setBackground(new java.awt.Color(0, 128, 0)); // コンポーネントの背景色を設定
        radioButton_1.setFont(new Font("MS UI Gothic", Font.BOLD, 50)); // フォントを設定
        radioButton_1.setForeground(new java.awt.Color(254, 254, 254)); // テキストの色を変更
        radioButton_2.setBackground(new java.awt.Color(0, 128, 0)); // コンポーネントの背景色を設定
        radioButton_2.setFont(new Font("MS UI Gothic", Font.BOLD, 50)); // フォントを設定
        radioButton_2.setForeground(new java.awt.Color(254, 254, 254)); // テキストの色を変更
        radioButton_1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 自分が先攻になる場合の処理
                turnString = "first";
                nameString = TitlePanel.myName;             
            }
        });
        radioButton_2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 相手が先攻になる場合の処理
                turnString = "second";
                nameString = TitlePanel.myName;
            }
        });
        group.add(radioButton_1);
        group.add(radioButton_2);
        
        // 3行目のラベル
        JLabel cardSelectLabel = new JLabel("カードの枚数を選択", SwingConstants.CENTER);
        cardSelectLabel.setOpaque(true);
        cardSelectLabel.setBackground(new java.awt.Color(0, 128, 0)); // コンポーネントの背景色を設定
        cardSelectLabel.setFont(new Font("MS UI Gothic", Font.BOLD, 50)); // フォントを設定
        cardSelectLabel.setForeground(new java.awt.Color(254, 254, 254)); // テキストの色を変更
        
        // 4行目のラジオボタン
        ButtonGroup cardCountGroup = new ButtonGroup();
        JRadioButton radioButton_8 = new JRadioButton("8枚");
        JRadioButton radioButton_10 = new JRadioButton("10枚");
        JRadioButton radioButton_12 = new JRadioButton("12枚");
        radioButton_8.setBackground(new java.awt.Color(0, 128, 0)); // コンポーネントの背景色を設定
        radioButton_8.setFont(new Font("MS UI Gothic", Font.BOLD, 50)); // フォントを設定
        radioButton_8.setForeground(new java.awt.Color(254, 254, 254)); // テキストの色を変更
        radioButton_10.setBackground(new java.awt.Color(0, 128, 0)); // コンポーネントの背景色を設定
        radioButton_10.setFont(new Font("MS UI Gothic", Font.BOLD, 50)); // フォントを設定
        radioButton_10.setForeground(new java.awt.Color(254, 254, 254)); // テキストの色を変更
        radioButton_12.setBackground(new java.awt.Color(0, 128, 0)); // コンポーネントの背景色を設定
        radioButton_12.setFont(new Font("MS UI Gothic", Font.BOLD, 50)); // フォントを設定
        radioButton_12.setForeground(new java.awt.Color(254, 254, 254)); // テキストの色を変更
        radioButton_8.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardInt = 8;               
            }
        });
        radioButton_10.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardInt = 10;               
            }
        });
        radioButton_12.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardInt = 12;               
            }
        });
        cardCountGroup.add(radioButton_8);
        cardCountGroup.add(radioButton_10);
        cardCountGroup.add(radioButton_12);
        
        
        // 5行目のラベル
        JLabel timeSelectLabel = new JLabel("制限時間を設定", SwingConstants.CENTER);
        timeSelectLabel.setOpaque(true);
        timeSelectLabel.setBackground(new java.awt.Color(0, 128, 0)); // コンポーネントの背景色を設定
        timeSelectLabel.setFont(new Font("MS UI Gothic", Font.BOLD, 50)); // フォントを設定
        timeSelectLabel.setForeground(new java.awt.Color(254, 254, 254)); // テキストの色を変更
        
        // 6行目のラジオボタン（3列に配置）
        ButtonGroup timerGroup = new ButtonGroup();
        JRadioButton radioButton_30 = new JRadioButton("30秒");
        JRadioButton radioButton_60 = new JRadioButton("60秒");
        JRadioButton radioButton_120 = new JRadioButton("120秒");
        radioButton_30.setBackground(new java.awt.Color(0, 128, 0)); // コンポーネントの背景色を設定
        radioButton_30.setFont(new Font("MS UI Gothic", Font.BOLD, 50)); // フォントを設定
        radioButton_30.setForeground(new java.awt.Color(254, 254, 254)); // テキストの色を変更
        radioButton_60.setBackground(new java.awt.Color(0, 128, 0)); // コンポーネントの背景色を設定
        radioButton_60.setFont(new Font("MS UI Gothic", Font.BOLD, 50)); // フォントを設定
        radioButton_60.setForeground(new java.awt.Color(254, 254, 254)); // テキストの色を変更
        radioButton_120.setBackground(new java.awt.Color(0, 128, 0)); // コンポーネントの背景色を設定
        radioButton_120.setFont(new Font("MS UI Gothic", Font.BOLD, 50)); // フォントを設定
        radioButton_120.setForeground(new java.awt.Color(254, 254, 254)); // テキストの色を変更
        radioButton_30.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limitTimeInt = 30;               
            }
        });
        radioButton_60.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limitTimeInt = 60;               
            }
        });
        radioButton_120.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limitTimeInt = 120;               
            }
        });
        timerGroup.add(radioButton_30);
        timerGroup.add(radioButton_60);
        timerGroup.add(radioButton_120);
        
        // 7行目のボタン
        JButton sendButton = new JButton("Game Start");       
        sendButton.setFont(new Font("MS UI Gothic", Font.BOLD, 50));
        sendButton.setForeground(new java.awt.Color(0, 0, 0));
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // optionPanelPaneを非表示する
                optionPanel.setVisible(false);
                
                // 初期設定の情報を送る
				sendMessage("");
				sendMessage("TURN " + nameString + " " + turnString);
				sendMessage("NUMBER " + Integer.toString(cardInt));
				sendMessage("TIMER " + Integer.toString(limitTimeInt));
                // gamePanelを表示にする
                String msg = "GAME " + cardInt + " " + limitTimeInt;
                sendMessage(msg);
            }
        });

        // オプションパネルに張り付ける
        optionPanel.add(turnSelectLabel);
        optionPanel.add(new JLabel("")); // グリッドを埋めるための空のラベル
        optionPanel.add(new JLabel("")); // 3列目を均等にするための空のラベル
        optionPanel.add(radioButton_1);
        optionPanel.add(radioButton_2);
        optionPanel.add(new JLabel("")); // 3列目を均等にするための空のラベル
        optionPanel.add(cardSelectLabel);
        optionPanel.add(new JLabel("")); // グリッドを埋めるための空のラベル
        optionPanel.add(new JLabel("")); // 3列目を均等にするための空のラベル
        optionPanel.add(radioButton_8);
        optionPanel.add(radioButton_10);
        optionPanel.add(radioButton_12);
        optionPanel.add(timeSelectLabel);
        optionPanel.add(new JLabel("")); // グリッドを埋めるための空のラベル
        optionPanel.add(new JLabel("")); // 3列目を均等にするための空のラベル
        optionPanel.add(radioButton_30);
        optionPanel.add(radioButton_60);
        optionPanel.add(radioButton_120);
        optionPanel.add(new JLabel("")); // グリッドを埋めるための空のラベル
        optionPanel.add(sendButton);
        optionPanel.add(new JLabel("")); // 3列目を均等にするための空のラベル

        // フレームを表示
        optionPanel.setVisible(false);
    }
}
