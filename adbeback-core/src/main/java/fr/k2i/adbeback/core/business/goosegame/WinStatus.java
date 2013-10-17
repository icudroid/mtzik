package fr.k2i.adbeback.core.business.goosegame;

public enum WinStatus {
	NotTranfered("Non transféré"),Transfering("En cours de transfert"),Transfered("Transféré");
	
	private String label;
	WinStatus(String label){
		this.label = label;
	}
	public String getLabel(){
		return label;
	}
}
