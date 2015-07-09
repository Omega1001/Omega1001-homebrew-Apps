package omega1001.task_spector;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import omega1001.task_spector_analyzer.gui.swt.Mainframe;

public class teset {

	public static void main(String[] args) throws IOException, InterruptedException {
		Mainframe.start(new File("taskLog"+new SimpleDateFormat("MMMMM.yyyy").format(new Date())+".txt"));
	}

}
