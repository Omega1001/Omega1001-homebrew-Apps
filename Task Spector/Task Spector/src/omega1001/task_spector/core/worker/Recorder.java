/**
 * 
 */
package omega1001.task_spector.core.worker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import omega1001.task_spector.basis.EventType;

/**
 * @author j.adam
 *
 */
public class Recorder implements Runnable {
	
	private File f = new File("TaskLog.txt");
	private PrintWriter out;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	private List<String> list = new ArrayList<>();
	private boolean shutdown;
	/**
	 * @throws FileNotFoundException 
	 * 
	 */
	public Recorder(){
		
	}
	
	public Recorder (File destination) {
		this.f = destination;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(f, true)));
			
			
			while (!list.isEmpty() || !shutdown){
				boolean run = false;
				synchronized (list){
					run = !list.isEmpty();
				}
					if (run){
						printToLog(list.get(0));
						list.remove(0);
						out.flush();
					}else{
						try{
							Thread.sleep(20);
						}catch (InterruptedException e){
							break;
						}
					}
			}			
			
			out.flush();
			out.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	private boolean printToLog (String contents){
		if (out == null){
			return false;
		}else{
				out.print("("+sdf.format(new Date())+")");
				out.print(" ");
				out.println(contents);
				return true;			
		}
	}
	
	public void printTaskChangeToLog (String taskName, EventType change){
		synchronized (list){
		 list.add("["+taskName+"] {"+change+"}" );
		}
	}
	
	public void shutdown(){
		shutdown = true;
	}

	public void printComment(String commentEvent) {
		synchronized (list){
		list.add("{"+EventType.COMMENT+"} ["+commentEvent+"] " );
		}
	}

}
