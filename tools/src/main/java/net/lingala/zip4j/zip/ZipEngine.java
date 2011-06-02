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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import net.lingala.zip4j.core.HeaderWriter;
import net.lingala.zip4j.crypto.AESEncrpyter;
import net.lingala.zip4j.crypto.IEncrypter;
import net.lingala.zip4j.crypto.StandardEncrypter;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.Zip4jInputRAF;
import net.lingala.zip4j.io.Zip4jOutputStream;
import net.lingala.zip4j.model.AESExtraDataRecord;
import net.lingala.zip4j.model.CentralDirectory;
import net.lingala.zip4j.model.EndCentralDirRecord;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.LocalFileHeader;
import net.lingala.zip4j.model.InternalZipEngineParameters;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.progress.ProgressMonitor;
import net.lingala.zip4j.util.ArchiveMaintainer;
import net.lingala.zip4j.util.InternalZipConstants;
import net.lingala.zip4j.util.Raw;
import net.lingala.zip4j.util.Zip4jConstants;
import net.lingala.zip4j.util.Zip4jUtil;

public class ZipEngine {
	
	private ZipModel zipModel;
	private Zip4jOutputStream outputStream;
	private Zip4jInputRAF inputStream;
	private boolean isSplitEligible;
	private long offsetStartOfNextLocalHeader;
	private String rootFolderPath;
	private int currSplitFileCounter;
	
	public ZipEngine(ZipModel zipModel, boolean isSplitEligible) throws ZipException {
		
		if (zipModel == null) {
			throw new ZipException("zip model is null in ZipEngine constructor");
		}
		
		this.zipModel = zipModel;
		this.isSplitEligible = isSplitEligible;
		this.currSplitFileCounter = 1;
	}
	
	public void addFiles(final ArrayList fileList, final ZipParameters parameters,
			final ProgressMonitor progressMonitor, boolean runInThread) throws ZipException {
		
		if (fileList == null || parameters == null) {
			throw new ZipException("one of the input parameters is null when adding files");
		}
		
		if(fileList.size() <= 0) {
			throw new ZipException("no files to add");
		}
		
		progressMonitor.setTotalWork(calculateTotalWork(fileList, parameters));
		progressMonitor.setCurrentOperation(ProgressMonitor.OPERATION_ADD);
		progressMonitor.setState(ProgressMonitor.STATE_BUSY);
		progressMonitor.setResult(ProgressMonitor.RESULT_WORKING);
		
		if (runInThread) {
			
			Thread thread = new Thread(InternalZipConstants.THREAD_NAME) {
				public void run() {
					try {
						initAddFiles(fileList, parameters, progressMonitor);
					} catch (ZipException e) {
					}
				}
			};
			thread.start();
			
		} else {
			initAddFiles(fileList, parameters, progressMonitor);
		}
	}
	
	private void initAddFiles(ArrayList fileList, ZipParameters parameters,
			ProgressMonitor progressMonitor) throws ZipException {

		try {
			if (fileList == null || parameters == null) {
				throw new ZipException("one of the input parameters is null when adding files");
			}
			
			if(fileList.size() <= 0) {
				throw new ZipException("no files to add");
			}
			
			if (zipModel.getEndCentralDirRecord() == null) {
				zipModel.setEndCentralDirRecord(createEndOfCentralDirectoryRecord());
			}
			
			outputStream = prepareFileOutputStream();
			
			calculateOffsetStartOfNextLocalHeader();
			
			for (int i = 0; i < fileList.size(); i++) {
				File file = (File)fileList.get(i);
				if (file == null) {
					throw new ZipException("input file is null, cannot add to zip file");
				}
				
				if (!Zip4jUtil.checkFileExists(file.getAbsolutePath())) {
					throw new ZipException("input file does not exist: " + file.getAbsolutePath());
				}
				
				if (!Zip4jUtil.checkFileReadAccess(file.getAbsolutePath())) {
					throw new ZipException("no read access to file: " + file.getAbsolutePath());
				}
				
				addFileToZip(file, parameters, progressMonitor);
				
				if (progressMonitor.isCancelAllTasks()) {
					progressMonitor.setResult(ProgressMonitor.RESULT_CANCELLED);
					progressMonitor.setState(ProgressMonitor.STATE_READY);
					return;
				}
				
				offsetStartOfNextLocalHeader = outputStream.getFilePointer();
				
			}
			
			if (fileList.size() > 0) {
				long endOfFilePointer = outputStream.getLength();
				
				if (endOfFilePointer < 0) {
					throw new ZipException("current zip file length is invalid, cannot finalize zip file");
				} else {
					outputStream.seek(endOfFilePointer);
				}
				
				zipModel.getEndCentralDirRecord().setNoOfThisDisk(currSplitFileCounter - 1);
				
				HeaderWriter headerWriter = new HeaderWriter();
				headerWriter.finalizeZipFile(zipModel, outputStream);
			}
			
			progressMonitor.endProgressMonitorSuccess();
		} catch (ZipException e) {
			progressMonitor.endProgressMonitorError(e);
			throw e;
		} catch (Exception e) {
			progressMonitor.endProgressMonitorError(e);
			throw new ZipException(e);
		} finally {
			if (outputStream != null) {
				outputStream.close();
			}
		}
	}
	
	public void addFolderToZip(File file, ZipParameters parameters, 
			ProgressMonitor progressMonitor, boolean runInThread) throws ZipException {
		if (file == null || parameters == null) {
			throw new ZipException("one of the input parameters is null, cannot add folder to zip");
		}
		
		if (!Zip4jUtil.checkFileExists(file.getAbsolutePath())) {
			throw new ZipException("input folder does not exist");
		}
		
		if (!file.isDirectory()) {
			throw new ZipException("input file is not a folder, user addFileToZip method to add files");
		}
		
		if (!Zip4jUtil.checkFileReadAccess(file.getAbsolutePath())) {
			throw new ZipException("cannot read folder: " + file.getAbsolutePath());
		}
		
		/*
		if (parameters.isIncludeRootFolder()) {
			this.rootFolderPath = file.getParent();
		} else {
			this.rootFolderPath = file.getPath();
		} */
		
		if (parameters.isIncludeRootFolder()) {
		    this.rootFolderPath = file.getParentFile() != null ? file.getParentFile().getAbsolutePath() : "";
		} else {
		    this.rootFolderPath = file.getAbsolutePath();
		}
		
		addFiles(Zip4jUtil.getFilesInDirectoryRec(file, parameters.isReadHiddenFiles()), 
				parameters, progressMonitor, runInThread);
	}
	
	private void addFileToZip(File file, ZipParameters parameters, 
			ProgressMonitor progressMonitor) throws ZipException {
		if (file == null || parameters == null) {
			throw new ZipException("input parameters are null in addFileToZip");
		}
		
		checkParameters(parameters); // if any of the parameters is invalid then an exception is thrown
		
		if (zipModel.getLocalFileHeaderList() == null) {
			zipModel.setLocalFileHeaderList(new ArrayList());
		}
		
		try {
			
			String relFileName = getRelativeFileName(file.getAbsolutePath(), parameters.getRootFolderInZip());
			removeFileIfExists(relFileName, progressMonitor);
			if (progressMonitor.isCancelAllTasks()) {
				progressMonitor.setResult(ProgressMonitor.RESULT_CANCELLED);
				progressMonitor.setState(ProgressMonitor.STATE_READY);
				return;
			}
			
			if (zipModel.getCentralDirectory() == null || 
					zipModel.getCentralDirectory().getFileHeaders() == null || 
					zipModel.getCentralDirectory().getFileHeaders().size() <= 0) {
				if (zipModel.isSplitArchive()) {
					byte[] splitSigBytes = new byte[4];
					Raw.writeIntLittleEndian(splitSigBytes, 0, (int)InternalZipConstants.SPLITSIG);
					outputStream.write(splitSigBytes);
					offsetStartOfNextLocalHeader = 4;
				}
			}
			
			// Do this so as not to change the compression type for all following zip files
			// when a directory is added
			// If this is not done, then after a directory is added, then all consecutive files
			// will be in STORE compression
			ZipParameters param = (ZipParameters)parameters.clone();
			
			InternalZipEngineParameters zipEngineParameters = new InternalZipEngineParameters();
			zipEngineParameters.setZipParameters(param);
			zipEngineParameters.setFileToZip(file);
			zipEngineParameters.setSplitArchive(zipModel.isSplitArchive());
			zipEngineParameters.setSplitEligible(isSplitEligible);
			zipEngineParameters.setSplitSize(zipModel.getSplitLength());
			zipEngineParameters.setZipEngine(this);
			zipEngineParameters.setZipModel(zipModel);
			zipEngineParameters.setRelativeFileName(relFileName);
			zipEngineParameters.setOutputStream(outputStream);
			
			progressMonitor.setFileName(relFileName);
			
			if (file.isDirectory()) {
				param.setCompressionMethod(Zip4jConstants.COMP_STORE);
				param.setEncryptionMethod(Zip4jConstants.ENC_NO_ENCRYPTION);
				
//				if (relFileName != null && !relFileName.endsWith(System.getProperty("file.separator"))) {
//					relFileName = relFileName + System.getProperty("file.separator");
//					zipEngineParameters.setRelativeFileName(relFileName);
//				}
				
				HeaderWriter headerWriter = new HeaderWriter();
				
				FileHeader fileHeader = createFileHeader(zipEngineParameters);
				fileHeader.setUncompressedSize(0);
				fileHeader.setCrc32(0);
				fileHeader.setOffsetLocalHeader(offsetStartOfNextLocalHeader);
				fileHeader.setDirectory(true);
				fileHeader.setDiskNumberStart(currSplitFileCounter - 1);
				
				LocalFileHeader localFileHeader = createLocalFileHeader(zipEngineParameters);
				localFileHeader.setUncompressedSize(0);
				localFileHeader.setCrc32(0);
				outputStream.seek(offsetStartOfNextLocalHeader);
				headerWriter.writeLocalFileHeader(zipModel, localFileHeader, zipEngineParameters);
				offsetStartOfNextLocalHeader = outputStream.getFilePointer();
				
				zipModel.getLocalFileHeaderList().add(localFileHeader);
				
				if (zipModel.getCentralDirectory() == null) {
					zipModel.setCentralDirectory(new CentralDirectory());
				}
				
				if (zipModel.getCentralDirectory().getFileHeaders() == null) {
					zipModel.getCentralDirectory().setFileHeaders(new ArrayList());
				}
				
				zipModel.getCentralDirectory().getFileHeaders().add(fileHeader);
				return;
			}
			
			inputStream = prepareFileInputStream(file);
			zipEngineParameters.setInputStream(inputStream);
			
			if (file.isFile()) {
				if (file.getAbsolutePath().endsWith(".zip")) {
					param.setCompressionMethod(Zip4jConstants.COMP_STORE);
				}
				
				if(Zip4jUtil.getFileLengh(file) <= 0) {
					param.setCompressionMethod(Zip4jConstants.COMP_STORE);
				}
			}
			
			if (param.isEncryptFiles()) {
				IEncrypter encrypter = null;
				switch (param.getEncryptionMethod()) {
				case Zip4jConstants.ENC_METHOD_STANDARD:
					encrypter = new StandardEncrypter(inputStream, param.getPassword(), progressMonitor);
					break;
				case Zip4jConstants.ENC_METHOD_AES:
					encrypter = new AESEncrpyter(param.getPassword(), param.getAesKeyStrength());
					break;
				default:
					throw new ZipException("invalid encrpytion method");
				}
				zipEngineParameters.setEncrypter(encrypter);
			}
			
			FileHeader fileHeader = null;
			
			switch (param.getCompressionMethod()) {
			case Zip4jConstants.COMP_STORE:
				IZipEngine storeZipEngine = new StoreZip(zipEngineParameters);
				fileHeader = storeZipEngine.zipCompleteFile(progressMonitor);
				break;
			case Zip4jConstants.COMP_DEFLATE:
				IZipEngine deflateZipEngine = new DeflateZip(zipEngineParameters);
				fileHeader = deflateZipEngine.zipCompleteFile(progressMonitor);
				break;
			default:
				throw new ZipException("invalid compression type, cannot add file: " + file.getName());
			}
			
			if (progressMonitor.isCancelAllTasks()) {
				progressMonitor.setResult(ProgressMonitor.RESULT_CANCELLED);
				return;
			}
			
			if (fileHeader == null) {
				throw new ZipException("file header is null, cannot write file header, zip file might be now courrupt");
			} else {
				fileHeader.setDirectory(false);
				
				if (param.getEncryptionMethod() == Zip4jConstants.ENC_METHOD_AES) {
					fileHeader.setAesExtraDataRecord(generateAESExtraDataRecord(param));
				}
				
				if (zipModel.getCentralDirectory() == null) {
					zipModel.setCentralDirectory(new CentralDirectory());
				}
				
				if (zipModel.getCentralDirectory().getFileHeaders() == null) {
					zipModel.getCentralDirectory().setFileHeaders(new ArrayList());
				}
				
				if (zipModel.getEndCentralDirRecord() == null) {
					zipModel.setEndCentralDirRecord(new EndCentralDirRecord());
				}
				
				zipModel.getCentralDirectory().getFileHeaders().add(fileHeader);
				zipModel.getEndCentralDirRecord().setOffsetOfStartOfCentralDir(outputStream.getFilePointer());
			}
			
		} catch (ZipException e) {
			progressMonitor.endProgressMonitorError(e);
			throw e;
		} catch (Exception e) {
			progressMonitor.endProgressMonitorError(e);
			throw new ZipException(e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					throw new ZipException(e);
				}
			}
		}
	}
	
	private void checkParameters(ZipParameters parameters) throws ZipException {
		
		if (parameters == null) {
			throw new ZipException("cannot validate zip parameters");
		}
		
		if ((parameters.getCompressionMethod() != Zip4jConstants.COMP_STORE) && 
				parameters.getCompressionMethod() != Zip4jConstants.COMP_DEFLATE) {
			throw new ZipException("unsupported compression type");
		}
		
		if (parameters.getCompressionMethod() == Zip4jConstants.COMP_DEFLATE) {
			if (parameters.getCompressionLevel() < 0 && parameters.getCompressionLevel() > 9) {
				throw new ZipException("invalid compression level. compression level dor deflate should be in the range of 0-9");
			}
		}
		
		if (parameters.isEncryptFiles()) {
			if (parameters.getEncryptionMethod() != Zip4jConstants.ENC_METHOD_STANDARD && 
					parameters.getEncryptionMethod() != Zip4jConstants.ENC_METHOD_AES) {
				throw new ZipException("unsupported encryption method");
			}
			
			String password = parameters.getPassword();
			if (!Zip4jUtil.isStringNotNullAndNotEmpty(password)) {
				throw new ZipException("input password is empty or null");
			}
		} else {
			parameters.setAesKeyStrength(-1);
			parameters.setEncryptionMethod(-1);
		}
		
	}
	
	/**
	 * Before adding a file to a zip file, we check if a file already exists in the zip file
	 * with the same fileName (including path, if exists). If yes, then we remove this file
	 * before adding the file<br><br>
	 * 
	 * <b>Note:</b> Relative path has to be passed as the fileName
	 * 
	 * @param zipModel
	 * @param fileName
	 * @throws ZipException
	 */
	private void removeFileIfExists(String fileName, ProgressMonitor progressMonitor) throws ZipException {
		if (zipModel == null || !Zip4jUtil.isStringNotNullAndNotEmpty(fileName)) {
			// Do nothing
			return;
		}
		
		if (zipModel.getCentralDirectory() == null || 
				zipModel.getCentralDirectory().getFileHeaders() == null || 
						zipModel.getCentralDirectory().getFileHeaders().size() <= 0) {
			//For a new zip file, this condition satisfies, so do nothing
			return;
		}
		
		FileHeader fileHeader = Zip4jUtil.getFileHeader(zipModel, fileName);
		if (fileHeader != null) {
			
			if (outputStream != null) {
				outputStream.close();
				outputStream = null;
			}
			
			ArchiveMaintainer archiveMaintainer = new ArchiveMaintainer();
			progressMonitor.setCurrentOperation(ProgressMonitor.OPERATION_REMOVE);
			HashMap retMap = archiveMaintainer.initRemoveZipFile(zipModel, fileHeader, progressMonitor);
			
			if(progressMonitor.isCancelAllTasks()) {
				progressMonitor.setResult(ProgressMonitor.RESULT_CANCELLED);
				progressMonitor.setState(ProgressMonitor.STATE_READY);
				return;
			}
			
			progressMonitor.setCurrentOperation(ProgressMonitor.OPERATION_ADD);
			
			if (outputStream == null) {
				outputStream = prepareFileOutputStream();
				
				if (retMap != null) {
					if (retMap.get(InternalZipConstants.OFFSET_CENTRAL_DIR) != null) {
						long offsetCentralDir = -1;
						try {
							offsetCentralDir = Long.parseLong((String)retMap.get(InternalZipConstants.OFFSET_CENTRAL_DIR));
						} catch (NumberFormatException e) {
							throw new ZipException("NumberFormatException while parsing offset central directory. Cannot update already existing file header");
						} catch (Exception e) {
							throw new ZipException("Error while parsing offset central directory. Cannot update already existing file header");
						}
						
						if (offsetCentralDir >= 0) {
							outputStream.seek(offsetCentralDir);
							offsetStartOfNextLocalHeader = offsetCentralDir;
						}
					}
				}
			}
		}
		
	}
	
	private Zip4jOutputStream prepareFileOutputStream() throws ZipException {
		String outPath = zipModel.getZipFile();
		if (!Zip4jUtil.isStringNotNullAndNotEmpty(outPath)) {
			throw new ZipException("invalid output path");
		}
		
		try {
			File outFile = new File(outPath);
			if (!outFile.getParentFile().exists()) {
				outFile.getParentFile().mkdirs();
			}
			return new Zip4jOutputStream(outFile, InternalZipConstants.WRITE_MODE, this);
		} catch (FileNotFoundException e) {
			throw new ZipException(e);
		}
	}
	
	private Zip4jInputRAF prepareFileInputStream(File file) throws ZipException {
		if (file == null) {
			throw new ZipException("file is null. cannot create input stream");
		}
		
		if (!Zip4jUtil.checkFileExists(file.getAbsolutePath())) {
			throw new ZipException("input file does not exist. cannot create input stream");
		}
		
		try {
			return new Zip4jInputRAF(file, InternalZipConstants.READ_MODE);
		} catch (Exception e) {
			throw new ZipException(e);
		}
	}
	
	private EndCentralDirRecord createEndOfCentralDirectoryRecord() {
		EndCentralDirRecord endCentralDirRecord = new EndCentralDirRecord();
		endCentralDirRecord.setSignature(InternalZipConstants.ENDSIG);
		endCentralDirRecord.setNoOfThisDisk(0);
		endCentralDirRecord.setTotNoOfEntriesInCentralDir(0);
		endCentralDirRecord.setTotNoOfEntriesInCentralDirOnThisDisk(0);
		endCentralDirRecord.setOffsetOfStartOfCentralDir(0);
		return endCentralDirRecord;
	}
	
	public LocalFileHeader createLocalFileHeader(InternalZipEngineParameters zipEngineParameters) 
		throws ZipException {
		
		if (zipEngineParameters == null) {
			throw new ZipException("no zip engine parameters. cannot create local file header");
		}
		
		LocalFileHeader localFileHeader = new LocalFileHeader();
		localFileHeader.setSignature((int)InternalZipConstants.LOCSIG);
		localFileHeader.setVersionNeededToExtract(10);
		
		if (zipEngineParameters.getZipParameters().isEncryptFiles() && 
				zipEngineParameters.getZipParameters().getEncryptionMethod() == Zip4jConstants.ENC_METHOD_AES) {
			localFileHeader.setCompressionMethod(Zip4jConstants.ENC_METHOD_AES);
		} else {
			localFileHeader.setCompressionMethod(zipEngineParameters.getZipParameters().getCompressionMethod());
		}
		
		localFileHeader.setLastModFileTime((int)Zip4jUtil.javaToDosTime((
				Zip4jUtil.getLastModifiedFileTime(
						zipEngineParameters.getFileToZip(), 
						zipEngineParameters.getZipParameters().getTimeZone()))));
		localFileHeader.setUncompressedSize(zipEngineParameters.getFileToZip().length());
		String fileName = zipEngineParameters.getRelativeFileName();
		
		if (zipEngineParameters.getZipParameters() != null && 
				Zip4jUtil.isStringNotNullAndNotEmpty(zipEngineParameters.getZipModel().getFileNameCharset())) {
			localFileHeader.setFileNameLength(Zip4jUtil.getEncodedStringLength(fileName, 
					zipEngineParameters.getZipModel().getFileNameCharset()));
		} else {
			localFileHeader.setFileNameLength(Zip4jUtil.getEncodedStringLength(fileName));
		}
		localFileHeader.setFileName(fileName);
		
		return localFileHeader;
		
	}
	
	public FileHeader createFileHeader(InternalZipEngineParameters zipEngineParameters) 
		throws ZipException {
		
		if (zipEngineParameters == null) {
			throw new ZipException("no zip engine parameters, cannot create file header");
		}
		
		FileHeader fileHeader = new FileHeader();
		fileHeader.setSignature((int)InternalZipConstants.CENSIG);
		fileHeader.setVersionMadeBy(10);
		fileHeader.setVersionNeededToExtract(10);
		if (zipEngineParameters.getZipParameters().isEncryptFiles() && 
				zipEngineParameters.getZipParameters().getEncryptionMethod() == Zip4jConstants.ENC_METHOD_AES) {
				fileHeader.setCompressionMethod(Zip4jConstants.ENC_METHOD_AES);
		} else {
			fileHeader.setCompressionMethod(zipEngineParameters.getZipParameters().getCompressionMethod());
		}
		fileHeader.setLastModFileTime(
				(int)Zip4jUtil.javaToDosTime((
						Zip4jUtil.getLastModifiedFileTime(
								zipEngineParameters.getFileToZip(), 
								zipEngineParameters.getZipParameters().getTimeZone()))));
		fileHeader.setUncompressedSize(zipEngineParameters.getFileToZip().length());
		String fileName = zipEngineParameters.getRelativeFileName();
		
		if (zipEngineParameters.getZipParameters() != null && 
				Zip4jUtil.isStringNotNullAndNotEmpty(zipEngineParameters.getZipModel().getFileNameCharset())) {
			fileHeader.setFileNameLength(Zip4jUtil.getEncodedStringLength(fileName, 
					zipEngineParameters.getZipModel().getFileNameCharset()));
		} else {
			fileHeader.setFileNameLength(Zip4jUtil.getEncodedStringLength(fileName));
		}
		fileHeader.setFileName(fileName);
		fileHeader.setDiskNumberStart(0);
		
		int fileAttrs = 0;
		if (zipEngineParameters.getFileToZip() != null) {
			fileAttrs = getFileAttributes(zipEngineParameters.getFileToZip());
		}
		byte[] externalFileAttrs = {(byte)fileAttrs, 0, 0, 0};
		fileHeader.setExternalFileAttr(externalFileAttrs);
		
		return fileHeader;
	}
	
	private void calculateOffsetStartOfNextLocalHeader() throws ZipException {
		if (zipModel == null) {
			offsetStartOfNextLocalHeader = 0;
		}
		
		if (zipModel.getEndCentralDirRecord() == null) {
			offsetStartOfNextLocalHeader = 0;
		}
		
		offsetStartOfNextLocalHeader = zipModel.getEndCentralDirRecord().getOffsetOfStartOfCentralDir();
	}
	
	private String getRelativeFileName(String file, String rootFolderInZip) throws ZipException {
		if (!Zip4jUtil.isStringNotNullAndNotEmpty(file)) {
			throw new ZipException("input file path/name is empty, cannot calculate relative file name");
		}
		
		String fileName = null;
		
		if (Zip4jUtil.isStringNotNullAndNotEmpty(this.rootFolderPath)) {
			String tmpFileName = file.substring(rootFolderPath.length());
			if (tmpFileName.startsWith(System.getProperty("file.separator"))) {
				tmpFileName = tmpFileName.substring(1);
			}
			
			File tmpFile = new File(file);
			if (tmpFile.isDirectory()) {
				tmpFileName = tmpFileName.replaceAll("\\\\", "/");
				tmpFileName += InternalZipConstants.ZIP_FILE_SEPARATOR;
			} else {
				String bkFileName = tmpFileName.substring(0, tmpFileName.indexOf(tmpFile.getName()));
				bkFileName = bkFileName.replaceAll("\\\\", "/");
				tmpFileName = bkFileName + tmpFile.getName();
			}
			
			fileName = tmpFileName;
		} else {
			File relFile = new File(file);
			if (relFile.isDirectory()) {
				fileName = relFile.getName() + InternalZipConstants.ZIP_FILE_SEPARATOR;
			} else {
				fileName = Zip4jUtil.getFileNameFromFilePath(new File(file));
			}
		}
		
		if (Zip4jUtil.isStringNotNullAndNotEmpty(rootFolderInZip)) {
			fileName = rootFolderInZip + fileName;
		}
		
		if (!Zip4jUtil.isStringNotNullAndNotEmpty(fileName)) {
			throw new ZipException("Error determining file name");
		}
		
		return fileName;
	}
	
	public AESExtraDataRecord generateAESExtraDataRecord(ZipParameters parameters) throws ZipException{
		
		if (parameters == null) {
			throw new ZipException("zip parameters are null, cannot generate AES Extra Data record");
		}
		
		AESExtraDataRecord aesDataRecord = new AESExtraDataRecord();
		aesDataRecord.setSignature(InternalZipConstants.AESSIG);
		aesDataRecord.setDataSize(7);
		aesDataRecord.setVendorID("AE");
		// Always set the version number to 2 as we donot store CRC for any AES encrypted files
		// only MAC is stored and as per the specification, if version number is 2, then MAC is read
		// and CRC is ignored
		aesDataRecord.setVersionNumber(2); 
		if (parameters.getAesKeyStrength() == Zip4jConstants.AES_STRENGTH_128) {
			aesDataRecord.setAesStrength(Zip4jConstants.AES_STRENGTH_128);
		} else if (parameters.getAesKeyStrength() == Zip4jConstants.AES_STRENGTH_256) {
			aesDataRecord.setAesStrength(Zip4jConstants.AES_STRENGTH_256);
		} else {
			throw new ZipException("invalid AES key strength, cannot generate AES Extra data record");
		}
		aesDataRecord.setCompressionMethod(parameters.getCompressionMethod());
		
		return aesDataRecord;
	}
	
	/**
	 * Checks the file attributes and returns an integer
	 * @param file
	 * @return
	 * @throws ZipException
	 */
	public int getFileAttributes(File file) throws ZipException {
		if (file == null) {
			throw new ZipException("input file is null, cannot get file attributes");
		}
		
		if (!file.exists()) {
			return 0;
		}
		
		if (file.isDirectory()) {
			if (file.isHidden()) {
				return InternalZipConstants.FOLDER_MODE_HIDDEN;
			} else {
				return InternalZipConstants.FOLDER_MODE_NONE;
			}
		} else {
			if (!file.canWrite() && file.isHidden()) {
				return InternalZipConstants.FILE_MODE_READ_ONLY_HIDDEN;
			} else if (!file.canWrite()) {
				return InternalZipConstants.FILE_MODE_READ_ONLY;
			} else if (file.isHidden()) {
				return InternalZipConstants.FILE_MODE_HIDDEN;
			} else {
				return InternalZipConstants.FILE_MODE_NONE;
			}
		}
	}
	
	private long calculateTotalWork(ArrayList fileList, ZipParameters parameters) throws ZipException {
		if (fileList == null) {
			throw new ZipException("file list is null, cannot calculate total work");
		}
		
		long totalWork = 0;
		
		for (int i = 0; i < fileList.size(); i++) {
			if(fileList.get(i) instanceof File) {
				if (((File)fileList.get(i)).exists()) {
					if (parameters.isEncryptFiles() && 
							parameters.getEncryptionMethod() == Zip4jConstants.ENC_METHOD_STANDARD) {
						totalWork += (Zip4jUtil.getFileLengh((File)fileList.get(i)) * 2);
					} else {
						totalWork += Zip4jUtil.getFileLengh((File)fileList.get(i));
					}
					
					if (zipModel.getCentralDirectory() != null && 
							zipModel.getCentralDirectory().getFileHeaders() != null && 
							zipModel.getCentralDirectory().getFileHeaders().size() > 0) {
						String relativeFileName = getRelativeFileName(
								((File)fileList.get(i)).getAbsolutePath(), parameters.getRootFolderInZip());
						FileHeader fileHeader = Zip4jUtil.getFileHeader(zipModel, relativeFileName);
						if (fileHeader != null) {
							totalWork += (Zip4jUtil.getFileLengh(new File(zipModel.getZipFile())) - fileHeader.getCompressedSize());
						}
					}
				}
			}
		}
		
		return totalWork;
	}
	
	public void updateZipModel(ZipModel zipModel) {
		this.zipModel = zipModel;
	}
	
	public long getOffsetStartOfNextLocalHeader() {
		return offsetStartOfNextLocalHeader;
	}

	public ZipModel getZipModel() {
		return zipModel;
	}

	public int getCurrSplitFileCounter() {
		return currSplitFileCounter;
	}

	public void incrementCurrSplitFileCounter() {
		currSplitFileCounter++;
	}
	
}
