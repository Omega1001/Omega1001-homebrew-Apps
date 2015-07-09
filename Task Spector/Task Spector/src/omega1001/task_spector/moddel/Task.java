/**
 * 
 */
package omega1001.task_spector.moddel;

/**
 * @author j.adam
 *
 */
public class Task {
	
	private String name;
	private boolean activ = false;
	
	/**
	 * 
	 */
	public Task(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	public boolean isActiv() {
		return activ;
	}

	@Override
	public boolean equals(Object obj) {
		Task t = (Task) obj;
		return this.name.equals(t.getName());
	}

	public void setActiv(boolean activ) {
		this.activ = activ;
	}


}
