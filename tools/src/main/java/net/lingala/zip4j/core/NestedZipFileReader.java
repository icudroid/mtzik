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

package net.lingala.zip4j.core;

import java.io.FileNotFoundException;

import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.Zip4jInputRAF;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.LocalFileHeader;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.util.InternalZipConstants;
import net.lingala.zip4j.util.Zip4jConstants;

class NestedZipFileReader {
	
	private Zip4jInputRAF zip4jRaf;
	
	public NestedZipFileReader(Zip4jInputRAF zip4jRaf) 
			throws ZipException {
		
		if (zip4jRaf == null) {
			throw new ZipException("Zip4jRandomAccessFile is null in NestedZipFileReader constructor");
		}
		this.zip4jRaf = zip4jRaf;
	}
	
	public ZipModel readNestedZipFile(ZipModel parentZipModel, FileHeader fileHeader) 
			throws ZipException {
		
		if (parentZipModel == null || fileHeader == null) {
			throw new ZipException("one of the input parameters was null in readNestedZipFile");
		}
		
		if (fileHeader.getCompressionMethod() == Zip4jConstants.COMP_AES_ENC) {
			if (fileHeader.getAesExtraDataRecord().getCompressionMethod() != Zip4jConstants.COMP_STORE) {
				throw new ZipException("nested zip file is not in a stored compression format");
			}
		} else if (fileHeader.getCompressionMethod() != Zip4jConstants.COMP_STORE) {
			throw new ZipException("nested zip file is not in a stored compression format");
		}
		
		if (fileHeader.isEncrypted()) {
			throw new ZipException("cannot read nested zip files in encrypted format. Extract the file and then try to read it");
		}
		
		if (parentZipModel.isSplitArchive()) {
			throw new ZipException("cannot read nested zip files from split archives. Extract the file and then try to read it");
		}
		
		HeaderReader headerReader = new HeaderReader(zip4jRaf);
		LocalFileHeader localFileHeader = headerReader.readLocalFileHeader(fileHeader);
		if (localFileHeader == null) {
			throw new ZipException("invalid local file header for file: " + fileHeader.getFileName());
		}
		
		long start = localFileHeader.getOffsetStartOfData();
		long end = localFileHeader.getOffsetStartOfData() + localFileHeader.getCompressedSize();
		ZipModel nestedZipModel = null;
		try {
			Zip4jInputRAF nestedZip4jRaf = new Zip4jInputRAF(parentZipModel.getZipFile(), InternalZipConstants.READ_MODE,
					start, end);
			headerReader = new HeaderReader(nestedZip4jRaf);
			nestedZipModel = headerReader.readAllHeaders();
			nestedZipModel.setNestedZipFile(true);
			nestedZipModel.setStart(start);
			nestedZipModel.setEnd(end);
			nestedZipModel.setZipFile(parentZipModel.getZipFile());
		} catch (FileNotFoundException e) {
			throw new ZipException(e);
		}
		return nestedZipModel;
	}
	
}
