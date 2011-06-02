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
import java.util.zip.Deflater;

import net.lingala.zip4j.core.HeaderWriter;
import net.lingala.zip4j.crypto.AESEncrpyter;
import net.lingala.zip4j.crypto.IEncrypter;
import net.lingala.zip4j.crypto.StandardEncrypter;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.Zip4jInputRAF;
import net.lingala.zip4j.io.Zip4jOutputStream;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.InternalZipEngineParameters;
import net.lingala.zip4j.model.LocalFileHeader;
import net.lingala.zip4j.progress.ProgressMonitor;
import net.lingala.zip4j.util.InternalZipConstants;
import net.lingala.zip4j.util.Raw;
import net.lingala.zip4j.util.Zip4jConstants;
import net.lingala.zip4j.util.Zip4jUtil;

public class DeflateZip implements IZipEngine {

	private InternalZipEngineParameters zipEngineParameters;
	private Zip4jInputRAF inputStream;
	private Zip4jOutputStream outputStream;
	
	public DeflateZip(InternalZipEngineParameters zipEngineParameters) throws ZipException {
		if (zipEngineParameters == null) {
			throw new ZipException("zip eingine parameters are null in delfate zip constructor");
		}
		this.zipEngineParameters = zipEngineParameters;
	}
	
	public FileHeader zipCompleteFile(ProgressMonitor progressMonitor) throws ZipException {
		
		if (this.zipEngineParameters.getInputStream() == null) {
			throw new ZipException("input stream is null, cannot deflate zip file");
		}
		
		if (this.zipEngineParameters.getOutputStream() == null) {
			throw new ZipException("output stream is null, cannot deflate zip file");
		}
		
		this.inputStream = zipEngineParameters.getInputStream();
		this.outputStream = zipEngineParameters.getOutputStream();
		
		LocalFileHeader localFileHeader = zipEngineParameters.getZipEngine().createLocalFileHeader(zipEngineParameters);
		FileHeader fileHeader = zipEngineParameters.getZipEngine().createFileHeader(zipEngineParameters);
		
		fileHeader.setOffsetLocalHeader(zipEngineParameters.getZipEngine().getOffsetStartOfNextLocalHeader());
		outputStream.seek(zipEngineParameters.getZipEngine().getOffsetStartOfNextLocalHeader());
		
		
		boolean isAESEncryption = false; 
		
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
				isAESEncryption = true;
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
		byte[] writeBuff;
		
		readBuff = new byte[InternalZipConstants.BUFF_SIZE];
		writeBuff = new byte[InternalZipConstants.BUFF_SIZE];
		
		int readLen = -2;
		CRC32 crc = new CRC32();
		long bytesRead = 0;
		long bytesWritten = 0;
		
		int compressionLevel = zipEngineParameters.getZipParameters().getCompressionLevel();
		if ((compressionLevel < 0 || compressionLevel > 9) && compressionLevel != -1) {
			throw new ZipException("invalid compression level for deflater. compression level should be in the range of 0-9");
		}
		
		Deflater deflater = new Deflater(compressionLevel);
		byte tmpByte[] = new byte[100];
		int deflateExtraBytes = 4;
		deflater.deflate(tmpByte);
		IEncrypter encrypter = this.zipEngineParameters.getEncrypter();
		
		byte[] pendingBuffer = null;
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
			
			if (Zip4jUtil.getFileLengh(zipEngineParameters.getFileToZip()) > 0) {
				while ((readLen = inputStream.read(readBuff, 0, readBuff.length)) != -1) {
					
					if (deflater.finished()) {
						throw new ZipException("deflater, write beyond end of stream");
					}
					progressMonitor.updateWorkCompleted(readLen);
					if (progressMonitor.isCancelAllTasks()) {
						progressMonitor.setResult(ProgressMonitor.RESULT_CANCELLED);
						progressMonitor.setState(ProgressMonitor.STATE_READY);
						return fileHeader;
					}
					deflater.setInput(readBuff, 0, readLen);
					while (!deflater.needsInput()) {
						
						int len = deflater.deflate(writeBuff, 0, writeBuff.length);
						if (len > 0) {
							if (encrypter != null) {
								// Pass only multiple of 16 byte blocks to AES Encryption
								if (isAESEncryption) {
									
									if (pendingBuffer != null && pendingBuffer.length > 0) {
										byte[] clonedPendingBuffer = (byte[])pendingBuffer.clone();
										byte[] clonedWriteBuffer = (byte[])writeBuff.clone();
										//first copy the pending buffer into the write buffer
										System.arraycopy(clonedPendingBuffer, 0, writeBuff, 0, clonedPendingBuffer.length);
										if (len == InternalZipConstants.BUFF_SIZE) {
											//cut the end of the write buffer and copy until the BUF_SIZE
											System.arraycopy(clonedWriteBuffer, 0, writeBuff, clonedPendingBuffer.length, len - clonedPendingBuffer.length);
											pendingBuffer = new byte[clonedPendingBuffer.length];
											//copy the above cut bytes into the pending buffer
											System.arraycopy(clonedWriteBuffer, len - clonedPendingBuffer.length, pendingBuffer, 0, clonedPendingBuffer.length);
										} else if (len < InternalZipConstants.BUFF_SIZE) {
											if (len + clonedPendingBuffer.length <= InternalZipConstants.BUFF_SIZE) {
												System.arraycopy(clonedWriteBuffer, 0, writeBuff, clonedPendingBuffer.length, len);
												pendingBuffer = null;
												if (len + clonedPendingBuffer.length == InternalZipConstants.BUFF_SIZE)
													len = InternalZipConstants.BUFF_SIZE;
												else
													len += clonedPendingBuffer.length;
											} else {
												System.arraycopy(clonedWriteBuffer, 0, writeBuff, clonedPendingBuffer.length, InternalZipConstants.BUFF_SIZE - clonedPendingBuffer.length);
												pendingBuffer = new byte[(len + clonedPendingBuffer.length) - InternalZipConstants.BUFF_SIZE];
												System.arraycopy(clonedWriteBuffer, len - pendingBuffer.length, pendingBuffer, 0, pendingBuffer.length);
												len = InternalZipConstants.BUFF_SIZE;
											}
										} else {
											throw new ZipException("deflater write buffer size more than permitted buffer size");
										}
									}
									
									if (len%16 != 0) {
										int pendingBufferLength = len%16;
										pendingBuffer = new byte[pendingBufferLength];
										System.arraycopy(writeBuff, (len/16)*16, pendingBuffer, 0, pendingBufferLength);
										len -= pendingBufferLength;
									}
								}
								len = encrypter.encryptData(writeBuff, 0, len);
							}
							
							outputStream.write(writeBuff, 0, len);
							bytesWritten += len;
						}
						
					}
					bytesRead += readLen;
					crc.update(readBuff, 0, readLen);
					
				}
				
				if (!deflater.finished()) {
					deflater.finish();
					while (!deflater.finished()) {
						int len = deflater.deflate(writeBuff, 0, writeBuff.length);
						if (progressMonitor.isCancelAllTasks()) {
							progressMonitor.setResult(ProgressMonitor.RESULT_CANCELLED);
							return fileHeader;
						}
						if (len > 0) {
							
							// Remove last 4 bytes
							if (deflater.finished()) {
								byte[] sourceBytes = (byte[])writeBuff.clone();
								if (len <= deflateExtraBytes) {
									bytesWritten -= (deflateExtraBytes - len);
									break;
								} else {
									writeBuff = new byte[writeBuff.length];
									System.arraycopy(sourceBytes, 0, writeBuff, 0, len - deflateExtraBytes);
									len = len - deflateExtraBytes;
								}
								
							}
							
							// do this after the above if block
							if (pendingBuffer != null && pendingBuffer.length > 0) {
								byte[] clonedPendingBuffer = (byte[])pendingBuffer.clone();
								byte[] clonedWriteBuffer = (byte[])writeBuff.clone();
								//first copy the pending buffer into the write buffer
								System.arraycopy(clonedPendingBuffer, 0, writeBuff, 0, clonedPendingBuffer.length);
								if (len == InternalZipConstants.BUFF_SIZE) {
									//cut the end of the write buffer and copy until the BUF_SIZE
									System.arraycopy(clonedWriteBuffer, 0, writeBuff, clonedPendingBuffer.length, len - clonedPendingBuffer.length);
									pendingBuffer = new byte[clonedPendingBuffer.length];
									//copy the above cut bytes into the pending buffer
									System.arraycopy(clonedWriteBuffer, len - clonedPendingBuffer.length, pendingBuffer, 0, clonedPendingBuffer.length);
								} else if (len < InternalZipConstants.BUFF_SIZE) {
									if (len + clonedPendingBuffer.length <= InternalZipConstants.BUFF_SIZE) {
										System.arraycopy(clonedWriteBuffer, 0, writeBuff, clonedPendingBuffer.length, len);
										pendingBuffer = null;
										if (len + clonedPendingBuffer.length == InternalZipConstants.BUFF_SIZE)
											len = InternalZipConstants.BUFF_SIZE;
										else
											len += clonedPendingBuffer.length;
									} else {
										System.arraycopy(clonedWriteBuffer, 0, writeBuff, clonedPendingBuffer.length, InternalZipConstants.BUFF_SIZE - clonedPendingBuffer.length);
										pendingBuffer = new byte[(len + clonedPendingBuffer.length) - InternalZipConstants.BUFF_SIZE];
										System.arraycopy(clonedWriteBuffer, len - pendingBuffer.length, pendingBuffer, 0, pendingBuffer.length);
										len = InternalZipConstants.BUFF_SIZE;
									}
								} else {
									throw new ZipException("deflater write buffer size more than permitted buffer size");
								}
							}
							
							if (encrypter != null) {
								len = encrypter.encryptData(writeBuff, 0, len);
							}
							outputStream.write(writeBuff, 0, len);
							bytesWritten += len;
							
							// if deflater is finished, then encrypt any remaining bytes in pending buffer
							// this applies to AES encryption only
							if (deflater.finished()) {
								if (isAESEncryption && pendingBuffer != null && pendingBuffer.length > 0) {
									if (encrypter != null) {
										len = encrypter.encryptData(pendingBuffer, 0, pendingBuffer.length);
										if (len > 0) {
											outputStream.write(pendingBuffer, 0, len);
											bytesWritten += len;
										}
									}
								}
							}
							
						}
					}
				}
			}
			
			if (encrypter != null) {
				if (encrypter instanceof AESEncrpyter) {
					byte[] macBytes = ((AESEncrpyter)encrypter).getFinalMac();
					outputStream.write(macBytes);
				}
			}
			
			byte[] intByte = new byte[4];
			byte[] longByte = new byte[8];
			
			// long actualWritten = deflater.getBytesWritten() - delfateOverheadBytes - deflateExtraBytes;
			long actualWritten = bytesWritten;
			if (zipEngineParameters.getZipParameters().isEncryptFiles()) {
				switch (zipEngineParameters.getZipParameters().getEncryptionMethod()) {
				case Zip4jConstants.ENC_METHOD_STANDARD:
					localFileHeader.setCompressedSize(actualWritten + InternalZipConstants.STD_DEC_HDR_SIZE);
					fileHeader.setCompressedSize(actualWritten + InternalZipConstants.STD_DEC_HDR_SIZE);
					break;
				case Zip4jConstants.ENC_METHOD_AES:
					int aesOverhead = ((AESEncrpyter)encrypter).getSaltLength()+ 10 + 
										((AESEncrpyter)encrypter).getPasswordVeriifierLength(); // 10 is for MAC
					localFileHeader.setCompressedSize(actualWritten + aesOverhead);
					fileHeader.setCompressedSize(actualWritten + aesOverhead);
					break;
				default:
					throw new ZipException("invalid encryption method, cannot calcualte compressed size");
				}
			} else {
				localFileHeader.setCompressedSize(actualWritten);
				fileHeader.setCompressedSize(actualWritten);
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
				//do nothing as CRC is already set to 0
			} else {
				localFileHeader.setCrc32(crc.getValue());
				fileHeader.setCrc32(localFileHeader.getCrc32());
				
				Raw.writeIntLittleEndian(intByte, 0, (int)localFileHeader.getCrc32());
				
				headerWriter.updateLocalFileHeader(localFileHeader, 
						fileHeader.getOffsetLocalHeader(), InternalZipConstants.UPDATE_LFH_CRC, zipEngineParameters, intByte, 
						 fileHeader.getDiskNumberStart());
			}
			
			zipEngineParameters.getZipEngine().updateZipModel(zipEngineParameters.getZipModel());
			
			return fileHeader;
		} catch (IOException e) {
			throw new ZipException(e);
		} catch (Exception e) {
			throw new ZipException(e);
		}
	}
}
