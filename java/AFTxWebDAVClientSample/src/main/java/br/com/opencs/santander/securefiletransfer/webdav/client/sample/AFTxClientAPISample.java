/*
 * BSD 3-Clause License
 * 
 * Copyright (c) 2007-2020, Open Communications Security
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
package br.com.opencs.santander.securefiletransfer.webdav.client.sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;

import br.com.opencs.santander.securefiletransfer.webdav.client.AFTxWebDAVClient;

// Nota aos desenvolvedores: Este código foi implementado originalmente em 2007
// como exemplo rápido e programa de testes não tendo sido totalmente documentado. 

/**
 * Cliente teste da API do AFTxWebDAV. Demonstra os passos básicos para a
 * utilização da API do AFTxWebDAVClient.
 * 
 * @author Augusto Kiramoto
 * @author Fabio Jun Takada Chino
 */
public class AFTxClientAPISample {

	/**
	 * Execucao do teste da API
	 * 
	 * @param args arg[0] = path do truststore arg[1] = password do truststore
	 *             arg[2] = URL arg[3] = arquivo log4j.xml
	 */
	public static void main(String[] args) {
		String AFTxURL = "https://localhost:9443";
		String trustStoreLocation = "keystore.jks";
		String trustStorePassword = "123456";

		if (args.length > 0) {
			AFTxURL = args[0];
		}
		if (args.length > 2) {
			trustStoreLocation = args[2];
		}
		if (args.length > 3) {
			trustStorePassword = args[3];
		}
		doMenu(trustStoreLocation, trustStorePassword, AFTxURL);
	}

	/**
	 * Metodo para guiar o usuario a apagar o arquivo de uma caixa postal do AFTx
	 * 
	 * @param client
	 */
	private static void deleteFile(AFTxWebDAVClient client) {
		System.out.println("deleteFile");
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Digite o usuário:");
			String username = reader.readLine();
			System.out.println("Digite a senha:");
			String password = reader.readLine();

			System.out.println("Digite o nome da caixa postal:");
			String inputBoxName = reader.readLine();
			System.out.println("Digite o nome do arquivo:");
			String aftFilename = reader.readLine();

			/*
			 * A função deleteFileInAFT apaga um dado arquivo, sendo necessário informar
			 * <ul> <li>o nome da caixa de entrada (por exemplo, 1234)</li> <li>o nome do
			 * usuário</li> <li>a senha do usuário</li> <li>o nome do arquivo a ser
			 * apagado.</li> </ul>
			 */
			int ret = client.deleteFileInAFT(inputBoxName, username, password, aftFilename);
			if (ret == 0) {
				System.out.println("SUCESSO!");
			} else {
				System.out.println("Erro : " + ret);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void doMenu(String trustStoreLocation, String trustStorePassword, String AFTxURL) {
		AFTxURL = normalizeURL(AFTxURL);

		System.out.println("Iniciando o programa de teste da API cliente do AFTx");
		System.out.println("====================================================");
		System.out.println("Usando:");
		System.out.println("  Arquivo TrustStore = " + trustStoreLocation);
		System.out.println("  Senha TrustStore   = " + trustStorePassword);
		System.out.println("  URL de Acesso      = " + AFTxURL);
		System.out.println();

		BufferedReader reader = null;
		try {

			/*
			 * Passo 1: Instancia-se um objeto do tipo AFTxWebDAVClient, informando <ul>
			 * <li>o caminho completo para o arquivo de configuração do log4j (por exemplo,
			 * <i>c:\myclient.dir\etc\log4j.xml)</i></li> <li>o caminho completo para o
			 * repositório de chaves (por exemplo, <i>c:\myclient.dir\mykeystore)</i></li>
			 * <li>a senha do repositório de chaves.</li> </ul>
			 */
			AFTxWebDAVClient client = new AFTxWebDAVClient(trustStoreLocation, trustStorePassword);

			/*
			 * Passo 2: configura-se a URL base do servidor. Por exemplo,
			 * <i>https://localhost:9443/AFTxWeb</i>.
			 */
			client.setAFTxBaseURL(AFTxURL);

			reader = new BufferedReader(new InputStreamReader(System.in));
			while (true) {
				System.out.println("Digite o comando: ");
				System.out.println("1 - List Files");
				System.out.println("2 - Get File");
				System.out.println("3 - Send File");
				System.out.println("4 - Delete File");
				System.out.println("5 - List Logs");
				System.out.println("6 - Get Log");
				System.out.println("0 - Sair");
				int command = Integer.parseInt(reader.readLine());

				/*
				 * Os comandos para listar os conteúdos das caixas, baixar, enviar ou apagar
				 * arquivos, bem como obter o log, podem ser executados em qualquer ordem.
				 */
				switch (command) {
				case 1:
					listFiles(client);
					break;
				case 2:
					getFile(client);
					break;
				case 3:
					sendFile(client);
					break;
				case 4:
					deleteFile(client);
					break;
				case 5:
					listLogs(client);
					break;
				case 6:
					getLog(client);
					break;
				case 0:
					System.out.println("Bye!");
					return;
				default:
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Metodo para guiar o usuario a obter um arquivo do AFTx
	 * 
	 * @param client
	 */
	private static void getFile(AFTxWebDAVClient client) {
		System.out.println("getFile");
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Digite o usuário:");
			String username = reader.readLine();
			System.out.println("Digite a senha:");
			String password = reader.readLine();

			System.out.println("Digite o nome da caixa postal:");
			String inputBoxName = reader.readLine();

			System.out.println("Digite o nome do arquivo a ser baixado:");
			String aftFile = reader.readLine();

			System.out.println("Digite o caminho completo do arquivo onde deve ser gravado o arquivo do AFT:");
			String fileFromAFT = reader.readLine();

			int ret = client.getFileFromAFT(inputBoxName, username, password, aftFile, new File(fileFromAFT));
			if (ret == 0) {
				System.out.println("SUCESSO!");
			} else {
				System.out.println("Erro : " + ret);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metodo para guiar o usuario a obter o log do AFTx
	 * 
	 * @param client
	 */
	private static void getLog(AFTxWebDAVClient client) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(System.in));

			System.out.println("Digite o usuário:");
			String username = reader.readLine();
			System.out.println("Digite a senha:");
			String password = reader.readLine();
			System.out.println("Digite o caminho completo do arquivo onde deve ser gravado o log do AFT:");
			String logFilePath = reader.readLine();

			System.out.println("Digite o nome do log a ser baixado:");
			String logFileName = reader.readLine();

			/*
			 * Para obter os logs deve-se usar a função getLogFromAFT, informando o usuário
			 * e a senha, bem como uma instância da classe File, com o caminho onde arquivo
			 * de log será gravado.
			 */
			int ret = client.getLogFromAFT(logFileName, username, password, new File(logFilePath));
			if (ret == 0) {
				System.out.println("SUCESSO!");
			} else {
				System.out.println("Erro : " + ret);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metodo para guiar o usuario a obter a lista de arquivos de um diretorio do
	 * AFTx
	 * 
	 * @param client
	 */
	private static void listFiles(AFTxWebDAVClient client) {
		System.out.println("listFiles");
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Digite o usuário:");
			String username = reader.readLine();
			System.out.println("Digite a senha:");
			String password = reader.readLine();
			System.out.println("Digite o nome da caixa postal:");
			String inputBoxName = reader.readLine();

			List<String> files = client.getFileList(inputBoxName, username, password);
			if (files != null) {
				System.out.println("Os arquivos existentes na caixa postal " + inputBoxName + " são:");
				for (String file : files) {
					System.out.println(">>> " + file);
				}
			} else {
				System.out.println("Nenhum arquivo encontrado!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metodo para guiar o usuario a obter a lista de arquivos de um diretorio do
	 * AFTx
	 * 
	 * @param client
	 */
	private static void listLogs(AFTxWebDAVClient client) {
		System.out.println("listLogs");
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Digite o usuário:");
			String username = reader.readLine();
			System.out.println("Digite a senha:");
			String password = reader.readLine();

			List<String> files = client.getLogsList(username, password);
			if (files != null) {
				System.out.println("Os logs existentes são:");
				for (String file : files) {
					System.out.println(">>> " + file);
				}
			} else {
				System.out.println("Nenhum log encontrado!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String normalizeURL(String AFTxURL) {
		AFTxURL = AFTxURL.trim();
		if (AFTxURL.endsWith("/"))
			AFTxURL = AFTxURL.substring(0, AFTxURL.length() - 1);
		return AFTxURL;
	}

	/**
	 * Metodo para guiar o usuario a enviar um arquivo ao AFTx
	 * 
	 * @param client
	 */
	private static void sendFile(AFTxWebDAVClient client) {
		System.out.println("sendFile");
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Digite o usuário:");
			String username = reader.readLine();
			System.out.println("Digite a senha:");
			String password = reader.readLine();

			System.out.println("Digite o nome da caixa postal:");
			String boxPath = reader.readLine();

			System.out.println("Digite o caminho completo do arquivo a ser enviado ao AFT:");
			String fileToAFTPath = reader.readLine();

			int ret = client.sendFileToAFT(boxPath, username, password, new File(fileToAFTPath));
			if (ret == 0) {
				System.out.println("SUCESSO!");
			} else {
				System.out.println("Erro : " + ret);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
