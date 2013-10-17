package fr.k2i.adbeback.core.business.player;


public enum Sex{
	Mr("user.mr"),Mme("user.mme"),Mlle("user.mlle");
    
	private String label;
	
	Sex(String label){
		this.label = label;
	}
    
	public String getLabel() {
		return label; 
	}


}
