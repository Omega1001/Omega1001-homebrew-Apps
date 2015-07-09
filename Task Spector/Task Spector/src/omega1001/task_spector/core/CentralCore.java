package omega1001.task_spector.core;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import omega1001.task_spector.basis.IniFile;
import omega1001.task_spector.core.worker.ActionServer;
import omega1001.task_spector.core.worker.IniServer;
import omega1001.task_spector.core.worker.Recorder;
import omega1001.task_spector.moddel.Task;

public class CentralCore {

	public CentralCore(File destination) {
		recorder = new Recorder(destination);
		ac = new ActionServer(recorder, iniServer);
	}

	private ExecutorService service = Executors.newCachedThreadPool();

	private Recorder recorder;
	private Future<?> _recorder;
	private ActionServer ac;
	private Future<?> _actionServer;
	private IniServer iniServer = new IniServer();
	private Future<?> _iniServer;

	public void ini() throws InterruptedException, ExecutionException {
		_actionServer = service.submit(ac);
		_iniServer = service.submit(iniServer);
		_recorder = service.submit(recorder);
	}

	public List<Task> getTasks() {
		return ac.getTasks();
	}

	public boolean isUpdate() {
		return ac.isUpdate();
	}

	public void shutdown() {
		ac.shutdown();
		while (!_actionServer.isDone()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				break;
			}
		}

		recorder.shutdown();

		while (!_recorder.isDone()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				break;
			}
		}

		_iniServer.cancel(true);

		while (!_iniServer.isDone()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				break;
			}
		}

		try {
			service.awaitTermination(1, TimeUnit.SECONDS);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		service.shutdownNow();
	}

	public IniFile getIniDatas() {
		return iniServer.getIniDatas();
	}

}
