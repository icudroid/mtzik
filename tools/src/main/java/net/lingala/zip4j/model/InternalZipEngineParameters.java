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

package net.lingala.zip4j.model;

import java.io.File;

import net.lingala.zip4j.crypto.IEncrypter;
import net.lingala.zip4j.io.Zip4jInputRAF;
import net.lingala.zip4j.io.Zip4jOutputStream;
import net.lingala.zip4j.zip.ZipEngine;

public class InternalZipEngineParameters {
	
	private ZipModel zipModel;
	private Zip4jInputRAF inputStream;
	private Zip4jOutputStream outputStream;
	private boolean splitEligible;
	private boolean splitArchive;
	private long splitSize;
	private ZipEngine zipEngine;
	private ZipParameters zipParameters;
	private File fileToZip;
	private String relativeFileName;
	private IEncrypter encrypter;
	
	public InternalZipEngineParameters() {
	}

	public Zip4jInputRAF getInputStream() {
		return inputStream;
	}

	public void setInputStream(Zip4jInputRAF inputStream) {
		this.inputStream = inputStream;
	}

	public Zip4jOutputStream getOutputStream() {
		return outputStream;
	}

	public void setOutputStream(Zip4jOutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public ZipModel getZipModel() {
		return zipModel;
	}

	public void setZipModel(ZipModel zipModel) {
		this.zipModel = zipModel;
	}

	public boolean isSplitEligible() {
		return splitEligible;
	}

	public void setSplitEligible(boolean splitEligible) {
		this.splitEligible = splitEligible;
	}

	public boolean isSplitArchive() {
		return splitArchive;
	}

	public void setSplitArchive(boolean splitArchive) {
		this.splitArchive = splitArchive;
	}

	public long getSplitSize() {
		return splitSize;
	}

	public void setSplitSize(long splitSize) {
		this.splitSize = splitSize;
	}

	public ZipEngine getZipEngine() {
		return zipEngine;
	}

	public void setZipEngine(ZipEngine zipEngine) {
		this.zipEngine = zipEngine;
	}

	public ZipParameters getZipParameters() {
		return zipParameters;
	}

	public void setZipParameters(ZipParameters zipParameters) {
		this.zipParameters = zipParameters;
	}

	public File getFileToZip() {
		return fileToZip;
	}

	public void setFileToZip(File fileToZip) {
		this.fileToZip = fileToZip;
	}

	public String getRelativeFileName() {
		return relativeFileName;
	}

	public void setRelativeFileName(String relativeFileName) {
		this.relativeFileName = relativeFileName;
	}

	public IEncrypter getEncrypter() {
		return encrypter;
	}

	public void setEncrypter(IEncrypter encrypter) {
		this.encrypter = encrypter;
	}
	
	
	
}
