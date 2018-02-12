package accountManager.model;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class AbstModel implements Model {
	private final ArrayList listeners = new ArrayList(5);
	
	public void notifyChanged(ModelEvent event){
		ArrayList list = (ArrayList)listeners.clone();
		Iterator it = list.iterator();
		while(it.hasNext()){
			ModelListener ml = (ModelListener)it.next();
			ml.modelChanged(event);
		}
	}
	public void addModelListener(ModelListener l){
		listeners.add(l);
	}
	public void removeModelListener(ModelListener l){
		listeners.remove(l);
	}
}
