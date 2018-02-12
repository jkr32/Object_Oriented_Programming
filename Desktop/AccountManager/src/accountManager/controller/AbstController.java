package accountManager.controller;
import accountManager.model.Model;
import accountManager.view.View;

public abstract class AbstController implements Controller {
	private View view;
	private View eViewDollar;
	private View eViewEuro;
	private View eViewYen;
	private Model model;
	
        public View getView(){return view;}
	public View getEViewDollar() {return eViewDollar;}
	public View getEViewEuro() {return eViewEuro;}
	public View getEViewYen() {return eViewYen;}
               
	public void setModel(Model model){this.model = model;}
        public void setView(View view){this.view = view;}
	public void setEViewDollar(View eView) {this.eViewDollar = eView;}
	public void setEViewEuro(View eView) {this.eViewEuro = eView;}
	public void setEViewYen(View eView) {this.eViewYen = eView;}
              
	public Model getModel(){return model;}
	
	
}