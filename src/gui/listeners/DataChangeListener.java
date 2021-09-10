package gui.listeners;

public interface DataChangeListener {

	// essa interface é criada para emitir "sons" quando algo q a gnt especificar acontecer
	// dessa forma é como se a aplicação esperasse ouvir algo (nosso som) para fazer algo (vai depender da lógica)
	
	void onDataChanged();
}
