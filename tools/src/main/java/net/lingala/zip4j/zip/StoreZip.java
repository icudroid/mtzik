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

package net.lingala.zip4j.zip;

import java.io.IOException;
import java.util.zip.CRC32;

import net.lingala.zip4j.core.HeaderWriter;
import net.lingala.zip4j.crypto.AESEncrpyter;
import net.lingala.zip4j.crypto.IEncrypter;
import net.lingala.zip4j.crypto.StandardEncrypter;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.Zip4jInputRAF;
import net.lingala.zip4j.io.Zip4jOutputStream;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.LocalFileHeader;
import net.lingala.zip4j.model.InternalZipEngineParameters;
import net.lingala.zip4j.progress.ProgressMonitor;
import net.lingala.zip4j.util.InternalZipConstants;
import net.lingala.zip4j.util.Raw;
import net.lingala.zip4j.util.Zip4jConstants;

public class StoreZip implements IZipEngine {

	private InternalZipEngineParameters zipEngineParameters;
	private Zip4jInputRAF inputStream;
	private Zip4jOutputStream outputStream;
	
	public StoreZip(InternalZipEngineParameters zipEngineParameters) throws ZipException {
		if (zipEngineParameters.getZipModel() == null || 
				zipEngineParameters.getInputStream() == null || 
				zipEngineParameters.getOutputStream() == null) {
			throw new ZipException("input parameters is null in Store Zip");
		}
		
		this.zipEngineParameters = zipEngineParameters;
	}
	
	public FileHeader zipCompleteFile(ProgressMonitor progressMonitor) throws ZipException {
		
		this.inputStream = zipEngineParameters.getInputStream();
		this.outputStream = zipEngineParameters.getOutputStream();
		
		LocalFileHeader localFileHeader = zipEngineParameters.getZipEngine().createLocalFileHeader(zipEngineParameters);
		FileHeader fileHeader = zipEngineParameters.getZipEngine().createFileHeader(zipEngineParameters);
		
		fileHeader.setOffsetLocalHeader(zipEngineParameters.getZipEngine().getOffsetStartOfNextLocalHeader());
		outputStream.seek(zipEngineParameters.getZipEngine().getOffsetStartOfNextLocalHeader());
		
		
		if (zipEngineParameters.getZipParameters().isEncryptFiles()) {
			localFileHeader.setEncrypted(true);
			fileHeader.setEncrypted(true);
			switch (zipEngineParameters.getZipParameters().getEncryptionMethod()) {
			case Zip4jConstants.ENC_METHOD_STANDARD:
				localFileHeader.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
				fileHeader.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
				break;
			case Zip4jConstants.ENC_METHOD_AES:
				localFileHeader.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
				fileHeader.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
				localFileHeader.setAesExtraDataRecord(
						zipEngineParameters.getZipEngine().generateAESExtraDataRecord(
								zipEngineParameters.getZipParameters()));
				break;
			default:
				throw new ZipException("invalid encrpytion type, cannot add encryption type to file headers");
			}
		}
		
		HeaderWriter headerWriter = new HeaderWriter();
		headerWriter.writeLocalFileHeader(zipEngineParameters.getZipModel(),
				localFileHeader, zipEngineParameters);
		
		fileHeader.setDiskNumberStart(zipEngineParameters.getZipEngine().getCurrSplitFileCounter() - 1);
		
		byte[] readBuff;
		
		if (localFileHeader.getUncompressedSize() < InternalZipConstants.BUFF_SIZE) {
			readBuff = new byte[(int)localFileHeader.getUncompressedSize()];
		} else {
			readBuff = new byte[InternalZipConstants.BUFF_SIZE];
		}
		
		int readLen = -2;
		CRC32 crc = new CRC32();
		long bytesRead = 0;
		IEncrypter encrypter = this.zipEngineParameters.getEncrypter();
		try {
			
			if (encrypter != null) {
				if (encrypter instanceof StandardEncrypter) {
					byte[] headerBytes = ((StandardEncrypter)encrypter).getHeaderBytes();
					if (headerBytes == null || headerBytes.length < InternalZipConstants.STD_DEC_HDR_SIZE) {
						throw new ZipException("invalid header bytes generated, cannot write header bytes");
					}
					outputStream.write(headerBytes, 0, headerBytes.length);
				} else if (encrypter instanceof AESEncrpyter) {
					byte[] saltBytes = ((AESEncrpyter)encrypter).getSaltBytes();
					byte[] passwordVerifier = ((AESEncrpyter)encrypter).getDerivedPasswordVerifier();
					outputStream.write(saltBytes);
					outputStream.write(passwordVerifier);
				}
			}
			
			while ((readLen = inputStream.read(readBuff)) != -1) {
				
				crc.update(readBuff, 0, readLen);
				
				if (encrypter != null) {
					readLen = encrypter.encryptData(readBuff, 0, readLen);
				}
				
				if (readLen == -1) {
					break;
				}
				
				outputStream.write(readBuff, 0, readLen);
				bytesRead += readLen;
				
				progressMonitor.updateWorkCompleted(progressMonitor.getWorkCompleted() + readLen);
				if (progressMonitor.isCancelAllTasks()) {
					progressMonitor.setResult(ProgressMonitor.RESULT_CANCELLED);
					progressMonitor.setState(ProgressMonitor.STATE_READY);
					return fileHeader;
				}
			}
			if (encrypter != null) {
				if (encrypter instanceof AESEncrpyter) {
					byte[] macBytes = ((AESEncrpyter)encrypter).getFinalMac();
					outputStream.write(macBytes);
				}
			}
		} catch (IOException e) {
			throw new ZipException(e);
		} catch (Exception e) {
			throw new ZipException(e);
		}
		
		byte[] intByte = new byte[4];
		byte[] longByte = new byte[8];
		if (zipEngineParameters.getZipParameters().isEncryptFiles()) {
			switch (zipEngineParameters.getZipParameters().getEncryptionMethod()) {
			case Zip4jConstants.ENC_METHOD_STANDARD:
				localFileHeader.setCompressedSize(localFileHeader.getUncompressedSize() + InternalZipConstants.STD_DEC_HDR_SIZE);
				fileHeader.setCompressedSize(fileHeader.getUncompressedSize() + InternalZipConstants.STD_DEC_HDR_SIZE);
				break;
			case Zip4jConstants.ENC_METHOD_AES:
				int aesOverhead = ((AESEncrpyter)encrypter).getSaltLength()+ 10 + 
									((AESEncrpyter)encrypter).getPasswordVeriifierLength(); // 10 is for MAC
				localFileHeader.setCompressedSize(localFileHeader.getUncompressedSize() + aesOverhead);
				fileHeader.setCompressedSize(fileHeader.getUncompressedSize() + aesOverhead);
				break;
			default:
				throw new ZipException("invalid encryption method, cannot calcualte compressed size");
			}
		} else {
			localFileHeader.setCompressedSize(localFileHeader.getUncompressedSize());
			fileHeader.setCompressedSize(fileHeader.getUncompressedSize());
		}
		
		if (localFileHeader.isWriteComprSizeInZip64ExtraRecord()) {
			Raw.writeLongLittleEndian(longByte, 0, localFileHeader.getCompressedSize());
			headerWriter.updateLocalFileHeader(localFileHeader, 
					fileHeader.getOffsetLocalHeader(), InternalZipConstants.UPDATE_LFH_COMP_SIZE, zipEngineParameters, 
					longByte, fileHeader.getDiskNumberStart());
		} else {
			Raw.writeIntLittleEndian(intByte, 0, (int)localFileHeader.getCompressedSize());
			headerWriter.updateLocalFileHeader(localFileHeader, 
					fileHeader.getOffsetLocalHeader(), InternalZipConstants.UPDATE_LFH_COMP_SIZE, zipEngineParameters, 
					intByte, fileHeader.getDiskNumberStart());
		}
		
		
		if (zipEngineParameters.getZipParameters().isEncryptFiles() && 
				zipEngineParameters.getZipParameters().getEncryptionMethod() == 
					Zip4jConstants.ENC_METHOD_AES) {
			
		} else {
			localFileHeader.setCrc32(crc.getValue());
			fileHeader.setCrc32(localFileHeader.getCrc32());
			
			
			Raw.writeIntLittleEndian(intByte, 0, (int)localFileHeader.getCrc32());
			
			headerWriter.updateLocalFileHeader(localFileHeader, 
					fileHeader.getOffsetLocalHeader(), InternalZipConstants.UPDATE_LFH_CRC, zipEngineParameters, 
					intByte, fileHeader.getDiskNumberStart());
		}
		
		zipEngineParameters.getZipEngine().updateZipModel(zipEngineParameters.getZipModel());
		
		return fileHeader;
	}
 	
}
