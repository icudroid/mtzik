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

public class Zip4jInputRAF extends RandomAccessFile {
	
	private long start;
	private long end;
	private File file;
	
	public Zip4jInputRAF(File file, String mode)
			throws FileNotFoundException {
		super(file, mode);
		start = 0;
		end = file.length();
		this.file = file;
	}
	
	public Zip4jInputRAF(String file, String mode) 
			throws FileNotFoundException {
		super(file, mode);
		File filePointer = new File(file);
		start = 0;
		end = filePointer.length();
		this.file = filePointer;
	}
	
	public Zip4jInputRAF(File file, String mode, long start, long end)
			throws FileNotFoundException {
		super(file, mode);
		this.start = start;
		this.end = end;
	}

	public Zip4jInputRAF(String file, String mode, long start, long end) 
			throws FileNotFoundException {
		super(file, mode);
		this.start = start;
		this.end = end;
	}
	
	public int read() throws IOException {
		long currentFilePointer = super.getFilePointer();
		if (currentFilePointer >= end) {
			return -1;
		}
		return super.read();
	}
	
	public int read(byte[] b) throws IOException {
		return this.read(b, 0, b.length);
	}
	
	public int read(byte[] b, int off, int len) throws IOException {
		
		long currentFilePointer = super.getFilePointer();
		if (currentFilePointer >= end) {
			return -1;
		}
		
		if (currentFilePointer + b.length > end) {
			len = (int)(end - currentFilePointer);
		}
		
		return super.read(b, off, len);
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
	}

	public File getFile() {
		return file;
	}
	
	

}
