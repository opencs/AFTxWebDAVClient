/*
 * BSD 3-Clause License
 * 
 * Copyright (c) 2007-2019, Open Communications Security
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * 
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * 
 * * Neither the name of the copyright holder nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package br.com.opencs.santander.securefiletransfer.webdav.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.jackrabbit.webdav.client.methods.DeleteMethod;
import org.apache.jackrabbit.webdav.client.methods.PutMethod;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Esta classe implementa o cliente do AFTx. 
 * 
 * @author Augusto Kiramoto
 * @author Rafael Teixeira
 * @author Fabio Jun Takada Chino
 * @author Cesar Luiz Ferracin
 * @since 1.0.0
 */
public class AFTxWebDAVClient implements IAFTxClient {
	
	private static final ResourceBundle RESOURCES = ResourceBundle.getBundle(AFTxWebDAVClient.class.getName());
	
	private static Logger logger = LoggerFactory.getLogger(AFTxWebDAVClient.class);

	private String AFTxBaseURL = null;

	/**
	 * Cria uma nova instância desta classe. 
	 * 
	 * @param trustStoreLocation Arquivo com o keystore a ser utilizado. Se nulo, o keystore
	 * @param trustStorePassword Senha do keystore a ser utilizado.
	 * @throws IOException Em caso de erro.
	 * @since 2.1.0
	 */
	public AFTxWebDAVClient(String trustStoreLocation, String trustStorePassword)
			throws IOException {
		
		if (trustStoreLocation != null) {
			File truststore = new File(trustStoreLocation);
			if (!truststore.isFile())
				throw new IOException(String.format("File '%1%s' doesn't exist",  truststore.getCanonicalPath())) ;
			System.setProperty("javax.net.ssl.trustStore", truststore.getCanonicalPath());
			System.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);
		}
	}
	
	/**
	 * Cria uma nova instância desta classe. 
	 * 
	 * @param log4JConfigurationFilePath Caminho para o arquivo de configuração do Log4J.
	 * @param trustStoreLocation Arquivo com o keystore a ser utilizado. Se nulo, o keystore
	 * @param trustStorePassword Senha do keystore a ser utilizado.
	 * @throws IOException Em caso de erro.
	 * @deprecated 
	 */
	@Deprecated
	public AFTxWebDAVClient(String log4JConfigurationFilePath, String trustStoreLocation, String trustStorePassword)
			throws IOException {
		this(trustStoreLocation, trustStorePassword);
	}	

	public int deleteFileInAFT(String inputBoxName, String username, String encryptedPassword, String aftFileName) {

		logger.info("User {} request deletion of file {}.", username, aftFileName);
		if (inputBoxName == null) {
			logger.error("boxPath is null for deleteFileInAFT");
			return AFTxWebDAVClientConstants.ERROR_URL_IS_NULL;
		}
		if (username == null) {
			logger.error("username is null for deleteFileInAFT");
			return AFTxWebDAVClientConstants.ERROR_USERNAME_IS_NULL;
		}
		if (encryptedPassword == null) {
			logger.error("password is null for deleteFileInAFT");
			return AFTxWebDAVClientConstants.ERROR_PASSWORD_IS_NULL;
		}
		if (aftFileName == null || aftFileName.length() == 0) {
			logger.error("aftFile is null for deleteFileInAFT");
			return AFTxWebDAVClientConstants.ERROR_FILE_TO_DELETE_IS_NULL;
		}

		String baseUrl = AFTxBaseURL + "/delete/" + inputBoxName + "/" + aftFileName;

		try {
			HttpClient client = getHttpClient(username, encryptedPassword);

			DeleteMethod method = new DeleteMethod(baseUrl);
			try {
				client.executeMethod(method);

				if (method.getStatusCode() != HttpStatus.SC_NO_CONTENT) {
					logger.error("HTTP return code is invalid: {}", method.getStatusCode());
					return AFTxWebDAVClientConstants.ERROR_HTTP_RETURN_CODE_IS_INVALID;
				}
				logger.info("User {} deleted file {}", username, aftFileName);
			} finally {
				method.releaseConnection();
			}
		} catch (MalformedURLException ex) {
			logger.error("error at deleteFileInAFT", ex);
			return AFTxWebDAVClientConstants.ERROR_URL_IS_INVALID;
		} catch (HttpException ex) {
			logger.error("error at deleteFileInAFT", ex);
			return AFTxWebDAVClientConstants.ERROR_HTTP_EXCEPTION;
		} catch (IOException ex) {
			logger.error("error at deleteFileInAFT", ex);
			return AFTxWebDAVClientConstants.ERROR_IO_EXCEPTION;
		}
		return AFTxWebDAVClientConstants.SUCCESS;
	}

	public int getFileFromAFT(String inputBoxName, String username, String encryptedPassword, String aftFileName,
			File fileFromAFT) {

		if (inputBoxName == null) {
			logger.error("inputBoxName is null for getFileFromAFT");
			return AFTxWebDAVClientConstants.ERROR_URL_IS_NULL;
		}
		if (username == null) {
			logger.error("username is null for getFileFromAFT");
			return AFTxWebDAVClientConstants.ERROR_USERNAME_IS_NULL;
		}
		if (encryptedPassword == null) {
			logger.error("username is null for getFileFromAFT");
			return AFTxWebDAVClientConstants.ERROR_PASSWORD_IS_NULL;
		}
		if (aftFileName == null || aftFileName.length() == 0) {
			logger.error("aftFileName is null for getFileFromAFT");
			return AFTxWebDAVClientConstants.ERROR_FILE_TO_GET_FROM_AFT_IS_NULL;
		}
		if (fileFromAFT == null) {
			logger.error("fileFromAFT is null for getFileFromAFT");
			return AFTxWebDAVClientConstants.ERROR_FILE_TO_WRITE_CONTENT_FROM_AFT_IS_NULL;
		}

		logger.info("User {} request aft file {} to write to {}",
				username, aftFileName, fileFromAFT.getAbsolutePath());

		String baseUrl = AFTxBaseURL + "/get/" + inputBoxName + "/" + aftFileName;
		try {
			HttpClient client = getHttpClient(username, encryptedPassword);

			GetMethod method = new GetMethod(baseUrl);

			try {
				client.executeMethod(method);
				if (method.getStatusCode() != HttpStatus.SC_OK) {
					return AFTxWebDAVClientConstants.ERROR_HTTP_RETURN_CODE_IS_INVALID;
				}

				InputStream inputStream = null;
				FileOutputStream out = null;
				byte[] buf = new byte[1024 * 100];
				try {
					inputStream = method.getResponseBodyAsStream();
					out = new FileOutputStream(fileFromAFT);

					while (true) {
						int amountRead = inputStream.read(buf);
						if (amountRead == -1)
							break;
						out.write(buf, 0, amountRead);
					}
					out.flush();
				} finally {
					if (inputStream != null)
						inputStream.close();
					if (out != null)
						out.close();
				}
				logger.info(
						"User {} got aft file {} to {}",
						username, aftFileName, fileFromAFT.getAbsolutePath());
			} finally {
				method.releaseConnection();
			}
		} catch (MalformedURLException ex) {
			logger.error("error at getFileFromAFT", ex);
			return AFTxWebDAVClientConstants.ERROR_URL_IS_INVALID;
		} catch (HttpException ex) {
			logger.error("error at getFileFromAFT", ex);
			return AFTxWebDAVClientConstants.ERROR_HTTP_EXCEPTION;
		} catch (IOException ex) {
			logger.error("error at getFileFromAFT", ex);
			return AFTxWebDAVClientConstants.ERROR_IO_EXCEPTION;
		}
		return AFTxWebDAVClientConstants.SUCCESS;
	}

	@SuppressWarnings("unchecked")
	public List<String> getFileList(String inputBoxName, String username, String encryptedPassword) {

		logger.info("User {} request list of aft available files at INBOX", username);

		if (inputBoxName == null) {
			logger.error("boxPath is null for getFileList");
			return null;
		}
		if (username == null) {
			logger.error("username is null for getFileList");
			return null;
		}
		if (encryptedPassword == null) {
			logger.error("encryptedPassword is null for getFileList");
			return null;
		}

		String baseUrl = AFTxBaseURL + "/list/" + inputBoxName;

		List<String> fileList = new ArrayList<String>();
		try {
			HttpClient client = getHttpClient(username, encryptedPassword);
			GetMethod method = new GetMethod(baseUrl);

			try {
				client.executeMethod(method);

				if (method.getStatusCode() != HttpStatus.SC_OK) {
					logger.error("HTTP return code is invalid: {}", method.getStatusCode());
					return null;
				}

				InputStream inputStream = null;
				try {
					inputStream = method.getResponseBodyAsStream();
					SAXBuilder builder = new SAXBuilder();
					Document doc = builder.build(inputStream);
					Element root = doc.getRootElement();
					List<Element> aftFiles = root.getChildren();
					for (Element aftFile : aftFiles) {
						fileList.add(aftFile.getText());
					}
				} finally {
					if (inputStream != null)
						inputStream.close();
				}
				logger.info("User {} got list of aft available files at INBOX", username);
			} finally {
				method.releaseConnection();
			}
		} catch (JDOMException ex) {
			logger.error("error at getFileList", ex);
			return null;
		} catch (MalformedURLException ex) {
			logger.error("error at getFileList", ex);
			return null;
		} catch (HttpException ex) {
			logger.error("error at getFileList", ex);
			return null;
		} catch (IOException ex) {
			logger.error("error at getFileList", ex);
			return null;
		}
		return fileList;
	}

	@SuppressWarnings("unchecked")
	public List<String> getLogsList(String username, String encryptedPassword) {

		logger.info("User {} request list of log files", username);

		if (username == null) {
			logger.error("username is null for getLogsList");
			return null;
		}
		if (encryptedPassword == null) {
			logger.error("encryptedPassword is null for getLogsList");
			return null;
		}

		String baseUrl = AFTxBaseURL + "/logs";

		List<String> fileList = new ArrayList<String>();
		try {
			HttpClient client = getHttpClient(username, encryptedPassword);
			GetMethod method = new GetMethod(baseUrl);

			try {
				client.executeMethod(method);

				if (method.getStatusCode() != HttpStatus.SC_OK) {
					logger.error("HTTP return code is invalid: {}", method.getStatusCode());
					return null;
				}

				InputStream inputStream = null;
				try {
					inputStream = method.getResponseBodyAsStream();
					SAXBuilder builder = new SAXBuilder();
					Document doc = builder.build(inputStream);
					Element root = doc.getRootElement();
					List<Element> logFiles = root.getChildren();
					for (Element logFile : logFiles) {
						fileList.add(logFile.getChildText("a"));
					}
				} finally {
					if (inputStream != null)
						inputStream.close();
				}
				logger.info("User {} got list of log files", username);
			} finally {
				method.releaseConnection();
			}
		} catch (JDOMException ex) {
			logger.error("error at getLogsList", ex);
			return null;
		} catch (MalformedURLException ex) {
			logger.error("error at getLogsList", ex);
			return null;
		} catch (HttpException ex) {
			logger.error("error at getLogsList", ex);
			return null;
		} catch (IOException ex) {
			logger.error("error at getLogsList", ex);
			return null;
		}
		return fileList;
	}

	public int getLogFromAFT(String logFileName, String username, String encryptedPassword, File logFile) {

		if (logFileName == null || logFileName.length() == 0) {
			logger.error("logFileName is null for getLogFromAFT");
			return AFTxWebDAVClientConstants.ERROR_FILE_TO_GET_FROM_AFT_IS_NULL;
		}
		if (username == null) {
			logger.error("username is null for getLogFromAFT");
			return AFTxWebDAVClientConstants.ERROR_USERNAME_IS_NULL;
		}
		if (encryptedPassword == null) {
			logger.error("encryptedPassword is null for getLogFromAFT");
			return AFTxWebDAVClientConstants.ERROR_PASSWORD_IS_NULL;
		}
		if (logFile == null) {
			logger.error("logFile is null for getLogFromAFT");
			return AFTxWebDAVClientConstants.ERROR_FILE_TO_WRITE_CONTENT_FROM_AFT_IS_NULL;
		}

		logger.info("User " + username + " request aft log file to write to " + logFile.getAbsolutePath());

		String baseUrl = AFTxBaseURL + "/log/" + logFileName;
		try {
			HttpClient client = getHttpClient(username, encryptedPassword);

			GetMethod method = new GetMethod(baseUrl);
			try {
				client.executeMethod(method);
				if (method.getStatusCode() != HttpStatus.SC_OK) {
					logger.error("HTTP return code is invalid: " + method.getStatusCode());
					return AFTxWebDAVClientConstants.ERROR_HTTP_RETURN_CODE_IS_INVALID;
				}

				InputStream inputStream = null;
				OutputStream out = null;
				byte[] buf = new byte[1024 * 100];
				try {
					inputStream = method.getResponseBodyAsStream();
					out = new FileOutputStream(logFile);
					while (true) {
						int amountRead = inputStream.read(buf);
						if (amountRead == -1)
							break;
						out.write(buf, 0, amountRead);
					}
					out.flush();
				} finally {
					if (inputStream != null)
						inputStream.close();
					if (out != null)
						out.close();
				}
				logger.info("User {} got aft log file to {}", username, logFile.getAbsolutePath());
			} finally {
				method.releaseConnection();
			}
		} catch (MalformedURLException ex) {
			logger.error("error at getLogFromAFT", ex);
			return AFTxWebDAVClientConstants.ERROR_URL_IS_INVALID;
		} catch (HttpException ex) {
			logger.error("error at getLogFromAFT", ex);
			return AFTxWebDAVClientConstants.ERROR_HTTP_EXCEPTION;
		} catch (IOException ex) {
			logger.error("error at getLogFromAFT", ex);
			return AFTxWebDAVClientConstants.ERROR_IO_EXCEPTION;
		}
		return AFTxWebDAVClientConstants.SUCCESS;
	}

	public int sendFileToAFT(String boxPath, String username, String encryptedPassword, File fileToAFT) {
		if (boxPath == null) {
			logger.error("boxPath is null for sendFileToAFT");
			return AFTxWebDAVClientConstants.ERROR_URL_IS_NULL;
		}
		if (username == null) {
			logger.error("username is null for sendFileToAFT");
			return AFTxWebDAVClientConstants.ERROR_USERNAME_IS_NULL;
		}
		if (encryptedPassword == null) {
			logger.error("encryptedPassword is null for sendFileToAFT");
			return AFTxWebDAVClientConstants.ERROR_PASSWORD_IS_NULL;
		}
		if (fileToAFT == null) {
			logger.error("fileToAFT is null for sendFileToAFT");
			return AFTxWebDAVClientConstants.ERROR_FILE_TO_SEND_TO_AFT_IS_NULL;
		}

		logger.info("User {} request to send file {} to aft OUTBOX {}",
				username, fileToAFT.getAbsolutePath(), boxPath);

		String baseUrl = AFTxBaseURL + "/put/" + boxPath + "/" + fileToAFT.getName();
		try {
			HttpClient client = getHttpClient(username, encryptedPassword);

			PutMethod method = new PutMethod(baseUrl);
			try {
				RequestEntity requestEntity = new InputStreamRequestEntity(new FileInputStream(fileToAFT));
				method.setRequestEntity(requestEntity);
				client.executeMethod(method);

				if (method.getStatusCode() != HttpStatus.SC_CREATED) {
					logger.error("HTTP return code is invalid: " + method.getStatusCode());
					return AFTxWebDAVClientConstants.ERROR_HTTP_RETURN_CODE_IS_INVALID;
				}
				logger.info(
						"User {} sent file {} to aft OUTBOX {}",
								username, fileToAFT.getAbsolutePath(), boxPath);
			} finally {
				method.releaseConnection();
			}

		} catch (MalformedURLException ex) {
			logger.error("error at sendFileToAFT", ex);
			return AFTxWebDAVClientConstants.ERROR_URL_IS_INVALID;
		} catch (HttpException ex) {
			logger.error("error at sendFileToAFT", ex);
			return AFTxWebDAVClientConstants.ERROR_HTTP_EXCEPTION;
		} catch (IOException ex) {
			logger.error("error at sendFileToAFT", ex);
			return AFTxWebDAVClientConstants.ERROR_IO_EXCEPTION;
		}
		return AFTxWebDAVClientConstants.SUCCESS;
	}

	public IAFTxClient setAFTxBaseURL(String AFTxBaseURL) {
		this.AFTxBaseURL = AFTxBaseURL;
		return this;
	}

	private HttpClient getHttpClient(String username, String encryptedPassword) {
		HttpClient client = new HttpClient();
		Credentials creds = new UsernamePasswordCredentials(username, encryptedPassword);
		client.getState().setCredentials(AuthScope.ANY, creds);
		return client;
	}

	/**
	 * Retorna a versão desta API.
	 * 
	 * @return A string da versão desta API.
	 */
	public static final String getVersion() {
		return RESOURCES.getString("version");
	}
}
