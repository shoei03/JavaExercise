import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Component;
import java.awt.Dimension;

/* 	
 * Game Startボタンがおされると、サーバーに初期設定の情報を送る
 * Optionボタンが押されると、サーバに初期設定の情報を送る
 */
public class TitlePanel {

	public static JPanel titlePanel; // タイトルパネルをstaticで宣言
	public static String myName;// ユーザー名をstaticで宣言
	private ImageIcon titleNameIcon = new ImageIcon("images/title_string.png");
	private ImageIcon titleImageIcon = new ImageIcon("images/title.png");
	private JLabel titleNameLabel; // タイトルラベルを宣言
	private JLabel titleImageLabel; // タイトルラベルを宣言
	private JButton gameStartButton; // ゲームスタートボタンを宣言
	private JButton optionButton; //オプションボタンを宣言
    
	public TitlePanel() {

		//名前の入力ダイアログを開く
		myName = JOptionPane.showInputDialog(null, "名前を入力してください", "名前の入力",JOptionPane.QUESTION_MESSAGE);
		if(myName.equals("")){
			myName = "No name";//名前がないときは，"No name"とする
		}

		// ソケットを作成
		new SetupNetWorkingPanel();

		// タイトルパネルを作成
		titlePanel = new JPanel(); // titlePanelを初期化
		titlePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE)); // 最大サイズを設定
		titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS)); // BoxLayoutを設定
		titlePanel.setBackground(new java.awt.Color(0, 128, 0)); // 背景色を深い緑色に設定

		// タイトルラベルを作成
		titleNameLabel = new JLabel();
		titleNameLabel.setIcon(titleNameIcon);
		titleNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		// タイトルラベルを作成	
		titleImageLabel = new JLabel();
		titleImageLabel.setIcon(titleImageIcon);
		titleImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		// ゲームスタートのボタンを作成
		gameStartButton = new JButton("Game Start");
		gameStartButton.setFont(new Font("MS UI Gothic", Font.BOLD, 46));
		gameStartButton.setBounds(255, 248, 500, 500);
		gameStartButton.setAlignmentX(JPanel.CENTER_ALIGNMENT); // 中央揃え
		gameStartButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {				
				titlePanel.setVisible(false); // タイトルパネルを非表示にする			
				// ゲームパネルの表示を依頼する
				SendMsg("");
				SendMsg("TURN " + myName + " first");
				SendMsg("NUMBER " + Integer.toString(10));
				SendMsg("TIMER " + Integer.toString(60));
                String msg = "GAME " + 10 + " " + 60;
                SendMsg(msg);
			}
		});
		
		// オプションボタンを作成
		optionButton = new JButton("Option");
		optionButton.setFont(new Font("MS UI Gothic", Font.BOLD, 46));
		optionButton.setBounds(255, 248, 500, 500);
		optionButton.setAlignmentX(JPanel.CENTER_ALIGNMENT); // 中央揃え
		optionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				titlePanel.setVisible(false); // タイトルパネルを非表示にする				
				OptionPanel.optionPanel.setVisible(true); // オプションパネルを表示にする
			}
		});
		
		// タイトルパネルに張り付ける
		titlePanel.add(titleNameLabel);
		titlePanel.add(titleImageLabel);
		titlePanel.add(gameStartButton);
		titlePanel.add(Box.createVerticalStrut(20)); // 隙間を作る
		titlePanel.add(optionButton);
		titlePanel.add(Box.createVerticalStrut(50)); // 隙間を作る
	}

	// サーバにメッセージを送信するためのメソッド
	public  void SendMsg(String msg) {
		SetupNetWorkingPanel.out.println(msg);
		SetupNetWorkingPanel.out.flush();
	}
}
