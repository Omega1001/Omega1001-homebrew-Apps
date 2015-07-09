package omega1001.task_spector.core.worker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;

import omega1001.task_spector.basis.IniFile;

public class IniServer implements Runnable {

	private static final String DEFAULT_FILE_PATH = "taskspector.ini";
	private File iniFile;
	private IniFile iniDatas = new IniFile();
	private boolean initialised = false;

	/**
	 * 
	 */
	public IniServer() {
		iniFile = new File(DEFAULT_FILE_PATH).getAbsoluteFile();
		initialised = false;
	}

	/**
	 * @param iniFile
	 */
	public IniServer(File iniFile) {
		if (iniFile.exists())
			this.iniFile = iniFile.getAbsoluteFile();
		else {
			this.iniFile = new File(DEFAULT_FILE_PATH).getAbsoluteFile();
		}
		initialised = false;

	}

	@Override
	public void run() {
		if (this.iniFile.exists()) {
			synchronized (this) {
				try {
					Scanner sc = new Scanner(new FileInputStream(iniFile));
					while (sc.hasNextLine()) {
						String ln = sc.nextLine();

						if (ln.contains("[EVENT]:")) {
							StringTokenizer tk = new StringTokenizer(ln, ":");
							tk.nextToken();
							iniDatas.getEvents().add(tk.nextToken());
						} else if (ln.contains("[TASK]:")) {
							StringTokenizer tk = new StringTokenizer(ln, ":");
							tk.nextToken();
							iniDatas.getTasks().add(tk.nextToken());
						}
					}
					sc.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		initialised = true;
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				break;
			}
		}

		synchronized (this) {
			try {
				PrintWriter writer = new PrintWriter(iniFile);
				Set<String> sts = iniDatas.getEvents();
				for (String s : sts) {
					writer.println("[EVENT]:" + s);
				}
				sts = iniDatas.getTasks();
				for (String s : sts) {
					writer.println("[TASK]:" + s);
				}
				writer.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

	}

	public void addEvent(String event) {
		while (!initialised) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				break;
			}
		}
		iniDatas.getEvents().add(event);
	}

	public void delEvent(String event) {
		while (!initialised) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				break;
			}
		}
		iniDatas.getEvents().remove(event);
	}

	public void addTask(String task) {
		while (!initialised) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				break;
			}
		}
		iniDatas.getTasks().add(task);
	}

	public void delTask(String task) {
		while (!initialised) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				break;
			}
		}
		iniDatas.getTasks().remove(task);
	}

	@Override
	public String toString() {
		while (!initialised) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				break;
			}
		}
		return "IniServer [iniFile=" + iniFile + ", iniDatas=" + iniDatas + "]";
	}

	public IniFile getIniDatas() {
		while (!initialised) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				break;
			}
		}
		return iniDatas;
	}

}
