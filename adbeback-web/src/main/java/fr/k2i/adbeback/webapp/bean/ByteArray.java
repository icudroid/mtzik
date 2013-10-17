package fr.k2i.adbeback.webapp.bean;

import java.io.Serializable;

public class ByteArray implements Serializable{
	private static final long serialVersionUID = 7616080770167966800L;
	private byte[]bytes;

	public ByteArray(byte[] bytes) {
		this.bytes = bytes;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
	

}
