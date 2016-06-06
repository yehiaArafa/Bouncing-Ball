package model;

import model.ModelTexture;

public class ModelAndTexuredInfo {
	
	private ModelInfo rawModel;
	private ModelTexture texture;

	
	public ModelAndTexuredInfo(ModelInfo model, ModelTexture texture){
		this.rawModel = model;
		this.texture = texture;
	}

	public ModelInfo getRawModel() {
		return rawModel;
	}

	public ModelTexture getTexture() {
		return texture;
	}

}
