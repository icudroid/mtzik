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

package net.lingala.zip4j.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.util.InternalZipConstants;
import net.lingala.zip4j.util.Zip4jUtil;
import net.lingala.zip4j.zip.ZipEngine;

public class Zip4jOutputStream {
	
	private File file;
	private long bytesWritten;
	private ZipEngine zipEngine;
	private RandomAccessFile outputStream;
	
	public Zip4jOutputStream(File file, String mode) throws FileNotFoundException {
		outputStream = new RandomAccessFile(file, mode);
	}
	
	public Zip4jOutputStream(File file, String mode, ZipEngine zipEngine)
			throws FileNotFoundException {
		this(file, mode);
		
		if (zipEngine != null) {
			this.zipEngine = zipEngine;
		}
		
		this.file = file;
	}
	
	public void write(byte b) throws ZipException {
		write(new byte[b], 0, 1);
	}
	
	public void write(byte[] b) throws ZipException {
		write(b, 0, b.length);
	}
	
	public void write(byte[] b, int off, int len) throws ZipException {
		
		if (outputStream == null) {
			throw new ZipException("output stream is null");
		}
		
		if (len == 0)
			return;
		
		try {
			if (this.zipEngine != null) {
				ZipModel zipModel = this.zipEngine.getZipModel();
				if (zipModel != null && zipModel.isSplitArchive()) {
					if (zipModel.getSplitLength() < InternalZipConstants.MIN_SPLIT_LENGTH) {
						throw new IOException("split length is less than minimum allowed split length of 64 KB");
					}
					int actualLen = len - off;
					if (bytesWritten + actualLen > zipModel.getSplitLength()) {
						int bytesLenToWrite = (int)(zipModel.getSplitLength() - bytesWritten);
						int bytesLenToWriteInNew = actualLen - bytesLenToWrite;
						byte[] tmpBytes = new byte[bytesLenToWrite];
						byte[] remBytes = new byte[bytesLenToWriteInNew];
						System.arraycopy(b, 0, tmpBytes, 0, bytesLenToWrite);
						System.arraycopy(b, bytesLenToWrite, remBytes, 0, bytesLenToWriteInNew);
						outputStream.write(tmpBytes, off, bytesLenToWrite);
						
						startNewSplitFile();
						
						outputStream.write(remBytes, 0, bytesLenToWriteInNew);
						
					} else {
						outputStream.write(b, off, len);
						bytesWritten += len;
					}
				} else {
					outputStream.write(b, off, len);
					bytesWritten += len;
				}
			} else {
				outputStream.write(b, off, len);
				bytesWritten += len;
			}
		} catch (IOException e) {
			throw new ZipException(e);
		}
		
	}
	
	public void seek(long pos) throws ZipException {
		try {
			outputStream.seek(pos);
		} catch (IOException e) {
			throw new ZipException(e);
		}
	}
	
	public long getFilePointer() throws ZipException {
		try {
			return outputStream.getFilePointer();
		} catch (IOException e) {
			throw new ZipException(e);
		}
	}
	
	public void close() throws ZipException {
		try {
			if (outputStream != null)
				outputStream.close();
		} catch (IOException e) {
			throw new ZipException(e);
		}
	}
	
	public void startNewSplitFile() throws ZipException {
		if(zipEngine == null) {
			throw new ZipException("internal error: zip engine is null, cannot start new split file");
		}
		
		int currSplitFileCounter = zipEngine.getCurrSplitFileCounter();
		String zipFileName = zipEngine.getZipModel().getZipFile();
		if(!Zip4jUtil.isStringNotNullAndNotEmpty(zipFileName)) {
			throw new ZipException("zip file name is empty or null, cannot start new split file");
		}
		File zipFile = new File(zipFileName);
		String zipFileWithoutExt = Zip4jUtil.getZipFileNameWithoutExt(zipFile.getName());
		File currSplitFile = null;
		
		if (currSplitFileCounter <= 9) {
			currSplitFile = new File(zipFile.getParent() + System.getProperty("file.separator") + 
					zipFileWithoutExt + ".z0" + currSplitFileCounter);
		} else {
			currSplitFile = new File(zipFile.getParent() + System.getProperty("file.separator") + 
					zipFileWithoutExt + ".z" + currSplitFileCounter);
		}
		System.out.println("starting new zip file: " + currSplitFile);
		try {
			outputStream.close();
			outputStream = null;
		} catch (IOException e1) {
			throw new ZipException(e1);
		}
		
		if (currSplitFile.exists()) {
			throw new ZipException("split file: " + currSplitFile.getName() + " already exists in the current directory, cannot rename this file");
		}
		
		if (!zipFile.renameTo(currSplitFile)) {
			throw new ZipException("cannot rename newly created split file");
		}
		
		file = new File(zipFileName);
		try {
			outputStream = new RandomAccessFile(file, InternalZipConstants.WRITE_MODE);
			bytesWritten = 0;
			
			zipEngine.incrementCurrSplitFileCounter();
		} catch (FileNotFoundException e) {
			throw new ZipException(e);
		}
	}
	
	public int getCurrentSplitFileCounter() {
		if (zipEngine != null) {
			return zipEngine.getCurrSplitFileCounter();
		} else {
			// default
			return 1;
		}
	}

	public File getFile() {
		return file;
	}
	
	public long getLength() throws ZipException {
		
		if (outputStream == null) {
			throw new ZipException("outputstream is null, cannot get length");
		}
		
		try {
			return outputStream.length();
		} catch (IOException e) {
			throw new ZipException(e);
		}
	}
	
}
