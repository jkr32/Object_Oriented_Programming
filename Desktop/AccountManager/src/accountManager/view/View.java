package accountManager.view;
import accountManager.model.Model;
import accountManager.controller.Controller;

public interface View {
    
	Controller getController();
	void setController(Controller controller);
	Model getModel();
	void setModel(Model model);
}
