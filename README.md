
# Desafio Sicredi

Esse repositório contém o desenvolvimento do desafio proposto pela NTConsult.
O objetivo desse projeto é gerenciar sessões de votação, cadastrando pautas, abrido sessões de votação para as pautas cadastradas e votar nas sessões abertas. 




## Stack utilizada

Java 11, Spring boot, Apache kafka, mongoDb, oracle database, maven, mockito.


## Exemplo de uso

1. Crie uma pauta:

| Método | URL | 
|--------|----------|
| POST | http://localhost:8081/pautas |

- Request body
```
{
    "pauta": "descricao da pauta"
}
```
- Response body
```
{
  "id": 1,
  "pauta": "descricao da pauta"
}
```
2. Listar todas as pautas:

| Método |   URL   |
|--------|----------|
| GET    | http://localhost:8081/pautas  |  
- Response body: 
```
[
    {
        "id": 1,
        "pauta": "descricao da pauta"
    }
]
```
3. Abrir votação:

| Método |   URL   |
|--------|----------|
| POST   | http://localhost:8082/votacoes| 
- Request body
```
{
  "duracao": 1,
  "pautaId": 1
}
``` 
- Response body
```
{
    "id": "63ebc970de94264b9df919bb",
    "pautaId": 1,
    "statusVotacao": "ABERTO",
    "duracao": 1,
    "iniciaEm": "2023-02-14T14:48:32.3779837",
    "terminaEm": "2023-02-14T14:58:32.3779837"
}
```
4. Listar todas as votações:

| Método |   URL   |
|--------|----------|
| GET    | http://localhost:8082/votacoes| 
- Response body 
```
[
    {
        "id": "63e44598f0af705068008625",
        "pautaId": 1,
        "statusVotacao": "ABERTO",
        "duracao": 1,
        "iniciaEm": "2023-02-14T14:48:32.3779837",
        "terminaEm": "2023-02-14T14:58:32.3779837"
    }
]
```
4. Votar: 

| Método |   URL   |
|--------|----------|
| POST   | http://localhost:8082/votos/votar   |
- Request body 
```
{
  "cpf": "11122233344",
  "opcaoDeVoto": "SIM",
  "pautaId": 1
}
``` 
- Response body 
```
{
    "votoId": "63ebcb37de94264b9df919bc",
    "pautaId": 1,
    "cpf": "11122233344",
    "opcaoDeVoto": "SIM"
}
```
5. Consultar o resultado da votação:

| Método |   URL   |
|--------|----------|
| GET   | http://localhots:8081/pautas/resultado/{pautaId}   |
- Response body
```
{
  "pautaId": 1,
  "votosSim": 0,
  "votosNao": 1,
  "votosContabilizados": 1,
  "resultado": "A pauta foi reprovada"
}
```
## Instalação

1. Para instalar o projeto você precisa instalar na sua máquina:
 - Maven
 - JDK 11.0.11
 - Oracle Database 21c Express Edition
 - MongoDB v5.0.5
 - Kafka_2.13-2.6.0
2. Clone o repositório para a sua máquina local: 
```
https://github.com/feLargato/desafio-sicredi.git
```
3. Inicie os serviços do Apache Kafka e do MongoDB.
4. Nas classes KafkaProducer e KafkaConsumer, na votacao-api e pauta-api respectivamente, edite as strings de conexão para conectar no seu serviço local do apache kafka.
5. Edite as strings de conexão do arquivo application.properties dos projetos votacao-api e pauta-api para conectar no seu banco de dados local.
6. Na pasta raiz de cada projeto compile o código usando o seguinte comando: 
```
mvn clean package
```
7. Execute o seguinte comando na pasta raiz de cada projeto para iniciar a aplicação:
```
java -jar target/nome-do-arquivo.jar
```
Substitua "nome_do_arquivo.jar" pelo nome do arquivo gerado no passo 6.

8. você pode encontrar a documentação das apis nos links:
- http://localhost:8080/swagger-ui.html para associado-api.
- http://localhost:8081/swagger-ui.html para pauta-api
- http://localhost:8082/swagger-ui.html para votacao-api
9. Pronto! Agora você pode começar a usar as apis. Qualquer dificuldade na Instalação por favor entre em contato pelo email lfrsantos05@gmail.com 



    
