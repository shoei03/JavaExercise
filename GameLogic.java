import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;

public class GameLogic extends MyComponent {
    
    private int time;
    private Thread timerThread; // タイマースレッドを追跡するための変数

    // カードのindexを調整する
    public  int upDateIndex(int index) {
        if (index > cardInt) {
            index = index - cardInt;
        }
        return index;    
    }

    // カードをめくる
    public  void flipCard(int index, int place) throws InterruptedException {
        card[place].setIcon(cardIcons[index]); // カードをめくる
        if (firstSelectedIndex == 0) { // 1枚目をめくるとき
            firstSelectedIndex = index;
            firstSelectedPlace = place;
        } else { // 2枚目をめくるとき
            secondSelectedIndex = index;
            secondSelectedPlace = place;
            try {
                Thread.sleep(1000);
            } catch(InterruptedException e){
                e.printStackTrace();
            } 
            if (firstSelectedIndex == secondSelectedIndex) { // 揃ったとき
                moveMatchedCards(firstSelectedPlace, secondSelectedPlace); // カードを移動させる
                setScoreLabel(); //スコアを更新する
                checkWinner(myScore, yourScore); // 勝敗を判定する
            } else { // 揃わなかったとき
                card[firstSelectedPlace].setIcon(backIcon); // カードを戻す
                card[secondSelectedPlace].setIcon(backIcon); // カードを戻す
                doPass(); //　パスの処理を行う
            }
            resetCard(firstSelectedPlace, secondSelectedPlace);
            startTimer(this.time); //タイマーをスタートする   
        }
    }

    // 揃ったカードを移動させる
    public  void moveMatchedCards(int firstSelectedPlace, int secondSelectedPlace) {
        card[firstSelectedPlace].setIcon(boardIcon);
        card[secondSelectedPlace].setIcon(boardIcon);

        // マッチしたカードのアイコンを作成
        JLabel matchedCardLabel = new JLabel(backIcon);
        JLabel matchedCardLabel2 = new JLabel(backIcon);

        // マッチしたカードを重ねるために、位置を調整
        int offset = mymatchedCardPanel.getComponentCount() * 2; // 重ねるためのオフセット
        matchedCardLabel.setBounds(offset, 0, backIcon.getIconWidth(), backIcon.getIconHeight());
        matchedCardLabel2.setBounds(offset+2, 0, backIcon.getIconWidth(), backIcon.getIconHeight());
        
        // マッチしたカードをパネルの最前面に追加
        if (myTurn == 1) {
            mymatchedCardPanel.add(matchedCardLabel, 0);
            mymatchedCardPanel.add(matchedCardLabel2, 0);
            
            mymatchedCardPanel.revalidate();
            mymatchedCardPanel.repaint();
        } else {
            yourmatchedCardPanel.add(matchedCardLabel, 0);
            yourmatchedCardPanel.add(matchedCardLabel2, 0);
            
            yourmatchedCardPanel.revalidate();
            yourmatchedCardPanel.repaint();
        }
    }
    // 得点のラベルを更新する
    public  void setScoreLabel() {
        if (myTurn == 1) {
            myScore += 2;
            myscoreLabel.setText("得点: " + myScore);
        } else {
            yourScore += 2;
            yourscoreLabel.setText("得点: " + yourScore);
        } 
    }
    // 勝敗を判定する
    public  void checkWinner(int myScore, int yourScore) {
        if (myScore+yourScore == cardInt*2) {
            // 既存のタイマースレッドを停止する
            if (timerThread != null && timerThread.isAlive()) {
                timerThread.interrupt(); // スレッドに割り込みを発生させる
            }

            int response;
            if (myScore > yourScore) {
                response = JOptionPane.showConfirmDialog(null, "あなたの勝ちです。再戦しますか？", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            } else if (myScore == yourScore) {
                response = JOptionPane.showConfirmDialog(null, "引き分けです。再戦しますか？", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            } else {
                response = JOptionPane.showConfirmDialog(null, "あなたの負けです。再戦しますか？", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            }

            if (response == 0) { // 再戦する場合
                resetGame();
            } else if (response == 1) { // 再戦しない場合
                resetGame();
                String msg = "TITLE";
                sendMessage(msg);
            } else {
                System.exit(0);
            }
        } 
    }

    // カードの情報を初期化する
    public  void resetCard(int firstSelectedPlace, int secondSelectedPlace) {
        firstSelectedIndex = 0;
        firstSelectedPlace = 0;
        secondSelectedIndex = 0;
        secondSelectedPlace = 0;   
    }
   
    public void returnTitle() {
        // 既存のタイマースレッドを停止する
        if (timerThread != null && timerThread.isAlive()) {
            timerThread.interrupt(); // スレッドに割り込みを発生させる
        }
        gamePanel.setVisible(false);
        MyClient.panelContainer.setVisible(true);
        TitlePanel.titlePanel.setVisible(true);
        TitlePanel.titlePanel.revalidate();
        TitlePanel.titlePanel.repaint();
    }
    
    public  void resetGame() {
        // カードのリセット
        for (int i = 1; i <= cardInt*2; i++) {
            card[i].setIcon(backIcon);
            card[i].setActionCommand(Integer.toString(i));
            card[i].setVisible(true);
        }
        // スコアのリセット
        myScore = 0;
        yourScore = 0;
        myscoreLabel.setText("得点: " + myScore);
        yourscoreLabel.setText("得点: " + yourScore);
        mymatchedCardPanel.removeAll();
        mymatchedCardPanel.revalidate();
        mymatchedCardPanel.repaint();
        yourmatchedCardPanel.removeAll();
        yourmatchedCardPanel.revalidate();
        yourmatchedCardPanel.repaint();
    }

    // 既存のタイマーを捨て、新しいタイマーを始めるメソッド
    public void startTimer(int time) {
        // 既存のタイマースレッドを停止する
        if (timerThread != null && timerThread.isAlive()) {
            timerThread.interrupt(); // スレッドに割り込みを発生させる
        }
        
        // 新しいタイマースレッドを作成して開始する
        timerThread = new Thread(() -> {
            int remainingTime = time;
            while (remainingTime >= 0 && myTurn == 1 && !Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // 割り込み発生時はループを抜ける
                    break;
                }
                timerLabel.setText("タイマー: " + remainingTime);
                remainingTime--;
                if (remainingTime == 0) {
                    String msg = "PASS";
                    sendMessage(msg);
                }
            }
        });
        timerThread.start();
    }
    
    // シャッフルをするメソッド
    public  void shuffleCard(int seed) throws InterruptedException {
        InfoDialog dlg = new InfoDialog(MyClient.frame, "images/shuffle.png");
        Thread.sleep(1000);
        dlg.setVisible(false); // シャッフルしたことを画面に表示する
        
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 1; i <= cardInt*2; i++) {
            if (card[i].getIcon() != boardIcon) { // カードが盤面に残っているとき
                list.add(Integer.parseInt(card[i].getActionCommand())); // 盤面に残ったカードの番号をリストに格納
            }           
        }
        
        Random random = new Random(seed); // ここでシードを設定
        Collections.shuffle(list, random); // カードの番号をシャッフル
        
        // カードを先頭から配置し直す
        for (int i = 1; i <= list.size(); i++) { 
            card[i].setActionCommand(Integer.toString(list.get(i-1)));
            card[i].setIcon(backIcon);         
        }
        for (int i = list.size()+1; i <= cardInt*2; i++) {
            card[i].setIcon(boardIcon);
        }
        list.clear();
    }
    
    // パスをするメソッド
    public void doPass() throws InterruptedException {
        InfoDialog dlg = new InfoDialog(MyClient.frame, "images/pass.png");
        Thread.sleep(1000);
        dlg.setVisible(false); // パスしたことを画面に表示する
        myTurn = 1 - myTurn; // ターンを交代
        setTurnLabel(myTurn); // ターンラベルを更新
        startTimer(this.time); // タイマーを更新
    }    
    public  void setTurnLabel(int myTurn) {
        if (myTurn == 1) {
            turnLabel.setText("あなたのターン");
        } else {
            turnLabel.setText("相手のターン");
        }       
    }

    // ゲームの初期設定をするメソッド
    public  void setTurn(String receivedName, String flag) {
        if (receivedName.equals(TitlePanel.myName)) {
            if (flag.equals("first")) {
                myTurn = 1;
            } else {
                myTurn = 0;
            }          
        } else {
            if (flag.equals("first")) {
                myTurn = 0;
            } else {
                myTurn = 1;
            }
        }
    }  
    public  void setCard(String number) {
        cardInt = Integer.parseInt(number);
    }
    public  void setTime(String time) {
        this.time = Integer.parseInt(time);
    }   
    public  void startGame(String cardInt, String time) {
        showGameWindow(Integer.parseInt(cardInt), Integer.parseInt(time), myTurn);
        startTimer(this.time); // タイマーをセットする
        if (myTurn == 1) { // ゲームの開始にシャッフルを行う
            Random rnd = new Random();
            int value = rnd.nextInt(101) + 1;
            String msg = "SHUFFLE " + value;
            sendMessage(msg);
        }
    }

    public void showGameWindow(int cardInt, int limitTimeInt, int myTurn) {
        // 行数と列数を指定
        int rows = 4;
        int cols;       
        switch (cardInt) {
            case 8:
                cols = 4;
                break;
            case 10:
                cols = 5;
                break;
            case 12:
                cols = 6;
                break;
            default:
                cols = 4;
                break;
        }
        

        gamePanel = new JPanel(); // ゲームパネルを初期化
        gamePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        gamePanel.setBackground(new Color(0, 51, 0)); // 深い緑色の背景を設定
        gamePanel.setLayout(new BorderLayout()); // ボーダーレイアウトを設定

        // カードのアイコンと裏面のアイコンを読み込み、backIconとcardIconsに設定する
        backIcon = new ImageIcon("images/back.png");
        boardIcon = new ImageIcon("images/board.png");
        // カードのアイコン配列の初期化
        cardIcons = new ImageIcon[cardInt + 1]; // サイズをNumberOfCard + 1に設定
        for (int i = 1; i <= cardInt; i++) {
            cardIcons[i] = new ImageIcon("images/card_" + i + "-min.png");
        }

        // カードアイコンのサイズを取得（裏面アイコンを使用）
        int cardWidth = backIcon.getIconWidth();
        int cardHeight = backIcon.getIconHeight();

        // カードのパネルを作成
        cardPanel = new JPanel(new GridLayout(rows, cols, 10, 10));
        cardPanel.setBackground(new Color(5, 140, 5)); // 深い緑色の背景を設定

        card = new JButton[cardInt*2 + 1];
        // 上下反対にするためのif文
        if (myTurn == 1) {
            for (int i = 1; i <= cardInt*2; i++) {
                card[i] = new JButton(Integer.toString(i)); // JButtonのインスタンスを作成して配列に格納
                card[i].setForeground(new Color(0, 128, 0));
                card[i].setBackground(new Color(0, 128, 0)); // 深い緑色の背景を設定
                card[i].setOpaque(true); // ボタンを不透明に設定
                card[i].setBorderPainted(false); // ボタンの境界線を描画しないように設定
                card[i].setActionCommand(Integer.toString(i));
                card[i].setIcon(backIcon);
                card[i].addMouseListener(this);
                cardPanel.add(card[i]);
            }
        } else {
            for (int i = cardInt*2; i >= 1; i--) {
                card[i] = new JButton(Integer.toString(i)); // JButtonのインスタンスを作成して配列に格納
                card[i].setForeground(new Color(0, 128, 0));
                card[i].setBackground(new Color(0, 128, 0)); // 深い緑色の背景を設定
                card[i].setOpaque(true); // ボタンを不透明に設定
                card[i].setBorderPainted(false); // ボタンの境界線を描画しないように設定
                card[i].setActionCommand(Integer.toString(i));
                card[i].setIcon(backIcon);
                card[i].addMouseListener(this);
                cardPanel.add(card[i]);
            }            
        }


        // 左側のサブ情報パネルの作成
        JPanel leftInfoPanel = new JPanel();
        leftInfoPanel.setLayout(new BoxLayout(leftInfoPanel, BoxLayout.Y_AXIS));
        leftInfoPanel.setPreferredSize(new Dimension(cardWidth*2, cardPanel.getPreferredSize().height)); // 横幅をカードの横幅に合わせる
        leftInfoPanel.setBackground(new Color(0, 51, 0)); // 深い緑色の背景を設定

        // シャッフルボタンを作成
        JButton shuffleButton = new JButton("シャッフル");
        shuffleButton.setOpaque(true);
        shuffleButton.setForeground(new Color(105, 105, 105));
        shuffleButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100)); // 幅を最大化
        shuffleButton.setPreferredSize(new Dimension(100, 100));
        shuffleButton.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 27));
        shuffleButton.addMouseListener(this);
               
        // 相手のスコアラベルを作成
        yourscoreLabel = new JLabel("得点: 0");
        yourscoreLabel.setOpaque(true);
        yourscoreLabel.setForeground(new Color(105, 105, 105));
        yourscoreLabel.setBackground(new Color(255, 250, 250));
        yourscoreLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100)); // 幅を最大化
        yourscoreLabel.setPreferredSize(new Dimension(100, 100));
        yourscoreLabel.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 30));
        
        // 相手のマッチしたカードの表示用パネルを作成
        yourmatchedCardPanel = new JPanel(null);
        yourmatchedCardPanel.setOpaque(true);
        yourmatchedCardPanel.setForeground(new Color(105, 105, 105));
        yourmatchedCardPanel.setBackground(new Color(0, 51, 0));
        yourmatchedCardPanel.setPreferredSize(new Dimension(cardWidth, cardHeight));

        // 左側のサブ情報パネルに張り付ける
        leftInfoPanel.add(shuffleButton);
        leftInfoPanel.add(Box.createRigidArea(new Dimension(0, 10))); // タイマーラベルとシャッフルボタンの間に隙間を追加
        leftInfoPanel.add(yourscoreLabel);
        leftInfoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        leftInfoPanel.add(yourmatchedCardPanel);

        // 右側のサブ情報パネルの作成
        JPanel rightInfoPanel = new JPanel();
        rightInfoPanel.setLayout(new BoxLayout(rightInfoPanel, BoxLayout.Y_AXIS));
        rightInfoPanel.setPreferredSize(new Dimension(cardWidth*2, cardPanel.getPreferredSize().height)); // 横幅をカードの横幅に合わせる
        rightInfoPanel.setBackground(new Color(0, 51, 0)); // 深い緑色の背景を設定

        // タイマーラベルを作成
        timerLabel = new JLabel("タイマー: " + limitTimeInt);
        timerLabel.setOpaque(true);
        timerLabel.setForeground(new Color(105, 105, 105));
        timerLabel.setBackground(new Color(255, 250, 250)); // 深い緑色の背景を設定
        timerLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100)); // 幅を最大化
        timerLabel.setPreferredSize(new Dimension(100, 100));
        timerLabel.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 27));
               
        // ターンラベルを作成
        if (myTurn == 1) {
            turnLabel = new JLabel("あなたのターン");
        } else {
            turnLabel = new JLabel("相手のターン");
        }
        turnLabel.setOpaque(true);
        turnLabel.setForeground(new Color(105, 105, 105));
        turnLabel.setBackground(new Color(255, 250, 250)); // 深い緑色の背景を設定
        turnLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100)); // 幅を最大化
        turnLabel.setPreferredSize(new Dimension(100, 100));
        turnLabel.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 27));
        
        // パスボタンを作成
        JButton passButton = new JButton("パス");
        passButton.setOpaque(true);
        passButton.setForeground(new Color(105, 105, 105));
        passButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100)); // 幅を最大化
        passButton.setPreferredSize(new Dimension(100, 100));
        passButton.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 30));
        passButton.addMouseListener(this);

        // タイトルボタンを作成
        JButton titleButton = new JButton("タイトル");
        titleButton.setOpaque(true);
        titleButton.setForeground(new Color(105, 105, 105));
        titleButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100)); // 幅を最大化
        titleButton.setPreferredSize(new Dimension(100, 100));
        titleButton.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 30));
        titleButton.addMouseListener(this);
        
        // スコアラベルを作成   
        myscoreLabel = new JLabel("得点: 0");
        myscoreLabel.setOpaque(true);
        myscoreLabel.setForeground(new Color(105, 105, 105));
        myscoreLabel.setBackground(new Color(255, 250, 250));
        myscoreLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100)); // 幅を最大化
        myscoreLabel.setPreferredSize(new Dimension(100, 100));
        myscoreLabel.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 30));
        
        // 自分のマッチしたカードの表示用パネルを作成
        mymatchedCardPanel = new JPanel(null);
        mymatchedCardPanel.setOpaque(true);
        mymatchedCardPanel.setForeground(new Color(255, 255, 224));
        mymatchedCardPanel.setBackground(new Color(0, 51, 0));
        mymatchedCardPanel.setPreferredSize(new Dimension(cardWidth, cardHeight));

        // 右側のサブ情報パネルに張り付ける
        rightInfoPanel.add(timerLabel);
        rightInfoPanel.add(Box.createRigidArea(new Dimension(0, 10))); // タイマーラベルとシャッフルボタンの間に隙間を追加
        rightInfoPanel.add(turnLabel);
        rightInfoPanel.add(Box.createRigidArea(new Dimension(0, 10))); // ターンラベルとパスボタンの間に隙間を追加
        rightInfoPanel.add(passButton);
        rightInfoPanel.add(Box.createRigidArea(new Dimension(0, 10))); // パスボタンとタイトルボタンの間に隙間を追加
        rightInfoPanel.add(titleButton);     
        rightInfoPanel.add(Box.createRigidArea(new Dimension(0, 10))); // タイトルボタンとスコアラベルの間に隙間を追加
        rightInfoPanel.add(myscoreLabel);
        rightInfoPanel.add(Box.createRigidArea(new Dimension(0, 5))); // スコアラベルとマッチしたカードの表示用パネルの間に隙間を追加
        rightInfoPanel.add(mymatchedCardPanel);

        // メインフレームにパネルを追加
        gamePanel.add(leftInfoPanel, BorderLayout.WEST);
        gamePanel.add(cardPanel, BorderLayout.CENTER);
        gamePanel.add(rightInfoPanel, BorderLayout.EAST);

        // frameにgamePanelを張り付ける       
        MyClient.frame.add(gamePanel, BorderLayout.CENTER);

        // gamePanelを表示する
        gamePanel.setVisible(true);
        MyClient.panelContainer.setVisible(false);
    }

}
