/*
* Copyright 2010 Srikanth Reddy Lingala  
* 
* Licensed under the Apache License, Version 2.0 (the "License"); 
* you may not use this file except in compliance with the License. 
* You may obtain a copy of the License at 
* 
* http://www.apache.org/licenses/LICENSE-2.0 
* 
* Unless required by applicable law or agreed to in writing, 
* software distributed under the License is distributed on an "AS IS" BASIS, 
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
* See the License for the specific language governing permissions and 
* limitations under the License. 
*/

package net.lingala.zip4j.crypto;

import java.util.Random;

import net.lingala.zip4j.crypto.engine.ZipCryptoEngine;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.Zip4jInputRAF;
import net.lingala.zip4j.progress.ProgressMonitor;
import net.lingala.zip4j.util.CRCUtil;
import net.lingala.zip4j.util.InternalZipConstants;
import net.lingala.zip4j.util.Zip4jUtil;

public class StandardEncrypter implements IEncrypter {

	private Zip4jInputRAF inputStream;
	private ZipCryptoEngine zipCryptoEngine;
	private byte[] headerBytes;
	
	public StandardEncrypter(Zip4jInputRAF inputStream, 
			String password, ProgressMonitor progressMonitor) throws ZipException {
		if (inputStream == null) {
			throw new ZipException("inputStream is null in Standard Encryptor constructor");
		}
		
		if (!Zip4jUtil.isStringNotNullAndNotEmpty(password)) {
			throw new ZipException("input password is null or empty in standard encrpyter constructor");
		}
		
		this.inputStream = inputStream;
		this.zipCryptoEngine = new ZipCryptoEngine();
		
		this.headerBytes = new byte[InternalZipConstants.STD_DEC_HDR_SIZE];
		init(password, progressMonitor);
	}
	
	private void init(String password, ProgressMonitor progressMonitor) throws ZipException {
		if (!Zip4jUtil.isStringNotNullAndNotEmpty(password)) {
			throw new ZipException("input password is null or empty, cannot initialize standard encrypter");
		}
		zipCryptoEngine.initKeys(password);
		headerBytes = generateRandomBytes(InternalZipConstants.STD_DEC_HDR_SIZE);
		
		//Calculate CRC of the file
		progressMonitor.setCurrentOperation(ProgressMonitor.OPERATION_CALC_CRC);
		int crc = (int)CRCUtil.computeFileCRC(inputStream.getFile().getAbsolutePath(), progressMonitor);
		
		headerBytes[InternalZipConstants.STD_DEC_HDR_SIZE - 1] = (byte)((crc >> 24));
		
		if (headerBytes.length < InternalZipConstants.STD_DEC_HDR_SIZE) {
			throw new ZipException("invalid header bytes generated, cannot perform standard encryption");
		}
		
		for (int i = 0; i < headerBytes.length; i++) {
			
			int val = headerBytes[i] & 0xff;
			headerBytes[i] = (byte) (val ^ zipCryptoEngine.decryptByte());
			zipCryptoEngine.updateKeys((byte)val);
			
//			byte c = headerBytes[i];
//			headerBytes[i] = (byte) (headerBytes[i] ^ zipCryptoEngine.decryptByte());
//			zipCryptoEngine.updateKeys(c);
		}
	}
	
	public int encryptData(byte[] buff) throws ZipException {
		if (buff == null) {
			throw new ZipException("input bytes are null, cannot perform a standard decryption");
		}
		return encryptData(buff, 0, buff.length);
	}
	
	public int encryptData(byte[] buff, int start, int len) throws ZipException {
		
		if (len < 0) {
			throw new ZipException("invalid length specified to decrpyt data");
		}
		
		try {
			for (int i = 0; i < len; i++) {
				
				int val = buff[i] & 0xff;
				buff[i] = (byte) (val ^ zipCryptoEngine.decryptByte() & 0xff);
				zipCryptoEngine.updateKeys((byte)val);
			}
			return len;
		} catch (Exception e) {
			throw new ZipException(e);
		}
	}
	
	protected static byte[] generateRandomBytes(int size) throws ZipException {
		
		if (size <= 0) {
			throw new ZipException("size is either 0 or less than 0, cannot generate header for standard encryptor");
		}
		
		byte[] buff = new byte[size];
		
		Random rand = new Random();
		
		for (int i = 0; i < buff.length; i ++) {
			buff[i] = (byte) rand.nextInt(256);
		}
		
//		buff[0] = (byte)87;
//		buff[1] = (byte)176;
//		buff[2] = (byte)-49;
//		buff[3] = (byte)-43;
//		buff[4] = (byte)93;
//		buff[5] = (byte)-204;
//		buff[6] = (byte)-105;
//		buff[7] = (byte)213;
//		buff[8] = (byte)-80;
//		buff[9] = (byte)-8;
//		buff[10] = (byte)21;
//		buff[11] = (byte)242;
		
//		for( int j=0; j<2; j++ ) {
//			Random rand = new Random();
//			int i = rand.nextInt();
//			buff[0+j*4] = (byte)(i>>24);
//			buff[1+j*4] = (byte)(i>>16);
//			buff[2+j*4] = (byte)(i>>8);
//			buff[3+j*4] = (byte)i;
//		}
		return buff;
	}

	public byte[] getHeaderBytes() {
		return headerBytes;
	}

}
