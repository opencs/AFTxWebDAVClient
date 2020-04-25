# AFTxWebDAVClient

## Introdução

Este repositório contém a implementação dos clientes do **AFTx** disponibilizados
pela **Open Communications Security**. Todo o conteúdo deste repositório é
considerado código-aberto e pode ser modificado e/ou utilizado livremente em qualquer
tipo de aplicação comercial ou não.

## Notas de compatibilidade

A partir da versão 2.x.x, o **AFTx** implementa 2 interfaces de programação (API), uma 
chamada de **Legada** e a outra chamada **REST**.

A **API Legada** é implementada em **WebDAV** e é compatível com todas as versões anteriores
do **AFTx**. A **API REST** oferece uma interface mais poderosa e flexível baseada
em **REST** e **JSON**.

Esta API implementa apenas a versão **Legada** da **API** (**WebDAV**) e não é 
recomendada para uso em novas integrações.

## Documentação

Para dar suporte a este repositório, alguns documentos podem ser necessários:

* [Interface Webdav do AFTx](AFTX-webdav-interface.md)

## Conteúdo

 Projeto | Localização | Descrição
 ------- | ----------- | ---------
AFTxWebDAVClient | /java/AFTxWebDAVClient | Implementação de um cliente do AFTx em Java.
AFTxWebDAVClientSample | /java/AFTxWebDAVClientSample | Implementação de exemplo em Java.

## Licenciamento

Todos os projetos liberados neste repositório estão licenciados sob a licença
**BSD 3-Clause License** e podem ser utilizadas livremente por qualquer aplicação,
comercial ou não.

## Problemas e suporte

O conteúdo deste repositório foi liberado como software livre e não possui nenhum tipo
de suporte oficial da Open Communications Security. Entretanto, como um projeto de software
livre, é possível reportar problemas e bugs diretamente no [Sistema de Tickets do
GitHub](https://github.com/opencs/AFTxWebDAVClient/issues). Entretanto, estes tickets não
possuem nenhum tipo de SLA e serão atendidos conforme a disponibilidade de tempo da equipe
ou por solicitação direta do Santander.

Para solicitar qualquer tipo de suporte profissional, entre em contato com o suporte ao 
**AFT/AFTx** do Santander.

## Contribuições

Este projeto também aceita contribução de código de terceiros desde que mantenham a 
licença **BSD 3-Clause License**. Para contribuir, basta enviar **push requests** diretamente
por meio do **GitHUB**. Toda e qualquer contribuição para este projeto será devidamente
creditada aos seus autores originais.
