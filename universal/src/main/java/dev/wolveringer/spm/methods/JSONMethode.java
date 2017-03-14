package dev.wolveringer.spm.methods;

import org.json.JSONObject;

import dev.wolveringer.spm.packets.JSONMessagePacket;

public class JSONMethode extends AbstractMethodeTransformer<JSONMessagePacket, JSONMessagePacket, JSONObject, JSONObject>{

	public JSONMethode() { }

	public JSONMethode(LamdaCallable<JSONObject, JSONObject> lamda) {
		super(lamda);
	}

	@Override
	protected JSONMessagePacket transform(JSONObject obj) {
		return obj == null ? JSONMessagePacket.EMPTY_JSON : new JSONMessagePacket(obj);
	}

	@Override
	protected JSONObject transform(JSONMessagePacket obj) {
		return obj == null ? new JSONObject() : obj.getObject();
	}

	
	
}
