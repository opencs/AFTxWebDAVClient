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

/**
 * Esta interface define os códigos de erros que podem ser gerados pelo cliente do AFTx.
 * 
 * @author Augusto Kiramoto
 * @author Rafael Teixeira
 * @author Fabio Jun Takada Chino
 * @since 1.0.0
 */
public class AFTxWebDAVClientConstants {

	/**
	 * Operação bem sucedida.
	 */
	public static final int SUCCESS = 0x0;
	
	/**
	 * URL base inválida.
	 */
	public static final int ERROR_URL_IS_INVALID = -1;
	
	/**
	 * O nome do usuário é nulo.
	 */
	public static final int ERROR_USERNAME_IS_NULL = -2;
	
	/**
	 * A senha do usu[ario é nula.
	 */
	public static final int ERROR_PASSWORD_IS_NULL = -3;
	
	/**
	 * A URL base é nula.
	 */
	public static final int ERROR_URL_IS_NULL = -4;
	
	/**
	 * O nome do arquivo a ser apagado é nulo.
	 */
	public static final int ERROR_FILE_TO_DELETE_IS_NULL = -5;
	
	/**
	 * Código de retorno HTTP recebido do servidor não esperado.
	 */
	public static final int ERROR_HTTP_RETURN_CODE_IS_INVALID = -6;
	
	/**
	 * Exceção durante a comunicação com o servidor.
	 */
	public static final int ERROR_HTTP_EXCEPTION = -7;
	
	/**
	 * Erro de entrada e saída.
	 */
	public static final int ERROR_IO_EXCEPTION = -8;
	
	/**
	 * Nome do arquivo a ser baixado é nulo.
	 */
	public static final int ERROR_FILE_TO_GET_FROM_AFT_IS_NULL = -9;
	
	/**
	 * Nome do arquivo de saída é nulo.
	 */
	public static final int ERROR_FILE_TO_WRITE_CONTENT_FROM_AFT_IS_NULL = -10;
	
	/**
	 * Caixa de saída é nula.
	 */
	public static final int ERROR_AFT_OUTBOX_IS_NULL = -11;

	/**
	 * 
	 */
	public static final int ERROR_FILE_TO_SEND_TO_AFT_IS_NULL = -12;
}
