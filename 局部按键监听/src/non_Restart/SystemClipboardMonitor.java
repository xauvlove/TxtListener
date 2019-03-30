package non_Restart;


import java.awt.KeyEventPostProcessor;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;  
import java.awt.datatransfer.Clipboard;  
import java.awt.datatransfer.ClipboardOwner;  
import java.awt.datatransfer.DataFlavor;  
import java.awt.datatransfer.StringSelection;  
import java.awt.datatransfer.Transferable;  
import java.awt.datatransfer.UnsupportedFlavorException;  
import java.awt.event.KeyEvent;
import java.io.IOException;  
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
  
/** 
 * ���������� 
 * ����Լ������ı��ļ�غͲ��� 
 * ���ڼ����Ҫһ��������ΪClipboardOwner���ʲ����þ�̬�� 
 * 
 */  
public class SystemClipboardMonitor implements ClipboardOwner{  
    private Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();  
    public static JFrame frame;
    public static Thread t;
    public SystemClipboardMonitor(){  
        //��������������ı���������ClipboardOwner��Ϊ�Լ�  
        if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)){  
            clipboard.setContents(clipboard.getContents(null), this);  
        }  
                   
    }      
  
    /************ 
     * ���Դ��� * 
     * ********** 
     */  
    public static void main(String[] args) {  
        SystemClipboardMonitor temp = new SystemClipboardMonitor();  
        frame = new JFrame();
        frame.setSize(300, 50);
        frame.setTitle("----");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventPostProcessor((KeyEventPostProcessor)(new KeyEventPostProcessor() {
			
			@Override
			public boolean postProcessKeyEvent(KeyEvent e) {
				
				if(e.getKeyCode() == KeyEvent.VK_F10) {
					String path = System.getProperty("user.dir");
					path += "/txtListener4.exe";
					try {
						Runtime.getRuntime().exec(path);
						frame.dispose();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					/*URL url = SystemClipboardMonitor.class.getProtectionDomain().getCodeSource().getLocation();
					String path = url.getPath()+"txtListener.exe";
					path = path.substring(1, path.length());
					System.out.println(path);
					try {
						Runtime.getRuntime().exec(path);
						System.exit(0);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}*/
					/*frame.dispose();
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							main(new String[0]);
						}
					}).start();*/
				
				}
				return false;
			}
		}));
        
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);       
    }  
  
    /********************************************** 
     * �������������ݸı䣬��ϵͳ�Զ����ô˷��� * 
     ********************************************** 
     */  
    @Override  
    public void lostOwnership(Clipboard clipboard, Transferable contents) {  
        // �������ͣһ�£��������׳�IllegalStateException  
        // �²��ǲ���ϵͳ����ʹ��ϵͳ���а壬����ʱ�޷�����  
        try {  
            Thread.sleep(1);  
        } catch (InterruptedException e) {  
            e.printStackTrace();  
        }  
        // ȡ���ı�������һ���ı�����  
        String text = null;  
        if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)){  
            try {  
                text = (String)clipboard.getData(DataFlavor.stringFlavor);  
            } catch (UnsupportedFlavorException e) {  
                e.printStackTrace();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        
        String clearedText = Translater.fomart(Translater.getSysClipboardText());
        // ��������壬��ע���Լ�Ϊ������  
        // ���Լ����һ�μ��������ݱ仯  
        StringSelection tmp = new StringSelection(clearedText);  
        clipboard.setContents(tmp, this);  
        
        t = new Thread(new Runnable() {
			@Override
			public void run() {
					try {
						frame.setTitle("�����Ѹ���");
						t.sleep(900);
						frame.setTitle("");
						t.sleep(900);
						frame.setTitle("�����Ѹ���");
						t.sleep(900);
						frame.setTitle("");
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
		});
        t.start();
    }  
}  
