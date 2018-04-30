package com.power.generator.window;



import com.boco.common.util.DateTimeUtil;
import com.power.generator.builder.CodeBuilder;
import com.power.generator.builder.CodeOuter;
import com.power.generator.model.DataModel;
import com.power.poi.excel.ExcelImportUtil;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author sunyu
 *
 */
public class MainWindow implements IMessagesManager, IExitManager {
	private static final int MESSAGES_QUEUE_SIZE = 200;
	private static final String OUTPUT_DIR = "D:\\build.sql";// 脚本输出路径
	private final String SERVER_NAME = "POWER-EXCEL建库工具V1.0";// 窗口名称
	private JFrame mainWindow;// 主窗口
	private JButton startButton;// 开始按钮
	private JButton selectButton;// 文件选择按钮
	private JLabel label;// 文件选择提示label
	private JTextField dirText;// 文件路径域
	private JFileChooser fileChooser;// 文件选择控件
	private JScrollPane logScrollPane;
	private JPanel panel;// 面板
	private JTextArea messageArea;// 输出信息区域
	private File file;// 读取到的文件

	private boolean isRunning;// 运行标示量

	public MainWindow() {
		do {
			this.initWindow();
		} while (false);
	}

	/**
	 * 运行服务主方法
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		// "com.seaglasslookandfeel.SeaGlassLookAndFeel");
		MainWindow mainWindow = new MainWindow();

		mainWindow.showMessages(Level.INFO, "祝您工作悦快！萌萌哒！！!");
		mainWindow.showMessages(Level.INFO, "请选择符合模板的数据库字段建模excel表！！！");

	}

	/**
	 * 初始化窗口组件
	 */
	public void initWindow() {

		Dimension screenSize = FrameTool.getScreenDimension();// 获取分辨率尺寸
		int width = screenSize.width;// 获取屏幕分辨率width
		// 针对电脑分辨率调整尺寸，避免高分屏电脑下看起来很吃力
		int mainWindowWidth = 700;// 默认宽度
		int mainWindowHegit = 470;// 默认高度
		int messageColunm = 65;// 指定textArea的列数
		int messageRows = 15;// 指定textArea显示多好行数据
		Font font = new Font("宋体", Font.PLAIN, 20);
		// 4k分辨率处理
		if (width == 3840) {
			font = new Font("宋体", Font.PLAIN, 30);
			mainWindowHegit = 850;
			mainWindowWidth = 1500;
			messageColunm = 95;
			messageRows = 20;
		} else if (width == 2048) {
			// 处理2k分辨率
			font = new Font("宋体", Font.PLAIN, 23);
			mainWindowHegit = 530;
			mainWindowWidth = 1000;
			messageColunm = 77;
			messageRows = 15;
		}
		mainWindow = new JFrame(SERVER_NAME);
		// 初始化消息输出文本域
		messageArea = new JTextArea();
		messageArea.setColumns(messageColunm);
		messageArea.setRows(messageRows);
		messageArea.setFont(font);
		messageArea.setLineWrap(true);
		// messageArea.setEnabled(false); // 禁止选中日志内容，避免光标位置影响信息插入。
		messageArea.setDisabledTextColor(Color.BLACK);

		// 初始化滚动面板，允许消息窗口滚动
		logScrollPane = new JScrollPane(messageArea);
		logScrollPane.getVerticalScrollBar().setValue(logScrollPane.getVerticalScrollBar().getMaximum());

		label = new JLabel("选择Excel");
		label.setFont(font);
		// 初始化路径
		dirText = new JTextField();
		dirText.setColumns(30);
		// dirText.setText(" ");
		dirText.setBounds(75, 35, 600, 20);
		// 初始化文件选择器
		fileChooser = new JFileChooser();
		fileChooser.isFontSet();
		// fileChooser.setSize(1000,700);
		fileChooser.setFileFilter(new FileNameExtensionFilter("Text File", "xls", "xlsx"));
		// 初始化文件选择按钮
		selectButton = new JButton("选择文件");
		selectButton.setFont(font);
		// 初始化开始按钮，默认禁止开始按钮
		startButton = new JButton("开始");
		startButton.setFont(font);
		startButton.setEnabled(false);
		if (isRunning) {// 程序启动后线程默认处于运行状态，将开始按钮设为不可编辑
			startButton.setEnabled(false);
			selectButton.setEnabled(false);
		}
		/**
		 * 为文件选择按钮添加监听器
		 */
		selectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!isRunning) {
					fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					fileChooser.showDialog(new JLabel(), "选择excel");
					file = fileChooser.getSelectedFile();
					// 文件路径设置到文本框
					if (null != file) {
						dirText.setText(file.getAbsolutePath());
						showMessages(Level.INFO, "您选择的文件：" + file.getAbsolutePath());
						showMessages(Level.INFO, "文件选择成功！请点击开始按钮！");
						// 选好文件后禁用文件选择按钮，激活开始按钮
						selectButton.setEnabled(false);
						startButton.setEnabled(true);
					} else {
						showMessages(Level.INFO, "您没有选择任何文件！！！");
					}
				}
			}
		});
		/**
		 * 为开始按钮添加监听器
		 */
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!isRunning) {
					// start();// 重新启动被停止的服务
					isRunning = true;
					showMessages(Level.INFO, "正在为您创建数据库脚本，请耐心等待！");
					selectButton.setEnabled(false);// 线程启动后将按钮设为不可编辑
					startButton.setEnabled(false);// 线程启动后将停止按钮开启
					try {
						writeSql(file, OUTPUT_DIR);
						showMessages(Level.INFO, "脚本创建成功  SUCCESSED!");
						showMessages(Level.INFO, "脚本已输出到：" + OUTPUT_DIR + "中 ！！！");
						dirText.setText("");
						isRunning = false;
					} catch (Exception e1) {
						isRunning = false;
						showMessages(Level.INFO, e1.getMessage());
					}
					if (!isRunning) {
						selectButton.setEnabled(true);
						startButton.setEnabled(false);
					}
				}
			}
		});
		panel = new JPanel();
		panel.add(logScrollPane);
		panel.add(label);
		panel.add(dirText);
		panel.add(selectButton);
		panel.add(startButton);
		// panel.add(stopButton);
		mainWindow.getContentPane().add(panel);
		mainWindow.setSize(mainWindowWidth, mainWindowHegit);
		mainWindow.setFont(font);
		mainWindow.setLocationRelativeTo(null);
		mainWindow.validate();// 使弹出组件正确显示
		mainWindow.setResizable(false);
		mainWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);// 窗口关闭时不是真正的关闭，用户点击确认关闭按钮才可关闭
		mainWindow.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				disposeAll();// 退出系统
			}
		});
		mainWindow.setVisible(true);
		FrameTool.centerContainer(null, mainWindow);
	}

	@Override
	public void exit() {
		mainWindow.dispose();
	}

	/**
	 * 退出系统，退出系统时先停止执行的义务逻辑
	 */
	private void disposeAll() {
		int option = JOptionPane.showConfirmDialog(mainWindow, "确认要退出系统吗？", "系统提示", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE);
		if (option == JOptionPane.NO_OPTION) {
			return;
		} else {
			// this.stop();// 退出义务逻辑线程
			exit();// 退出系统
		}
	}

	/**
	 * 生成Sql脚本
	 * 
	 * @param file
	 * @param outDir
	 * @return
	 * @throws Exception
	 */
	public boolean writeSql(File file, String outDir) throws Exception {
		Map<String, List<DataModel>> map = ExcelImportUtil.readExcelIntoMap(file, 1, DataModel.class);
		String sql = CodeBuilder.createSqlForMysql(map);
		File sqlFile = new File(outDir);
		boolean flag = CodeOuter.writeFile(sql, sqlFile);
		return flag;
	}

	/**
	 * 往界面输出信息
	 */
	@Override
	public boolean showMessages(Level level, String messages) {
		// 超过200行，开始清理
		if (messageArea.getLineCount() > MESSAGES_QUEUE_SIZE) {
			try {
				messageArea.replaceRange("", 0, messageArea.getLineEndOffset(0));
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
		messageArea.append(DateTimeUtil.getTime() + "：" + messages + "\r\n");
		return true;
	}

}
