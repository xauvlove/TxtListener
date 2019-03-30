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
 * 剪贴板监控器 
 * 负责对剪贴板文本的监控和操作 
 * 由于监控需要一个对象作为ClipboardOwner，故不能用静态类 
 * 
 */  
public class SystemClipboardMonitor implements ClipboardOwner{  
    private Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();  
    public static JFrame frame;
    public static Thread t;
    public SystemClipboardMonitor(){  
        //如果剪贴板中有文本，则将它的ClipboardOwner设为自己  
        if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)){  
            clipboard.setContents(clipboard.getContents(null), this);  
        }  
                   
    }      
  
    /************ 
     * 测试代码 * 
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
     * 如果剪贴板的内容改变，则系统自动调用此方法 * 
     ********************************************** 
     */  
    @Override  
    public void lostOwnership(Clipboard clipboard, Transferable contents) {  
        // 如果不暂停一下，经常会抛出IllegalStateException  
        // 猜测是操作系统正在使用系统剪切板，故暂时无法访问  
        try {  
            Thread.sleep(1);  
        } catch (InterruptedException e) {  
            e.printStackTrace();  
        }  
        // 取出文本并进行一次文本处理  
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
        // 存入剪贴板，并注册自己为所有者  
        // 用以监控下一次剪贴板内容变化  
        StringSelection tmp = new StringSelection(clearedText);  
        clipboard.setContents(tmp, this);  
        
        t = new Thread(new Runnable() {
			@Override
			public void run() {
					try {
						frame.setTitle("内容已更新");
						t.sleep(900);
						frame.setTitle("");
						t.sleep(900);
						frame.setTitle("内容已更新");
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
