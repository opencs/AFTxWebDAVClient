# Certificados SSL para o AFTx
Copyright (c) 2007-2019 Open Commnications Security

* Autor:
  * Fabio Jun Takada Chino

## Sobre este documento

Este artigo explica como gerar um certificado para o AFTx usando o Java keytool diretamente
da linha de comando.

Após ler este documento, espera-se que o leitor seja capaz de gerar certificados auto-assinados
ou assinados por uma autoridade certificadora que podem ser instalados no **AFTx**.

### Pré-requisitos

Para utilizar este documento, será necessário:

* Java Development Kit 8;
* OpenSSL 1.1.0g ou superior (opcional);
* Microsoft Windows ou Linux;

### Audiência

Este documento destina-se a profissionais de infraestrutura que possuem conhecimentos
basicos sobre certificação digital e uso de ferramentas em linha de comando (cmd, sh, bash, etc).

## Gerando a chave para o certificado

Primeiro, é necessário criar uma chave inicial com o comando:

```
$ keytool -genkey -storetype jks -alias jetty -keyalg RSA -sigalg SHA256withRSA -keysize 2048 -keystore keystore.opencs -storepass "file not found!" -keypass "parameter can not be null!"
```

Responda às seguinte perguntas:

```
What is your first and last name?
  [Unknown]:  servername
What is the name of your organizational unit?
  [Unknown]:  AFTx
What is the name of your organization?
  [Unknown]:  AFTx
What is the name of your City or Locality?
  [Unknown]:  SAO PAULO
What is the name of your State or Province?
  [Unknown]:  SAO PAULO
What is the two-letter country code for this unit?
  [Unknown]:  BR
Is CN=servername, OU=AFTx, O=AFTx, L=SAO PAULO, ST=SAO PAULO, C=BR correct?
  [no]:  yes
```

Na pergunta ``What is your first and last name?``, responda com o nome do servidor no DNS. Por
exemplo, se o endereço de acesso for ``https://servername:9443/``, use apenas ``servername``. Os
demais valores podem ser preenchidos da forma que for mais apropriada para a sua organização, 
lembrando apenas que a acentuação deve ser evitada.

Depois deste procedimento, o Keystore já pode ser utilizado com um certificado auto-assinado.

Este certificado pode ser exportado com o comando:

```
$ keytool -export -RFC -alias jetty -storepass "${KEYSTORE_PASSWORD}" -keystore keystore.opencs -storepass "file not found!" -file selfsig.cer
```

Para verificar este certificado, use o comando:

```
$ openssl x509 -text -in selfsig.cer
```

## Gerar um certificado assinado por uma CA

Uma vez que o certificado auto-assinado foi gerado, é necessário gerar o 
**Certificate Signing Request** (CSR) que será enviado para a autoridade certificadora:

```
$ keytool -certreq -storetype jks -alias jetty -keystore keystore.opencs -storepass "file not found!" -keypass "parameter can not be null!" -file certreq.csr
```

O resultado pode ser verificado pelo comando:

```
$ openssl req -text -noout -verify -in certreq.csr
```

Envie o CSR para a autoridade certificadora e aguarde o resultado.

## Importando o resultado da autoridade certificadora

O resultado da certificadora provavelmente será:

1. O certificado assinado (e.g.: mycert.cer);
1. Um ou mais certificados intermediários (e.g.: ca-int.cer);
1. O certificado raiz (e.g.: cacert.cer);

Em primeiro lugar, é necessário importar os certificado raiz. Isto pode ser feito com o 
comando:

```
$ keytool -importcert -storetype jks -trustcacerts -keystore keystore.opencs -storepass "file not found!" -keypass "parameter can not be null!" -alias root-ca -file cacert.pem
```

Ajuste os parâmetros "-alias root-ca" e "-file cacert.pem" para refleritem o nome do certificado
raiz e do arquivo.

No caso de existirem certificados intermediários, use, para cada certificado intermediário,
o comando:

```
$ keytool -importcert -storetype jks -trustcacerts -keystore keystore.opencs -storepass "file not found!" -keypass "parameter can not be null!" -alias intermediate-cert -file ca-int.cer
```

Novamente, ajuste os parâmetros "-alias intermediate-cert" e "-file ca-int.cer" para refleritem o nome do certificado intermediário e do arquivo.

Finalmente, importe o certificado gerado com o comando:

```
$ keytool -importcert -storetype jks  -keystore keystore.opencs -storepass "file not found!" -keypass "parameter can not be null!" -alias jetty -file  mycert.cer
```

Ajuste os parâmetros "-file ca-int.cer" para refler nome do arquivo.

É importante salientar que um cerntificado só pode ser importado se as chave pública corresponder
à chave privada da entrada. Em caso de erro, uma mensagem como ``keytool error: java.lang.Exception: Certificate reply and certificate in keystore are identical`` pode ser exibida.

## Ativando o certificado

Uma vez concluída a geração do certificado, basta:

1. Parar o **AFTx**;
1. Copiar o novo **keystore.opencs** para o diretório correto;
1. Reiniciar o AFTx para as mudanças terem efeito;

## Dúvidas frequentes

### Onde está o certificado?

No **AFTx**, o certificado SSL encontra-se em um keystore em um dos seguintes
diretórios:

* \<instalação do AFTx>/plugins/AFTxWebDAVPlugin/etc/keystore.opencs
* \<instalação do AFTx>/bin/plugins/AFTxWebDAVPlugin/etc/keystore.opencs

Porém, na versão 2.0.0 e posteriores, a localização do keystore pode ser modificada
por meio de um arquivo de configuração. Veja o manual para maiores informações.

### Qual é a senha do keystore?

Nas versões do **AFTx** anteriores à versão 2.0.0, existe uma senha fixa que é "file not found!".
Nas versões 2.0.0 e posteriores, a senha pode ser configurada. A senha da chave é "parameter can not be null!".

Esta abordagem é muito similar à utilizada pelo keystore padrão Java, cuja senha padrão é 
"changeit".

### Qual é o nome da chave utilizada?

Por padrão, o nome da chave utilizada pelo AFTx é "jetty".

### Devo proteger o acesso ao arquivo "keystore.opencs"?

O acesso ao arquivo "keystore.opencs" deve ser restrito apenas ao usuário que executa
o serviço do **AFTx**. Isto é necessário para que a chave do certificado SSL usado pelo
**AFTx**.

## Referências

1. [Java Platform, Standard Edition Tools Reference, keytool](https://docs.oracle.com/javase/8/docs/technotes/tools/unix/keytool.html)
