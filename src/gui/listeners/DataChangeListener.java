package gui.listeners;

public interface DataChangeListener {

	// essa interface � criada para emitir "sons" quando algo q a gnt especificar acontecer
	// dessa forma � como se a aplica��o esperasse ouvir algo (nosso som) para fazer algo (vai depender da l�gica)
	
	void onDataChanged();
}
