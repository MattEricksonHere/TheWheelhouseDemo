package controller;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * The parent frame of the application
 * @author Me
 */
public class DemoMasterPanel extends JFrame {
	private static final long serialVersionUID = -3864149241166858442L;
	
	private HarmonicsPanel harmonicsPanel;
	private TonnetzPanel tonnetzPanel;
	private KeyPanel keyPanel;
	
	public DemoMasterPanel() {
		UIBot.initUIDefaults();
		initPanel();
	}
	
	/**
	 * Initializes the UI of the frame
	 */
	private void initPanel() {
		setSize(1200, 800);
		setResizable(false);
		
		BufferedImage img;
		try {
			img = ImageIO.read(DemoMasterPanel.class.getResource("/logogo64.gif"));
			setIconImage(img);
		} catch (IOException e) {
			System.out.println("Could not load icon");
			e.printStackTrace();
		}
		
		JPanel masterPanel = new JPanel();
		masterPanel.setBackground(UIBot.BGColor2);
		getContentPane().add(masterPanel);
		
		masterPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		
		tonnetzPanel = new TonnetzPanel();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		masterPanel.add(tonnetzPanel, c);

		harmonicsPanel = new HarmonicsPanel();
		c.gridx = 1;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		masterPanel.add(harmonicsPanel, c);

		keyPanel = new KeyPanel();
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.weighty = 0;
		masterPanel.add(keyPanel, c);
	}
	
	/**
	 * Application start
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			loadJarDll("/jvsthost2.dll");
			loadJarDll("/mda ePiano.dll");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		DemoMasterPanel controller = new DemoMasterPanel();
		controller.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		controller.setVisible(true);
	}
	
	/**
	 * Copies the specified dll into the working directory of the 
	 * application so it can be used at runtime
	 * @param name the name of the dll to be loaded at runtime
	 * @return the resulting filepath of the loaded dll
	 * @throws IOException
	 */
	public static String loadJarDll(String name) throws IOException {
        InputStream inputStream = DemoMasterPanel.class.getResourceAsStream(name);
        OutputStream fileStream = null;
        
        File fileOut = new File(System.getProperty("user.dir") + name);
        fileOut.deleteOnExit();

        try {
            final byte[] buf = new byte[1024];
            fileStream = new FileOutputStream(fileOut);
            int i = 0;
            while ((i = inputStream.read(buf)) != -1) {
                fileStream.write(buf, 0, i);
            }
        } finally {
            close(inputStream);
            close(fileStream);
        }

        return fileOut.getAbsolutePath();
	}

    /**
     * Closes the specified stream, if possible
     * @param stream the stream to be closed
     */
    private static void close(final Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (final IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
