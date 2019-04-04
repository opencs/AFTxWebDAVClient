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
import java.util.List;

/**
 * Esta interface define os métodos do cliente do AFTx em Java.
 * 
 * <p>Instâncias desta classe permitem:</p>
 * 
 * <ul>
 * 	<li>Enviar arquivos para a caixa de saída;</li>
 * 	<li>Listar arquivos na caixa de entrada;</li>
 * 	<li>Baixar arquivos da caixa de entrada;</li>
 * 	<li>Apagar arquivos da caixa de entrada;</li>
 * 	<li>Listar arquivos de log;</li>
 * 	<li>Baixar arquivos de log;</li>
 * </ul>
 * 
 * @author Augusto Kiramoto
 * @author Rafael Teixeira
 * @author Fabio Jun Takada Chino
 * @since 1.0.0
 */
public interface IAFTxClient {

	/**
	 * Apaga um arquivo da caixa de entrada.
	 * 
	 * @param inputBoxName Nome da caixa de entrada.
	 * @param username Nome do usuário do AFTx.
	 * @param encryptedPassword Senha do usuário do AFTx.
	 * @param aftFilename Nome do arquivo na caixa de entrada.
	 * @return Código de erro representado o resultado do procedimento.
	 * @see br.com.opencs.santander.securefiletransfer.webdav.client.AFTxWebDAVClientConstants
	 */
	public int deleteFileInAFT(String inputBoxName, String username, String encryptedPassword, String aftFilename);

	/**
	 * Recupera um arquivo da caixa de entrada.
	 * 
	 * @param inputBoxName Nome da caixa de entrada.
	 * @param username Nome do usuário do AFTx.
	 * @param encryptedPassword Senha do usuário do AFTx.
	 * @param aftFilename Nome do arquivo na caixa de entrada.
	 * @param localFile Caminho para o arquivo de saída.
	 * @return Código de erro representado o resultado do procedimento.
	 * @see br.com.opencs.santander.securefiletransfer.webdav.client.AFTxWebDAVClientConstants
	 */
	public int getFileFromAFT(String inputBoxName, String username, String encryptedPassword, String aftFilename,
			File localFile);

	/**
	 * Retorna a lista dos arquivos da caixa de entrada.
	 * 
	 * @param inputBoxName Nome da caixa de entrada.
	 * @param username Nome do usuário do AFTx.
	 * @param encryptedPassword Senha do usuário do AFTx.
	 * @return Código de erro representado o resultado do procedimento.
	 * @see br.com.opencs.santander.securefiletransfer.webdav.client.AFTxWebDAVClientConstants
	 */
	public List<String> getFileList(String inputBoxName, String username, String encryptedPassword);

	/**
	 * Recupera um arquivo de log da caixa de log.
	 * 
	 * @param logFileName Nome do log.
	 * @param username Nome do usuário do AFTx.
	 * @param encryptedPassword Senha do usuário do AFTx.
	 * @param localLogFile Nome do arquivo que irá receber o arquivo de log.
	 * @return Código de erro representado o resultado do procedimento.
	 * @see br.com.opencs.santander.securefiletransfer.webdav.client.AFTxWebDAVClientConstants
	 */
	public int getLogFromAFT(String logFileName, String username, String encryptedPassword, File localLogFile);

	/**
	 * Envia um arquivo para a caixa de saída.
	 * 
	 * @param outputBoxName Nome da caixa de saída.
	 * @param username Nome do usuário do AFTx.
	 * @param encryptedPassword Senha do usuário do AFTx.
	 * @param localFile Nome do arquivo a ser enviado.
	 * @return Código de erro representado o resultado do procedimento.
	 * @see br.com.opencs.santander.securefiletransfer.webdav.client.AFTxWebDAVClientConstants
	 */
	public int sendFileToAFT(String outputBoxName, String username, String encryptedPassword, File localFile);

	/**
	 * Retorna a lista de arquivos de log.
	 * 
	 * @param username Nome do usuário do AFTx.
	 * @param encryptedPassword Senha do usuário do AFTx.
	 * @return Código de erro representado o resultado do procedimento.
	 * @see br.com.opencs.santander.securefiletransfer.webdav.client.AFTxWebDAVClientConstants
	 */
	public List<String> getLogsList(String username, String encryptedPassword);
	
	/**
	 * URL base do AFTx.
	 * 
	 * @param AFTxBaseURL URL base do AFTx.
	 * @return Código de erro representado o resultado do procedimento.
	 * @see br.com.opencs.santander.securefiletransfer.webdav.client.AFTxWebDAVClientConstants
	 */
	public IAFTxClient setAFTxBaseURL(String AFTxBaseURL);
}
