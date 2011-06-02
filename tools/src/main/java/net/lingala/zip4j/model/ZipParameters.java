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

import java.util.TimeZone;

import net.lingala.zip4j.util.InternalZipConstants;
import net.lingala.zip4j.util.Zip4jConstants;
import net.lingala.zip4j.util.Zip4jUtil;

public class ZipParameters implements Cloneable {
	
	private int compressionMethod;
	private int compressionLevel;
	private boolean encryptFiles;
	private int encryptionMethod;
	private boolean readHiddenFiles;
	private String password;
	private int aesKeyStrength;
	private boolean includeRootFolder;
	private String rootFolderInZip;
	private TimeZone timeZone;
	
	public ZipParameters() {
		compressionMethod = Zip4jConstants.COMP_DEFLATE;
		encryptFiles = false;
		readHiddenFiles = true;
		encryptionMethod = Zip4jConstants.ENC_NO_ENCRYPTION;
		aesKeyStrength = -1;
		includeRootFolder = true;
		timeZone = TimeZone.getDefault();
	}

	public int getCompressionMethod() {
		return compressionMethod;
	}

	public void setCompressionMethod(int compressionMethod) {
		this.compressionMethod = compressionMethod;
	}

	public boolean isEncryptFiles() {
		return encryptFiles;
	}

	public void setEncryptFiles(boolean encryptFiles) {
		this.encryptFiles = encryptFiles;
	}

	public int getEncryptionMethod() {
		return encryptionMethod;
	}

	public void setEncryptionMethod(int encryptionMethod) {
		this.encryptionMethod = encryptionMethod;
	}

	public int getCompressionLevel() {
		return compressionLevel;
	}

	public void setCompressionLevel(int compressionLevel) {
		this.compressionLevel = compressionLevel;
	}

	public boolean isReadHiddenFiles() {
		return readHiddenFiles;
	}

	public void setReadHiddenFiles(boolean readHiddenFiles) {
		this.readHiddenFiles = readHiddenFiles;
	}
	
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getAesKeyStrength() {
		return aesKeyStrength;
	}

	public void setAesKeyStrength(int aesKeyStrength) {
		this.aesKeyStrength = aesKeyStrength;
	}

	public boolean isIncludeRootFolder() {
		return includeRootFolder;
	}

	public void setIncludeRootFolder(boolean includeRootFolder) {
		this.includeRootFolder = includeRootFolder;
	}

	public String getRootFolderInZip() {
		return rootFolderInZip;
	}

	public void setRootFolderInZip(String rootFolderInZip) {
		if (Zip4jUtil.isStringNotNullAndNotEmpty(rootFolderInZip)) {
			
			if (!rootFolderInZip.endsWith("\\") && !rootFolderInZip.endsWith("/")) {
				rootFolderInZip = rootFolderInZip + InternalZipConstants.FILE_SEPARATOR;
			}
			
			rootFolderInZip = rootFolderInZip.replaceAll("\\\\", "/");
			
//			if (rootFolderInZip.endsWith("/")) {
//				rootFolderInZip = rootFolderInZip.substring(0, rootFolderInZip.length() - 1);
//				rootFolderInZip = rootFolderInZip + "\\";
//			}
		} 
		this.rootFolderInZip = rootFolderInZip;
	}

	public TimeZone getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}
	
}
