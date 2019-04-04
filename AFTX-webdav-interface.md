# Interface Webdav do AFTx


## Introdução




## Operações



* https://baseUrl:port/delete/inboxName/filename

* https://baseUrl:port/get/inboxName/filename
* https://baseUrl:port/list/inboxName
* https://baseUrl:port/logs
* https://baseUrl:port/log/logname

### PUT

* https://baseUrl:port/put/outboxName/filename


##

##


### Resposta da listagem

```
<availableFiles>
    <file>b_20190404041913</file>
</availableFiles>

```

### Resposta da listagem dos logs

```
<?xml version="1.0" encoding="UTF-8"?>
<ul>
  <li>
    <a href="/log/Plain-Text">Plain-Text</a>
  </li>
</ul>
```

